package com.lcy.yozoraforum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateForumDTO {
    //论坛帖子id
    private Long forumId;
    //论坛帖子标题
    private String forumTitle;
    //论坛帖子内容
    private String forumBody;
    //论坛帖子最后修改时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateDate;

}
