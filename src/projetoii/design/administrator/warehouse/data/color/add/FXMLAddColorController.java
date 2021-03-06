package projetoii.design.administrator.warehouse.data.color.add;

import helpers.CorBLL;
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
import projetoii.design.administrator.warehouse.data.color.list.FXMLListColorController;
import projetoii.design.administrator.warehouse.data.product.add.FXMLAddProductController;
import projetoii.design.administrator.warehouse.data.product.edit.FXMLEditProductController;
import services.CorService;

public class FXMLAddColorController implements Initializable {

    @FXML private TextField colorName;
    @FXML private Button addColorButton;
    @FXML private Label errorLabel;
    
    private FXMLAddProductController addProductController;
    private FXMLEditProductController editProductController;
    private FXMLListColorController listColorController;
    private ObservableList<CorBLL> colorList;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        addColorButton.setDisable(true);
    }    
   
    /**
     * Initializes all variables when getting called from another controller
     * @param addProductController controller who called this controller
     * @param colorList all colors
     */
    public void initializeOnAddProductControllerCall(FXMLAddProductController addProductController, ObservableList<CorBLL> colorList)
    {
        setListAddProductController(addProductController);
        setObservableList(colorList);
    }
    
    /* * Sets list controller * */
    private void setListAddProductController(FXMLAddProductController addProductController)
    {
        this.addProductController = addProductController;
    }
    
    /**
     * Initializes all variables when getting called from another controller
     * @param editProductController controller who called this controller
     * @param colorList all colors
     */
    public void initializeOnEditProductControllerCall(FXMLEditProductController editProductController, ObservableList<CorBLL> colorList)
    {
        setListEditProductController(editProductController);
        setObservableList(colorList);
    }
    
    /* * Sets list controller * */
    private void setListEditProductController(FXMLEditProductController editProductController)
    {
        this.editProductController = editProductController;
    }
    
    
    /* * Initializes all variables when getting called from another controller * */
    public void initializeOnControllerCall(FXMLListColorController listColorController, ObservableList<CorBLL> colorList)
    {
        setListController(listColorController);
        setObservableList(colorList);
    }
    
    /* * Sets list controller * */
    private void setListController(FXMLListColorController listolorController)
    {
        this.listColorController = listolorController;
    }
    
    /* * Sets observable list from a given observable list * */
    private void setObservableList(ObservableList<CorBLL> colorList)
    {
        this.colorList = colorList;
    }
    
    /**
     * Adds a new color and updates the database
     * @param event triggered event
     */
    @FXML void onAddClick(ActionEvent event)
    {
        String nonCharacters = "[^\\p{L}\\p{Nd}]";
        
        CorBLL newColor = new CorBLL();
        newColor.setNome(StringUtils.capitalize(colorName.getText()).replaceAll(nonCharacters, ""));
        
        colorList.add(newColor);
        insertColor(newColor);
        
        List<CorBLL> helperList = CorService.getHelperList("FROM Cor ORDER BY idcor ASC");
        
        if(this.listColorController != null)
        {
            this.listColorController.setSearchedTableValues(helperList);
            this.listColorController.colorTable.refresh();
        }
        else if(this.addProductController != null)
        {
            this.addProductController.colorComboBox.getSelectionModel().select(colorList.size()-1);
        }
        else if(this.editProductController != null)
        {
            this.editProductController.colorComboBox.getSelectionModel().select(colorList.size()-1);
        }
        
        closeStage(event);
    }
    
    /**
     * Searches for a color with the same name as the new one in the color list
     * @param name name that will be searched for its existence
     * @param nonCharacters characters to be stripped from the name
     * @return name existence
     */
    private boolean checkForExistentColor(String name, String nonCharacters)
    {
        if(!(colorList.isEmpty()))
        {
            for(CorBLL color : colorList)
            {
                String searchColorName = StringUtils.stripAccents(color.getNome().replaceAll(nonCharacters, "").toLowerCase());

                if(name.equals(searchColorName))
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
        String newColorName = StringUtils.stripAccents(colorName.getText().replaceAll(nonCharacters, "").toLowerCase());
        
        boolean exists = checkForExistentColor(newColorName, nonCharacters);
       
        if(colorName.getText().isEmpty())
        {
            addColorButton.setDisable(true);
            errorLabel.setText("");
        }
        else
        {
            if(exists)
            {
                errorLabel.setText("Cor já existente");
                addColorButton.setDisable(true);
            }
            else
            {
                errorLabel.setText("");
                addColorButton.setDisable(false);
            }
        }
    }
    
    /**
     * Inserts entity into the database
     * @param color color to be created
     */
    private void insertColor(CorBLL color)
    {
        color.create();
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
