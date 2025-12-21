package com.lcy.yozoraforum.controller;

import com.lcy.yozoraforum.dto.ForumDTO;
import com.lcy.yozoraforum.dto.ShowForumDTO;
import com.lcy.yozoraforum.entity.Forum;
import com.lcy.yozoraforum.service.ForumService;
import com.lcy.yozoraforum.service.impl.ForumServiceImpl;
import com.lcy.yozoraforum.util.Result;
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
    /**
     * 用户发布论坛帖子
     * @param forumDTO
     * @return
     */
  @PostMapping("/insertForum")
    public Result insertForum(@RequestBody ForumDTO forumDTO){
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
    public Result<List<ForumVo>> showForumList(int page, int pageSize){
       List<Forum> forumList = forumService.showForumList(page,pageSize);
       //stream流操作,把每个 Forum 转成 ForumVo。
       List<ForumVo> forumVoList = forumList.stream()
               .map(forum -> {
                   ForumVo forumVo = new ForumVo();
                   forumVo.setForumId(forum.getForumId());
                   forumVo.setForumTitle(forum.getForumTitle());
                   forumVo.setForumBody(forum.getForumBody());
                   forumVo.setForumLike(forum.getForumLike());
                   forumVo.setCreateDate(forum.getCreateDate());
                   forumVo.setUserId(forum.getUserId());
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
   public Result<ForumVo> showForum(@RequestBody ShowForumDTO showForumDTO){
      ForumWrapper forumWrapper = forumService.showForum(showForumDTO);

      ForumVo forumVo = new ForumVo();
      //将ForumWrapper对象赋值给ForumVo对象
      BeanUtils.copyProperties(forumWrapper,forumVo);

      return Result.success(forumVo);
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


}
