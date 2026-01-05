package com.lcy.yozoraforum.mapper;

import com.lcy.yozoraforum.dto.InsertCommentsDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CommentsMapper {
    void insert(@Param("insertCommentsDTO") InsertCommentsDTO insertCommentsDTO,@Param("userId") Long userId);
}
