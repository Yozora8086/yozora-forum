package com.lcy.yozoraforum.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForumTagRelation {
    //帖子-分类标签关联id
    private Long relationId;
    //分类标签id
    private Long tagId;
    //帖子id
    private Long forumId;
}
