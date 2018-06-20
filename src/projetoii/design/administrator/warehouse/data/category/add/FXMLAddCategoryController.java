package projetoii.design.administrator.warehouse.data.category.add;

import helpers.TipoProdutoBLL;
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
import projetoii.design.administrator.warehouse.data.category.list.FXMLListCategoryController;
import projetoii.design.administrator.warehouse.data.product.add.FXMLAddProductController;
import services.TipoProdutoService;

public class FXMLAddCategoryController implements Initializable {
    
    @FXML private TextField categoryName;
    @FXML private Button addCategoryButton;
    @FXML private Label errorLabel;
    
    private FXMLAddProductController addProductController;
    private FXMLListCategoryController listCategoryController;
    private ObservableList<TipoProdutoBLL> productTypeList;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        addCategoryButton.setDisable(true);
    }    
    
    /**
     * Initializes all variables when getting called from another controller
     * @param addProductController
     * @param typeList 
     */
    public void initializeOnAddProductControllerCall(FXMLAddProductController addProductController, ObservableList<TipoProdutoBLL> typeList)
    {
        setListAddProductController(addProductController);
        setObservableList(typeList);
    }
    
    /* * Sets list controller * */
    private void setListAddProductController(FXMLAddProductController addProductController)
    {
        this.addProductController = addProductController;
    }
    
    /* * Initializes all variables when getting called from another controller * */
    public void initializeOnControllerCall(FXMLListCategoryController listCategoryController, ObservableList<TipoProdutoBLL> productTypeList)
    {
        setListController(listCategoryController);
        setObservableList(productTypeList);
    }
    
    /* * Sets list controller * */
    private void setListController(FXMLListCategoryController listCategoryController)
    {
        this.listCategoryController = listCategoryController;
    }
    
    /* * Sets observable list from a given observable list * */
    private void setObservableList(ObservableList<TipoProdutoBLL> productTypeList)
    {
        this.productTypeList = productTypeList;
    }
    
    /**
     * Adds a new category and updates the database
     * @param event triggered event
     */
    @FXML void onAddClick(ActionEvent event)
    {
        String nonCharacters = "[^\\p{L}\\p{Nd}]";
        
        TipoProdutoBLL newType = new TipoProdutoBLL();
        newType.setNome(StringUtils.capitalize(categoryName.getText()).replaceAll(nonCharacters, ""));
        
        productTypeList.add(newType);
        insertCategory(newType);
        
        List<TipoProdutoBLL> helperList = TipoProdutoService.getHelperList("FROM Tipoproduto ORDER BY idtipoproduto ASC");
        
        if(this.listCategoryController != null)
        {
            this.listCategoryController.setSearchedTableValues(helperList);
            this.listCategoryController.categoryTable.refresh();
        }
        else if(this.addProductController != null)
        {
            this.addProductController.typeComboBox.getSelectionModel().select(productTypeList.size()-1);
        }
        
        closeStage(event);
    }
    
    /**
     * Searches for a category with the same name as the new one in the category list
     * @param name name that will be searched for existence
     * @param nonCharacters non characters to be stripped from the name
     * @return name existence
     */
    private boolean checkForExistentCategory(String name, String nonCharacters)
    {
        if(!(productTypeList.isEmpty()))
        {
            for(TipoProdutoBLL type : productTypeList)
            {
                String typeName = StringUtils.stripAccents(type.getNome().replaceAll(nonCharacters, "").toLowerCase());

                if(name.equals(typeName))
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
        String newTypeName = StringUtils.stripAccents(categoryName.getText().replaceAll(nonCharacters, "").toLowerCase());
        
        boolean exists = checkForExistentCategory(newTypeName, nonCharacters);
       
        if(categoryName.getText().isEmpty())
        {
            addCategoryButton.setDisable(true);
            errorLabel.setText("");
        }
        else
        {
            if(exists)
            {
                errorLabel.setText("Categoria já existente");
                addCategoryButton.setDisable(true);
            }
            else
            {
                errorLabel.setText("");
                addCategoryButton.setDisable(false);
            }
        }
    }
    
    /**
     * Inserts entity into the database
     * @param type product type that will be inserted into the database
     */
    private void insertCategory(TipoProdutoBLL type)
    {
        type.create();
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
