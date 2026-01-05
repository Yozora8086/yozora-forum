package com.lcy.yozoraforum.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentsVO {
    //评论id
    private Long commentId;
    //评论资源id
    private Long resource;
    //评论用户id
    private Long userId;
    //评论内容
    private String content;
    //上级id,顶级为0
    private Long parentId;
    //评论创建时间
    private LocalDateTime createTime;
    //评论获得的赞
    private int commentLike;
    // 子评论列表
    private List<CommentsVO> comments = new ArrayList<>();
}
