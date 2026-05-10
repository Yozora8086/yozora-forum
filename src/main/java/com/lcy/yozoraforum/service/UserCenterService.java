package com.lcy.yozoraforum.service;

import com.lcy.yozoraforum.dto.UpdateForumDTO;
import com.lcy.yozoraforum.entity.Forum;
import com.lcy.yozoraforum.vo.ShowUserMsgVO;

import java.util.List;

public interface UserCenterService {

    /**
     * 分页查询我发布的帖子
     * @param page
     * @param pageSize
     * @return
     */
    List<Forum> showForumList(int page, int pageSize);


    /**
     * 修改我发布的帖子
     * @param updateForumDTO
     */
    void updateForum(UpdateForumDTO updateForumDTO);

    /**
     * 删除我发布的帖子
     * @param forumId
     */
    void deleteMineForum(Long forumId);

    /**
     * 查询用户自己的个人信息
     * @return
     */
    ShowUserMsgVO selectMineMsg();
}
