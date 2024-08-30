package com.cloudstudio.readingservice.service.Ipml;

import com.cloudstudio.readingservice.model.UserInfo;
import com.cloudstudio.readingservice.model.mapper.UserInfoMapper;
import com.cloudstudio.readingservice.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @ClassName UserServiceIpml
 * @Author Create By matrix
 * @Date 2024/8/28 8:25
 */
@Service("UserInfoService")
public class UserServiceIpml implements UserInfoService {
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public UserInfo infoQuery(String account) {
        return userInfoMapper.infoQuery(account);
    }

    @Override
    public int login(Map<String, Object> map) {
        return userInfoMapper.login(map);
    }

    @Override
    public boolean register(Map<String, Object> map) {
        return userInfoMapper.register(map);
    }

    @Override
    public boolean fresh_login_status(Map<String,Object> map) {
        return userInfoMapper.fresh_login_status(map);
    }

    @Override
    public boolean fresh_logout_status(Map<String, Object> map) {
        return userInfoMapper.fresh_logout_status(map);
    }
}
