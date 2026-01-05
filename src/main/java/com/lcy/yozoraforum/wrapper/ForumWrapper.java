package com.lcy.yozoraforum.wrapper;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lcy.yozoraforum.entity.Tags;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ForumWrapper {
    //论坛帖子id
    private Long forumId;
    //论坛帖子标题
    private String forumTitle;
    //论坛帖子内容
    private String forumBody;
    //论坛帖子获得的赞
    private int forumLike;
    //论坛帖子创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;
    //论坛帖子发布用户id
    private Long userId;
    //论坛帖子的分类标签
    List<Tags> tags;
}
