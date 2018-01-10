package sample.Client;

import com.google.gson.*;
import sample.Interfaces.CallBackInterface;
import sample.Interfaces.StringOperations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;

/**
 * Created by as on 10.01.2018.
 */
public class Client implements Runnable {
    private String mode; // 1- array, 2- JSON
    private String userData;

    public static CallBackInterface callback;

    public Client(CallBackInterface callback, String mode, String userData) {
        this.mode = mode;
        this.userData = userData;
        this.callback = callback;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getUserData() {
        return userData;
    }

    public void setUserData(String userData) {
        this.userData = userData;
    }

    public static CallBackInterface getCallback() {
        return callback;
    }

    public static void setCallback(CallBackInterface callback) {
        Client.callback = callback;
    }

    @Override
    public void run() {
        BufferedReader buffer = null;
        try {
            Registry locReg = LocateRegistry.getRegistry(6000);
            StringOperations remObj = (StringOperations)
                    locReg.lookup("rmi://localhost:6000/OurServer");
            // String text = buffer.readLine();
            boolean stop = true;
            while (stop != false) {
                if (mode.equals("DEFAULT")) {
                    int[] tab;
                    if(!userData.equals("")){
                        tab=stringToArray(userData);
                        String result = remObj.toMedian(tab);
                        System.out.println(Arrays.toString(tab));
                        System.out.println(result);
                        Client.callback.updateView("Client table: " + Arrays.toString(tab));
                        Client.callback.updateView("Median: " + result);
                        stop=false;
                    }else{
                        tab = randomArray();
                        String result = remObj.toMedian(tab);
                        System.out.println(Arrays.toString(tab));
                        System.out.println(result);
                        Client.callback.updateView("Client table: " + Arrays.toString(tab));
                        Client.callback.updateView("Median: " + result);
                    }
                } else if (mode.equals("JSON")) {

                    if(!userData.equals("")){
                        String produceJSON = clientProduceJSON(userData);
                        String medFromServer = remObj.toMedianJSON(produceJSON);
                        String result = clientConsumeJSON(medFromServer);
                        System.out.println(result);
                        Client.callback.updateView("Client JSON: " + produceJSON);
                        Client.callback.updateView("Server JSON: " + medFromServer);
                        Client.callback.updateView("Median: " + result);
                        stop=false;
                    }else{
                        String produceJSON = clientProduceJSON();
                        String medFromServer = remObj.toMedianJSON(produceJSON);
                        String result = clientConsumeJSON(medFromServer);
                        System.out.println(result);
                        Client.callback.updateView("Client JSON: " + produceJSON);
                        Client.callback.updateView("Server JSON: " + medFromServer);
                        Client.callback.updateView("Median: " + result);
                    }
                } else {
                    System.out.println("bad mode: " + mode);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
        } catch (NotBoundException e) {
        }
    }

    public static int[] randomArray() {
        Random random = new Random();
        int size = random.nextInt(6) + 1;
        int[] tab = new int[size];
        for (int i = 0; i < tab.length; i++) {
            tab[i] = random.nextInt(100);
        }
        return tab;
    }

    public static String clientProduceJSON() {

        System.out.println("CLIENT_PRODUCE_JSON-------------------");

        Random random = new Random();
        int size = random.nextInt(6) + 1;
        List objList = new ArrayList();
        for (int i = 0; i < size; i++) {
            objList.add(random.nextInt(100));
        }

        HashMap objMap = new HashMap();
        objMap.put("user", "as");
        objMap.put("table", objList);

        String jobject = new Gson().toJson(objMap);

        // PRETTY JSON
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(jobject).getAsJsonObject();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String prettyJson = gson.toJson(json);
        System.out.println("\n" + prettyJson);

        return jobject;
    }

    public static String clientProduceJSON(String s) {

        System.out.println("CLIENT_PRODUCE_JSON-------------------user_data");

        List objList;
        objList = stringToArrayList(s);
        HashMap objMap = new HashMap();
        objMap.put("user", "as");
        objMap.put("table", objList);

        String jobject = new Gson().toJson(objMap);

        // PRETTY JSON
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(jobject).getAsJsonObject();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String prettyJson = gson.toJson(json);
        System.out.println("\n" + prettyJson);

        return jobject;
    }

    public static String clientConsumeJSON(String json) {

        System.out.println("CLIENT_CONSUME_JSON-------------------");

        JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
        // get OBJECT table
        JsonPrimitive jsonPrimitive = jsonObject.getAsJsonPrimitive("median");
        // SET TYPE
        //Type listType = new TypeToken<List<String>>() {}.getType();
        //ArrayList<String> yourList = new Gson().serverConsumeJSON(jsonPrimitive, listType);
        String avg = jsonPrimitive.getAsString();
        System.out.println("table median: " + avg);
        return avg;
    }

    public static int[] stringToArray(String s) {
        String[] integerStrings = s.split(",");
        int[] integers = new int[integerStrings.length];
        for (int i = 0; i < integers.length; i++) {
            integers[i] = Integer.parseInt(integerStrings[i]);

        }
        return integers;
    }

    public static List<String> stringToArrayList(String s) {

        List objList = new ArrayList();
        String[] integerStrings = s.split(",");

        for (int i = 0; i < integerStrings.length; i++) {
            objList.add(Integer.parseInt(integerStrings[i]));
        }
        return objList;
    }

}