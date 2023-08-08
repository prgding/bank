package cc.ding.bankweb.controller;

import cc.ding.bankweb.model.Account;
import cc.ding.bankweb.model.LoginUser;
import cc.ding.bankweb.model.Result;
import cc.ding.bankweb.service.LoginService;
import cc.ding.bankweb.util.TokenUtils;
import com.google.code.kaptcha.Producer;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

@RestController
@CrossOrigin
public class LoginController {
    @Autowired
    private Producer producer;

    @Autowired
    private LoginService loginService;

    @Autowired
    private StringRedisTemplate redis;

    @Autowired
    private TokenUtils tokenUtils;

    private String captchaCode;

    @RequestMapping("/captcha-code")
    public Result getCaptchaCode() {
        return Result.ok("获取验证码成功", captchaCode);
    }

    @RequestMapping("/captcha")
    public void getCaptcha(HttpServletResponse response) {
        // 准备 HTTP 响应的头部信息，不缓存图片
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");
        //生成验证码文本
        String code = producer.createText();
        captchaCode = code;
        System.out.println("code = " + code);
        //生成验证码图片
        BufferedImage bi = producer.createImage(code);

        //将验证码文本存储到redis
        redis.opsForValue().set(code, code, 5, TimeUnit.MINUTES);

        //将验证码图片响应给浏览器
        //输出图片
        try (OutputStream out = response.getOutputStream()) {
            ImageIO.write(bi, "jpg", out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/register")
    public String register(@RequestBody LoginUser user) {
        if (loginService.checkIfExists(user.getUsername()) != null) {
            return "用户已存在";
        }
        if (Boolean.FALSE.equals(redis.hasKey(user.getCaptchaCode()))) {
            return "验证码错误";
        }
        return loginService.register(user.getUsername(), user.getPassword());
    }

    @PostMapping("/login")
    public Result login(@RequestBody LoginUser user) {
        if (loginService.checkIfExists(user.getUsername()) == null) {
            System.out.println("用户不存在，请先注册");
            return Result.err(404, "用户不存在，请先注册");
        }
        if (Boolean.FALSE.equals(redis.hasKey(user.getCaptchaCode()))) {
            System.out.println("验证码错误");
            return Result.err(Result.CODE_ERR_BUSINESS, "验证码错误");
        }
        Account checkPwd = loginService.checkPwd(user.getUsername(), user.getPassword());
        if (checkPwd != null) {
            System.out.println("登录成功");
            String token = tokenUtils.loginSign(checkPwd);
            return Result.ok("登录成功", token);
        } else {
            System.out.println("密码错误");
            return Result.err(Result.CODE_ERR_BUSINESS, "密码错误");
        }
    }
}