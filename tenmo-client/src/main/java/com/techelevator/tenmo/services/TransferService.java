package com.techelevator.tenmo.services;

import com.techelevator.tenmo.App;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfers;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class TransferService {




    public String sendMoneyToUser(AuthenticatedUser currentUser, RestTemplate resttemplate, ConsoleService consoleService, String API_BASE_URL, BigDecimal accountbalance){
        int currentUserId = currentUser.getUser().getId();
        String resulted = "";
        boolean UserTrue = true;
        int amountToSend = 0;
        int userIDtosend = 0;
        while (UserTrue) {
            int userId = currentUser.getUser().getId();

            System.out.println("Current balance is: " + accountbalance);
            Map<Integer, String> mapOfUser = resttemplate.getForObject(API_BASE_URL + "test2/" + userId, Map.class);
            String displayUser = "";
            for (int i = 0; i < 1; i++) {
                displayUser += mapOfUser.entrySet();
                displayUser += "\t";
                String newDisplay = displayUser.replace("=", "    ");
                String replaceAgain = newDisplay.replace(",", "\n");
                String oneLast = replaceAgain.replace("[", " ");
                String oneLastOne = oneLast.replace("]", " ");
                System.out.println("----------------------------------------------");
                System.out.println(" Users\n  Id" + "      name");
                System.out.println(oneLastOne);
                System.out.println("------\n" + "\n");
                displayUser = "";
            }

            System.out.println("Enter ID of user you are sending to (0 to cancel): ");
            System.out.println("Enter amount: ");
            String sendId = consoleService.getScanner().nextLine();
           String userAmount = consoleService.getScanner().next();
            boolean isValidNumber = true;
            while (isValidNumber) {
                try {

                    userIDtosend = Integer.valueOf(sendId);
                    amountToSend = Integer.valueOf(userAmount);
                    isValidNumber = false;
                } catch (NumberFormatException e) {
                    System.out.println("Please provide us with an valid Amount and an UserId");
                    userAmount = consoleService.getScanner().nextLine();
                    sendId = consoleService.getScanner().nextLine();
                }
            }
            if (userIDtosend == 0) {
                System.out.println("Good-bye");
                UserTrue = false;
            }
            Boolean stillHere = true;
            while (stillHere) {
                if (userIDtosend == currentUserId) {
                    System.out.println("Please don't use your ID");
                    try {
                        sendId = consoleService.getScanner().nextLine();
                        userIDtosend = Integer.valueOf(sendId);
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Please try again!");
                        sendId = consoleService.getScanner().nextLine();
                    }
                }

                stillHere = false;
                // TODO Auto-generated method stub
            }
            boolean miniLoop = true;
            boolean secondCheck = true;
            while (secondCheck) {
                while (miniLoop) {
                    int firstCheck = accountbalance.intValue();
                    if (amountToSend <= 0 || amountToSend >= firstCheck) {
                        System.out.println("**** Please enter an amount that less than you're balance or a amount that greater than 0 ****");
                        try {
                            userAmount = consoleService.getScanner().nextLine();
                            amountToSend = Integer.valueOf(userAmount);
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println("Please enter an number");
                            userAmount = consoleService.getScanner().nextLine();
                        }
                    }
                    break;
                }

                int finalCheck = accountbalance.intValue();
                if (amountToSend >= 1 && amountToSend <= finalCheck) {
                    break;
                } else {
                    System.out.println("Please try again");
                }
            }

        }
            Transfers newTransfer = new Transfers();
            newTransfer.setAmount(BigDecimal.valueOf(amountToSend));
            newTransfer.setAccountTo(userIDtosend);
            newTransfer.setAccountFrom(currentUser.getUser().getId());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Transfers> entityTransfer = new HttpEntity<>(newTransfer,headers);
           resulted =  resttemplate.postForObject(API_BASE_URL+"/test2",entityTransfer, String.class);


        return resulted;

    }

    public String confirmedStatus(BigDecimal currentbalance, AuthenticatedUser user, RestTemplate resttemplate, String API_BASE_URL, ConsoleService consoleService){

        System.out.println("Here is you current balance: "+ currentbalance);
        boolean userTrue = true;
        int currentUserId= user.getUser().getId();
        while(userTrue) {
            Map<Integer, String> userToTenmo = new HashMap<>();
            userToTenmo = resttemplate.getForObject(API_BASE_URL + "test2/" + user.getUser().getId(), Map.class);
            String displayUser = "";
            for (int i = 0; i < userToTenmo.size(); i++) {
                displayUser += userToTenmo.entrySet();
                displayUser += "\t";
                String newDisplay = displayUser.replace("=", "    ");
                String replaceAgain = newDisplay.replace(",", "\n");
                String oneLast = replaceAgain.replace("[", " ");
                String oneLastOne = oneLast.replace("]", " ");
                System.out.println("----------------------------------------------");
                System.out.println(" Users\n  Id" + "      name");
                System.out.println(oneLastOne);
                System.out.println("------\n" + "\n");
                displayUser = "";
            }
            System.out.println("Enter ID of user you are requesting fom (0 to cancel");
            System.out.println("Enter amount:");
            String userId = consoleService.getScanner().nextLine();
            String amount = consoleService.getScanner().nextLine();
            int amountToSend = 0;
            int userIDtosend = 0;
            boolean isValidNumber = true;
            while (isValidNumber) {
                try {
                    userIDtosend = Integer.valueOf(userId);
                    amountToSend = Integer.valueOf(amount);
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Please provide us with an valid Amount and an UserId");
                    userId = consoleService.getScanner().nextLine();
                    amount = consoleService.getScanner().nextLine();
                }
            }
            if (userIDtosend == 0) {
                System.out.println("Good-bye");
                userTrue = false;
            }
            Boolean stillHere = true;
            while (stillHere) {
                if (userIDtosend == currentUserId) {
                    System.out.println("Please don't use your ID");
                    try {
                        userId = consoleService.getScanner().nextLine();
                        userIDtosend = Integer.valueOf(userId);
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Please try again!");
                        userId = consoleService.getScanner().nextLine();
                    }
                }

                stillHere = false;
                // TODO Auto-generated method stub
            }
            boolean miniLoop = true;
            boolean secondCheck = true;
            while (secondCheck) {
                while (miniLoop) {
                    if (amountToSend <= 0) {
                        System.out.println("Please check you're amount, you're amount is an invalid amount");
                        try {
                            amount = consoleService.getScanner().nextLine();
                            amountToSend = Integer.valueOf(amount);
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println("Please enter an number");
                            amount = consoleService.getScanner().nextLine();
                        }
                    }
                    break;
                }
                if (amountToSend >= 1) {
                    break;
                }
            }

            Transfers transferRequest = new Transfers();
            System.out.println("save me");

            transferRequest.setAccountFrom(user.getUser().getId());
            transferRequest.setAccountTo(userIDtosend);
            transferRequest.setAmount(BigDecimal.valueOf(amountToSend));
            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Transfers>newTransfer = new HttpEntity<>(transferRequest,header);
            String approve = "";
            approve = resttemplate.postForObject(API_BASE_URL+"test2/tryThis",newTransfer, String.class);

return "*"+" "+approve+" "+"*";
        }

        return null;
    }

    public void lisOfRequest(BigDecimal userBalance){
        System.out.println("Here you current balance: "+ userBalance);

    }
}