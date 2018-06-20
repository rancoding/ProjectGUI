package projetoii.design.administrator.warehouse.data.size.add;

import helpers.TamanhoBLL;
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
import projetoii.design.administrator.warehouse.data.product.add.FXMLAddProductController;
import projetoii.design.administrator.warehouse.data.size.list.FXMLListSizeController;
import services.TamanhoService;

public class FXMLAddSizeController implements Initializable {

    @FXML private TextField sizeName;
    @FXML private Button addSizeButton;
    @FXML private Label errorLabel;
    
    private FXMLAddProductController addProductController;
    private FXMLListSizeController listSizeController;
    private ObservableList<TamanhoBLL> sizeList;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        addSizeButton.setDisable(true);
    }    
    
    public void setList(ObservableList brandsList){
    
    }
    
    /**
     * Initializes all variables when getting called from another controller
     * @param addProductController controller who called this controller
     * @param sizeList all sizes
     */
    public void initializeOnAddProductControllerCall(FXMLAddProductController addProductController, ObservableList<TamanhoBLL> sizeList)
    {
        setListAddProductController(addProductController);
        setObservableList(sizeList);
    }
    
    /* * Sets list controller * */
    private void setListAddProductController(FXMLAddProductController addProductController)
    {
        this.addProductController = addProductController;
    }
    
    /**
     * Initializes all variables when getting called from another controller
     * @param listSizeController controller who called this controller
     * @param sizeList all sizes
     */
    public void initializeOnControllerCall(FXMLListSizeController listSizeController, ObservableList<TamanhoBLL> sizeList)
    {
        setListController(listSizeController);
        setObservableList(sizeList);
    }
    
    /* * Sets list controller * */
    private void setListController(FXMLListSizeController listSizeController)
    {
        this.listSizeController = listSizeController;
    }
    
    /* * Sets observable list from a given observable list * */
    private void setObservableList(ObservableList<TamanhoBLL> sizeList)
    {
        this.sizeList = sizeList;
    }
    
    /**
     * Adds a new size and updates the database
     * @param event triggered event
     */
    @FXML void onAddClick(ActionEvent event)
    {
        String nonCharacters = "[^\\p{L}\\p{Nd}]";
        
        TamanhoBLL newSize = new TamanhoBLL();
        newSize.setIdtamanho((byte) (sizeList.size() + 1));
        newSize.setDescricao(sizeName.getText().toUpperCase().replaceAll(nonCharacters, ""));
        
        insertSize(newSize);
        sizeList.add(newSize);
        
        List<TamanhoBLL> helperList = TamanhoService.getHelperList("FROM Tamanho ORDER BY idtamanho ASC");
        
        if(this.listSizeController != null)
        {
            this.listSizeController.setSearchedTableValues(helperList);
            this.listSizeController.sizeTable.refresh();
        }
        else if(this.addProductController != null)
        {
            this.addProductController.sizeComboBox.getSelectionModel().select(sizeList.size()-1);
        }
        closeStage(event);
    }
    
    /**
     * Searches for a size with the same name as the new one in the size list
     * @param name name to be searched
     * @param nonCharacters characters to be stripped from the name
     * @return name existence
     */
    private boolean checkForExistentSize(String name, String nonCharacters)
    {
        if(!(sizeList.isEmpty()))
        {
            for(TamanhoBLL size : sizeList)
            {
                String sizeName = StringUtils.stripAccents(size.getDescricao().replaceAll(nonCharacters, "").toLowerCase());

                if(name.equals(sizeName))
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
        String newSizeName = StringUtils.stripAccents(sizeName.getText().replaceAll(nonCharacters, "").toLowerCase());
        
        boolean exists = checkForExistentSize(newSizeName, nonCharacters);
       
        if(sizeName.getText().isEmpty())
        {
            addSizeButton.setDisable(true);
            errorLabel.setText("");
        }
        else
        {
            if(exists)
            {
                errorLabel.setText("Tamanho já existente");
                addSizeButton.setDisable(true);
            }
            else
            {
                errorLabel.setText("");
                addSizeButton.setDisable(false);
            }
        }
    }
    
    /**
     * Inserts entity into the database
     * @param size size to be created
     */
    private void insertSize(TamanhoBLL size)
    {
        size.create();
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
