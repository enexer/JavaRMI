package sample.Server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by as on 10.01.2018.
 */
public class Server
{
    public static void main(String[] args) {
        try {
            Registry locReg=LocateRegistry.createRegistry(6000);
            RemoteObject remObj=new RemoteObject();
            locReg.rebind("rmi://localhost:6000/OurServer", remObj);
        }
        catch(RemoteException e) {}
    }
}
