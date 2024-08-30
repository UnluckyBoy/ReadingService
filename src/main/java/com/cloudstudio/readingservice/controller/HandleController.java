package com.cloudstudio.readingservice.controller;

import com.cloudstudio.readingservice.model.UserInfo;
import com.cloudstudio.readingservice.service.UserInfoService;
import com.cloudstudio.readingservice.tool.*;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName HandleController
 * @Author Create By matrix
 * @Date 2024/8/28 8:10
 */
@Controller
@RequestMapping("/api")
public class HandleController {
    private final String hashKey="MATRIX_PASSWORD_HASH_CODE";
    private static Gson gson=new Gson();//Json数据对象
    private UserInfo mUser;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private RedisTemplate redisTemplate;


    @RequestMapping("/login")
    public void checkLogin(HttpServletResponse response, HttpServletRequest request,
                           @RequestParam("authorization") String authorization,
                           @RequestParam("account") String account,
                           @RequestParam("password") String password) throws IOException,Exception {
        /** @RequestBody Map<String, Object> requestBody **/

        String ip= IPUtil.getIpAddress(request);
        System.out.println("请求头_Token:"+authorization+" IP:"+ip);

        if (StringUtil.isNullOrEmpty(authorization)) {
            Map<String,Object> requestBody=new HashMap<>();
            requestBody.put("account",account);
            requestBody.put("password",PasswordUtil.encode(hashKey,password));
            int loginKey=userInfoService.login(requestBody);
            if (loginKey>0){
                String responseToken= VerificationUtil.generateToken(account+ip);
                System.out.println("登录请求_token:"+responseToken);
                Map<String,Object> freshMap=new HashMap<>();
                freshMap.put("account",account);
                freshMap.put("status",1);
                freshMap.put("addressIp",ip);
                boolean freshKey=userInfoService.fresh_login_status(freshMap);
                if(freshKey){
                    Map<String,Object> bodyMap=new HashMap<>();
                    bodyMap.put("authorization",responseToken);
                    bodyMap.put("account",account);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write(gson.toJson(WebServerResponse.success("登录成功",bodyMap)));
                    redisTemplate.opsForHash().putAll(responseToken,bodyMap);
//                    response.getWriter().write(gson.toJson(WebServerResponse.success("请求成功",responseToken,mUser)));
//                    redisTemplate.opsForHash().putAll(responseToken,CommonClass2Map(mUser));//写入Redis
                    redisTemplate.expire(responseToken,5, TimeUnit.MINUTES);//缓存存在5min
                }else{
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write(gson.toJson(WebServerResponse.failure("登录异常")));
                }
            }else{
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(gson.toJson(WebServerResponse.failure("登录失败")));
            }
        }else{
            authorization = authorization.substring(7);// 移除"Bearer "前缀
            try {
                // 解析Token(这里使用静态方法，但在实际应用中，你可能想要注入JwtUtil实例）
                Claims claims = VerificationUtil.parseToken(authorization);
                // 假设JwtUtil.parseToken返回一个Claims对象（这里只是模拟）
                // 你可以使用claims对象中的信息，例如用户名、角色等
                // 假设Token有效，继续处理请求
                if(!(claims.isEmpty())){
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write(gson.toJson(WebServerResponse.success("登录成功",authorization,
                            redisTemplate.boundHashOps(authorization).entries())));
                }
            } catch (Exception e) {
                // 如果Token无效（过期、签名不匹配等），返回错误响应
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(gson.toJson(WebServerResponse.abnormal("登录失败-Token无效")));
            }
        }
    }

    @RequestMapping("/logout")
    public void doLogout(HttpServletResponse response,
                         HttpServletRequest request,
                         @RequestParam("account") String account) throws IOException,Exception {
        //String ip= IPUtil.getIpAddress(request);
        Map<String,Object> logoutMap=new HashMap<>();
        logoutMap.put("status",0);
        logoutMap.put("account",account);
        boolean logoutKey=userInfoService.fresh_logout_status(logoutMap);
        if(logoutKey){
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(gson.toJson(WebServerResponse.success("登出成功")));
        }else{
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(gson.toJson(WebServerResponse.failure("登出异常")));
        }
    }
    @RequestMapping("/query_user")
    public void getUserInfo(HttpServletResponse response,
                         HttpServletRequest request,
                         @RequestParam("account") String account) throws IOException,Exception {
        //String ip= IPUtil.getIpAddress(request);
        mUser=userInfoService.infoQuery(account);
        if(mUser!=null){
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(gson.toJson(WebServerResponse.success("查询成功",mUser)));
        }else{
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(gson.toJson(WebServerResponse.failure("查询异常")));
        }
    }

    @RequestMapping("/test_password")
    public void testPassword(HttpServletResponse response,@RequestParam("password") String password) throws IOException,Exception {
        String encryptedText = PasswordUtil.encode(hashKey,password);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(
                gson.toJson(WebServerResponse.success("测试_原密码:"+password+"_加密:"+encryptedText)));
        System.out.println("测试_原密码:"+password+"_加密:"+encryptedText);
    }

    /**
     * 类转Map方法
     * @param temp
     * @return
     */
    public Map CommonClass2Map(UserInfo temp){
        Map<String,Object> tempMap=new HashMap();
        tempMap.put("id",temp.getMId());
        tempMap.put("head",temp.getMHead());
        tempMap.put("name",temp.getMName());
        tempMap.put("account",temp.getMAccount());
        tempMap.put("sex",temp.getMSex());
        tempMap.put("phone",temp.getMPhone());
        tempMap.put("email",temp.getMEmail());
        tempMap.put("level",temp.getMLevel());
        tempMap.put("status",temp.getMStatus());
        tempMap.put("ip",temp.getMAddressIp());

        return tempMap;
    }
}
