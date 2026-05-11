package com.lcy.yozoraforum.service.impl;

import com.lcy.yozoraforum.mapper.UserMapper;
import com.lcy.yozoraforum.service.AnotherCenterService;
import com.lcy.yozoraforum.vo.ShowUserMsgVO;
import com.lcy.yozoraforum.vo.VisitAnotherDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnotherCenterServiceImpl implements AnotherCenterService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 获取其他用户的信息
     * @param userId
     * @return
     */
    @Override
    public VisitAnotherDetailVO showDetail(Long userId) {
        //查询其他用户的信息
        ShowUserMsgVO userMsgVO = userMapper.selectUserMsg(userId);

        VisitAnotherDetailVO anotherDetailVO = VisitAnotherDetailVO.builder()
                .userName(userMsgVO.getUserName())
                .userId(userMsgVO.getUserId())
                .userMessage(userMsgVO.getUserMessage())
                .registerTime(userMsgVO.getRegisterTime())
                .build();
        return anotherDetailVO;
    }
}
