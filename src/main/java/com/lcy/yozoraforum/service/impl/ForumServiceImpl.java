package com.lcy.yozoraforum.service.impl;

import com.alibaba.fastjson.JSONObject;
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
import com.lcy.yozoraforum.util.RedisLockUtil;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.*;
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
//    @Autowired
//    private RedisTemplate<String,Object> redis;
    @Autowired
    private CommentsService commentsService;
    @Autowired
    private ForumResourceUrlMapper forumResourceUrlMapper;
    @Autowired
    private DefaultRedisScript<Long> cacheForumAndForumPVScript;
    @Autowired
    private DefaultRedisScript<String> checkTTLScript;
    @Autowired
    private RedisLockUtil redisLockUtil;

    @Autowired
    private OSS ossClient;

    private final String bucketName = "yozora-forum";

    /**
     * 用户发布论坛帖子
     * @param forumDTO
     */
    @Override
    @Transactional
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
        //当没有资源上传时直接返回
        if (forumDTO.getResource().isEmpty()){
            return;
        }
        //遍历用户上传信息集合
        for (MultipartFile multipartFile : resource) {
            //给每个资源生成唯一的uuid
            UUID uuid = UUID.randomUUID();
            //uuid+文件名 拼接
            String fileName = uuid + multipartFile.getOriginalFilename();
            try {
                System.out.println("oss开始上传");
                //上传到oss
                ossClient.putObject(bucketName,fileName,multipartFile.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
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
        List<ForumWrapper> forumWrappersList = forumMapper.showForumList(offset,pageSize);
        //stream流操作,把每个 Forum 转成 ForumWrapper。
//        List<ForumWrapper> forumWrapperList = forumList.stream()
//                .map(forum -> {
//                    ForumWrapper forumWrapper = new ForumWrapper();
//                    forumWrapper.setForumId(forum.getForumId());
//                    forumWrapper.setForumTitle(forum.getForumTitle());
//                    forumWrapper.setForumBody(forum.getForumBody());
//                    forumWrapper.setForumLike(forum.getForumLike());
//                    forumWrapper.setCreateDate(forum.getCreateDate());
//                    forumWrapper.setUserId(forum.getUserId());
//                    forumWrapper.setForumCommentCount(forum.getForumCommentCount());
//                    forumWrapper.setForumPV(forum.getForumPV());
//                    return forumWrapper;
//                })
//                .collect(Collectors.toList());

        //遍历数据库返回的集合，因为Forum实体类缺少Tags集合
        for (ForumWrapper forumWrapper : forumWrappersList) {
            //将查询到tags集合存入ForumWrapper对象中
            List<Tags> tagsList = forumTagRelationMapper.selectTags(forumWrapper.getForumId());
            forumWrapper.setTags(tagsList);
        }
        return forumWrappersList;
    }

    /**
     * 获取帖子表里的帖子总数量
     * @return
     */
    @Override
    public Long selectAllForum() {
        //获取帖子表里的帖子总数量
        Long forumCount = forumMapper.selectAll();
        return forumCount;
    }

    /**
     * 浏览帖子(进入所选择的帖子)
     * @param forumId
     * @return
     */
    @Override
    public ForumWrapper showForum(Long forumId) {

        String forumJson = redisTemplate.execute(checkTTLScript, Collections.emptyList(), forumId.toString(), "3600", "86400");

        //缓存命中直接返回
        if (forumJson != null){
            ForumWrapper forumWrapper = JSONObject.parseObject(forumJson, ForumWrapper.class);
            return forumWrapper;
        }

        //缓存未命中
        String lockKey = "lock:forum:" + forumId.toString();
        //获取互斥锁
        boolean result = redisLockUtil.tryLock(lockKey);
        if (result == true){
                //根据帖子id查询帖子
                ForumWrapper forumWrapper = forumMapper.selectForum(forumId);
                //根据帖子id查询帖子所携带的资源
                List<String> forumResourceUrlList = forumResourceUrlMapper.select(forumId);

                //帖子浏览量自增
//                forumMapper.updatePV(forumId);


//                for (String s : forumResourceUrlList) {
//                    System.out.println("------------------------------"+s);
//                }

                //根据帖子id查询当前帖子所添加的分类标签
                List<Tags> tagsList = forumTagRelationMapper.selectTags(forumId);

                //将帖子标签挂载到帖子对象中
                forumWrapper.setTags(tagsList);
                //将帖子资源挂载到帖子对象中
                forumWrapper.setUrl(forumResourceUrlList);

                //将查询到的数据缓存到redis中

                String jsonString = JSONObject.toJSONString(forumWrapper);

                //声明TTL(单位s)
                Integer ttl = 86400;

                //执行lua脚本
                redisTemplate.execute(cacheForumAndForumPVScript,Collections.emptyList(),forumId.toString(),jsonString,forumWrapper.getForumPV().toString(),ttl.toString());

    //          disTemplate.opsForValue().set("forum:cache:" + forumId,jsonString);

                //释放锁
                redisLockUtil.unLock(lockKey);

                return forumWrapper;
        } else {
            try {
                Thread.sleep(50);
            } catch (Exception e){

            }
            showForum(forumId);
        }

        return null;

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
        //执行查询
        List<ForumWrapper> forumWrappersList = forumMapper.showSearchForum(body,offset,pageSize);
        //stream流操作,把每个 Forum 转成 ForumWrapper。
//        List<ForumWrapper> forumWrapperList = forumList.stream()
//                .map(forum -> {
//                    ForumWrapper forumWrapper = new ForumWrapper();
//                    forumWrapper.setForumId(forum.getForumId());
//                    forumWrapper.setForumTitle(forum.getForumTitle());
//                    forumWrapper.setForumBody(forum.getForumBody());
//                    forumWrapper.setForumLike(forum.getForumLike());
//                    forumWrapper.setCreateDate(forum.getCreateDate());
//                    forumWrapper.setUserId(forum.getUserId());
//                    forumWrapper.setForumCommentCount(forum.getForumCommentCount());
//                    forumWrapper.setForumPV(forum.getForumPV());
//                    return forumWrapper;
//                })
//                .collect(Collectors.toList());

        //遍历数据库返回的集合，因为Forum实体类缺少Tags集合
        for (ForumWrapper forumWrapper : forumWrappersList) {
            //将查询到tags集合存入ForumWrapper对象中
            List<Tags> tagsList = forumTagRelationMapper.selectTags(forumWrapper.getForumId());
            forumWrapper.setTags(tagsList);
        }
        return forumWrappersList;
    }

    /**
     * 获取帖子表里的帖子总数量(模糊查询)
     * @param body
     * @return
     */
    @Override
    public Long selectSearchAllForum(String body) {
        //获取帖子表里的帖子总数量
        Long forumCount = forumMapper.selectSearchAll(body);
        return forumCount;
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
