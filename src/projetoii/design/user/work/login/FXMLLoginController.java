/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoii.design.user.work.login;

import dao.Loja;
import helpers.FuncionarioBLL;
import helpers.LojaBLL;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import projetoii.design.user.work.login.password.FXMLLoginPasswordController;
import services.FuncionarioService;
import services.LojaService;

/**
 * FXML Controller class
 *
 * @author Gustavo Vieira
 */
public class FXMLLoginController implements Initializable {

    @FXML private ComboBox workLocationComboBox;
    private List<FuncionarioBLL> employeeList;
    private List<LojaBLL> shopList;
    
    private static int employeesPerPage = 12;
    private static int currentPage = 0;

    public static int getEmployeesPerPage() {
        return employeesPerPage;
    }

    public static int getCurrentPage() {
        return currentPage;
    }

    public static void setCurrentPage(int currentPage) {
        FXMLLoginController.currentPage = currentPage;
    }
    
    private List<Button> buttonList;
    
    /* Login Buttons */
    @FXML private Button loginBtn1;
    @FXML private Button loginBtn2;
    @FXML private Button loginBtn3;
    @FXML private Button loginBtn4;
    @FXML private Button loginBtn5;
    @FXML private Button loginBtn6;
    @FXML private Button loginBtn7;
    @FXML private Button loginBtn8;
    @FXML private Button loginBtn9;
    @FXML private Button loginBtn10;
    @FXML private Button loginBtn11;
    @FXML private Button loginBtn12;
    
    /* Previous and Next Button */
    @FXML private Button prevButton;
    @FXML private Button nextButton;
    
    @FXML private Label pageLabel;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        employeeList = new ArrayList<>();
        shopList = new ArrayList<>();
        buttonList = new ArrayList<>();
        
        shopList = LojaService.getHelperList("FROM Loja l WHERE (SELECT COUNT(*) FROM Funcionario f WHERE l.idloja = f.localtrabalho) > 0 ORDER BY idloja ASC");
        
        if(!(shopList.isEmpty()))
        {
            workLocationComboBox.getItems().addAll(shopList);
            workLocationComboBox.getSelectionModel().select(0);
        }
        
        employeeList = FuncionarioService.getHelperList("FROM Funcionario WHERE localtrabalho.idlocaltrabalho = " + shopList.get(0).getIdloja() + " ORDER BY nome");
        
