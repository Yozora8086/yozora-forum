package com.lcy.yozoraforum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InsertCommentsDTO {
    //帖子id
    private Long forumId;
    //资源id
    private Long resourceId;
    //评论内容
    private String content;
    //父评论 没有父评论 = 0
    private Long parentId;
    //评论时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
