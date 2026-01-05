package com.lcy.yozoraforum.service.impl;

import com.lcy.yozoraforum.context.BaseContext;
import com.lcy.yozoraforum.dto.UpdateForumDTO;
import com.lcy.yozoraforum.entity.Forum;
import com.lcy.yozoraforum.exception.BizException;
import com.lcy.yozoraforum.mapper.ForumLikeUserRelationMapper;
import com.lcy.yozoraforum.mapper.ForumMapper;
import com.lcy.yozoraforum.mapper.ForumTagRelationMapper;
import com.lcy.yozoraforum.service.UserCenterService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserCenterServiceImpl implements UserCenterService {
    @Autowired
    private ForumMapper forumMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ForumLikeUserRelationMapper forumLikeUserRelationMapper;
    @Autowired
    private ForumTagRelationMapper forumTagRelationMapper;
    /**
     * 分页查询我发布的帖子
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public List<Forum> showForumList(int page, int pageSize) {
        //当前页数起始条
        int offset = (page - 1) * pageSize;
        //执行查询
        List<Forum> forumList = forumMapper.showMineForumList(offset, pageSize, BaseContext.getCurrentId());
        return forumList;
    }

    /**
     * 修改我发布的帖子
     * @param updateForumDTO
     */
    @Override
    public void updateForum(UpdateForumDTO updateForumDTO) {
        //修改帖子，并返回修改结果
        int rows = forumMapper.updateForum(updateForumDTO,BaseContext.getCurrentId());

        if (rows == 0){
            throw new BizException("无权 修改/删除 该帖子或帖子不存在");
        }
    }

    /**
     * 删除我发布的帖子
     * @param forumId
     */
    @Transactional
    @Override
    public void deleteMineForum(Long forumId) {
        //先删除帖子点赞记录关联表 (子表)
        forumLikeUserRelationMapper.delete(forumId,BaseContext.getCurrentId());
        //再删除帖子分类标签关联表 (子表)
        forumTagRelationMapper.delete(forumId);
        //删除子表，返回删除结果
        int rows = forumMapper.deleteForum(forumId,BaseContext.getCurrentId());


        if (rows == 0){
            throw new BizException("无权 修改/删除 该帖子或帖子不存在");
        }

        redisTemplate.delete(List.of(
                "like:forum:" + forumId,
                "like:count:" + forumId
        ));

    }
}
