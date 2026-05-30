package com.lcy.yozoraforum.context;

/**
 * 在线程中处理用户Id
 */
public class BaseContext {
    public static ThreadLocal<Long> threadLocal = new ThreadLocal<>();
    public static ThreadLocal<Integer> levelThreadLocal = new ThreadLocal<>();

    /**
     * 保存用户id
     * @param id
     */
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    /**
     * 获取用户id
     * @return
     */
    public static Long getCurrentId(){
        return threadLocal.get();
    }

    /**
     * 删除线程中的用户id
     */
    public static void removeCurrentId(){
        threadLocal.remove();
    }

    /**
     * 保存当前用户权限等级
     * @param level
     */
    public static void setCurrentLevel(Integer level){
        levelThreadLocal.set(level);
    }

    /**
     * 获取当前用户权限等级
     * @return
     */
    public static Integer getCurrentLevel(){
        return levelThreadLocal.get();
    }

    /**
     * 删除线程中的用户权限等级
     */
    public static void removeCurrentLevel(){
        levelThreadLocal.remove();
    }
}
