package com.lcy.yozoraforum.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForumLikeUserRelation {
    //帖子用户点赞--帖子关联id
    private Integer relationId;
    //帖子id
    private Integer forumId;
    //用户id
    private Integer userId;
}
