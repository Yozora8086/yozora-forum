package com.lcy.yozoraforum.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForumResourceUrl {
    //资源ID
    private Long urlId;
    //帖子id
    private Long forumId;
    //资源型ID
    private Long resourceId;
    //资源url
    private String resourceUrl;
}
