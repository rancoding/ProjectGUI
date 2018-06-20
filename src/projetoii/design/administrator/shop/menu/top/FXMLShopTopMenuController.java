/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoii.design.administrator.shop.menu.top;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import projetoii.FXMLAccountTypeController;
import projetoii.design.administrator.shop.employee.list.FXMLListEmployeeController;
import projetoii.design.administrator.shop.menu.left.FXMLShopLeftMenuController;
import projetoii.design.administrator.shop.reposition.history.list.FXMLListRepositionHistoryController;
import projetoii.design.administrator.shop.sale.list.FXMLListSaleController;

public class FXMLShopTopMenuController implements Initializable {

    @FXML private ToggleButton shopButton;
    @FXML private ToggleButton employeeButton;
    @FXML private ToggleButton repositionButton;
    @FXML private ToggleButton saleButton;
    @FXML private BorderPane shopTopMenu;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        switchCenter();
    }    
     /* * Switches center on initialize * */
    private void switchCenter()
    {
        disableButtonSelection(true, false, false, false);
        this.switchCenter(FXMLShopLeftMenuController.class, "FXMLShopLeftMenu.fxml", "Administrador - Loja");
    }
    
    /* * Switches border pane center depending on selected combobox item * */
    @FXML private void switchCenter(ActionEvent event)
    {
        switch(((ToggleButton) event.getSource()).getId())
        {  
            case "shopButton":
            {
                disableButtonSelection(true, false, false, false);
                switchCenter(FXMLShopLeftMenuController.class, "FXMLShopLeftMenu.fxml", "Administrador - Loja");
                break;
            }
            
            case "employeeButton":
            {
                disableButtonSelection(false, true, false, false);
                switchCenter(FXMLListEmployeeController.class, "FXMLListEmployee.fxml", "Administrador - Listagem de Funcionários");
                break;
            }
            
            case "repositionButton":
            {
                disableButtonSelection(false, false, true, false);
                switchCenter(FXMLListRepositionHistoryController.class, "FXMLListRepositionHistory.fxml", "Administrador - Listagem de Reposições");
                break;
            }
            
            case "saleButton":
            {
                disableButtonSelection(false, false, false, true);
                switchCenter(FXMLListSaleController.class, "FXMLListSale.fxml", "Administrador - Listagem de Vendas");
                break;
            }
        }
    }
    
    /* * Sets the border pane center * */
    private void switchCenter(Class controller, String file, String title)
    {
        try
        {
            Pane newPane = FXMLLoader.load(controller.getResource(file));
            shopTopMenu.setCenter(newPane);
            FXMLAccountTypeController.getStage().setTitle(title);
        }
        catch(Exception e)
        {
        }
    }
    
    /* * Disables toggle buttons depending on button clicked * */
    private void disableButtonSelection(boolean shop, boolean employee, boolean reposition, boolean sale)
    {
        shopButton.setSelected(shop);
        employeeButton.setSelected(employee);
        repositionButton.setSelected(reposition);
        saleButton.setSelected(sale);
    }
}
