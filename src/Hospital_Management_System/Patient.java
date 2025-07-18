package Hospital_Management_System;

import com.mysql.cj.xdevapi.DeleteStatement;

import java.sql.*;
import java.util.Scanner;


public class Patient {
    private Connection connection;
    private Scanner scanner;

    public Patient(Connection connection , Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }

    public void addPatient(){
        System.out.print("Enter Patient's Name : ");
        String name = scanner.next();
        System.out.print("Enter Patient's Age : ");
        int age = scanner.nextInt();
        System.out.print("Enter Patient's Gender : ");
        String gender = scanner.next();

        try{
            String query = "INSERT INTO patients(name,age,gender) VALUES (?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,name);
            preparedStatement.setInt(2,age);
            preparedStatement.setString(3,gender);
            int affectedRows = preparedStatement.executeUpdate();
            if(affectedRows > 0){
                System.out.println("Patient Added Successfully...");
            } else{
                System.out.println("Failed To Add Patient!");
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void viewPatients(){
        String query = "select * from patients";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("PATIENTS -->");
            System.out.println("+------------+--------------------+---------+---------------+");
            System.out.println("| Patient Id | Name               | Age     | Gender        |");
            System.out.println("+------------+--------------------+---------+---------------+");
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");
                System.out.printf("|%-12s|%-20s|%-9s|%-15s\n",id,name,age,gender);
                System.out.println("+------------+--------------------+---------+---------------+");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public boolean getPatientById(int id){
        String query = "select * from patients WHERE id = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }
            else{
                return false;
            }
        }  catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
