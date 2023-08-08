package cc.ding.bankweb.filter;

import cc.ding.bankweb.model.Result;
import com.alibaba.fastjson2.JSON;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class SecurityFilter implements Filter {

    private StringRedisTemplate stringRedisTemplate;

    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String path = request.getServletPath();
        System.out.print(path + ", " + request.getMethod() + ", ");

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "*");

        // Allow lists
        ArrayList<String> allowList = new ArrayList<>();
        allowList.add("/login");
        allowList.add("/register");
        allowList.add("/captcha-code");
        allowList.add("/captcha");
        allowList.add("/api/upload");
        allowList.add("/img");
        allowList.add("/favicon.ico");

        // Release these requests
        if (allowList.contains(path)||path.contains("/img")) {
            System.out.println("White list release");
            filterChain.doFilter(req, res);
            return;
        }

        String token = request.getHeader("Token");
        // Check Token
        if (StringUtils.hasText(token) && stringRedisTemplate.hasKey(token)) {
            System.out.println("Has token, release");
            filterChain.doFilter(req, res);
            return;
        }

        System.out.println("Others, block");
        // Then block others
        Result result = Result.err(Result.CODE_ERR_UNLOGINED, "未登陆");
        String jsonString = JSON.toJSONString(result);
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.write(jsonString);
        out.flush();
        out.close();
    }
}
