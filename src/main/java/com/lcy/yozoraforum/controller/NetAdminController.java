package com.lcy.yozoraforum.controller;

import com.lcy.yozoraforum.dto.SuperNotificationDTO;
import com.lcy.yozoraforum.service.NetAdminService;
import com.lcy.yozoraforum.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/yozora/admin")
public class NetAdminController {
    @Autowired
    private NetAdminService netAdminService;

    /**
     * 发送系统通知信息
     * @param superNotificationDTO
     * @return
     */
    @PostMapping("/superNotification")
    public Result sendAllUserNotification(@RequestBody SuperNotificationDTO superNotificationDTO){
        netAdminService.sendAllUserNotification(superNotificationDTO);
        return Result.success();
    }
}
