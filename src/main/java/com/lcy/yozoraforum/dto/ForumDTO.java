package com.lcy.yozoraforum.dto;

import com.lcy.yozoraforum.entity.Tags;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForumDTO {
    //论坛帖子标题
    private String forumTitle;
    //论坛帖子内容
    private String forumBody;
    //帖子的资源
    private List<MultipartFile> resource;
    //论坛帖子分类标签
    private List<Tags> tags;
}
