package cc.ding.bankweb.controller;

import cc.ding.bankweb.exception.AccountOverDrawnException;
import cc.ding.bankweb.exception.InvalidDepositException;
import cc.ding.bankweb.exception.InvalidWithdrawException;
import cc.ding.bankweb.model.Account;
import cc.ding.bankweb.model.Result;
import cc.ding.bankweb.service.AccountService;
import cc.ding.bankweb.service.LoginService;
import cc.ding.bankweb.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
public class AccountController {
    @Autowired
    private LoginService loginService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TokenUtils tokenUtils;

    @GetMapping("/curr-user")
    public Result getCurrUser(@RequestHeader("Token") String token) {
        Account account = tokenUtils.getAccount(token);

        Account dbAct = loginService.checkIfExists(account.getUsername());
        System.out.println("dbAct = " + dbAct);

        if (account != null) {
            return Result.ok("获取当前用户成功", dbAct);
        }
        return Result.err(Result.CODE_ERR_BUSINESS, "获取当前用户失败");
    }

    @GetMapping("/inquiry")
    public Result inquiry(@RequestHeader("Token") String token) {
        Account account = tokenUtils.getAccount(token);
        String balance = accountService.inquiry(account.getUsername()).toString();
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
            String withdrawals = accountService.withdrawals(account.getUsername(), new BigDecimal(amount));
            return Result.ok("取款成功", withdrawals);
        } catch (AccountOverDrawnException e) {
            return Result.err(Result.CODE_ERR_BUSINESS, "余额不足");
        } catch (InvalidWithdrawException e) {
            return Result.err(Result.CODE_ERR_BUSINESS, "取款金额不能为负");
        }
    }

    @PostMapping("/deposit")
    public Result deposit(@RequestHeader("Token") String token, @RequestBody Map<String, Double> payload) {
        Double amount = payload.get("amount");
        Account account = tokenUtils.getAccount(token);
        try {
            String deposit = accountService.deposit(account.getUsername(), new BigDecimal(amount));
            return Result.ok("存款成功", deposit);
        } catch (InvalidDepositException e) {
            return Result.err(Result.CODE_ERR_BUSINESS, "存款金额不合法");
        } catch (InvalidWithdrawException e) {
            return Result.err(Result.CODE_ERR_BUSINESS, "存款金额不能为负");

        }
    }

    @Transactional
    @PostMapping("/transfer")
    public Result transfer(@RequestHeader("Token") String token, @RequestBody Map<String, String> payload) {
        String toName = payload.get("toName");
        String transMoney = payload.get("transMoney");
        if (new BigDecimal(transMoney).signum()<0) {
            return Result.err(Result.CODE_ERR_BUSINESS, "转账不能为负");
        }

        Account account = tokenUtils.getAccount(token);
        String transfer;
        try {
            transfer = accountService.transfer(account.getUsername(), toName, new BigDecimal(transMoney));
            if (transfer.equals("转账成功")) {
                return Result.ok("转账成功", transfer);
            }
        } catch (AccountOverDrawnException e) {
            return Result.err(Result.CODE_ERR_BUSINESS, "余额不足");
        }
        return Result.err(Result.CODE_ERR_BUSINESS, transfer);
    }
}
