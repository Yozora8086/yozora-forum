package com.lcy.yozoraforum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForumPVDTO {
    //帖子Id
    private Long forumId;
    //帖子点赞量
    private Long forumPV;

}
