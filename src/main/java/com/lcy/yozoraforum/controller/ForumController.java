package com.lcy.yozoraforum.controller;

import com.lcy.yozoraforum.dto.ForumDTO;
import com.lcy.yozoraforum.dto.ShowForumDTO;
import com.lcy.yozoraforum.entity.Forum;
import com.lcy.yozoraforum.entity.Comments;
import com.lcy.yozoraforum.service.CommentsService;
import com.lcy.yozoraforum.service.ForumService;
import com.lcy.yozoraforum.service.impl.ForumServiceImpl;
import com.lcy.yozoraforum.util.Result;
import com.lcy.yozoraforum.vo.CommentsVO;
import com.lcy.yozoraforum.vo.ForumCommentsVO;
import com.lcy.yozoraforum.vo.ForumVo;
import com.lcy.yozoraforum.wrapper.ForumWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 论坛帖子模块
 */

@RestController
@RequestMapping("/yozora/forum")
public class ForumController {
    @Autowired
    private ForumService forumService;
    @Autowired
    private CommentsService commentsService;
    /**
     * 用户发布论坛帖子
     * @param forumDTO
     * @return
     */
  @PostMapping("/insertForum")
    public Result insertForum(@ModelAttribute ForumDTO forumDTO){
      forumService.insert(forumDTO);
      return Result.success();
  }


    /**
     * 分页查询帖子
     * @param page
     * @param pageSize
     * @return
     */
  @GetMapping("/showList")
    public Result<List<ForumVo>> showForumList(@RequestParam int page,@RequestParam int pageSize){
       List<ForumWrapper> forumList = forumService.showForumList(page,pageSize);
       //stream流操作,把每个ForumWrapper转成 ForumVo。
       List<ForumVo> forumVoList = forumList.stream()
               .map(forum -> {
                   ForumVo forumVo = new ForumVo();
                   forumVo.setForumId(forum.getForumId());
                   forumVo.setForumTitle(forum.getForumTitle());
                   forumVo.setForumBody(forum.getForumBody());
                   forumVo.setForumLike(forum.getForumLike());
                   forumVo.setCreateDate(forum.getCreateDate());
                   forumVo.setUserId(forum.getUserId());
                   forumVo.setTags(forum.getTags());
                   return forumVo;
               }).collect(Collectors.toList());

       return Result.success(forumVoList);
  }

    /**
     * 浏览帖子(进入所选择的帖子)
     * @param showForumDTO 是分页查询每个帖子对象所携带的帖子id
     * @return
     */
  @GetMapping("/showForum")
   public Result<ForumCommentsVO> showForum(@RequestBody ShowForumDTO showForumDTO){
      ForumWrapper forumWrapper = forumService.showForum(showForumDTO);

      ForumVo forumVo = new ForumVo();
      //将ForumWrapper对象赋值给ForumVo对象
      BeanUtils.copyProperties(forumWrapper,forumVo);
      //获取帖子所属评论集合
      List<CommentsVO> commentsVOList = commentsService.showComment(showForumDTO);

      //封装成一个帖子评论一体的对象
      ForumCommentsVO forumCommentsVO = ForumCommentsVO.builder()
              .forumId(forumVo.getForumId())
              .forumTitle(forumVo.getForumTitle())
              .url(forumVo.getUrl())
              .forumBody(forumVo.getForumBody())
              .forumLike(forumVo.getForumLike())
              .createDate(forumVo.getCreateDate())
              .userId(forumVo.getUserId())
              .tags(forumVo.getTags())
              .comments(commentsVOList)
              .build();

      //返沪
      return Result.success(forumCommentsVO);
   }

    /**
     * 帖子点赞/取消点赞
     * @param showForumDTO 这里的参数是帖子的id
     * @return
     */
   @PostMapping("/like")
   public Result like(@RequestBody ShowForumDTO showForumDTO){
      boolean flag = forumService.like(showForumDTO);

      if (flag == true){
          return Result.success("成功点赞");
      } else {
          return Result.success("取消点赞");
      }
   }

    /**
     * 搜索帖子(模糊查询）
     * @param body
     * @return
     */
   @GetMapping("/search")
   public Result<List<ForumVo>> searchForum(@RequestParam String body,@RequestParam int page,@RequestParam int pageSize){
       List<ForumWrapper> forumList = forumService.searchPost(body,page,pageSize);
       //stream流操作,把每个 ForumWrapper 转成 ForumVo。
       List<ForumVo> forumVoList = forumList.stream()
               .map(forum -> {
                   ForumVo forumVo = new ForumVo();
                   forumVo.setForumId(forum.getForumId());
                   forumVo.setForumTitle(forum.getForumTitle());
                   forumVo.setForumBody(forum.getForumBody());
                   forumVo.setForumLike(forum.getForumLike());
                   forumVo.setCreateDate(forum.getCreateDate());
                   forumVo.setUserId(forum.getUserId());
                   forumVo.setTags(forum.getTags());
                   return forumVo;
               })
               .collect(Collectors.toList());

       return Result.success(forumVoList);
   }


}
