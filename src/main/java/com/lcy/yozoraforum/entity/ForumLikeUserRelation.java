package com.lcy.yozoraforum.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForumLikeUserRelation {
    //帖子用户点赞--帖子关联id
    private Long relationId;
    //帖子id
    private Long forumId;
    //用户id
    private Long userId;
}
