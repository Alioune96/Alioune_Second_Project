package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.JdbcTransfersDao;
import com.techelevator.tenmo.dao.TransferDao;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class TransferController {
    TransferDao transferDao;

    public TransferController(TransferDao transferDao){
        this.transferDao=transferDao;
    }


    @RequestMapping(path = "test2/{userId}", method = RequestMethod.GET)
    public Map<Integer,String>listofUser(@PathVariable int userId){
        return transferDao.listOf(userId);
    }


}
