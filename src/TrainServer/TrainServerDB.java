/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TrainServer;

import CalcServer.CalcServer;
import RMI.DBManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Mikael Kofod, Sune Heide
 */
public class TrainServerDB {

    Statement stmt;
    String[] currentSplitDay, dbSplitDay, currentSplitTime, dbSplitTime;

    public TrainServerDB() {
        connection();
    }

    /**
     * This method creates the connection to the database which we need to be
     * able to access the data which is stored.
     */
    public void connection() {
        try {
            Connection connection = DBManager.getInstance().getConnection();
            stmt = connection.createStatement();
        } catch (Exception e) {
            Logger.getLogger(TrainServerDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }
    }

    /**
     * This method requires 2 parameters which are used to withdraw data from
     * the database and checks if the found values are correct.
     *
     * @param ssn
     * @param password
     * @return int
     */
    public boolean loginPDA(String ssn, String password) {
        try {
            ResultSet rset;
            rset = stmt.executeQuery("SELECT PASSWORD, BALANCE FROM mikael3e13.customer WHERE SSN = " + ssn + " ");
            if (rset.next() == true) {
                String passwordDB = rset.getString("password");
                int balDB = rset.getInt("balance");

                if (passwordDB.equals(password) && balDB > 0) {
                    return true;
                } else if (passwordDB.equals(password) && balDB < 0) {
                    return false;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            Logger.getLogger(TrainServerDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
            return false;
        }
    }

    /**
     * This method creates a row in the Travels table which is used to store the
     * journeys of customers, A few columns are created as null to work together
     * with another method.
     *
     * @param ssn
     * @param vehicle
     * @param stime
     * @param splace
     */
    public void createTravel(String ssn, String vehicle, String stime, String splace) {

        try {
            String lastStartTime = "";
            int lastTravel = -1;
            ResultSet rset;
            rset = stmt.executeQuery("SELECT MAX(ID) AS ID FROM mikael3e13.travels WHERE SSN = " + ssn + " ");
            if (rset.next() == true) {
                lastTravel = rset.getInt("ID");

            }
            rset = stmt.executeQuery("SELECT PRICE, STARTTIME, STARTPLACE, ENDTIME FROM mikael3e13.travels WHERE ID = " + lastTravel + " ");
            if (rset.next() == true) {
                lastStartTime = rset.getString("STARTTIME");
            }
            if (!compare(lastStartTime)){
                stmt.executeUpdate("INSERT INTO mikael3e13.travels VALUES ('" + ssn + "',"
                    + " '" + vehicle + "',"
                    + " '" + stime + "',"
                    + " '" + splace + "',"
                    + " null, null, null, null)");
            }
            

        } catch (Exception e) {
            Logger.getLogger(TrainServerDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }

    }

    /**
     * This method finds the previously created row for the correct customer and
     * updates the data with new content.
     *
     * @param ssn
     * @param etime
     * @param eplace
     * @param price
     */
    public void updateTravel(String ssn, String etime, String eplace, double price) {

        try {
            int lastTravel = -1;
            String sPlace = "", lastStartTime = "", endTime = "";
            double balance = 0, dbPrice = 100;
            CalcServer cs = new CalcServer();
            ResultSet rset;

            rset = stmt.executeQuery("SELECT MAX(ID) AS ID FROM mikael3e13.travels WHERE SSN = " + ssn + " ");
            if (rset.next() == true) {
                lastTravel = rset.getInt("ID");

            }
            rset = stmt.executeQuery("SELECT PRICE, STARTTIME, STARTPLACE, ENDTIME FROM mikael3e13.travels WHERE ID = " + lastTravel + " ");
            if (rset.next() == true) {
                dbPrice = rset.getDouble("PRICE");
                lastStartTime = rset.getString("STARTTIME");
                sPlace = rset.getString("STARTPLACE");
                endTime = rset.getString("ENDTIME");

            }
            rset = stmt.executeQuery("SELECT BALANCE FROM mikael3e13.customer WHERE SSN = " + ssn + " ");
            if (rset.next() == true) {
                balance = rset.getDouble("BALANCE");

            }
            double calcPrice = cs.getPrice(sPlace, eplace);
            if (compare(lastStartTime) && endTime != null) {
                double tempPrice = calcPrice - dbPrice;
                dbPrice = dbPrice + tempPrice;
                stmt.executeUpdate("UPDATE mikael3e13.travels SET ENDTIME = '" + etime + "',"
                        + " ENDPLACE = '" + eplace + "',"
                        + " PRICE = " + dbPrice + ""
                        + " WHERE ID = " + lastTravel + " ");
                balance = balance - tempPrice;
                stmt.executeUpdate("UPDATE mikael3e13.customer SET BALANCE = " + balance + " WHERE SSN = " + ssn + " ");
            } else  {
                stmt.executeUpdate("UPDATE mikael3e13.travels SET ENDTIME = '" + etime + "',"
                        + " ENDPLACE = '" + eplace + "',"
                        + " PRICE = " + calcPrice + ""
                        + " WHERE ID = " + lastTravel + " ");
                balance = balance - calcPrice;
                stmt.executeUpdate("UPDATE mikael3e13.customer SET BALANCE = " + balance + " WHERE SSN = " + ssn + " ");
            }

        } catch (Exception e) {
            Logger.getLogger(TrainServerDB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }

    }

       /*
        
       
      */
    public String currentTime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
        String DateNow = sdf.format(date) + " " + sdf2.format(date);

        return DateNow;
    }

    
      /*
        
       
      */
    public boolean compare(String lastStartTime) {
        currentSplitDay = currentTime().split(" ");
        dbSplitDay = lastStartTime.split(" ");
        
            if (currentSplitDay[0].equals(dbSplitDay[0])) {
                currentSplitTime = currentSplitDay[1].split(":");
                dbSplitTime = dbSplitDay[1].split(":");
                double currenth = Double.parseDouble(currentSplitTime[0]);
                double currentm = Double.parseDouble(currentSplitTime[1]);
                double dbh = Double.parseDouble(dbSplitTime[0]);
                double dbm = Double.parseDouble(dbSplitTime[1]);
                double checkCurrent = currenth + currentm / 100;
                double checkdb = dbh + dbm / 100;
                if (checkCurrent - checkdb < 1.15) {
                    return true;
                }
                return false;
            }
            return false;
        }

        
    
}
