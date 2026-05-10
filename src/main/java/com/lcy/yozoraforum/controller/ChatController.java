package com.lcy.yozoraforum.controller;

import com.lcy.yozoraforum.context.BaseContext;
import com.lcy.yozoraforum.dto.ChatDTO;
import com.lcy.yozoraforum.service.ChatService;
import com.lcy.yozoraforum.util.Result;
import com.lcy.yozoraforum.vo.ChatVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 私信
 */

@RestController
@RequestMapping("/yozora/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    /**
     * 当前用户发送私信
     * @return
     */
    @PostMapping("/send")
    public Result sendMessage(@RequestBody ChatDTO chatDTO){
       chatService.sendMessage(chatDTO.getReceiverId(),chatDTO.getContent());
       return Result.success();
    }

    /**
     * 获取与目标用户的聊天记录
     * @param receiverId
     * @return
     */
    @GetMapping("/select")
    public Result<List<ChatVO>> selectMessage(@RequestParam String receiverId){
        Long senderId = BaseContext.getCurrentId();
        List<ChatVO> chatList = chatService.select(senderId, receiverId);
        return Result.success(chatList);
    }
}
