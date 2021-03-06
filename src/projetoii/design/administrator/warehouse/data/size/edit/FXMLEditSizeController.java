/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoii.design.administrator.warehouse.data.size.edit;

import helpers.TamanhoBLL;
import java.io.IOException;
import java.net.URL;
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
import projetoii.design.administrator.warehouse.data.size.list.FXMLListSizeController;

/**
 * FXML Controller class
 *
 * @author Gustavo Vieira
 */
public class FXMLEditSizeController implements Initializable {

    /* New category name, edit button and error label button */
    @FXML private TextField sizeName;
    @FXML private Button editSizeNameButton;
    @FXML private Label errorLabel;
    
    /* Controller to be able to refresh the table on edit button click, and size list to be able to edit and search for existent sizes */
    private FXMLListSizeController listSizeController;
    private ObservableList<TamanhoBLL> sizeList;
    private TamanhoBLL size;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        editSizeNameButton.setDisable(true);
    }    
    
    /**
     * To be called when needing to initialize values from the list size controller
     * @param listSizeController controller where this controller has been called from
     * @param sizeList all sizes
     * @param size to be edited size
     */
    public void initializeOnControllerCall(FXMLListSizeController listSizeController, ObservableList<TamanhoBLL> sizeList, TamanhoBLL size)
    {
        /* Sets all variables accordingly to received parameters */
        setListSizeController(listSizeController);
        setSizeList(sizeList);
        setSize(size);
        setField();
    }
    
    private void setListSizeController(FXMLListSizeController listSizeController)
    {
        this.listSizeController = listSizeController;
    }
    
    private void setSizeList(ObservableList<TamanhoBLL> sizeList)
    {
        this.sizeList = sizeList;
    }
    
    private void setSize(TamanhoBLL size)
    {
        this.size = size;
    }
    
    private void setField()
    {
        this.sizeName.setText(size.getDescricao());
    }
    
    /**
     * Sets the new size name, updates in the database, refreshes the list controller table and closes current window
     * @param event triggered event
     * @throws IOException 
     */
    @FXML
    void onEditButtonClick(ActionEvent event) throws IOException
    {
        size.setDescricao(sizeName.getText().toUpperCase());
        
        updateSize(size);
        
        this.listSizeController.sizeTable.refresh();
        closeStage(event);
    }
    
    /**
     * Checks if the size name typed in the text field already exists
     * @param name name to be searched
     * @param nonCharacters characters to be stripped
     * @return name existence
     */
    private boolean checkIfNameExists(String name, String nonCharacters)
    {
        for(TamanhoBLL s : sizeList)
        {
            if(s.getIdtamanho() != size.getIdtamanho())
            {
                String sizeName = StringUtils.stripAccents(s.getDescricao().replaceAll(nonCharacters, "").toLowerCase());
                
                if(name.equals(sizeName))
                {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * If size name exists, disables edit button and shows an error in a label
     * @param message error message
     */
    private void disableEditButtonAndShowError(String message)
    {
        editSizeNameButton.setDisable(true);
        errorLabel.setText(message);
    }
    
    /**
     * Checks if the typed name exists, disabling or enabling the edit button accordingly, and showing label error
     */
    @FXML
    void checkNewNameToSetButtonDisable()
    {
        String nonCharacters = "[^\\p{L}\\p{Nd}]";
        String editedSizeName = StringUtils.stripAccents(sizeName.getText().replaceAll(nonCharacters, "").toLowerCase());
        String searchSizeName = StringUtils.stripAccents(size.getDescricao().replaceAll(nonCharacters, "").toLowerCase());
        
        if(sizeName.getText().isEmpty())
        {
            editSizeNameButton.setDisable(true);
            errorLabel.setText("");
        }
        else
        {
            if(!(editedSizeName.equals(searchSizeName)))
            {
                boolean exists = checkIfNameExists(editedSizeName, nonCharacters);

                if(exists)
                {
                    disableEditButtonAndShowError("Tamanho já existe");
                }
                else
                {
                    if(!(editSizeNameButton.getText().isEmpty()))
                    {
                        errorLabel.setText("");
                    }

                    editSizeNameButton.setDisable(false);
                }
            }
            else
            {
                if(!(editSizeNameButton.getText().isEmpty()))
                {
                    errorLabel.setText("");
                }

                editSizeNameButton.setDisable(true);
            }
        }
    }
    
    /**
     * Updates entity on database
     * @param size to be updated size
     */
    private void updateSize(TamanhoBLL size)
    {
        size.update();
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
