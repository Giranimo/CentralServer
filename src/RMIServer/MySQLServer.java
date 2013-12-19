package RMIServer;

import RMI.MySQLObject;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.*;
import java.util.ArrayList;

/**
 * MySQLServer: Server main for creating registry and binding an object
 *
 * @author Sune Heide
 */
public class MySQLServer {

    /*
        
     */
    public static void main(String[] args) throws Exception {

        System.out.println("RMI server started");

        try {


            Registry registry = LocateRegistry.createRegistry(1337);
            System.out.println("java RMI registry created.");
            MySQLObject obj = new MySQLObject();
            registry.rebind("MySQL", obj);
        } catch (RemoteException e) {
            System.out.println("java RMI registry already exists: " + e);
        }


    }
}