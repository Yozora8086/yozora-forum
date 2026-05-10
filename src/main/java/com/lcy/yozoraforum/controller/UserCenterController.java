package com.lcy.yozoraforum.controller;

import com.lcy.yozoraforum.dto.UpdateForumDTO;
import com.lcy.yozoraforum.entity.Forum;
import com.lcy.yozoraforum.service.UserCenterService;
import com.lcy.yozoraforum.util.Result;
import com.lcy.yozoraforum.vo.ForumVo;
import com.lcy.yozoraforum.vo.MineForumVO;
import com.lcy.yozoraforum.vo.ShowUserMsgVO;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/yozora/userController")
public class UserCenterController {
    @Autowired
    private UserCenterService userCenterService;

    /**
     * 查询用户自己的个人信息
     * @return
     */
    @GetMapping("/showMineMsg")
    public Result<ShowUserMsgVO> selectMineMsg(){
        ShowUserMsgVO userMsgVO = userCenterService.selectMineMsg();
        return Result.success(userMsgVO);
    }

    /**
     * 分页查询我发布的帖子
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/showMineForum")
    public Result<List<MineForumVO>> showMineForum(int page,int pageSize){
        List<Forum> forumList = userCenterService.showForumList(page,pageSize);

        //stream流操作,把每个 Forum 转成 ForumVo。
        List<MineForumVO> mineForumVoList = forumList.stream()
                .map(forum -> {
                    MineForumVO mineForumVo = new MineForumVO();
                    mineForumVo.setForumId(forum.getForumId());
                    mineForumVo.setForumTitle(forum.getForumTitle());
                    mineForumVo.setForumBody(forum.getForumBody());
                    mineForumVo.setForumLike(forum.getForumLike());
                    mineForumVo.setCreateDate(forum.getCreateDate());
                    mineForumVo.setUpdateDate(forum.getUpdateDate());
                    mineForumVo.setUserId(forum.getUserId());
                    return mineForumVo;
                }).collect(Collectors.toList());

        return Result.success(mineForumVoList);
    }

    /**
     * 修改我发布的帖子
     * @param updateForumDTO
     * @return
     */
    @PatchMapping("/updateForum")
    public Result updateForum(@RequestBody UpdateForumDTO updateForumDTO){
       userCenterService.updateForum(updateForumDTO);
        return Result.success("修改成功");
    }

    /**
     * 删除我发布的帖子
     * @param forumId
     * @return
     */
    @DeleteMapping("deleteMineForum/{forumId}")
    public Result deleteMineForum(@PathVariable Long forumId){
        userCenterService.deleteMineForum(forumId);
        return Result.success("删除成功");
    }
}
