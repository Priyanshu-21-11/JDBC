package Banking_Management_System;

import java.sql.*;
import java.util.Scanner;



public class BankingApp {
    private static final String url = "jdbc:mysql://localhost:3306/the_royal_mint";
    private static final String username = "root";
    private static final String password = "priyanshu@";

    public static void main(String[] args) throws SQLException,ClassNotFoundException{
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            Scanner scanner = new Scanner(System.in);
            User user = new User(connection,scanner);
            Accounts accounts = new Accounts(connection,scanner);
            AccountManager accountManager = new AccountManager(connection,scanner);

            String email;
            long account_number;

            while(true){
                System.out.println();
                System.out.println("            ***************** ~THE_ROYAL_MINT~ *****************");
                System.out.println();
                System.out.println(".........Welcome To Banking System........");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Enter Your Choice : ");
                int choice1 = scanner.nextInt();
                switch (choice1){
                    case 1:
                        user.register();
                        break;
                    case 2:
                       email = user.login();
                       if(email != null){
                           System.out.println();
                           System.out.println("USER LOGGED IN...");
                           if(!accounts.account_exists(email)){
                               System.out.println();
                               System.out.println("1. Open a new Bank Account");
                               System.out.println("2. Exit");
                               System.out.print("Enter Your Choice : ");
                               if(scanner.nextInt() == 1){
                                  account_number = accounts.open_account(email);
                                   System.out.println("Account Created Successfully.");
                                   System.out.println("Your Account Number :"+ account_number);
                               }else{
                                   break;
                               }
                           }

                           account_number = accounts.getAccount_number(email);
                           int choice2 = 0;
                           while(choice2 != 5){
                               System.out.println();
                               System.out.println("1. Debit Money");
                               System.out.println("2. Credit Money");
                               System.out.println("3. Transfer Money");
                               System.out.println("4. Check Balance");
                               System.out.println("5. Log Out");
                               System.out.print("Enter Your Choice : ");
                               choice2 = scanner.nextInt();
                               switch (choice2){
                                   case 1:
                                       accountManager.debit_money(account_number);
                                       break;
                                   case 2:
                                       accountManager.credit_money(account_number);
                                       break;
                                   case 3:
                                       accountManager.transfer_money(account_number);
                                       break;
                                   case 4:
                                       accountManager.getBalance(account_number);
                                       break;
                                   case 5:
                                       System.out.println();
                                       System.out.println("Logged Out...");
                                       break;
                                   default:
                                       System.out.println("Invalid Choice! Try Again.");
                                       break;
                               }
                           }
                       }
                       else{
                           System.out.println("Incorrect Email Or Password !");
                       }
                    case 3:
                        System.out.println();
                        System.out.println("||||||THANK YOU FOR USING BANKING SYSTEM||||||");
                        System.out.println();
                        System.out.println("Exiting System.....");
                        return;
                    case 4:
                        System.out.println("Invalid Choice! Try Again.");
                        break;
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
