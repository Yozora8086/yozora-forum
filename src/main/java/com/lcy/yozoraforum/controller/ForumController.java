package com.lcy.yozoraforum.controller;

import com.lcy.yozoraforum.dto.ForumDTO;
import com.lcy.yozoraforum.dto.ShowForumDTO;
import com.lcy.yozoraforum.service.CommentsService;
import com.lcy.yozoraforum.service.ForumService;
import com.lcy.yozoraforum.util.Result;
import com.lcy.yozoraforum.vo.ForumMsgVO;
import com.lcy.yozoraforum.vo.ForumVo;
import com.lcy.yozoraforum.wrapper.ForumWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
                   forumVo.setForumPV(forum.getForumPV());
                   forumVo.setForumCommentCount(forum.getForumCommentCount());
                   forumVo.setUserName(forum.getUserName());
                   return forumVo;
               }).collect(Collectors.toList());

       return Result.success(forumVoList);
  }

    /**
     * 获取帖子表里的帖子总数量
     * @return
     */
  @GetMapping("/getAllForum")
  public Result<Long> selectAllForum(){
      Long forumCount = forumService.selectAllForum();
      return Result.success(forumCount);
  }

    /**
     * 获取帖子表里的帖子总数量(模糊查询)
     * @return
     */
    @GetMapping("/getSearchAllForum")
    public Result<Long> selectSearchAllForum(@RequestParam String body){
        Long forumCount = forumService.selectSearchAllForum(body);
        return Result.success(forumCount);
    }

    /**
     * 浏览帖子(进入所选择的帖子)
     * @param forumId 是分页查询每个帖子对象所携带的帖子id
     * @return
     */
  @GetMapping("/showForum")
   public Result<ForumMsgVO> showForum(@RequestParam Long forumId){
      ForumWrapper forumWrapper = forumService.showForum(forumId);

      ForumVo forumVo = new ForumVo();
      //将ForumWrapper对象赋值给ForumVo对象
      BeanUtils.copyProperties(forumWrapper,forumVo);

      //封装成一个帖子评论一体的对象
      ForumMsgVO forumMsgVO = ForumMsgVO.builder()
              .forumId(forumVo.getForumId())
              .forumTitle(forumVo.getForumTitle())
              .url(forumVo.getUrl())
              .forumBody(forumVo.getForumBody())
              .forumLike(forumVo.getForumLike())
              .createDate(forumVo.getCreateDate())
              .userId(forumVo.getUserId())
              .tags(forumVo.getTags())
              .forumPV(forumVo.getForumPV())
              .userName(forumVo.getUserName())
              .status(forumVo.getStatus())
              .build();

      //返回
      return Result.success(forumMsgVO);
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
                   forumVo.setForumPV(forum.getForumPV());
                   forumVo.setForumCommentCount(forum.getForumCommentCount());
                   forumVo.setUserName(forum.getUserName());
                   return forumVo;
               })
               .collect(Collectors.toList());

       return Result.success(forumVoList);
   }

    /**
     * 获取置顶帖子列表
     * @return
     */
   @GetMapping("/selectTopForumList")
    public Result<List<ForumVo>> selectTopForumList(){
       List<ForumWrapper> forumList = forumService.selectTopForumList();
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
                   forumVo.setForumPV(forum.getForumPV());
                   forumVo.setForumCommentCount(forum.getForumCommentCount());
                   forumVo.setUserName(forum.getUserName());
                   forumVo.setForumCommentCount(forum.getForumCommentCount());
                   return forumVo;
               })
               .collect(Collectors.toList());
       return Result.success(forumVoList);
   }

    /**
     * 根据分类标签查询帖子
     * @param tagIds
     * @return
     */
   @GetMapping("/selectForumByTag")
    public Result<List<ForumVo>> selectForumByTag(@RequestBody List<Long> tagIds){
       List<ForumWrapper> forumList = forumService.selectForumByTag(tagIds);
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
                   forumVo.setForumPV(forum.getForumPV());
                   forumVo.setForumCommentCount(forum.getForumCommentCount());
                   forumVo.setUserName(forum.getUserName());
                   forumVo.setForumCommentCount(forum.getForumCommentCount());
                   return forumVo;
               })
               .collect(Collectors.toList());
       return Result.success(forumVoList);
   }


}
