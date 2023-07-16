package cc.ding.bankweb.controller;

import cc.ding.bankweb.model.Account;
import cc.ding.bankweb.model.LoginUser;
import cc.ding.bankweb.model.Result;
import cc.ding.bankweb.service.BankService;
import cc.ding.bankweb.util.AccountOverDrawnException;
import cc.ding.bankweb.util.InvalidDepositException;
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
import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

@RestController
@CrossOrigin
public class BankController {
    @Autowired
    private Producer producer;

    @Autowired
    private BankService bankService;

    @Autowired
    private StringRedisTemplate redis;

    @Autowired
    private TokenUtils tokenUtils;

    @RequestMapping("/captcha")
    public void getCaptcha(HttpServletResponse response) {
        // 准备 HTTP 响应的头部信息，不缓存图片
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");
        //生成验证码文本
        String code = producer.createText();
        System.out.println("code = " + code);
        //生成验证码图片
        BufferedImage bi = producer.createImage(code);

        //将验证码文本存储到redis
        redis.opsForValue().set(code, code, 2, TimeUnit.MINUTES);

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
        if (bankService.checkIfExists(user.getUsername())) {
            return "用户已存在";
        }
        if (Boolean.FALSE.equals(redis.hasKey(user.getCaptchaCode()))) {
            return "验证码错误";
        }
        return bankService.register(user.getUsername(), user.getPassword());
    }

    @PostMapping("/login")
    public Result login(@RequestBody LoginUser user) {
        if (!bankService.checkIfExists(user.getUsername())) {
            System.out.println("用户不存在，请先注册");
            return Result.err(404, "用户不存在，请先注册");
        }
        if (Boolean.FALSE.equals(redis.hasKey(user.getCaptchaCode()))) {
            System.out.println("验证码错误");
            return Result.err(Result.CODE_ERR_BUSINESS, "验证码错误");
        }
        if (bankService.login(user.getUsername(), user.getPassword()) != null) {
            System.out.println("登录成功");
            Account account = bankService.checkPwd(user.getUsername(), user.getPassword());
            System.out.println("account = " + account);
            String token = tokenUtils.loginSign(account);
            System.out.println("token = " + token);
            return Result.ok("登录成功", token);
        } else {
            System.out.println("密码错误");
            return Result.err(Result.CODE_ERR_BUSINESS, "密码错误");
        }
    }

    @GetMapping("/inquiry")
    public String inquiry(@RequestHeader("Token") String token) {
        System.out.println("inquiry exec");
        System.out.println("token = " + token);
        Account currentUser = tokenUtils.getCurrentUser(token);
        return bankService.inquiry(currentUser.getUsername()).toString();
    }

    @PostMapping("/withdraw")
    public String withdraw(String amount) {
        try {
            return bankService.withdrawals(new BigDecimal(amount));
        } catch (AccountOverDrawnException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/deposit")
    public String deposit(String amount) {
        try {
            return bankService.deposit(new BigDecimal(amount));
        } catch (InvalidDepositException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/transfer")
    public String transfer(String toName, String transMoney) {
        try {
            return bankService.transfer(toName, new BigDecimal(transMoney));
        } catch (AccountOverDrawnException e) {
            throw new RuntimeException(e);
        }
    }
}