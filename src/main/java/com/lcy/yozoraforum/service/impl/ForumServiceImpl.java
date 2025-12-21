package com.lcy.yozoraforum.service.impl;

import com.lcy.yozoraforum.context.BaseContext;
import com.lcy.yozoraforum.dto.ForumDTO;
import com.lcy.yozoraforum.dto.ShowForumDTO;
import com.lcy.yozoraforum.entity.Forum;
import com.lcy.yozoraforum.entity.Tags;
import com.lcy.yozoraforum.exception.ForumExistLikeException;
import com.lcy.yozoraforum.mapper.ForumMapper;
import com.lcy.yozoraforum.mapper.ForumTagRelationMapper;
import com.lcy.yozoraforum.mapper.TagsMapper;
import com.lcy.yozoraforum.service.ForumService;
import com.lcy.yozoraforum.vo.ForumVo;
import com.lcy.yozoraforum.wrapper.ForumWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.ParameterResolutionDelegate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ForumServiceImpl implements ForumService {
    @Autowired
    private ForumMapper forumMapper;
    @Autowired
    private ForumTagRelationMapper forumTagRelationMapper;
    @Autowired
    private TagsMapper tagsMapper;
    @Autowired
    private RedisTemplate redisTemplate;
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
        List<Integer> tagsId = tagsMapper.selectTags(tagsList);

        //插入论坛帖子数据，并返回帖子对应的id
        forumMapper.insert(forum);
        Integer forumId = forum.getForumId();
        System.out.println(forumId);

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
    public List<Forum> showForumList(int page, int pageSize) {
        //当前页数起始条
        int offset = (page - 1) * pageSize;
        //执行查询
        List<Forum> forumList = forumMapper.showForumList(offset,pageSize);
        return forumList;
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
                .build();


        return forumWrapper;
    }

    /**
     * 帖子点赞/取消点赞
     * @param showForumDTO
     */
    @Override
    public boolean like(ShowForumDTO showForumDTO) {
        //设置redis中Set集合的key,在 Redis里,Key通常设计成："业务名:对象类型:对象ID"
        String setLikeKey = "like:forum:" + showForumDTO.getForumId();
        String setLikeCountKey = "like:count:" + showForumDTO.getForumId();

        //判断当前登录用户是否已经点过赞了
        Long added = redisTemplate.opsForSet().add(setLikeKey, BaseContext.getCurrentId());
        if (added == 0){
            //帖子点赞数-1
            redisTemplate.opsForValue().decrement(setLikeCountKey);
            //未办！！！！！！！！！！！！
            //未办！！！！！！！！！！！！
            //未办！！！！！！！！！！！！
            //未办！！！！！！！！！！！！
            //帖子点赞落库后，取消点赞只删除掉redis中的数据，而MySQL中数据未删除
            redisTemplate.opsForSet().remove(setLikeKey,BaseContext.getCurrentId());
            return false;
        }

        //帖子点赞数+1
        redisTemplate.opsForValue().increment(setLikeCountKey);
        return true;
    }


}
