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

    final ToggleGroup group = new ToggleGroup();

    public Client client;
    public String dataType="DEFAULT";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        radioButtonJSON.setToggleGroup(group);
        radioButtonJSON.setUserData("JSON");
        radioButtonDefault.setToggleGroup(group);
        radioButtonDefault.setUserData("DEFAULT");
        radioButtonDefault.setSelected(true);

        client = new Client(this, dataType);

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

        System.out.println("ok");
    }


    @FXML
    private void handleButtonAction1(ActionEvent event) {
        new Thread(client).start();
    }

    @Override
    public void updateView(String val) {
        Platform.runLater(() -> {
            listView.getItems().add(val);
            listView.scrollTo(listView.getItems().size() - 1);
        });
    }
}
