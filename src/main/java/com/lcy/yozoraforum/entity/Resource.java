package com.lcy.yozoraforum.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Resource {
    //资源id
    private Long resourceId;
    //上传资源用户id
    private Long userId;
    //资源标题
    private String resourceTitle;
    //资源内容介绍
    private String resourceBody;
    //资源本体Url
    private String resourceFile;
    //该资源获得的赞
    private int resourceLike;
}
