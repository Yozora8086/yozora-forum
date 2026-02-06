package com.lcy.yozoraforum.service.impl;

import com.aliyun.oss.OSS;
import com.lcy.yozoraforum.context.BaseContext;
import com.lcy.yozoraforum.dto.ForumDTO;
import com.lcy.yozoraforum.dto.ShowForumDTO;
import com.lcy.yozoraforum.entity.Forum;
import com.lcy.yozoraforum.entity.ForumResourceUrl;
import com.lcy.yozoraforum.entity.Tags;
import com.lcy.yozoraforum.exception.ForumExistLikeException;
import com.lcy.yozoraforum.exception.ForumNotFindException;
import com.lcy.yozoraforum.handler.NotifyWebSocketHandler;
import com.lcy.yozoraforum.mapper.ForumMapper;
import com.lcy.yozoraforum.mapper.ForumResourceUrlMapper;
import com.lcy.yozoraforum.mapper.ForumTagRelationMapper;
import com.lcy.yozoraforum.mapper.TagsMapper;
import com.lcy.yozoraforum.service.CommentsService;
import com.lcy.yozoraforum.service.ForumService;
import com.lcy.yozoraforum.vo.CommentsVO;
import com.lcy.yozoraforum.vo.ForumVo;
import com.lcy.yozoraforum.wrapper.ForumWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.ParameterResolutionDelegate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class ForumServiceImpl implements ForumService {
    @Autowired
    private ForumMapper forumMapper;
    @Autowired
    private ForumTagRelationMapper forumTagRelationMapper;
    @Autowired
    private TagsMapper tagsMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private CommentsService commentsService;
    @Autowired
    private ForumResourceUrlMapper forumResourceUrlMapper;

    @Autowired
    private OSS ossClient;

    private final String bucketName = "yozora-forum";

    /**
     * 用户发布论坛帖子
     * @param forumDTO
     */
    @Override
    public void insert(ForumDTO forumDTO) {
        Forum forum = new Forum();

        forum.setForumTitle(forumDTO.getForumTitle());
        forum.setForumBody(forumDTO.getForumBody());

        //获取用户设置的分类标签
        List<Tags> tagsList = forumDTO.getTags();

        //设置帖子发布者为当前用户
        forum.setUserId(BaseContext.getCurrentId());
        forum.setForumLike(0);
        forum.setCreateDate(LocalDateTime.now());
        forum.setUpdateDate(LocalDateTime.now());

        //查询集合中所有分类标签对应的id
        List<Long> tagsId = tagsMapper.selectTags(tagsList);

        //插入论坛帖子数据，并返回帖子对应的id
        forumMapper.insert(forum);
        Long forumId = forum.getForumId();
        System.out.println(forumId);

        //获取用户上传信息集合
        List<MultipartFile> resource = forumDTO.getResource();

        //创建url集合
        List<String> urls = new ArrayList<>();

        //遍历用户上传信息集合
        for (MultipartFile multipartFile : resource) {
            //给每个资源生成唯一的uuid
            UUID uuid = UUID.randomUUID();
            //uuid+文件名 拼接
            String fileName = uuid + multipartFile.getOriginalFilename();
            try {
                //上传到oss
                ossClient.putObject(bucketName,fileName,multipartFile.getInputStream());
            } catch (IOException e) {
                throw new RuntimeException("OSS文件上传失败");
            }
            // 返回访问URL
            String url = String.format("https://%s.%s/%s", bucketName, "oss-cn-beijing.aliyuncs.com", fileName);
            // 将url放入urls集合
            urls.add(url);
            System.out.println(url);
        }


        //将资源插入资源表
        forumResourceUrlMapper.inserts(forumId,urls);

        //将论坛帖子和分类标签进行绑定到帖子分类标签表
        forumTagRelationMapper.insert(tagsId,forumId);
    }

    /**
     * 分页查询帖子
     * @param page
     * @param pageSize
     * @return
     */

    @Override
    public List<ForumWrapper> showForumList(int page, int pageSize) {
        //当前页数起始条
        int offset = (page - 1) * pageSize;
        //执行查询
        List<Forum> forumList = forumMapper.showForumList(offset,pageSize);
        //stream流操作,把每个 Forum 转成 ForumWrapper。
        List<ForumWrapper> forumWrapperList = forumList.stream()
                .map(forum -> {
                    ForumWrapper forumWrapper = new ForumWrapper();
                    forumWrapper.setForumId(forum.getForumId());
                    forumWrapper.setForumTitle(forum.getForumTitle());
                    forumWrapper.setForumBody(forum.getForumBody());
                    forumWrapper.setForumLike(forum.getForumLike());
                    forumWrapper.setCreateDate(forum.getCreateDate());
                    forumWrapper.setUserId(forum.getUserId());
                    return forumWrapper;
                })
                .collect(Collectors.toList());
        //遍历数据库返回的集合，因为Forum实体类缺少Tags集合
        for (ForumWrapper forumWrapper : forumWrapperList) {
            //将查询到tags集合存入ForumWrapper对象中
            List<Tags> tagsList = forumTagRelationMapper.selectTags(forumWrapper.getForumId());
            forumWrapper.setTags(tagsList);
        }
        return forumWrapperList;
    }

    /**
     * 浏览帖子(进入所选择的帖子)
     * @param showForumDTO
     * @return
     */
    @Override
    public ForumWrapper showForum(ShowForumDTO showForumDTO) {
        //根据帖子id查询帖子
        Forum forum = forumMapper.selectForum(showForumDTO);
        //根据帖子id查询帖子所携带的资源
        List<String> forumResourceUrlList = forumResourceUrlMapper.select(showForumDTO.getForumId());

        for (String s : forumResourceUrlList) {
            System.out.println("------------------------------"+s);
        }

        //根据帖子id查询当前帖子所添加的分类标签
        List<Tags> tagsList = forumTagRelationMapper.selectTags(showForumDTO);

        //将Forum对象赋值给ForumWrapper对象
        ForumWrapper forumWrapper = ForumWrapper.builder()
                .forumId(forum.getForumId())
                .forumTitle(forum.getForumTitle())
                .forumBody(forum.getForumBody())
                .forumLike(forum.getForumLike())
                .createDate(forum.getCreateDate())
                .userId(forum.getUserId())
                .tags(tagsList)
                .url(forumResourceUrlList)
                .build();


        return forumWrapper;
    }

    /**
     * 帖子点赞/取消点赞
     * @param showForumDTO
     */
    @Override
    public boolean like(ShowForumDTO showForumDTO) {
        //从resource读取Lua脚本文件
        ClassPathResource resource = new ClassPathResource("lua/like.lua");
        //luaScript脚本语句变量初始化
        String luaScript = null;
        try {
            //获取lua脚本语句
            luaScript = new String(Files.readAllBytes(resource.getFile().toPath()), StandardCharsets.UTF_8);
        } catch (IOException e){
            throw new RuntimeException("读取 Lua 脚本失败", e);
        }

        //创建一个redis lua脚本的包装对象
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        //装载脚本
        redisScript.setScriptText(luaScript);
        //设置脚本返回值
        redisScript.setResultType(Long.class);

        //设置redis中Set集合的key,在 Redis里,Key通常设计成："业务名:对象类型:对象ID"
        String setLikeKey = "like:forum:" + showForumDTO.getForumId();
        String setLikeCountKey = "like:count:" + showForumDTO.getForumId();

        //脚本所需数据
        List<String> keys = Arrays.asList(setLikeKey,setLikeCountKey);
        List<String> args = Arrays.asList(String.valueOf(BaseContext.getCurrentId()),String.valueOf(3 * 24 * 60 * 60));

        System.out.println(luaScript);

        Long result = null;
        try {
            //脚本执行
            result = redisTemplate.execute(redisScript, keys, args.toArray(new String[0]));
        } catch (Exception e) {
            e.printStackTrace();
        }


        return result != null && result == 1;

    }

    /**
     * 搜索帖子(模糊查询）
     * @param body
     * @return
     */
    @Override
    public List<ForumWrapper> searchPost(String body,int page,int pageSize) {
        //当前页数起始条
        int offset = (page - 1) * pageSize;
        List<Forum> forumList = forumMapper.showSearchForum(body,offset,pageSize);
        //判断是否查询到帖子
        if (forumList.isEmpty()){
            throw new ForumNotFindException("没有查询到帖子");
        }



        //stream流操作,把每个 Forum 转成 ForumWrapper。
        List<ForumWrapper> forumWrapperList = forumList.stream()
                .map(forum -> {
                    ForumWrapper forumWrapper = new ForumWrapper();
                    forumWrapper.setForumId(forum.getForumId());
                    forumWrapper.setForumTitle(forum.getForumTitle());
                    forumWrapper.setForumBody(forum.getForumBody());
                    forumWrapper.setForumLike(forum.getForumLike());
                    forumWrapper.setCreateDate(forum.getCreateDate());
                    forumWrapper.setUserId(forum.getUserId());
                    return forumWrapper;
                })
                .collect(Collectors.toList());
        //遍历数据库返回的集合，因为Forum实体类缺少Tags集合
        for (ForumWrapper forumWrapper : forumWrapperList) {
            //将查询到tags集合存入ForumWrapper对象中
            List<Tags> tagsList = forumTagRelationMapper.selectTags(forumWrapper.getForumId());
            forumWrapper.setTags(tagsList);
        }
        return forumWrapperList;
    }

    //    /**
//     * 获取当前帖子下所有的评论
//     * @param showForumDTO
//     * @return
//     */
//    public List<CommentsVO> getForumComments(ShowForumDTO showForumDTO){
//        List<CommentsVO> commentsVOList = commentsService.showComment(showForumDTO);
//        return commentsVOList;
//    }




}
