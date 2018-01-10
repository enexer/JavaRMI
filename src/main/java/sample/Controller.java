package sample;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import sample.Client.Client;
import sample.Interfaces.CallBackInterface;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class Controller implements Initializable, CallBackInterface {

    @FXML
    private Button buttonStart;
    @FXML
    private ListView<String> listView;
    @FXML
    private RadioButton radioButtonJSON;
    @FXML
    private RadioButton radioButtonDefault;
    @FXML
    private TextField textField;

    final ToggleGroup group = new ToggleGroup();

    public Client client;
    public String dataType = "DEFAULT";
    public String userData="";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        radioButtonJSON.setToggleGroup(group);
        radioButtonJSON.setUserData("JSON");
        radioButtonDefault.setToggleGroup(group);
        radioButtonDefault.setUserData("DEFAULT");
        radioButtonDefault.setSelected(true);

        userData = textField.getText();

        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("textfield changed from " + oldValue + " to " + newValue);
            userData = newValue;
        });

        client = new Client(this, dataType, null);

        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle old_toggle, Toggle new_toggle) {
                if (group.getSelectedToggle() != null) {
                    System.out.println(group.getSelectedToggle().getUserData().toString());
                    dataType = group.getSelectedToggle().getUserData().toString();
                    client.setMode(dataType);
                }
            }
        });

        System.out.println(Arrays.toString(stringToArray(textField.getText())));
    }


    @FXML
    private void handleButtonAction1(ActionEvent event) {

        if(userData.equals("")){
            System.out.println("random mode");
            client.setUserData("");
            new Thread(client).start();
        }else if(userData.length()>0 && Character.isDigit(userData.charAt(userData.length()-1))){
            System.out.println("user mode");
            client.setUserData(userData);
            new Thread(client).start();
        }else{
            System.out.println("BAD ARRAY FORMAT");
            listView.getItems().add("BAD ARRAY FORMAT");
            listView.scrollTo(listView.getItems().size() - 1);
        }
    }

    @Override
    public void updateView(String val) {
        Platform.runLater(() -> {
            listView.getItems().add(val);
            listView.scrollTo(listView.getItems().size() - 1);
        });
    }

    public static int[] stringToArray(String s) {
        String[] integerStrings = s.split(",");
        int[] integers = new int[integerStrings.length];
        for (int i = 0; i < integers.length; i++) {
            integers[i] = Integer.parseInt(integerStrings[i]);

        }
        return integers;
    }
}
