package com.lcy.yozoraforum.mapper;

import com.lcy.yozoraforum.dto.InsertCommentsDTO;
import com.lcy.yozoraforum.dto.ShowForumDTO;
import com.lcy.yozoraforum.vo.CommentsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentsMapper {
    void insert(@Param("insertCommentsDTO") InsertCommentsDTO insertCommentsDTO,@Param("userId") Long userId);

    /**
     * 获取当前帖子下的所有评论
     * @param forumId
     * @return
     */
    List<CommentsVO> getForumAllComments(Long forumId);

//    /**
//     * 获取当前评论下所有的子评论
//     * @param parentId
//     * @return
//     */
//    List<CommentsVO> getForumCommentsAllChilds(@Param("forumId") Long forumId,@Param("parentId") Long parentId);
}
