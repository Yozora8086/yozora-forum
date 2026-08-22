package com.lcy.yozoraforum.constant;

public class RedisConstants {
    //所有普通通知权限总数KEY
    public static final String ALL_SUPER_NOTIFICATION_COUNT_KEY = "allSuperNotification:count";
    //所有管理员权限通知总数KEY
    public static final String ALL_SUPER_NOTIFICATION_ADMIN_COUNT_KEY = "allSuperNotification:admin:count";
    //用户/管理员 未读通知数量KEY
    public static final String NOTIFICATION_COUNT_KEY = "notification:count:";
    //用户系统已读通知数量KEY
    public static final String SUPER_NOTIFICATION_COUNT_KEY = "superNotification:count:";
    //管理员系统已读通知数量KEY
    public static final String ADMIN_SUPER_NOTIFICATION_COUNT_KEY = "superNotification:admin:count:";

    //帖子缓存阈值KEY
    public static final String FORUM_CACHE_THRESHOLD = "forum:cache:threshold:";
    //帖子浏览量缓存KEY
    public static final String FORUM_CACHE_PV = "forum:cachePV:";

}
