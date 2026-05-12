package com.lcy.yozoraforum.service.impl;

import com.aliyun.oss.OSS;
import com.lcy.yozoraforum.context.BaseContext;
import com.lcy.yozoraforum.dto.UpdateForumDTO;
import com.lcy.yozoraforum.entity.Forum;
import com.lcy.yozoraforum.exception.BizException;
import com.lcy.yozoraforum.mapper.*;
import com.lcy.yozoraforum.service.UserCenterService;
import com.lcy.yozoraforum.util.OssUtils;
import com.lcy.yozoraforum.vo.ShowUserMsgVO;
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
    @Autowired
    private ForumResourceUrlMapper forumResourceUrlMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OSS ossClient;

    /**
     * 查询用户自己的个人信息
     * @return
     */
    @Override
    public ShowUserMsgVO selectMineMsg() {
        ShowUserMsgVO userMsgVO = userMapper.selectUserMsg(BaseContext.getCurrentId());
        return userMsgVO;
    }

    private static final String BUCKET_NAME = "yozora-forum";
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
            throw new BizException();
        }
    }

    /**
     * 删除我发布的帖子
     * @param forumId
     */
    @Transactional
    @Override
    public void deleteMineForum(Long forumId) {
        //查询这个帖子所携带的资源
        List<String> urls = forumResourceUrlMapper.select(forumId);
        //先删除帖子点赞记录关联表 (子表)
        forumLikeUserRelationMapper.delete(forumId,BaseContext.getCurrentId());
        //再删除帖子分类标签关联表 (子表)
        forumTagRelationMapper.delete(forumId);
        //删除帖子所携带的资源
        forumResourceUrlMapper.delete(forumId);
        //遍历帖子所携带的资源
        for (String url : urls) {
            String fileName = OssUtils.extractObjectName(url);
            //删除上传在oss的资源
            ossClient.deleteObject(BUCKET_NAME,fileName);
        }

        //删除子表，返回删除结果
        int rows = forumMapper.deleteForum(forumId,BaseContext.getCurrentId());


        if (rows == 0){
            throw new BizException();
        }

        redisTemplate.delete(List.of(
                "like:forum:" + forumId,
                "like:count:" + forumId
        ));

    }
}
