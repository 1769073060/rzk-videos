package com.rzk.service.impl;

import com.rzk.mapper.PunishUsersMapper;
import com.rzk.mapper.UsersMapper;
import com.rzk.pojo.PunishUsers;
import com.rzk.pojo.Users;
import com.rzk.service.PunishUsersService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Auther 刘瑞涛
 * @Date 2020-05-08 11:28
 **/
@Transactional
@Service
public class PunishUsersServiceImpl implements PunishUsersService {
    @Resource
    private UsersMapper usersMapper;
    @Resource
    private PunishUsersMapper punishUsersMapper;
    @Transactional(propagation = Propagation.SUPPORTS )
    @Override
    public Integer selectByDate(String id) {
        Users users = new Users();
        Users users1 = new Users();
        PunishUsers punishUsers =  punishUsersMapper.selectMyLikeVideos(id);

        users1= usersMapper.selectByPrimaryKey(id);
            if (punishUsers==null||users1.getUserStatus()==1){
                    return 1;
        }else {

                SimpleDateFormat f = new SimpleDateFormat( "yyyy:MM:dd:HH:mm:ss");
                Date date = new Date();
                long diff = date.getTime() -  punishUsers.getStartTime().getTime();//这样得到的差值是毫秒级别
                long days = diff / (1000 * 60 * 60 * 24);
              /*  System.out.println("相减："+days);*/
       /*         System.out.println(  "id:"+   punishUsers.getId()
            +"time:"  + f.format(punishUsers.getStartTime()) +"day"+
                punishUsers.getViolation());*/
                if ((int)days- punishUsers.getViolation()>=0){
                    users.setId(id);
                    users.setUserStatus(1);
                    usersMapper.updateByPrimaryKeySelective(users);
                    return 1;
                }
            }

        return 0;
    }
}
