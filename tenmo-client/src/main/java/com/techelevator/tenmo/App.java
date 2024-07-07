package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import org.springframework.http.ResponseEntity;
import com.techelevator.tenmo.services.TransferService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import java.math.BigDecimal;

public class App {
    private final TransferService transferService = new TransferService();
    private final RestTemplate resttemplate = new RestTemplate();

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);

    private AuthenticatedUser currentUser;

    public void setAuthenticatedUser(AuthenticatedUser currentUser) {
        this.currentUser = currentUser;
    }

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
        }else {
            transferService.setAuthenticatedUser(currentUser);
            transferService.setAuthToken(currentUser.getToken());
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
            transferService.getTransferHistory(currentUser,resttemplate, consoleService, API_BASE_URL);
    }





    private void viewPendingRequests() {

        transferService.viewPendingRequests(currentBalance(),currentUser,resttemplate, consoleService, API_BASE_URL);



	}

	private void sendBucks() {
            String hello = transferService.sendMoneyToUser(currentUser,resttemplate,consoleService,API_BASE_URL,currentBalance());
        System.out.println(hello);

    }
	private void requestBucks() {
          String keepTHis =  transferService.confirmedStatus(currentBalance(),currentUser,resttemplate,API_BASE_URL,consoleService);
        System.out.println(keepTHis);
	}

    public BigDecimal currentBalance(){
        User currentuser  = currentUser.getUser();
        BigDecimal hold = resttemplate.getForObject(API_BASE_URL+"test/"+currentuser.getId(), BigDecimal.class);
        return hold;
    }

}
