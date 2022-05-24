package com.rzk.service.impl;

import com.rzk.mapper.BgmMapper;
import com.rzk.pojo.Bgm;
import com.rzk.pojo.BgmExample;
import com.rzk.service.BgmService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Auther 刘瑞涛
 * @Date 2020-03-11 23:28
 **/
@Transactional
@Service
public class BgmServiceImpl implements BgmService {
    @Resource
    private BgmMapper bgmMapper;



    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Bgm> queryBgmList() {
        Bgm bgm = new Bgm();
        BgmExample bgmExample = new BgmExample();
    // List<Bgm> bgms = new ArrayList<Bgm>();
        List<Bgm>   bgmss =  bgmMapper.selectByExample(bgmExample);
      /*  for(int i = 0; i < bgmss.size(); i++) {
            bgm = bgmss.get(i);
            bgms.add(bgm);
        }
*/
        return bgmss;
    }
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Bgm queryBgmId(String id) {
        return bgmMapper.selectByPrimaryKey(id);    }
}
