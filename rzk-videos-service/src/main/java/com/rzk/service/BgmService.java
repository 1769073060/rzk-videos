package com.rzk.service;

import com.rzk.pojo.Bgm;

import java.util.List;

public interface BgmService {
    /**
     * 查询背景音乐
     * @return
     */
    public List<Bgm> queryBgmList();

    public Bgm queryBgmId(String id);

}
