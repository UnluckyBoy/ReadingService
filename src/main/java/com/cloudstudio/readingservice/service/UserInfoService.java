package com.cloudstudio.readingservice.service;

import com.cloudstudio.readingservice.model.UserInfo;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @ClassName UserInfoService
 * @Author Create By matrix
 * @Date 2024/8/27 22:57
 */
@Service
public interface UserInfoService {
    UserInfo infoQuery(Map<String,Object> map);;
    UserInfo login(Map<String,Object> map);
    boolean register(Map<String,Object> map);
}
