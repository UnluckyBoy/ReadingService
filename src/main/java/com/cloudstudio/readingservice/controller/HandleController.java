package com.cloudstudio.readingservice.controller;

import com.cloudstudio.readingservice.model.UserInfo;
import com.cloudstudio.readingservice.service.UserInfoService;
import com.cloudstudio.readingservice.tool.PasswordUtil;
import com.cloudstudio.readingservice.tool.StringUtil;
import com.cloudstudio.readingservice.tool.VerificationUtil;
import com.cloudstudio.readingservice.tool.WebServerResponse;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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
    public void checkLogin(HttpServletResponse response,
                           @RequestParam("authorization") String authorization,
                           @RequestParam("account") String account,
                           @RequestParam("password") String password) throws IOException,Exception {
        /** @RequestBody Map<String, Object> requestBody **/
        // 从请求头中获取Token
        //String token = response.getHeader("Authorization");
        System.out.println("请求头_Token:"+authorization);
        if (StringUtil.isNullOrEmpty(authorization)) {
            Map<String,Object> requestBody=new HashMap<>();
            requestBody.put("account",account);
            requestBody.put("password",PasswordUtil.encode(hashKey,password));
            mUser=userInfoService.login(requestBody);
            if (mUser!=null){
                String responseToken= VerificationUtil.generateToken(mUser.getMAccount());
                System.out.println("登录请求_token:"+responseToken);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(gson.toJson(WebServerResponse.success("请求成功",responseToken,mUser)));

                redisTemplate.opsForHash().putAll(responseToken,CommonClass2Map(mUser));//写入Redis
                redisTemplate.expire(responseToken,10, TimeUnit.HOURS);
            }else{
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(gson.toJson(WebServerResponse.failure("请求失败")));
            }
        }else{
            authorization = authorization.substring(7);// 移除"Bearer "前缀
            try {
                // 解析Token（这里使用静态方法，但在实际应用中，你可能想要注入JwtUtil实例）
                // Claims claims = jwtUtil.parseToken(token);
                // 假设JwtUtil.parseToken返回一个Claims对象（这里只是模拟）
                // 你可以使用claims对象中的信息，例如用户名、角色等
                // 假设Token有效，继续处理请求
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(gson.toJson(WebServerResponse.success("请求成功",authorization,
                        redisTemplate.boundHashOps(authorization).entries())));
            } catch (Exception e) {
                // 如果Token无效（过期、签名不匹配等），返回错误响应
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(gson.toJson(WebServerResponse.failure("请求失败-Token无效")));
            }
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
