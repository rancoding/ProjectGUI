/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoii.design.administrator.warehouse.menu.top;

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
import projetoii.design.administrator.warehouse.box.list.FXMLListBoxController;
import projetoii.design.administrator.warehouse.employee.list.FXMLListEmployeeController;
import projetoii.design.administrator.warehouse.menu.left.FXMLWarehouseLeftMenuController;
import projetoii.design.administrator.warehouse.reposition.current.list.FXMLListCurrentRepositionController;

public class FXMLWarehouseTopMenuController implements Initializable {

    @FXML private ToggleButton warehouseButton;
    @FXML private ToggleButton employeeButton;
    @FXML private ToggleButton repositionButton;
    @FXML private ToggleButton boxButton;
    @FXML public BorderPane warehouseTopMenu;
    
    private static BorderPane staticPane;

    public static BorderPane getStaticPane() {
        return staticPane;
    }

    public static void setStaticPane(BorderPane staticPane) {
        FXMLWarehouseTopMenuController.staticPane = staticPane;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        setStaticPane(warehouseTopMenu);
        switchCenter();
    }    
    
    /**
     * Switches center on initialize
     */
    private void switchCenter()
    {
        disableButtonSelection(true, false, false, false);
        this.switchCenter(FXMLWarehouseLeftMenuController.class, "FXMLWarehouseLeftMenu.fxml", "Administrador - Armazém");
    }
    
    /**
     * Switches border pane center depending on selected combo box item
     * @param event triggered event
     */
    @FXML private void switchCenter(ActionEvent event)
    {
        switch(((ToggleButton) event.getSource()).getId())
        {  
            case "warehouseButton":
            {
                disableButtonSelection(true, false, false, false);
                switchCenter(FXMLWarehouseLeftMenuController.class, "FXMLWarehouseLeftMenu.fxml", "Administrador - Armazém");
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
                switchCenter(FXMLListCurrentRepositionController.class, "FXMLListCurrentReposition.fxml", "Administrador - Listagem de Reposições");
                break;
            }
            
            case "boxButton":
            {
                disableButtonSelection(false, false, false, true);
                switchCenter(FXMLListBoxController.class, "FXMLListBox.fxml", "Administrador - Listagem de Caixas");
                break;
            }
                   
        }
    }
    
    /**
     * Sets the border pane center
     * @param controller To be displayed controller
     * @param file To be opened file
     */
    private void switchCenter(Class controller, String file, String title)
    {
        try
        {
            Pane newPane = FXMLLoader.load(controller.getResource(file));
            warehouseTopMenu.setCenter(newPane);
            FXMLAccountTypeController.getStage().setTitle(title);
        }
        catch(Exception e)
        {
        }
    }
    
    /**
     * Disables toggle buttons depending on button clicked
     * @param warehouse warehouse menu button
     * @param employee employee menu button
     * @param reposition reposition menu button
     * @param box box menu button
     */
    private void disableButtonSelection(boolean warehouse, boolean employee, boolean reposition, boolean box)
    {
        warehouseButton.setSelected(warehouse);
        employeeButton.setSelected(employee);
        repositionButton.setSelected(reposition);
        boxButton.setSelected(box);
    }
}
