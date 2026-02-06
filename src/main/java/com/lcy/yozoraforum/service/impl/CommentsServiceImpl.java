package com.lcy.yozoraforum.service.impl;

import com.lcy.yozoraforum.context.BaseContext;
import com.lcy.yozoraforum.dto.CommentPositionDTO;
import com.lcy.yozoraforum.dto.InsertCommentsDTO;
import com.lcy.yozoraforum.dto.ShowCommentDTO;
import com.lcy.yozoraforum.dto.ShowForumDTO;
import com.lcy.yozoraforum.entity.Notification;
import com.lcy.yozoraforum.exception.CommentIsNullException;
import com.lcy.yozoraforum.exception.CommentNotExistException;
import com.lcy.yozoraforum.handler.NotifyWebSocketHandler;
import com.lcy.yozoraforum.mapper.CommentsMapper;
import com.lcy.yozoraforum.mapper.ForumMapper;
import com.lcy.yozoraforum.mapper.NotificationMapper;
import com.lcy.yozoraforum.service.CommentsService;
import com.lcy.yozoraforum.util.CalculatePosition;
import com.lcy.yozoraforum.util.NotificationPush;
import com.lcy.yozoraforum.vo.CommentsVO;
import com.lcy.yozoraforum.vo.PositionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import com.lcy.yozoraforum.entity.Comments;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CommentsServiceImpl implements CommentsService {
    @Autowired
    private CommentsMapper commentsMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ForumMapper forumMapper;
    @Autowired
    private NotificationMapper notificationMapper;


    /**
     * 用户发表评论
     * @param insertCommentsDTO
     */
    @Override
    public Comments insertComment(InsertCommentsDTO insertCommentsDTO) {
        //校验评论是否为空
        if (insertCommentsDTO.getContent() == null || insertCommentsDTO.getContent().trim().isEmpty()) {
            throw new CommentIsNullException("评论为空");
        }
        commentsMapper.insert(insertCommentsDTO, BaseContext.getCurrentId());
        Comments comments = Comments.builder()
                .forumId(insertCommentsDTO.getForumId())
                .userId(BaseContext.getCurrentId())
                .content(insertCommentsDTO.getContent())
                .parentId(insertCommentsDTO.getParentId())
                .createTime(insertCommentsDTO.getCreateTime())
                .resource(insertCommentsDTO.getResourceId())
                .build();

        //获取帖子发布者的id
        Long userId = forumMapper.selectUserByForum(insertCommentsDTO.getForumId());

        //通知插入数据库前的包装
        Notification notification = NotificationPush.Add(userId, 1, 1, comments.getContent(),"");

        //插入数据库
        notificationMapper.InsertNotification(notification);
        //WebSocket 推送通知
        NotifyWebSocketHandler.push(userId,comments.getUserId().toString() + "评论了你:" + comments.getContent());
        return comments;
    }

    /**
     * 获取当前帖子下的所有评论及子评论
     * @param showForumDTO
     * @return
     */
    @Override
    public List<CommentsVO> showComment(ShowForumDTO showForumDTO) {
        List<CommentsVO> commentsVOList = commentsMapper.getForumAllComments(showForumDTO.getForumId());

        //建立 commentId -> CommentsVO 的索引表
        Map<Long, CommentsVO> map = commentsVOList.stream()
                .collect(Collectors.toMap(CommentsVO::getCommentId, c -> c));

        //组装树
        List<CommentsVO> tree = new ArrayList<>();

        for (CommentsVO commentsVO : commentsVOList) {
            if (commentsVO.getParentId() == 0L) {
                // 顶级评论
                tree.add(commentsVO);
            } else {
                // 子评论，挂到父评论下
                CommentsVO parent = map.get(commentsVO.getParentId());
                if (parent != null) {
                    parent.getComments().add(commentsVO);
                }
            }
        }

        return tree;
    }

    /**
     * 删除评论
     * @param commentId
     */
    @Override
    public void deleteComment(Long commentId) {
        commentsMapper.delete(commentId,BaseContext.getCurrentId());
    }

    /**
     * 评论点赞/取消点赞
     * @param showCommentDTO
     * @return
     */
    @Override
    public boolean like(ShowCommentDTO showCommentDTO) {
        //从resource读取Lua脚本文件
        ClassPathResource resource = new ClassPathResource("lua/like.lua");
        //luaScript脚本语句变量初始化
        String luaScript = null;

        try {
            //获取lua脚本语句
            luaScript = new String(Files.readAllBytes(resource.getFile().toPath()), StandardCharsets.UTF_8);
        } catch (IOException e){
            throw new RuntimeException("读取 Lua 脚本失败", e);
        }

        //创建一个redis lua脚本的包装对象
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        //装载脚本
        redisScript.setScriptText(luaScript);
        //设置脚本返回值
        redisScript.setResultType(Long.class);

        //设置redis中Set集合的key,在 Redis里,Key通常设计成："业务名:对象类型:对象ID"
        String setLikeKey = "like:comment:" + showCommentDTO.getCommentId();
        String setLikeCountKey = "like:countComment:" + showCommentDTO.getCommentId();

        //脚本所需数据
        List<String> keys = Arrays.asList(setLikeKey,setLikeCountKey);
        List<String> args = Arrays.asList(String.valueOf(BaseContext.getCurrentId()),String.valueOf(3 * 24 * 60 * 60));

        Long result = null;
        try {
            //脚本执行
            result = (Long) redisTemplate.execute(redisScript, keys, args.toArray(new String[0]));
        } catch (Exception e) {
            e.printStackTrace();
        }



        return result != null && result == 1;
    }

    // TODO
    /**
     * 评论通知跳转前的定位
     * @param commentPositionDTO
     * @return
     */
    @Override
    public PositionVO selectPositon(CommentPositionDTO commentPositionDTO) {
        //获取评论是第几条
        Long position = commentsMapper.selectPosition(commentPositionDTO);

        if (position == 0){
            throw new CommentNotExistException("当前评论不存在");
        }

        //计算页数
        PositionVO positionVO =  CalculatePosition.Calculate(position);

        return positionVO;

    }


}
