package com.lcy.yozoraforum.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PositionVO {
    //所在页数
    private int page;
    //每页的数据量
    private int pageSize;
    //在当前页的位置
    private int index;

}
