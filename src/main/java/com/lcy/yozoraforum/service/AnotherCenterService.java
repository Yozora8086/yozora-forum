package com.lcy.yozoraforum.service;

import com.lcy.yozoraforum.vo.VisitAnotherDetailVO;

public interface AnotherCenterService {

    /**
     * 获取其他用户的信息
     * @param userId
     * @return
     */
    VisitAnotherDetailVO showDetail(Long userId);
}
