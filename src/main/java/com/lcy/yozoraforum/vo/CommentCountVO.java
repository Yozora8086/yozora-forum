package com.lcy.yozoraforum.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentCountVO {
    //评论总数
    private Long commentCount;
    //评论总数(排除子评论)
    private Long noChildCommentCount;
}
