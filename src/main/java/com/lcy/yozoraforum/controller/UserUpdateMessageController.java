package com.lcy.yozoraforum.controller;

import com.lcy.yozoraforum.dto.UserUpdateMsgDTO;
import com.lcy.yozoraforum.service.UserUpdateMessageService;
import com.lcy.yozoraforum.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/yozora/updateMsg")
public class UserUpdateMessageController {
    @Autowired
    private UserUpdateMessageService userUpdateMessageService;

    /**
     * 用户修改个人信息
     * @param updateMsgDTO
     * @return
     */
    @PatchMapping("/upd")
    public Result updateUserMSG(@RequestBody UserUpdateMsgDTO updateMsgDTO){

        userUpdateMessageService.update(updateMsgDTO);

        return Result.success();
    }
}
