package sample.Interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by as on 10.01.2018.
 */
public interface StringOperations extends Remote
{
    String toUpper(String s) throws RemoteException;

    String toMedian(int[] s) throws RemoteException;

    String toMedianJSON(String json) throws RemoteException;
}
