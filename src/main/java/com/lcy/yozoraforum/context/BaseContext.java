package com.lcy.yozoraforum.context;

/**
 * 在线程中处理用户Id
 */
public class BaseContext {
    public static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

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
}
