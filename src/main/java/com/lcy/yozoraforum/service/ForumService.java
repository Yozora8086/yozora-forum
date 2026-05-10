package com.lcy.yozoraforum.service;

import com.lcy.yozoraforum.dto.ForumDTO;
import com.lcy.yozoraforum.dto.ShowForumDTO;
import com.lcy.yozoraforum.entity.Forum;
import com.lcy.yozoraforum.vo.ForumVo;
import com.lcy.yozoraforum.wrapper.ForumWrapper;

import java.util.List;

public interface ForumService {
    /**
     * 用户发布论坛帖子
     * @param forumDTO
     */
    void insert(ForumDTO forumDTO);

    /**
     * 分页查询帖子
     * @param page
     * @param pageSize
     * @return
     */
    List<ForumWrapper> showForumList(int page, int pageSize);

    /**
     * 浏览帖子(进入所选择的帖子)
     * @param forumId
     * @return
     */
    ForumWrapper showForum(Long forumId);

    /**
     * 帖子点赞/取消点赞
     * @param showForumDTO
     */
    boolean like(ShowForumDTO showForumDTO);

    /**
     * 搜索帖子(模糊查询）
     * @param body
     * @return
     */
    List<ForumWrapper> searchPost(String body,int page,int pageSize);

    /**
     * 获取数据库帖子总数
     * @return
     */
    Long selectAllForum();

    /**
     * 获取帖子表里的帖子总数量(模糊查询)
     * @param body
     * @return
     */
    Long selectSearchAllForum(String body);
}
