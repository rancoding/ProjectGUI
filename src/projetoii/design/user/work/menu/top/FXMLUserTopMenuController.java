/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoii.design.user.work.menu.top;

import helpers.FuncionarioBLL;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import projetoii.design.user.work.reposition.current.list.FXMLListCurrentRepositionController;
import projetoii.design.user.work.sale.current.list.FXMLListCurrentSaleController;
import projetoii.design.user.work.sale.saved.list.FXMLListSavedSaleController;
import projetoii.design.user.work.schedule.FXMLScheduleController;

/**
 * FXML Controller class
 *
 * @author Gustavo Vieira
 */
public class FXMLUserTopMenuController implements Initializable {

    @FXML private ToggleButton salesBtn;
    @FXML private ToggleButton savedSalesBtn;
    @FXML private ToggleButton repositionBtn;
    @FXML private ToggleButton workingHoursBtn;
    @FXML private BorderPane userTopMenu;
    
    public static FuncionarioBLL employee;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        switchCenter();
    }    

    private void switchCenter()
    {
        disableButtonSelection(true, false, false, false);
        this.switchCenter(FXMLListCurrentSaleController.class, "FXMLListCurrentSale.fxml");
    }
    
    /**
     * Switches border pane center depending on selected combo box item
     * @param event triggered event
     */
    @FXML private void switchCenter(ActionEvent event)
    {
        switch(((ToggleButton) event.getSource()).getId())
        {  
            case "salesBtn":
            {
                disableButtonSelection(true, false, false, false);
                switchCenter(FXMLListCurrentSaleController.class, "FXMLListCurrentSale.fxml");
                break;
            }
            
            case "savedSalesBtn":
            {
                disableButtonSelection(false, true, false, false);
                switchCenter(FXMLListSavedSaleController.class, "FXMLListSavedSale.fxml");
                break;
            }
            
            case "repositionBtn":
            {
                disableButtonSelection(false, false, true, false);
                switchCenter(FXMLListCurrentRepositionController.class, "FXMLListCurrentReposition.fxml");
                break;
            }
            
            case "workingHoursBtn":
            {
                disableButtonSelection(false, false, false, true);
                switchCenter(FXMLScheduleController.class, "FXMLSchedule.fxml");
                break;
            }
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
            userTopMenu.setCenter(newPane);
        }
        catch(Exception e)
        {
        }
    }
    
    /**
     * Disables toggle buttons depending on button clicked
     * @param sales sales menu button
     * @param savedSales saved saled menu button
     * @param reposition reposition menu button
     * @param schedule schedule menu button
     */
    private void disableButtonSelection(boolean sales, boolean savedSales, boolean reposition, boolean schedule)
    {
        salesBtn.setSelected(sales);
        savedSalesBtn.setSelected(savedSales);
        repositionBtn.setSelected(reposition);
        workingHoursBtn.setSelected(schedule);
    }
    
    public void initializeOnControllerCall(FuncionarioBLL employee) {
        setEmployee(employee);
    }

    private void setEmployee(FuncionarioBLL employee) {
        this.employee = employee;
    }
}
