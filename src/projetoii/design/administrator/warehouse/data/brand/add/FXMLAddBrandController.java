package projetoii.design.administrator.warehouse.data.brand.add;

import helpers.MarcaBLL;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import projetoii.design.administrator.warehouse.data.brand.list.FXMLListBrandController;
import projetoii.design.administrator.warehouse.data.product.add.FXMLAddProductController;
import services.MarcaService;

public class FXMLAddBrandController implements Initializable {
   
    @FXML private TextField brandName;
    @FXML private Button addBrandButton;
    @FXML private Label errorLabel;
    
    private FXMLAddProductController addProductController;
    private FXMLListBrandController listBrandController;
    private ObservableList<MarcaBLL> brandList;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        addBrandButton.setDisable(true);
    }    
    
    public void setList(ObservableList brandsList){
        
    }    
 
    /**
     * Initializes all variables when getting called from another controller
     * @param addProductController Sets this controller to be the controller sent from the list controller
     * @param brandList Current brand list
     */
    public void initializeOnAddProductControllerCall(FXMLAddProductController addProductController, ObservableList<MarcaBLL> brandList)
    {
        setListAddProductController(addProductController);
        setObservableList(brandList);
    }
    
    /**
     * Sets list controller
     * @param addProductController Controller to be set
     */
    private void setListAddProductController(FXMLAddProductController addProductController)
    {
        this.addProductController = addProductController;
    }

    /**
     * Initializes all variables when getting called from another controller
     * @param listBrandController Sets the controller that called this controller
     * @param brandList Current brand list
     */
    public void initializeOnControllerCall(FXMLListBrandController listBrandController, ObservableList<MarcaBLL> brandList)
    {
        setListController(listBrandController);
        setObservableList(brandList);
    }
    
    /**
     * Sets list controller
     * @param listBrandController List controller to be set
     */
    private void setListController(FXMLListBrandController listBrandController)
    {
        this.listBrandController = listBrandController;
    }
    
    /**
     * Sets observable list from a given observable list
     * @param brandList Brand list to be set
     */
    private void setObservableList(ObservableList<MarcaBLL> brandList)
    {
        this.brandList = brandList;
    }
    
    /**
     * Adds a new brand and updates the database
     * @param event triggered event
     */
    @FXML void onAddClick(ActionEvent event)
    {
        String nonCharacters = "[^\\p{L}\\p{Nd}]";
        
        MarcaBLL newBrand = new MarcaBLL();
        newBrand.setNome(StringUtils.capitalize(brandName.getText()));
        
        brandList.add(newBrand);
        insertBrand(newBrand);
        
        List<MarcaBLL> helperList = MarcaService.getHelperList("FROM Marca ORDER BY idmarca ASC");
        
        if(this.listBrandController != null)
        {
            this.listBrandController.setSearchedTableValues(helperList);
            this.listBrandController.brandTable.refresh();
        } 
        else if(this.addProductController != null)
        {
            this.addProductController.brandComboBox.getSelectionModel().select(brandList.size()-1);
        }
        
        closeStage(event);
    }
    
    /**
     * Searches for a brand with the same name as the new one in the brand list
     * @param name New brand name
     * @param nonCharacters NonCharacters to be stripped
     * @return name existence
     */
    private boolean checkForExistentBrand(String name, String nonCharacters)
    {
        if(!(brandList.isEmpty()))
        {
            for(MarcaBLL brand : brandList)
            {
                String newBrandName = StringUtils.stripAccents(brand.getNome().replaceAll(nonCharacters, "").toLowerCase());

                if(name.equals(newBrandName))
                {
                    return true;
                }
            }
        }
            
        return false;
    }
    
    /**
     * Enables or disables the button
     */
    @FXML void setAddButtonUsability()
    {
        String nonCharacters = "[^\\p{L}\\p{Nd}]";
        String newBrandName = StringUtils.stripAccents(brandName.getText().replaceAll(nonCharacters, "").toLowerCase());
        
        boolean exists = checkForExistentBrand(newBrandName, nonCharacters);
       
        if(brandName.getText().isEmpty())
        {
            addBrandButton.setDisable(true);
            errorLabel.setText("");
        }
        else
        {
            if(exists)
            {
                errorLabel.setText("Marca já existente");
                addBrandButton.setDisable(true);
            }
            else
            {
                errorLabel.setText("");
                addBrandButton.setDisable(false);
            }
        }
    }
    
    /**
     * Inserts entity into the database
     * @param brand Brand that will be added onto the database
     */
    private void insertBrand(MarcaBLL brand)
    {
        brand.create();
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
