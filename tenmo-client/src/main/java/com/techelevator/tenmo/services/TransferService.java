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
import java.util.Map;

public class TransferService {




    public String sendMoneyToUser(AuthenticatedUser currentUser, RestTemplate resttemplate, ConsoleService consoleService, String API_BASE_URL, BigDecimal accountbalance){
        int currentUserId = currentUser.getUser().getId();
        boolean UserTrue = true;
        while (UserTrue) {
            int userId = currentUser.getUser().getId();
            System.out.println("Would you like to have you current balance displayed?");
            String displayBalance = consoleService.getScanner().nextLine();
            String userInput = displayBalance.toLowerCase();
            if (userInput.contains("yes")) {
                System.out.println("Current balance is: "+accountbalance);
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
            }
            System.out.println("Enter ID of user you are sending to (0 to cancel)");
            System.out.println("Enter amount: ");
            int amountToSend = 0;
            int userIDtosend = 0;
            String userAmount = consoleService.getScanner().nextLine();
            String sendId = consoleService.getScanner().nextLine();
            boolean isValidNumber = true;
            while (isValidNumber) {
                try {
                    userIDtosend = Integer.valueOf(sendId);
                    amountToSend = Integer.valueOf(userAmount);
                    break;
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
                    if (amountToSend <= 0) {
                        System.out.println("Please enter valid greater than 0");
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
                if (amountToSend >= 1) {
                    break;
                }
            }
            Transfers newTransfer = new Transfers();
            newTransfer.setAmount(BigDecimal.valueOf(amountToSend));
            newTransfer.setAccountTo(userIDtosend);
            newTransfer.setAccountFrom(currentUser.getUser().getId());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Transfers> entityTransfer = new HttpEntity<>(newTransfer,headers);
           String resulted =  resttemplate.postForObject(API_BASE_URL+"/test2",entityTransfer, String.class);
            return resulted;
        }
        return null;
    }
}