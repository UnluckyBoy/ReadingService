package com.cloudstudio.readingservice.controller;

import com.cloudstudio.readingservice.model.UserInfo;
import com.cloudstudio.readingservice.service.UserInfoService;
import com.cloudstudio.readingservice.tool.PasswordUtil;
import com.cloudstudio.readingservice.tool.VerificationUtil;
import com.cloudstudio.readingservice.tool.WebServerResponse;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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


    @RequestMapping("/login")
    public void checkLogin(HttpServletResponse response,
                           @RequestParam("account") String account,
                           @RequestParam("password") String password) throws IOException,Exception {
        /** @RequestBody Map<String, Object> requestBody **/
        // 从请求头中获取Token
        String token = response.getHeader("Authorization");
        System.out.println("请求头_Token:"+token);
        if (token == null) {
            Map<String,Object> requestBody=new HashMap<>();
            requestBody.put("account",account);
            requestBody.put("password",PasswordUtil.encode(hashKey,password));
            mUser=userInfoService.login(requestBody);
            if (mUser!=null){
                String responseToken= VerificationUtil.generateToken(mUser.getMAccount());
                System.out.println("token:"+responseToken);
                response.setContentType("application/json;charset=UTF-8");
                response.setHeader("Authorization", "Bearer " + responseToken);
                response.getWriter().write(gson.toJson(WebServerResponse.success("请求成功",mUser)));
            }else{
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(gson.toJson(WebServerResponse.failure("请求失败")));
            }
        }else{
            token = token.substring(7);// 移除"Bearer "前缀
            try {
                // 解析Token（这里使用静态方法，但在实际应用中，你可能想要注入JwtUtil实例）
                // Claims claims = jwtUtil.parseToken(token);
                // 假设JwtUtil.parseToken返回一个Claims对象（这里只是模拟）
                // 你可以使用claims对象中的信息，例如用户名、角色等
                // 假设Token有效，继续处理请求
                response.setContentType("application/json;charset=UTF-8");
                response.setHeader("Authorization", "Bearer " + token);
                response.getWriter().write(gson.toJson(WebServerResponse.success("请求成功",mUser)));
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
}
