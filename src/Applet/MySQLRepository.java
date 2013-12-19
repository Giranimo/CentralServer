package Applet;

import RMI.DBManager;
import RMI.MySQL;
import java.sql.*;
import java.util.*;

/**
 * MySQLRepository: data accessor
 *
 * @author Sune Heide
 */
public class MySQLRepository {

    /*
 
     */
    public static int save(MySQL p) {
        int iRet = -1;
        try {

            Connection connection = DBManager.getInstance().getConnection();
            String quary = "INSERT INTO mikael3e13.customer VALUES (" + p.getcpr() + ","
                    + " '" + p.getPassword() + "',"
                    + " '" + p.getFirstName() + "',"
                    + " '" + p.getLastName() + "',"
                    + " '" + p.getAddress() + "',"
                    + " '" + p.getMail() + "',"
                    + " " + p.getBalance() + ","
                    + "" + p.getAdmin() + ")";
            PreparedStatement pstmt = connection.prepareStatement(quary);
            iRet = pstmt.executeUpdate();

            pstmt.close();
        } catch (SQLException se) {
            System.out.println(se);
        }

        return iRet;
    }
    /*
 
     */

    public static int update(MySQL p) {
        int iRet = -1;
        try {

            Connection connection = DBManager.getInstance().getConnection();
            String query = "UPDATE mikael3e13.customer SET PASSWORD = '" + p.getPassword() + "',"
                    + " FIRSTNAME = '" + p.getFirstName() + "',"
                    + " SURNAME = '" + p.getLastName() + "',"
                    + " ADDRESS = '" + p.getAddress() + "',"
                    + " MAIL = '" + p.getMail() + "',"
                    + " BALANCE = " + p.getBalance() + ","
                    + " ADMIN = " + p.getAdmin() + ""
                    + " WHERE SSN = " + p.getcpr() + " ";
            PreparedStatement pstmt = connection.prepareStatement(query);
            iRet = pstmt.executeUpdate();

            pstmt.close();
        } catch (SQLException se) {
            System.out.println(se);
        }

        return iRet;
    }

    /*
 
     */
    public static int delete(String ssn) {
        int iRet = -1;
        try {
            Connection connection = DBManager.getInstance().getConnection();
            String query = "DELETE FROM mikael3e13.customer WHERE SSN = " + ssn;
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.executeUpdate();
            iRet = pstmt.executeUpdate();

            pstmt.close();
        } catch (SQLException se) {
            System.out.println(se);
        }
        return iRet;
    }

    /*
 
     */
    public static ArrayList<ArrayList<String>> findAll(String table) {
        ArrayList<ArrayList<String>> TableAll = new ArrayList<>();

        try {
            Connection connection = DBManager.getInstance().getConnection();
            String query = "SELECT * FROM mikael3e13." + table + " ORDER by SSN";
            Statement stmt = connection.createStatement();
            ResultSet rset = stmt.executeQuery(query);

            ResultSetMetaData metaData = rset.getMetaData();
            int colNr = metaData.getColumnCount();

            while (rset.next()) {
                ArrayList<String> temp = new ArrayList<>();
                for (int i = 1; i <= colNr; i++) {
                    temp.add(rset.getString(i));
                }
                TableAll.add(temp);
            }
            stmt.close();
        } catch (SQLException se) {
            System.out.println(se);
        }
        return TableAll;
    }

    /*
 
     */
    public static ArrayList<ArrayList<String>> tableCustomerSearch(String searchText) {
        ArrayList<ArrayList<String>> TableCustomerSearchDB = new ArrayList<>();

        try {

            Connection connection = DBManager.getInstance().getConnection();
            String query = "SELECT * FROM mikael3e13.customer WHERE SSN LIKE '%" + searchText + "%' "
                    + "OR FIRSTNAME LIKE '%" + searchText + "%' "
                    + "OR SURNAME LIKE '%" + searchText + "%' "
                    + "ORDER BY SSN ";

            PreparedStatement Pstmt = connection.prepareStatement(query);
            ResultSet rset = Pstmt.executeQuery();
            ResultSetMetaData metaData = rset.getMetaData();
            int colNr = metaData.getColumnCount();

            while (rset.next()) {
                ArrayList<String> temp = new ArrayList<>();
                for (int i = 1; i <= colNr; i++) {
                    temp.add(rset.getString(i));
                }
                TableCustomerSearchDB.add(temp);
            }
        } catch (Exception e) {
            System.out.println("fejl i system Cus Search");
            e.printStackTrace();
        }
        return TableCustomerSearchDB;
    }

