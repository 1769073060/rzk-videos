package com.rzk.service;

/**
 * @Auther 刘瑞涛
 * @Date 2020-05-08 11:27
 **/
public interface PunishUsersService {
    /**
     * 查看是否封禁时间是否到期
     *
     * @return
     */
   public Integer selectByDate(String id);
}
