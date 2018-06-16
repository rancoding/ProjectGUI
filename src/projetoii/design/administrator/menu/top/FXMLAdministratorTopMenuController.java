package projetoii.design.administrator.menu.top;

import helpers.ArmazemBLL;
import helpers.LojaBLL;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import projetoii.design.administrator.shop.menu.top.FXMLShopTopMenuController;
import projetoii.design.administrator.warehouse.menu.top.FXMLWarehouseTopMenuController;
import services.ArmazemService;
import services.LojaService;

public class FXMLAdministratorTopMenuController implements Initializable {

    @FXML private ComboBox workLocationComboBox;
    @FXML private BorderPane adminTopMenu;
    
    private static int workLocationId;

    public static int getWorkLocationId() {
        return workLocationId;
    }

    public static void setWorkLocationId(int workLocationId) {
        FXMLAdministratorTopMenuController.workLocationId = workLocationId;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        /* Retrieves all database warehouses to an arraylist and initializes the table values if it is not empty */
        List<ArmazemBLL> warehouseList = ArmazemService.getHelperList("FROM Armazem");
        List<LojaBLL> shopList = LojaService.getHelperList("FROM Loja");
        
        /* Adds both lists to the combobox */
        if(!(warehouseList.isEmpty()))
        {
            workLocationComboBox.getItems().addAll(warehouseList);
        }
        if(!(warehouseList.isEmpty()))
        {
            workLocationComboBox.getItems().addAll(shopList);
        }
        
        switchCenter();
    }    
    
    /**
     * Switches center when initializing
     */
    private void switchCenter()
    {
        workLocationComboBox.getSelectionModel().select(0);
            
        if(!(workLocationComboBox.getSelectionModel().isEmpty()))
        {
            if(workLocationComboBox.getSelectionModel().getSelectedItem() instanceof ArmazemBLL)
            {
                setWorkLocationId( ((ArmazemBLL) workLocationComboBox.getSelectionModel().getSelectedItem()).getIdarmazem() );
                switchCenter(FXMLWarehouseTopMenuController.class, "FXMLWarehouseTopMenu.fxml");
            }
            else if(workLocationComboBox.getSelectionModel().getSelectedItem() instanceof LojaBLL)
            {
                setWorkLocationId( ((LojaBLL) workLocationComboBox.getSelectionModel().getSelectedItem()).getIdloja());
                switchCenter(FXMLShopTopMenuController.class, "FXMLShopTopMenu.fxml");
            }
        }
    }
    
    /**
     * Switches border pane center depending on selected combo box item
     * @param event triggered event
     */
    @FXML private void switchCenter(ActionEvent event)
    {
        if(((ComboBox) event.getSource()).getSelectionModel().getSelectedItem() instanceof ArmazemBLL)
        {
            setWorkLocationId( ((ArmazemBLL) workLocationComboBox.getSelectionModel().getSelectedItem()).getIdarmazem() );
            switchCenter(FXMLWarehouseTopMenuController.class, "FXMLWarehouseTopMenu.fxml");
        }
        else if(((ComboBox) event.getSource()).getSelectionModel().getSelectedItem() instanceof LojaBLL)
        {
            setWorkLocationId( ((LojaBLL) workLocationComboBox.getSelectionModel().getSelectedItem()).getIdloja());
            switchCenter(FXMLShopTopMenuController.class, "FXMLShopTopMenu.fxml");
        }
    }
    
    /**
     * Sets the border pane center
     * @param controller To be displayed controller
     * @param file To be opened file
     */
    private void switchCenter(Class controller, String file)
    {
        try
        {
            Pane newPane = FXMLLoader.load(controller.getResource(file));
            adminTopMenu.setCenter(newPane);
        }
        catch(Exception e)
        {
        }
    }
    
}
