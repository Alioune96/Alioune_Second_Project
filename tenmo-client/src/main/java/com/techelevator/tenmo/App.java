package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import java.math.BigDecimal;

public class App {
    private final RestTemplate resttemplate = new RestTemplate();

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);

    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();


        if (authenticationService.register(credentials)) {

            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

	private void viewCurrentBalance() {

        User currentuser  = currentUser.getUser();
        BigDecimal userBalance = resttemplate.getForObject(API_BASE_URL+"test/"+currentuser.getId(), BigDecimal.class);
        System.out.println("Your current account balance is: "+userBalance);


	}

	private void viewTransferHistory() {

    }



	private void viewPendingRequests() {



	}

	private void sendBucks() {
        int userId = currentUser.getUser().getId();
        System.out.println("Would you like to have you current balance displayed?");
        String displayBalance = consoleService.getScanner().nextLine();
        String userInput = displayBalance.toLowerCase();
        if(userInput.contains("yes")) {
            currentBalance();
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
            while (isValidNumber){
                try{
                    userIDtosend = Integer.valueOf(sendId);
                    amountToSend = Integer.valueOf(userAmount);
                    isValidNumber=false;
            }catch (NumberFormatException e){
                    System.out.println("Please provide us with an valid Amount and an UserId");
                    userAmount=consoleService.getScanner().nextLine();
                    sendId=consoleService.getScanner().nextLine();
                }
            }
            if(userIDtosend==0){
                System.out.println("Good-bye");
                mainMenu();
            }


		// TODO Auto-generated method stub

	}

	private void requestBucks() {
		// TODO Auto-generated method stub

	}

    public void currentBalance(){
        User currentuser  = currentUser.getUser();
        BigDecimal hold = resttemplate.getForObject(API_BASE_URL+"test/"+currentuser.getId(), BigDecimal.class);
        System.out.println("Current balance "+hold);
    }

}
