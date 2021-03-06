package projetoii.design.administrator.warehouse.data.color.edit;

import helpers.CorBLL;
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
import projetoii.design.administrator.warehouse.data.color.list.FXMLListColorController;

public class FXMLEditColorController implements Initializable {
    
    /* New category name, edit button and error label button */
    @FXML private TextField colorName;
    @FXML private Button editColorNameButton;
    @FXML private Label errorLabel;
    
    /* Controller to be able to refresh the table on edit button click, and color list to be able to edit and search for existent color */
    private FXMLListColorController listColorController;
    private ObservableList<CorBLL> colorList;
    private CorBLL color;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        editColorNameButton.setDisable(true);
    }    
    
    /**
     * To be called when needing to initialize values from the list color controller
     * @param listColorController controller where this has been called from
     * @param colorList all colors
     * @param color color to be edited
     */
    public void initializeOnControllerCall(FXMLListColorController listColorController, ObservableList<CorBLL> colorList, CorBLL color)
    {
        /* Sets all variables accordingly to received parameters */
        setListColorController(listColorController);
        setColorList(colorList);
        setColor(color);
        setField();
    }
    
    private void setListColorController(FXMLListColorController listColorController)
    {
        this.listColorController = listColorController;
    }
    
    private void setColorList(ObservableList<CorBLL> colorList)
    {
        this.colorList = colorList;
    }
    
    private void setColor(CorBLL color)
    {
        this.color = color;
    }
    
    private void setField()
    {
        this.colorName.setText(color.getNome());
    }
    
    /**
     * Sets the new color name, updates in the database, refreshes the list controller table and closes current window
     * @param event triggered event
     * @throws IOException 
     */
    @FXML
    void onEditButtonClick(ActionEvent event) throws IOException
    {
        color.setNome(WordUtils.capitalizeFully(colorName.getText()));
        
        updateColor(color);
        
        this.listColorController.colorTable.refresh();
        closeStage(event);
    }
    
    /**
     * Checks if the color name typed in the text field already exists
     * @param name name that will be checked if exists
     * @param nonCharacters characters that will be stripped from the name
     * @return name existence
     */
    private boolean checkIfNameExists(String name, String nonCharacters)
    {
        for(CorBLL c : colorList)
        {
            if(c.getIdcor() != color.getIdcor())
            {
                String colorName = StringUtils.stripAccents(c.getNome().replaceAll(nonCharacters, "").toLowerCase());
                
                if(name.equals(colorName))
                {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * If color name exists, disables edit button and shows an error in a label
     * @param message error message
     */
    private void disableEditButtonAndShowError(String message)
    {
        editColorNameButton.setDisable(true);
        errorLabel.setText(message);
    }
    
    /**
     * Checks if the typed name exists, disabling or enabling the edit button accordingly, and showing label error
     */
    @FXML
    void checkNewNameToSetButtonDisable()
    {
        String nonCharacters = "[^\\p{L}\\p{Nd}]";
        String editedColorName = StringUtils.stripAccents(colorName.getText().replaceAll(nonCharacters, "").toLowerCase());
        String searchColorName = StringUtils.stripAccents(color.getNome().replaceAll(nonCharacters, "").toLowerCase());
        
        if(colorName.getText().isEmpty())
        {
            editColorNameButton.setDisable(true);
            errorLabel.setText("");
        }
        else
        {
            if(!(editedColorName.equals(searchColorName)))
            {
                boolean exists = checkIfNameExists(editedColorName, nonCharacters);

                if(exists)
                {
                    disableEditButtonAndShowError("Cor já existe");
                }
                else
                {
                    if(!(editColorNameButton.getText().isEmpty()))
                    {
                        errorLabel.setText("");
                    }

                    editColorNameButton.setDisable(false);
                }
            }
            else
            {
                if(!(editColorNameButton.getText().isEmpty()))
                {
                    errorLabel.setText("");
                }

                editColorNameButton.setDisable(true);
            }
        }
    }
    
    /**
     * Updates entity on database
     * @param color color to be updated
     */
    private void updateColor(CorBLL color)
    {
        color.update();
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
