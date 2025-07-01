package Banking_Management_System;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.DoubleSummaryStatistics;
import java.util.Scanner;

public class AccountManager {
    private Connection connection;
    private Scanner scanner;
    public AccountManager(Connection connection,Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }

    public void credit_money(long account_number)throws SQLException{
        scanner.nextLine();
        System.out.print("Enter Amount :");
        Double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Security Pin :");
        String security_pin = scanner.nextLine();

        try {
            connection.setAutoCommit(false);
            if(account_number != 0) {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Accounts WHERE account_number = ? AND security_pin = ?");
                preparedStatement.setLong(1,account_number);
                preparedStatement.setString(2,security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if(resultSet.next()){
                    String credit_query = "UPDATE Accounts SET balance = balance + ? WHERE account_number = ?";
                    PreparedStatement preparedStatement1 = connection.prepareStatement(credit_query);
                    preparedStatement1.setDouble(1,amount);
                    preparedStatement1.setLong(2,account_number);
                    int affectedRows = preparedStatement.executeUpdate();
                    if(affectedRows > 0){
                        System.out.println("Rs."+amount+" Credited Successfully...");
                        connection.commit();
                        connection.setAutoCommit(true);
                        return;
                    } else{
                        System.out.println("Transcation Failed!");
                        connection.rollback();
                        connection.setAutoCommit(true);
                    }
                }else{
                    System.out.println("Invalid Security Pin! Try Again.");
                }
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }

    public void debit_money(long account_number) throws SQLException{
        scanner.nextLine();
        System.out.print("Enter Amount :");
        Double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Security Pin :");
        String security_pin = scanner.nextLine();
        try {
            connection.setAutoCommit(false);
            if(account_number != 0) {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Accounts WHERE account_number = ? AND security_pin");
                preparedStatement.setLong(1,account_number);
                preparedStatement.setString(2,security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next()){
                    double current_balance = resultSet.getDouble("balance");
                    if(amount <= current_balance) {
                        String debit_query = "UPDATE Accounts SET balance = balance - ? WHERE account_number = ? AND security_pin = ?";
                        PreparedStatement preparedStatement1 = connection.prepareStatement(debit_query);
                        preparedStatement1.setLong(1, account_number);
                        preparedStatement.setString(2, security_pin);
                        int affectedRows = preparedStatement1.executeUpdate();
                        if (affectedRows > 0) {
                            System.out.println("Rs."+amount+"Debited Successfully.");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        }else{
                            System.out.println("Transaction Failed!");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }else{
                        System.out.println("Insufficient Balance !!!!!");
                    }
                } else{
                    System.out.println("Invalid Security Pin!");
                }
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }

    public void transfer_money(long sender_account_number) throws SQLException{
        scanner.nextLine();
        System.out.print("Enter Receiver Account Number :");
        long receiver_account_number = scanner.nextLong();
        System.out.print("Enter Amount :");
        Double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Security Pin :");
        String security_pin = scanner.nextLine();
        try{
            connection.setAutoCommit(false);
            if(sender_account_number != 0 && receiver_account_number != 0){
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Accounts WHERE sender_account_number = ? AND security_pin = ?");
                preparedStatement.setLong(1,sender_account_number);
                preparedStatement.setString(2,security_pin);
               ResultSet resultSet = preparedStatement.executeQuery();
               if(resultSet.next()){
                   double current_balance = resultSet.getDouble("balance");
                   if(amount <= current_balance){
                       String credit_query = "UPDATE Accounts SET balance = balance + ? WHERE account_number = ?";
                       String debit_query = "UPDATE Accounts SET balance = balance - ? WHERE account_number = ?";
                       PreparedStatement creditpreparedStatement = connection.prepareStatement(credit_query);
                       PreparedStatement debitpreparedStatement = connection.prepareStatement(debit_query);
                       creditpreparedStatement.setDouble(1,amount);
                       creditpreparedStatement.setLong(2,receiver_account_number);
                       debitpreparedStatement.setDouble(1,amount);
                       debitpreparedStatement.setLong(2,sender_account_number);
                       int rowsAffected1 = creditpreparedStatement.executeUpdate();
                       int rowsAffected2 = debitpreparedStatement.executeUpdate();
                       if(rowsAffected2 > 0 && rowsAffected1 > 0){
                           System.out.println("TRANSACTION SUCCESSFUL...");
                           connection.commit();
                           connection.setAutoCommit(true);
                           return;
                       } else {
                           System.out.println("TRANSACTION FAILED!!");
                           connection.rollback();
                           connection.setAutoCommit(true);
                       }
                   } else{
                       System.out.println("INSUFFICIENT BALANCE!");
                   }
               } else{
                   System.out.println("INVALID SECURITY PIN");
               }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }


    public void getBalance(long account_number) throws SQLException{
        scanner.nextLine();
        System.out.print("Enter Security Pin :");
        String security_pin = scanner.nextLine();
        try {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT balance FROM Accounts WHERE account_number = ? AND security_pin = ?");
            preparedStatement.setLong(1,account_number);
            preparedStatement.setString(2,security_pin);
            ResultSet resultSet = preparedStatement.executeQuery();
           if(resultSet.next()){
              double balance = resultSet.getDouble("balance");
               System.out.println("Balance :" +balance);
           }else{
               System.out.println("Invalid Pin!");
           }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
