package com.lcy.yozoraforum.util;

import com.lcy.yozoraforum.vo.CommentsVO;
import com.lcy.yozoraforum.vo.PositionVO;

/**
 * 跳转 定位计算
 */
public class CalculatePosition {

    /**
     * 计算
     * @param position
     * @return
     */
    public static PositionVO Calculate(Long position){
        //所在页数(后续 + 1)
        int page = Math.toIntExact(position) / 10;
        //所在当前页的第 ? 条数据
        int index = Math.toIntExact(position) % 10;

        //封装对象
        PositionVO positionVO = PositionVO.builder()
                .page(page + 1)
                .pageSize(10)
                .index(index)
                .build();

        return positionVO;
    }
}
