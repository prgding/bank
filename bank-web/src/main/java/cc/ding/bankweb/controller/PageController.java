package cc.ding.bankweb.controller;

import cc.ding.bankweb.model.Account;
import cc.ding.bankweb.model.Result;
import cc.ding.bankweb.page.MyPage;
import cc.ding.bankweb.service.PageService;
import cc.ding.bankweb.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;

@RestController
public class PageController {
    @Autowired
    private PageService pageService;
    @Autowired
    private TokenUtils tokenUtils;

    @RequestMapping("/users-amount")
    public Result totalRowNum() {
        int usersAmount = pageService.usersAmount();
        return Result.ok("ok", usersAmount);
    }

    @RequestMapping("/logs-amount")
    public Result logsAmount() {
        int logsAmount = pageService.logsAmount();
        return Result.ok("ok", logsAmount);
    }

    @RequestMapping("/users-page")
    public Result usersPage(@RequestBody MyPage page) {
        Page<Account> users = pageService.findUsers(page.getCurrentPage(), page.getPageSize());
        return Result.ok("ok", users);
    }

    @RequestMapping("/logs-page")
    public Result logsPage(@RequestBody MyPage page) {
        Page<Object[]> logs = pageService.findLogs(page.getCurrentPage(), page.getPageSize());
        ArrayList<HashMap<String, Object>> logsList = new ArrayList<>();
        logs.forEach(log -> {
            HashMap<String, Object> map = new HashMap<>();
            map.put("username", log[0]);
            map.put("logId", log[1]);
            map.put("logAmount", log[2]);
            map.put("logType", log[3]);
            map.put("userid", log[4]);
            logsList.add(map);
        });
        return Result.ok("ok", logsList);
    }

    @RequestMapping("/logs-one-page")
    public Result logsOnePage(@RequestHeader("Token") String token, @RequestBody MyPage page) {
        Account account = tokenUtils.getAccount(token);
        Integer id = account.getId();

        Page<Object[]> logs = pageService.findLogsByUserId(page.getCurrentPage(), page.getPageSize(), id);
        ArrayList<HashMap<String, Object>> logsList = new ArrayList<>();
        logs.forEach(log -> {
            HashMap<String, Object> map = new HashMap<>();
            map.put("username", log[0]);
            map.put("logId", log[1]);
            map.put("logAmount", log[2]);
            map.put("logType", log[3]);
            map.put("userid", log[4]);
            logsList.add(map);
        });
        return Result.ok("ok", logsList);
    }

    @RequestMapping("/logs-one-count")
    public Result logsOneCount(@RequestHeader("Token") String token) {
        Account account = tokenUtils.getAccount(token);
        Integer id = account.getId();

        int logsAmount = pageService.logsAmountByUserId(id);
        return Result.ok("ok", logsAmount);
    }
}