        setButtonList();
        setLabelPage();
        enableButtons();
    }
    
    @FXML private void enableButtons()
    {
        int number = 0;
        int count = 0;
        
        if(employeeList.size() - (getCurrentPage() * getEmployeesPerPage()) > getEmployeesPerPage())
        {
            number = getEmployeesPerPage();
        }
        else
        {
            number = employeeList.size() - (getCurrentPage() * getEmployeesPerPage());
        }
        
        for(int i = getCurrentPage() * getEmployeesPerPage(); i < getEmployeesPerPage() * (getCurrentPage() + 1); i++)
        {
            if( i >= ((getEmployeesPerPage() * (getCurrentPage() + 1)) - (getEmployeesPerPage() - number)) )
            {
                buttonList.get(count).setDisable(true);
                buttonList.get(count).setText("");
            }
            else
            {
                buttonList.get(count).setDisable(false);
                buttonList.get(count).setText(employeeList.get(i).getNome());
            }
            
            count++;
        }
        
        setNextButton();
        setPrevButton();
    }
    
    private void setNextButton()
    {
        if(employeeList.size() - (getCurrentPage() * getEmployeesPerPage()) > getEmployeesPerPage())
        {
            nextButton.setDisable(false);
        }
        else
        {
            nextButton.setDisable(true);
        }
    }
    
    private void setPrevButton()
    {
        if(getCurrentPage() != 0)
        {
            prevButton.setDisable(false);
        }
        else
        {
            prevButton.setDisable(true);
        }
    }
    
    @FXML private void clickNextButton()
    {
        setCurrentPage(getCurrentPage() + 1);
        setLabelPage();
        enableButtons();
    }
    
    @FXML private void clickPrevButton()
    {
        setCurrentPage(getCurrentPage() - 1);
        setLabelPage();
        enableButtons();
    }
    
    /**
     * Gets the current selected shop employee list
     */
    @FXML private void getNewEmployeeList()
    {
        setCurrentPage(0);
        employeeList.clear();
        employeeList = FuncionarioService.getHelperList("FROM Funcionario WHERE localtrabalho.idlocaltrabalho = " + ((LojaBLL) workLocationComboBox.getSelectionModel().getSelectedItem()).getIdloja() + " ORDER BY nome ASC");
        enableButtons();
        setLabelPage();
    }
    
    /**
     * Adds all login buttons to a list
     */
    private void setButtonList()
    {
        this.buttonList.add(loginBtn1);
        this.buttonList.add(loginBtn2);
        this.buttonList.add(loginBtn3);
        this.buttonList.add(loginBtn4);
        this.buttonList.add(loginBtn5);
        this.buttonList.add(loginBtn6);
        this.buttonList.add(loginBtn7);
        this.buttonList.add(loginBtn8);
        this.buttonList.add(loginBtn9);
        this.buttonList.add(loginBtn10);
        this.buttonList.add(loginBtn11);
        this.buttonList.add(loginBtn12);
    }
    
    @FXML private void onAccountButtonClick(ActionEvent event)
    {
        FuncionarioBLL employee = new FuncionarioBLL();
        
        switch(((Button) event.getSource()).getId())
        {
            case "loginBtn1":
            {
                employee = employeeList.get((getCurrentPage() * getEmployeesPerPage()));
                break;
            }
            
            case "loginBtn2":
            {
                employee = employeeList.get((getCurrentPage() * getEmployeesPerPage()) + 1);
                break;
            }
            
            case "loginBtn3":
            {
                employee = employeeList.get((getCurrentPage() * getEmployeesPerPage()) + 2);
                break;
            }
            
            case "loginBtn4":
            {
                employee = employeeList.get((getCurrentPage() * getEmployeesPerPage()) + 3);
                break;
            }
            
            case "loginBtn5":
            {
                employee = employeeList.get((getCurrentPage() * getEmployeesPerPage()) + 4);
                break;
            }
            
            case "loginBtn6":
            {
                employee = employeeList.get((getCurrentPage() * getEmployeesPerPage()) + 5);
                break;
            }
            
            case "loginBtn7":
            {
                employee = employeeList.get((getCurrentPage() * getEmployeesPerPage()) + 6);
                break;
            }
            
            case "loginBtn8":
            {
                employee = employeeList.get((getCurrentPage() * getEmployeesPerPage()) + 7);
                break;
            }
            
            case "loginBtn9":
            {
                employee = employeeList.get((getCurrentPage() * getEmployeesPerPage()) + 8);
                break;
            }
            
            case "loginBtn10":
            {
                employee = employeeList.get((getCurrentPage() * getEmployeesPerPage()) + 9);
                break;
            }
            
            case "loginBtn11":
            {
                employee = employeeList.get((getCurrentPage() * getEmployeesPerPage()) + 10);
                break;
            }
            
            case "loginBtn12":
            {
                employee = employeeList.get((getCurrentPage() * getEmployeesPerPage()) + 11);
                break;
            }
        }
        
        openPasswordWindow( employee );
    }
    
    /* * Opens the password window for the given user * */
    private void openPasswordWindow(FuncionarioBLL employee)
    {
        
        try {
            FXMLLoader loader = new FXMLLoader(FXMLLoginPasswordController.class.getResource("FXMLLoginPassword.fxml"));
            Parent root = (Parent) loader.load();
            
            FXMLLoginPasswordController passController = (FXMLLoginPasswordController) loader.getController();
            passController.initializeOnControllerCall(employee);
            
            Stage stage = new Stage();
            stage.setTitle("Login - Password");
            stage.setScene(new Scene(root));
            stage.show();
        } 
        catch (IOException ex) 
        {
            System.out.println("Erro ao abrir o ficheiro FXMLLoginPassword.fxml");
        }
    }
    
    private void setLabelPage()
    {
        this.pageLabel.setText("Página " + (getCurrentPage() + 1) + " de " + (int)Math.ceil((float)employeeList.size() / 12.0f));
    }
}
