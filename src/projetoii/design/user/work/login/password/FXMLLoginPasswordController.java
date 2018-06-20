/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoii.design.user.work.login.password;

import helpers.FuncionarioBLL;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import projetoii.design.user.work.menu.top.FXMLUserTopMenuController;

/**
 * FXML Controller class
 *
 * @author Gustavo Vieira
 */
public class FXMLLoginPasswordController implements Initializable {

    private FuncionarioBLL employee;
    
    private String password = "";
    
    @FXML private PasswordField display;
    @FXML private Button button0;
    @FXML private Button button1;
    @FXML private Button button2;
    @FXML private Button button3;
    @FXML private Button button4;
    @FXML private Button button5;
    @FXML private Button button6;
    @FXML private Button button7;
    @FXML private Button button8;
    @FXML private Button button9;
    
    @FXML private Button clear;
    @FXML private Button accept;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        accept.setDisable(true);
    }    

    public void initializeOnControllerCall(FuncionarioBLL employee) {
        setEmployee(employee);
    }
    
    private void setEmployee(FuncionarioBLL employee)
    {
        this.employee = employee;
    }
    
    @FXML private void onNumberClick(ActionEvent event)
    {
        if(accept.isDisable())
        {
            accept.setDisable(false);
        }
        
        switch( ((Button) event.getSource()).getId() )
        {
            case "button0": { password = password + "0"; break; }
            case "button1": { password = password + "1"; break; }
            case "button2": { password = password + "2"; break; }
            case "button3": { password = password + "3"; break; }
            case "button4": { password = password + "4"; break; }
            case "button5": { password = password + "5"; break; }
            case "button6": { password = password + "6"; break; }
            case "button7": { password = password + "7"; break; }
            case "button8": { password = password + "8"; break; }
            case "button9": { password = password + "9"; break; }
        }
        
        if(password.length() == 4)
        {
            setButtonDisable(true);
        }
        
        display.setText(password);
    }
    
    @FXML private void onClearClick()
    {
        password = "";
        setButtonDisable(false);
        accept.setDisable(true);
        display.setText(password);
    }
    
    private void setButtonDisable(boolean type)
    {
        button0.setDisable(type);
        button1.setDisable(type);
        button2.setDisable(type);
        button3.setDisable(type);
        button4.setDisable(type);
        button5.setDisable(type);
        button6.setDisable(type);
        button7.setDisable(type);
        button8.setDisable(type);
        button9.setDisable(type);
    }
    
    @FXML private void onAcceptClick()
    {
        if(Short.parseShort(password) == employee.getPassword())
        {
            try 
            {
                FXMLLoader loader = new FXMLLoader(FXMLUserTopMenuController.class.getResource("FXMLUserTopMenu.fxml"));
                Parent root = (Parent) loader.load();
                
                FXMLUserTopMenuController menuController = (FXMLUserTopMenuController) loader.getController();
                menuController.initializeOnControllerCall(employee);
                
                Stage stage = new Stage();
                stage.setTitle("Funcionário");
                stage.setScene(new Scene(root));
                stage.show();
            } 
            catch (IOException ex) 
            {
            }
        }
        else
        {
            System.out.println("You've failed this time!");
            System.out.println(employee.getPassword());
        }
    }
}
