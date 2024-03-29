package cc.ding.bankweb.util;


import cc.ding.bankweb.exception.BusinessException;
import cc.ding.bankweb.model.Account;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * token工具类
 */
@Component
public class TokenUtils {

    private static final String CLAIM_NAME_ID = "CLAIM_NAME_ID";
    private static final String CLAIM_NAME_BALANCE = "CLAIM_NAME_BALANCE";
    private static final String CLAIM_NAME_PASSWORD = "CLAIM_NAME_PASSWORD";
    private static final String CLAIM_NAME_USERNAME = "CLAIM_NAME_USERNAME";
    private static final String CLAIM_NAME_USERFLAG = "CLAIM_NAME_USERFLAG";
    private static final String CLAIM_NAME_IMAGEURL = "CLAIM_NAME_IMAGEURL";
    //注入redis模板
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    //注入配置文件中的warehouse.expire-time属性 -- token的过期时间
    @Value("${token.expire-time}")
    private int expireTime;

    private String sign(Account account) {
        return JWT.create()
                .withClaim(CLAIM_NAME_ID, account.getId())
                .withClaim(CLAIM_NAME_BALANCE, String.valueOf(account.getBalance()))
                .withClaim(CLAIM_NAME_PASSWORD, account.getPassword())
                .withClaim(CLAIM_NAME_USERNAME, account.getUsername())
                .withClaim(CLAIM_NAME_USERFLAG, account.getUserFlag())
                .withClaim(CLAIM_NAME_IMAGEURL, account.getImageUrl())
                .withIssuedAt(new Date())//发行时间
                .withExpiresAt(new Date(System.currentTimeMillis() + expireTime * 1000L))//有效时间
                .sign(Algorithm.HMAC256(account.getPassword()));
    }

    /**
     * 将当前用户信息以用户密码为密钥生成token的方法
     */
    public String loginSign(Account account) {
        //生成token
        String token = sign(account);
        //将token保存到redis中,并设置token在redis中的过期时间
        stringRedisTemplate.opsForValue().set(token, token, expireTime * 2L, TimeUnit.SECONDS);
        return token;
    }

    /**
     * 从客户端归还的token中获取用户信息的方法
     */
    public Account getAccount(String token) {
        if (StringUtils.isEmpty(token)) {
            throw new BusinessException("令牌为空，请登录！");
        }
        //对token进行解码,获取解码后的token
        DecodedJWT decodedJWT;
        try {
            decodedJWT = JWT.decode(token);
        } catch (JWTDecodeException e) {
            throw new BusinessException("令牌格式错误，请登录！");
        }
        //从解码后的token中获取用户信息并封装到CurrentUser对象中返回
        Integer id = decodedJWT.getClaim(CLAIM_NAME_ID).asInt();
        BigDecimal balance = new BigDecimal(decodedJWT.getClaim(CLAIM_NAME_BALANCE).asString());
        String password = decodedJWT.getClaim(CLAIM_NAME_PASSWORD).asString();
        String userName = decodedJWT.getClaim(CLAIM_NAME_USERNAME).asString();
        Integer userFlag = decodedJWT.getClaim(CLAIM_NAME_USERFLAG).asInt();
        String imageUrl = decodedJWT.getClaim(CLAIM_NAME_IMAGEURL).asString();
        System.out.println("imageUrl = " + imageUrl);

        if (StringUtils.isEmpty(userName)) {
            throw new BusinessException("令牌缺失用户信息，请登录！");
        }
        return new Account(id, userName, password, balance, userFlag, imageUrl);
    }
}