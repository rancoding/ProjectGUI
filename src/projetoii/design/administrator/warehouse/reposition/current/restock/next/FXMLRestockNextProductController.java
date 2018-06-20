/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoii.design.administrator.warehouse.reposition.current.restock.next;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.stage.Stage;
import projetoii.design.administrator.warehouse.reposition.current.restock.FXMLRestockProductController;

/**
 * FXML Controller class
 *
 * @author Gustavo Vieira
 */
public class FXMLRestockNextProductController implements Initializable {
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML private void onNextButton()
    {
        FXMLRestockProductController.setCount(FXMLRestockProductController.getCount() + 1);
        FXMLRestockProductController.setBottomBar();
        FXMLRestockProductController.setStageTitle();
    }
    
    /**
     * Closes the stage on cancel button click
     * @param event triggered event
     */
    @FXML void onCancelClick(ActionEvent event)
    {
        closeStage(event);
    }
    
    /**
     * Closes current window
     * @param event triggered event
     */
    private void closeStage(ActionEvent event)
    {
        Node node = (Node)event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }
}
