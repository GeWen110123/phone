package com.phone;

import com.phone.common.config.RuoYiConfig;
import com.phone.module.controller.AccountFolderReader;
import com.phone.module.domain.Account;
import com.phone.module.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Order(1)//指定执行顺序
@Component
public class test implements ApplicationRunner {
    @Autowired
    private AccountFolderReader accountFolderReader;
    @Autowired
    private IAccountService accountService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
//
//        try {
//            String folderPath = RuoYiConfig.getProfile() + "/account";
//            List<Account> accounts = accountFolderReader.readAccountsFromFolder(folderPath);
//
//            System.out.println("解析完成，共 " + accounts.size() + " 条：");
//            for (Account account : accounts) {
//                System.out.println(account.getDouyinId() + " => " + account.getJsonString());
//                try {
//                    accountService.insertAccount(account);
//
//                } catch (Exception e) {
//                    System.out.println(e);
//                    account.setUpdateTime(new Date());
//                    accountService.updateAccount(account);
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}

