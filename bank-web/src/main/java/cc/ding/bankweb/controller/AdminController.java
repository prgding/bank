package cc.ding.bankweb.controller;

import cc.ding.bankweb.model.Account;
import cc.ding.bankweb.model.Log;
import cc.ding.bankweb.model.Result;
import cc.ding.bankweb.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
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
            map.put("id", account.getId());
            map.put("username", account.getUsername());
            map.put("balance", account.getBalance());
            map.put("userFlag", account.getUserFlag());
            formatList.add(map);
        });
        return Result.ok("ok", formatList);
    }

    @RequestMapping("/log-list")
    public Result logList() {
        List<Object[]> allLog = adminService.logList();
        ArrayList<HashMap<String, Object>> formatList = new ArrayList<>();
        for (Object[] objects : allLog) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("username", objects[0]);
            map.put("logId", objects[1]);
            map.put("logAmount", objects[2]);
            map.put("logType", objects[3]);
            formatList.add(map);
        }
        return Result.ok("ok", formatList);
    }

    @RequestMapping("/freeze")
    public Result freeze(@RequestBody Account act) {
        adminService.freeze(act.getId());
        return Result.ok("冻结成功");
    }

    @RequestMapping("/unfreeze")
    public Result unfreeze(@RequestBody Account act) {
        adminService.unfreeze(act.getId());
        return Result.ok("解冻成功");
    }

    @RequestMapping("/user-change")
    public Result userChange(@RequestBody Account act) {
        adminService.updateUser(act);
        return Result.ok("修改成功");
    }

    @RequestMapping("/user-del")
    public Result userDelete(@RequestBody Account act) {
        adminService.deleteUser(act.getId());
        return Result.ok("删除成功");
    }

    @RequestMapping("/log-change")
    public Result logChange(@RequestBody Log log) {
        adminService.updateLog(log);
        return Result.ok("修改成功");
    }

    @RequestMapping("/log-del")
    public Result logDelete(@RequestBody Log log) {
        adminService.deleteLog(log.getLogId());
        return Result.ok("删除成功");
    }
}