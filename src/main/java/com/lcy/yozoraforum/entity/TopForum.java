package com.lcy.yozoraforum.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopForum {
    //置顶帖子id
    private Integer topForumId;
    //被置顶帖子Id
    private Long forumId;
}
