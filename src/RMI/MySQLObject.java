package RMI;

import Applet.MySQLRepository;
import CalcServer.CalcServer;
import TrainServer.TrainServerDB;
import java.rmi.*;
import java.rmi.server.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Server object
 *
 * @author Sune Heide
 */
public class MySQLObject extends UnicastRemoteObject
        implements IRemoteMySQL {

    public MySQLObject() throws RemoteException {
        super();
    }

    /**
     * @param args the command line arguments
     */
    @Override
    public int save(MySQL p) {
        try {
            System.out.println("Invoke save from " + getClientHost());
        } catch (ServerNotActiveException snae) {
            Logger.getLogger(MySQLObject.class.getName()).log(Level.SEVERE, "message", snae);
        }
        return MySQLRepository.save(p);
    }

    /**
     * @param args the command line arguments
     */
    @Override
    public int update(MySQL p) {
        try {
            System.out.println("Invoke update from " + getClientHost());
        } catch (ServerNotActiveException snae) {
            Logger.getLogger(MySQLObject.class.getName()).log(Level.SEVERE, "message", snae);
        }
        return MySQLRepository.update(p);
    }

    /**
     * @param args the command line arguments
     */
    @Override
    public int delete(String ssn) {
        try {
            System.out.println("Invoke delete from " + getClientHost());
        } catch (ServerNotActiveException snae) {
            Logger.getLogger(MySQLObject.class.getName()).log(Level.SEVERE, "message", snae);
        }
        return MySQLRepository.delete(ssn);
    }

    /**
     * @param args the command line arguments
     */
    @Override
    public ArrayList<ArrayList<String>> findAll(String table) {
        try {
            System.out.println("Invoke findAll from " + getClientHost());
        } catch (ServerNotActiveException snae) {
            Logger.getLogger(MySQLObject.class.getName()).log(Level.SEVERE, "message", snae);
        }
        return MySQLRepository.findAll(table);
    }

    /**
     * @param args the command line arguments
     */
    @Override
    public ArrayList tableCustomerSearch(String searchText) {
        try {
            System.out.println("Invoke findByName from " + getClientHost());
        } catch (ServerNotActiveException snae) {
            Logger.getLogger(MySQLObject.class.getName()).log(Level.SEVERE, "message", snae);
        }
        return MySQLRepository.tableCustomerSearch(searchText);
    }

    /**
     * @param args the command line arguments
     */
    @Override
    public ArrayList tableTravelsSearch(String searchText) {
        try {
            System.out.println("Invoke findByName from " + getClientHost());
        } catch (ServerNotActiveException snae) {
            Logger.getLogger(MySQLObject.class.getName()).log(Level.SEVERE, "message", snae);
        }
        return MySQLRepository.tableTravelsSearch(searchText);
    }

    @Override
    public ArrayList<String> columnNames(String table) {
        try {
            System.out.println("Invoke columnNames from " + getClientHost());
        } catch (ServerNotActiveException snae) {
            Logger.getLogger(MySQLObject.class.getName()).log(Level.SEVERE, "message", snae);
        }
        return MySQLRepository.columnNames(table);
    }

    /**
     * @param args the command line arguments
     */
    @Override
    public int login(String ssn, String password) {
        try {
            System.out.println("Invoke login from " + getClientHost());
        } catch (ServerNotActiveException snae) {
            Logger.getLogger(MySQLObject.class.getName()).log(Level.SEVERE, "message", snae);
        }
        return MySQLRepository.login(ssn, password);
    }

    /**
     * @param args the command line arguments
     */
    @Override
    public int userEditUser(MySQL p) {
        try {
            System.out.println("Invoke userEditUser from " + getClientHost());
        } catch (ServerNotActiveException snae) {
            Logger.getLogger(MySQLObject.class.getName()).log(Level.SEVERE, "message", snae);
        }
        return MySQLRepository.userEditUser(p);
    }

    /**
     * @param args the command line arguments
     */
    @Override
    public int registerCreateUser(MySQL p) {
        try {
            System.out.println("Invoke registerCreateUser from " + getClientHost());
        } catch (ServerNotActiveException snae) {
            Logger.getLogger(MySQLObject.class.getName()).log(Level.SEVERE, "message", snae);

        }
        return MySQLRepository.registerCreateUser(p);
    }

    /**
     * @param args the command line arguments
     */
    public double getPrice(String first, String end) {
        CalcServer cs = new CalcServer();
        try {
            System.out.println("Invoke getPrice from " + getClientHost());
        } catch (ServerNotActiveException snae) {
            Logger.getLogger(MySQLObject.class.getName()).log(Level.SEVERE, "message", snae);

        }
        return cs.getPrice(first, end);
    }

    /**
     *
     * @param first
     * @param end
     * @return
     */
    @Override
    public String getRoute(String first, String end) {
        CalcServer cs = new CalcServer();
        try {
            System.out.println("Invoke getRoute from " + getClientHost());
        } catch (ServerNotActiveException snae) {
            Logger.getLogger(MySQLObject.class.getName()).log(Level.SEVERE, "message", snae);

        }
        return cs.getRoute(first, end);
    }

    /**
     * @param args the command line arguments
     */
    @Override
    public boolean loginPDA(String ssn, String password) {
        TrainServerDB Tdb = new TrainServerDB();
        try {
            System.out.println("Invoke loginTrainServer from " + getClientHost());
        } catch (ServerNotActiveException snae) {
            Logger.getLogger(MySQLObject.class.getName()).log(Level.SEVERE, "message", snae);
        }
        return Tdb.loginPDA(ssn, password);
    }

    /**
     * @param args the command line arguments
     */
    @Override
    public void createTravel(String ssn, String vehicle, String stime, String splace) {
        TrainServerDB Tdb = new TrainServerDB();
        try {
            System.out.println("Invoke createTravel from " + getClientHost());
        } catch (ServerNotActiveException snae) {
            Logger.getLogger(MySQLObject.class.getName()).log(Level.SEVERE, "message", snae);
        }
        Tdb.createTravel(ssn, vehicle, stime, splace);
    }

    /**
     * @param args the command line arguments
     */
    @Override
    public void updateTravel(String ssn, String etime, String eplace, double price) {
        TrainServerDB Tdb = new TrainServerDB();
        try {
            System.out.println("Invoke updateTravel from " + getClientHost());
        } catch (ServerNotActiveException snae) {
            Logger.getLogger(MySQLObject.class.getName()).log(Level.SEVERE, "message", snae);
        }
        Tdb.updateTravel(ssn, etime, eplace, price);
    }
}
