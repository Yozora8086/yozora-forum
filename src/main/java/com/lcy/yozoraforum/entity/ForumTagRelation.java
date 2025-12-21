package com.lcy.yozoraforum.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForumTagRelation {
    //帖子-分类标签关联id
    private int relationId;
    //分类标签id
    private int tagId;
    //帖子id
    private int forumId;
}
