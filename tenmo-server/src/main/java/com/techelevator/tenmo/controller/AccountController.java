package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@RestController
public class AccountController {
    private AccountDao accountDao;

    public AccountController(AccountDao accountDao){
        this.accountDao = accountDao;
    }


    @RequestMapping(path = "/test", method = RequestMethod.GET)
    public BigDecimal forTest(){
      return   accountDao.getAllAccount();
    }

    @RequestMapping(path = "test/{userId}", method = RequestMethod.GET)
    public BigDecimal currentUserBalance(@PathVariable int userId ){
        return accountDao.getbalance(userId);

    }



}