    /*
 
     */
    public static ArrayList<ArrayList<String>> tableTravelsSearch(String searchText) {
        ArrayList<ArrayList<String>> TableTravelsSearchDB = new ArrayList<>();

        try {
            Connection connection = DBManager.getInstance().getConnection();
            String query = "SELECT * FROM mikael3e13.travels WHERE SSN LIKE '%" + searchText + "%' "
                    + "OR VEHICLE LIKE '%" + searchText + "%' "
                    + "ORDER BY SSN ";

            PreparedStatement Pstmt = connection.prepareStatement(query);
            ResultSet rset = Pstmt.executeQuery();

            ResultSetMetaData metaData = rset.getMetaData();
            int colNr = metaData.getColumnCount();

            while (rset.next()) {
                ArrayList<String> temp = new ArrayList<>();
                for (int i = 1; i <= colNr; i++) {
                    temp.add(rset.getString(i));
                }
                TableTravelsSearchDB.add(temp);
            }

        } catch (Exception e) {
            System.out.println("fejl i system lol");
            e.printStackTrace();
        }
        return TableTravelsSearchDB;
    }
    /*
 
     */

    public static ArrayList<String> columnNames(String table) {
        ArrayList<String> colNamesDB = new ArrayList<>();
        try {
            Connection connection = DBManager.getInstance().getConnection();
            String query = "SELECT * FROM mikael3e13." + table;
            PreparedStatement pstmt = connection.prepareStatement(query);
            ResultSet rset = pstmt.executeQuery();

            ResultSetMetaData metaData = rset.getMetaData();
            int colNr = metaData.getColumnCount();

            for (int i = 1; i <= colNr; i++) {
                colNamesDB.add(metaData.getColumnName(i));
            }

        } catch (Exception e) {
            System.out.println("fejl i columnNames");
            e.printStackTrace();
        }
        return colNamesDB;
    }

    /*
 
     */
    public static int login(String ssn, String password) {

        try {
            Connection connection = DBManager.getInstance().getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rset = stmt.executeQuery("SELECT PASSWORD, ADMIN FROM mikael3e13.customer WHERE SSN = '" + ssn + "' ");
            if (rset.next() == true) {
                String passwordDB = rset.getString("password");
                int adminDB = rset.getInt("admin");

                if (passwordDB.equals(password) && adminDB == 1) {
                    return 2;
                } else if (passwordDB.equals(password) && adminDB == 0) {
                    return 1;
                } else {
                    return 0;
                }
            } else {
                return 0;
            }
        } catch (Exception e) {
            System.out.println("login 0: " + e);
            return 0;
        }
    }

    /*
 
     */
    public static int registerCreateUser(MySQL p) {
        int iRet = -1;
        try {

            Connection connection = DBManager.getInstance().getConnection();
            String quary = "INSERT INTO mikael3e13.customer VALUES (" + p.getcpr() + ","
                    + " '" + p.getPassword() + "',"
                    + " '" + p.getFirstName() + "',"
                    + " '" + p.getLastName() + "',"
                    + " '" + p.getAddress() + "',"
                    + " '" + p.getMail() + "',"
                    + " " + p.getBalance() + ","
                    + "" + p.getAdmin() + ")";
            PreparedStatement pstmt = connection.prepareStatement(quary);
            iRet = pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException se) {
            System.out.println(se);
        }

        return iRet;
    }

    /*
 
     */
    public static int userEditUser(MySQL p) {
        int iRet = -1;
        try {

            Connection connection = DBManager.getInstance().getConnection();
            String quary = "UPDATE mikael3e13.customer SET PASSWORD = '" + p.getPassword() + "',"
                    + " FIRSTNAME = '" + p.getFirstName() + "',"
                    + " SURNAME = '" + p.getLastName() + "',"
                    + " ADDRESS = '" + p.getAddress() + "',"
                    + " MAIL = '" + p.getMail() + "',"
                    + " BALANCE = " + p.getBalance() + ","
                    + " ADMIN = " + p.getAdmin() + ""
                    + " WHERE SSN = " + p.getcpr() + " ";
            PreparedStatement pstmt = connection.prepareStatement(quary);
            iRet = pstmt.executeUpdate();

            pstmt.close();
        } catch (SQLException se) {
            System.out.println(se);
        }
        return iRet;
    }
}
