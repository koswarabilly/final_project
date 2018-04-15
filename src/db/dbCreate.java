/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author DMI3
 */
public class dbCreate {
    public static Connection connectAndCreateTableCus() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:./hotelsystem.sqlite");
            Statement stmt = conn.createStatement();
            String sql = "create table if not exists customer (" +
                            "name varchar(100) not null, " +
                            "checkindt varchar(100) not null, " +
                            "checkoutdt varchar(100) not null,"+
                            "room integer PRIMARY KEY" +
                            ")";
           

            stmt.execute(sql);
        } catch (SQLException e ) {
            e.printStackTrace();
        }
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:./hotelsystem.sqlite");
            Statement stmt = conn.createStatement();
            
            String sqlw = "create table if not exists worker (" +
                            "id varchar(100) not null PRIMARY KEY, " +
                            "name varchar(100) not null, " +
                            "position varchar(100) not null,"+
                            "since varchar(100) not null" +
                            ")";

            stmt.execute(sqlw);
        } catch (SQLException e ) {
            e.printStackTrace();
        }

        return conn;
    }
    
    public static void deleteByGender(Connection conn, Character gender) {
        try {
            String sql = "delete from Person where gender = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, gender.toString());
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void closeDatabase(Connection conn) {
        try {
            conn.close();
            System.out.println("Database closed");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void insertData(Statement stmt, String name,String checkindt,String checkoutdt, int room) {
        try {
            String sql = "Insert Into customer(name,checkindt,checkoutdt,room) values ('" + name + "', '" + checkindt +"','"+checkoutdt+"',"+room + ")";
            stmt.execute(sql);
            System.out.println("Data " + name + "(" + room + ") inserted");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static ResultSet queryData(Statement stmt, String sql) {
        ResultSet result = null;
        try {
            result = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    
     public static void displayResult(ResultSet result) {
        if (result != null) {
            try {
                while (result.next()) {
                    String name = result.getString("name");
                    System.out.println(name);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void main(String[] args) {
        Connection conn = connectAndCreateTableCus();
        Statement stmt = null;
        
        if (conn != null) {
            try {
                stmt = conn.createStatement();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            if (stmt != null) {
                // insert data to table Person
//                insertData(stmt, "Rudy","2018-4-3","2018-5-7", 30);
//                insertData(stmt, "Susy", 'f');

                // query ke database
                ResultSet result = queryData(stmt, "select * from customer");
                System.out.println("After query");
                displayResult(result);
            }

            // close database
            closeDatabase(conn);
        }
    }
}
