package com.lcy.yozoraforum.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comments {
    //评论id
    private int commentId;
    //评论帖子id
    private int forumId;
    //评论用户id
    private int userId;
    //评论内容
    private String content;
    //上级id,顶级为0
    private int parentId;
    //评论创建时间
    private LocalDateTime createTime;
    //评论获得的赞
    private int commentLike;

}
