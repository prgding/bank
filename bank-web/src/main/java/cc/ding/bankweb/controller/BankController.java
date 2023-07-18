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
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@CrossOrigin()
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
        Account checkPwd = bankService.checkPwd(user.getUsername(), user.getPassword());
        if (checkPwd != null) {
            System.out.println("登录成功");
            String token = tokenUtils.loginSign(checkPwd);
            return Result.ok("登录成功", token);
        } else {
            System.out.println("密码错误");
            return Result.err(Result.CODE_ERR_BUSINESS, "密码错误");
        }
    }

    @GetMapping("/curr-user")
    public Result getCurrUser(@RequestHeader("Token") String token) {
        System.out.println("exec");
        Account account = tokenUtils.getAccount(token);
        if (account != null) {
            return Result.ok("获取当前用户成功", account);
        }
        return Result.err(Result.CODE_ERR_BUSINESS, "获取当前用户失败");
    }

    @GetMapping("/inquiry")
    public Result inquiry(@RequestHeader("Token") String token) {
        Account account = tokenUtils.getAccount(token);
        String balance = bankService.inquiry(account.getUsername()).toString();
        if (balance != null) {
            return Result.ok("查询成功", balance);
        }
        return Result.err(Result.CODE_ERR_BUSINESS, "查询失败");
    }

    @PostMapping("/withdraw")
    public Result withdraw(@RequestHeader("Token") String token, @RequestBody Map<String, Double> payload) {
        System.out.println("payload = " + payload);
        Double amount = payload.get("amount");
        System.out.println("amount = " + amount);
        Account account = tokenUtils.getAccount(token);
        try {
            String withdrawals = bankService.withdrawals(account.getUsername(), new BigDecimal(amount));
            return Result.ok("取款成功", withdrawals);
        } catch (AccountOverDrawnException e) {
            return Result.err(Result.CODE_ERR_BUSINESS, "余额不足");
        }
    }

    @PostMapping("/deposit")
    public Result deposit(@RequestHeader("Token") String token, @RequestBody Map<String, Double> payload) {
        Double amount = payload.get("amount");
        Account account = tokenUtils.getAccount(token);
        try {
            String deposit = bankService.deposit(account.getUsername(), new BigDecimal(amount));
            return Result.ok("存款成功", deposit);
        } catch (InvalidDepositException e) {
            return Result.err(Result.CODE_ERR_BUSINESS, "存款金额不合法");
        }
    }

    @PostMapping("/transfer")
    public Result transfer(@RequestHeader("Token") String token, @RequestBody Map<String, String> payload) {
        String toName = payload.get("toName");
        String transMoney = payload.get("transMoney");
        Account account = tokenUtils.getAccount(token);
        String transfer;
        try {
            transfer = bankService.transfer(account.getUsername(), toName, new BigDecimal(transMoney));
            if (transfer.equals("转账成功")) {
                return Result.ok("转账成功", transfer);
            }
        } catch (AccountOverDrawnException e) {
            return Result.err(Result.CODE_ERR_BUSINESS, "余额不足");
        }
        return Result.err(Result.CODE_ERR_BUSINESS, transfer);
    }
}