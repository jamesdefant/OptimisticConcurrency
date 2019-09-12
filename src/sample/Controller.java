package sample;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class Controller {

    @FXML
    private Button uxSave;

    @FXML
    private TextField uxName, uxId, uxNickname, uxAge;


    @FXML
    private ListView<Person> uxPersons;

    Person selectedPerson;


    private boolean isNumeric(String strNum) {
        try {
            Integer i = Integer.parseInt(strNum);
        }
        catch(NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }
    @FXML
    void onAction_uxSave(ActionEvent event) {

        try {
            int personId = Integer.parseInt(uxId.getText());

            // Check if the textField is an int
            Integer age = isNumeric(uxAge.getText()) ? Integer.parseInt(uxAge.getText()) : null;

            System.out.println("Age = " + age);

            Person alteredPerson = new Person(personId,
                    uxName.getText(),
                    uxNickname.getText(),
                    age);

            if(!PersonDB.updatePerson(selectedPerson, alteredPerson)) {

                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "There was an error updating the table", ButtonType.OK);
                alert.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                        "Update successful", ButtonType.OK);
                alert.show();
                selectedPerson = alteredPerson;
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        loadData();
    }

    @FXML
    void initialize() {
        assert uxSave != null : "fx:id=\"uxSave\" was not injected: check your FXML file 'sample.fxml'.";
        assert uxName != null : "fx:id=\"uxName\" was not injected: check your FXML file 'sample.fxml'.";
        assert uxId != null : "fx:id=\"uxId\" was not injected: check your FXML file 'sample.fxml'.";
        assert uxPersons != null : "fx:id=\"uxPersons\" was not injected: check your FXML file 'sample.fxml'.";
        assert uxNickname != null : "fx:id=\"uxNickname\" was not injected: check your FXML file 'sample.fxml'.";
        assert uxAge != null : "fx:id=\"uxAge\" was not injected: check your FXML file 'sample.fxml'.";

        uxPersons.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Person>() {
            @Override
            public void changed(ObservableValue<? extends Person> observable, Person oldValue, Person newValue) {
                if (newValue != null) {
                    selectedPerson = newValue;
                    updateDisplay();
                }
            }
        });

        loadData();
    }

    private void loadData() {
        // Add items to comboBox
        ObservableList<Person> list = FXCollections.observableList(PersonDB.getPersonList());
        uxPersons.setItems(list);
        uxPersons.getSelectionModel().select(0);

        updateDisplay();
    }

    private void updateDisplay() {
        if (selectedPerson != null) {
            uxId.setText(String.valueOf(selectedPerson.getId()));
            uxName.setText(selectedPerson.getName());
            uxNickname.setText(selectedPerson.getNickname());
        }
    }
}
