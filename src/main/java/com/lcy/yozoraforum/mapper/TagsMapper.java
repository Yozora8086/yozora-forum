package com.lcy.yozoraforum.mapper;

import com.lcy.yozoraforum.entity.Tags;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TagsMapper {

    /**
     * 按分类标签名称来查询集合中所有分类标签对应的id
     * @param tagsList
     * @return
     */
    List<Integer> selectTags(List<Tags> tagsList);
}
