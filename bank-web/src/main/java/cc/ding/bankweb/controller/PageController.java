package cc.ding.bankweb.controller;

import cc.ding.bankweb.model.Account;
import cc.ding.bankweb.model.Log;
import cc.ding.bankweb.model.Result;
import cc.ding.bankweb.page.MyPage;
import cc.ding.bankweb.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PageController {
    @Autowired
    private PageService pageService;

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
        Page<Log> logs = pageService.findLogs(page.getCurrentPage(), page.getPageSize());
        logs.forEach(System.out::println);
        return Result.ok("ok", logs);
    }
}
