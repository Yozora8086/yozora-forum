package com.lcy.yozoraforum.controller;

import com.lcy.yozoraforum.service.AnotherCenterService;
import com.lcy.yozoraforum.util.Result;
import com.lcy.yozoraforum.vo.VisitAnotherDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/yozora/anotherController")
public class AnotherCenterController {

    @Autowired
    private AnotherCenterService anotherCenterService;

    /**
     * 获取其他用户的信息
     * @param userId
     * @return
     */
    @GetMapping("/another")
    public Result<VisitAnotherDetailVO> showDetail(@RequestParam Long userId){

       VisitAnotherDetailVO anotherDetailVO = anotherCenterService.showDetail(userId);

       return Result.success(anotherDetailVO);
    }
}
