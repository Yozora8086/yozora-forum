package com.lcy.yozoraforum.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Forum {
    //论坛帖子id
    private Integer forumId;
    //论坛帖子标题
    private String forumTitle;
    //论坛帖子内容
    private String forumBody;
    //论坛帖子获得的赞
    private Integer forumLike;
    //论坛帖子创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;
    //论坛帖子最后修改时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateDate;
    //论坛帖子发布用户id
    private Integer userId;



}
