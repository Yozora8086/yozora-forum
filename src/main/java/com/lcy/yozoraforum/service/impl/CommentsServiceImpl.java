package com.lcy.yozoraforum.service.impl;

import com.lcy.yozoraforum.context.BaseContext;
import com.lcy.yozoraforum.dto.InsertCommentsDTO;
import com.lcy.yozoraforum.dto.ShowForumDTO;
import com.lcy.yozoraforum.exception.CommentIsNullException;
import com.lcy.yozoraforum.mapper.CommentsMapper;
import com.lcy.yozoraforum.service.CommentsService;
import com.lcy.yozoraforum.vo.CommentsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lcy.yozoraforum.entity.Comments;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CommentsServiceImpl implements CommentsService {
    @Autowired
    private CommentsMapper commentsMapper;

    /**
     * 用户发表评论
     * @param insertCommentsDTO
     */
    @Override
    public Comments insertComment(InsertCommentsDTO insertCommentsDTO) {
        //校验评论是否为空
        if (insertCommentsDTO.getContent() == null || insertCommentsDTO.getContent().trim().isEmpty()) {
            throw new CommentIsNullException("评论为空");
        }
        commentsMapper.insert(insertCommentsDTO, BaseContext.getCurrentId());
        Comments comments = Comments.builder()
                .forumId(insertCommentsDTO.getForumId())
                .userId(BaseContext.getCurrentId())
                .content(insertCommentsDTO.getContent())
                .parentId(insertCommentsDTO.getParentId())
                .createTime(insertCommentsDTO.getCreateTime())
                .resource(insertCommentsDTO.getResourceId())
                .build();
        return comments;
    }

    /**
     * 获取当前帖子下的所有评论
     * @param showForumDTO
     * @return
     */
    @Override
    public List<CommentsVO> showComment(ShowForumDTO showForumDTO) {
        List<CommentsVO> commentsVOList = commentsMapper.getForumAllComments(showForumDTO.getForumId());

        // 2. 建立 commentId -> CommentsVO 的索引表
        Map<Long, CommentsVO> map = commentsVOList.stream()
                .collect(Collectors.toMap(CommentsVO::getCommentId, c -> c));

        // 3. 组装树
        List<CommentsVO> tree = new ArrayList<>();

        for (CommentsVO commentsVO : commentsVOList) {
            if (commentsVO.getParentId() == 0L) {
                // 顶级评论
                tree.add(commentsVO);
            } else {
                // 子评论，挂到父评论下
                CommentsVO parent = map.get(commentsVO.getParentId());
                if (parent != null) {
                    parent.getComments().add(commentsVO);
                }
            }
        }

        return tree;
    }
}
