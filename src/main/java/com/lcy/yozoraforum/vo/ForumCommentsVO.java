package com.lcy.yozoraforum.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lcy.yozoraforum.entity.Tags;

import java.time.LocalDateTime;
import java.util.List;

public class ForumCommentsVO {
    //论坛帖子id
    private Long forumId;
    //论坛帖子标题
    private String forumTitle;
    //论坛帖子内容
    private String forumBody;
    //论坛帖子获得的赞
    private int forumLike;
    //论坛帖子创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;
    //论坛帖子发布用户id
    private Long userId;
    //论坛帖子的分类标签
    List<Tags> tags;
    //帖子评论
    List<CommentsVO> comments;
}
