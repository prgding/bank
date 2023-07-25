package cc.ding.bankweb.controller;

import cc.ding.bankweb.model.Account;
import cc.ding.bankweb.model.Result;
import cc.ding.bankweb.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin
public class AdminController {

    @Autowired
    private AdminService adminService;

    @RequestMapping("/user-list")
    public Result userList() {
        List<Account> all = adminService.userList();
        ArrayList<HashMap<String, Object>> formatList = new ArrayList<>();
        all.forEach((account) -> {
            HashMap<String, Object> map = new HashMap<>();
            map.put("username", account.getUsername());
            map.put("balance", account.getBalance());
            map.put("flag", account.getUserFlag());
            formatList.add(map);
        });
        System.out.println(formatList);
        return Result.ok("ok", formatList);
    }

    @RequestMapping("/log-list")
    public Result logList() {
        List<Object[]> allLog = adminService.logList();
        System.out.println("allLog = " + allLog);
        ArrayList<HashMap<String, Object>> formatList = new ArrayList<>();
        for (Object[] objects : allLog) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("username", objects[0]);
            map.put("amount", objects[2]);
            map.put("type", objects[3]);
            formatList.add(map);
        }
        System.out.println(formatList);
        return Result.ok("ok", formatList);
    }
}