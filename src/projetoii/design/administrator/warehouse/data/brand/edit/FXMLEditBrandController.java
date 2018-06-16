/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoii.design.administrator.warehouse.data.brand.edit;

import dao.Marca;
import helpers.MarcaBLL;
import hibernate.HibernateGenericLibrary;
import hibernate.HibernateUtil;
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
import org.apache.commons.lang3.text.WordUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import projetoii.design.administrator.warehouse.data.brand.list.FXMLListBrandController;

/**
 * FXML Controller class
 *
 * @author Gustavo Vieira
 */
public class FXMLEditBrandController implements Initializable {

    /* New category name, edit button and error label button */
    @FXML private TextField brandName;
    @FXML private Button editBrandNameButton;
    @FXML private Label errorLabel;
    
    /* Controller to be able to refresh the table on edit button click, and brand list to be able to edit and search for existent brands */
    private FXMLListBrandController listBrandController;
    private ObservableList<MarcaBLL> brandList;
    private MarcaBLL brand;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        editBrandNameButton.setDisable(true);
    }    
    
    /**
     * To be called when needing to initialize values from the list brand controller
     * @param listBrandController Controller that called edit brand controller
     * @param brandList Current brand list
     * @param brand Brand that will be edited
     */
    public void initializeOnControllerCall(FXMLListBrandController listBrandController, ObservableList<MarcaBLL> brandList, MarcaBLL brand)
    {
        /* Sets all variables accordingly to received parameters */
        setListBrandController(listBrandController);
        setBrandList(brandList);
        setBrand(brand);
        setField();
    }
    
    private void setListBrandController(FXMLListBrandController listBrandController)
    {
        this.listBrandController = listBrandController;
    }
    
    private void setBrandList(ObservableList<MarcaBLL> brandList)
    {
        this.brandList = brandList;
    }
    
    private void setBrand(MarcaBLL brand)
    {
        this.brand = brand;
    }
    
    private void setField()
    {
        this.brandName.setText(brand.getNome());
    }
    
    /**
     * Sets the new brand name, updates in the database, refreshes the list controller table and closes current window
     * @param event triggered event
     * @throws IOException 
     */
    @FXML
    void onEditButtonClick(ActionEvent event) throws IOException
    {
        brand.setNome(WordUtils.capitalizeFully(brandName.getText()));
        
        updateBrand(brand);
        
        this.listBrandController.brandTable.refresh();
        closeStage(event);
    }
    
    /**
     * Checks if the brand name typed in the text field already exists
     * @param name New brand name to be searched
     * @param nonCharacters Non characters to be stripped from names
     * @return 
     */
    private boolean checkIfNameExists(String name, String nonCharacters)
    {
        for(MarcaBLL b : brandList)
        {
            if(b.getIdmarca() != brand.getIdmarca())
            {
                String newBrandName = StringUtils.stripAccents(b.getNome().replaceAll(nonCharacters, "").toLowerCase());
                
                if(name.equals(newBrandName))
                {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * If brand name exists, disables edit button and shows an error in a label
     * @param message error to be displayed
     */
    private void disableEditButtonAndShowError(String message)
    {
        editBrandNameButton.setDisable(true);
        errorLabel.setText(message);
    }
    
    /**
     * Checks if the typed name exists, disabling or enabling the edit button accordingly, and showing label error
     */
    @FXML
    void checkNewNameToSetButtonDisable()
    {
        String nonCharacters = "[^\\p{L}\\p{Nd}]";
        String editedBrandName = StringUtils.stripAccents(brandName.getText().replaceAll(nonCharacters, "").toLowerCase());
        String searchBrandName = StringUtils.stripAccents(brand.getNome().replaceAll(nonCharacters, "").toLowerCase());
        
        if(brandName.getText().isEmpty())
        {
            editBrandNameButton.setDisable(true);
            errorLabel.setText("");
        }
        else
        {
            if(!(editedBrandName.equals(searchBrandName)))
            {
                boolean exists = checkIfNameExists(editedBrandName, nonCharacters);

                if(exists)
                {
                    disableEditButtonAndShowError("Marca já existe");
                }
                else
                {
                    if(!(editBrandNameButton.getText().isEmpty()))
                    {
                        errorLabel.setText("");
                    }

                    editBrandNameButton.setDisable(false);
                }
            }
            else
            {
                if(!(editBrandNameButton.getText().isEmpty()))
                {
                    errorLabel.setText("");
                }

                editBrandNameButton.setDisable(true);
            }
        }
    }
    
    /**
     * Updates entity on database
     */
    private void updateBrand(MarcaBLL brand)
    {
        brand.update();
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
