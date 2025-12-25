package com.lcy.yozoraforum.wrapper;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ForumLikeUserRelationWrapper {
    //帖子id
    private Integer forumId;
    //用户id
    private Integer userId;
    //点赞状态
    private Integer status;
    //时间戳
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
