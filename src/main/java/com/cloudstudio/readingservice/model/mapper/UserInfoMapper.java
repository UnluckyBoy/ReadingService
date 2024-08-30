package com.cloudstudio.readingservice.model.mapper;

import com.cloudstudio.readingservice.model.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @ClassName UserInfoMapper
 * @Author Create By matrix
 * @Date 2024/8/28 8:26
 */
@Mapper //mybatis的mapper类
@Repository //将mapper交由spring容器管理
public interface UserInfoMapper {
    UserInfo infoQuery(String account);//查询
    int login(Map<String,Object> map);//登录
    boolean register(Map<String,Object> map);//注册
    boolean fresh_login_status(Map<String,Object> map);//刷新登录信息
    boolean fresh_logout_status(Map<String,Object> map);//登出

    /***分页操作***/
    /*用户信息分页*/
//    @Select("select " +
//            "uId," +
//            "uHead," +
//            "uName," +
//            "uPassword," +
//            "uSex," +
//            "uAccount," +
//            "uPhone," +
//            "uEmail," +
//            "uLevel," +
//            "uStatus," +
//            "uAddressIp " +
//            "from user_info_data")
//    List<UserInfo> getPageUserInfo(@Param("pageNum") int pageNum,
//                                   @Param("pageSize") int pageSize);
}
