package com.lcy.yozoraforum.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tags {
    //分类标签id
    private Long tagId;
    //分类标签名称
    private String tagName;
}
