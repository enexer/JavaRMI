package sample.Server;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import sample.Interfaces.StringOperations;

import java.lang.reflect.Type;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 * Created by as on 10.01.2018.
 */
public class RemoteObject extends UnicastRemoteObject implements StringOperations {
    protected RemoteObject() throws RemoteException {
        super();
    }

    public String toUpper(String s) throws RemoteException {
        return s.toUpperCase();
    }

    @Override
    public String toMedian(int[] tab) throws RemoteException {
        String med = median(tab);
        System.out.println("SERVER_CONSUME_ARRAY-------------------");
        System.out.println(Arrays.toString(tab));
        System.out.println("SERVER_PRODUCE_MEDIAN-------------------");
        System.out.println("median: "+med);
        return med;
    }

    @Override
    public String toMedianJSON(String json) throws RemoteException {
        System.out.println("SERVER_CONSUME_JSON-------------------");
        JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
        // get OBJECT table
        JsonArray jsonArray = jsonObject.getAsJsonArray("table");
        // SET TYPE
        Type listType = new TypeToken<List<String>>() {
        }.getType();
        ArrayList<String> yourList = new Gson().fromJson(jsonArray, listType);

        System.out.println(yourList.toString());

        return produceJSON(yourList);
    }

    public static String produceJSON(ArrayList<String> list) {

        System.out.println("SERVER_PRODUCE_JSON-------------------");

        String med = new String();
        try {
            Integer[] array = list.stream()
                    .map(v -> Integer.valueOf(v))
                    .toArray(Integer[]::new);
            int[] intArray = Arrays.stream(array).mapToInt(i -> i).toArray();
            med = median(intArray);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        HashMap objMap = new HashMap();
        objMap.put("user", "server");
        objMap.put("median", med);
        String jobject = new Gson().toJson(objMap);

        // PRETTY JSON
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(jobject).getAsJsonObject();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String prettyJson = gson.toJson(json);
        System.out.println(prettyJson);

        return jobject;
    }

    public static String median(int[] tab) throws RemoteException {
        double mediana;
        double srednia;
        Arrays.sort(tab);

        if (tab.length % 2 == 0) {
            srednia = tab[tab.length / 2] + tab[(tab.length / 2) - 1];
            mediana = srednia / 2.0;
        } else {
            mediana = tab[tab.length / 2];
        }

        return Double.toString(mediana);
    }
}