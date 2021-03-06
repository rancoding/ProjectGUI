package projetoii.design.administrator.warehouse.data.color.list;

import helpers.CorBLL;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.commons.lang3.StringUtils;
import projetoii.design.administrator.warehouse.data.color.add.FXMLAddColorController;
import projetoii.design.administrator.warehouse.data.color.edit.FXMLEditColorController;
import services.CorService;

public class FXMLListColorController implements Initializable {

    /* Variables used for setting up the table content */
    @FXML public TableView<CorBLL> colorTable;
    @FXML private TableColumn<CorBLL, Byte> idColumn;
    @FXML private TableColumn<CorBLL, String> nameColumn;
    @FXML private TableColumn<CorBLL, String> editColumn;
    private ObservableList<CorBLL> colorObservableList;
    
    /* Text field used to search colors on the table, updating as it searches */
    @FXML private TextField searchColorTextField;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        idColumn.setStyle("-fx-alignment: CENTER;");
        
        /* Retrieves all database colors to an arraylist and initializes the table values if it is not empty */
        List<CorBLL> colorList = CorService.getHelperList("FROM Cor ORDER BY idcor ASC");
        
        if(!(colorList.isEmpty()))
        {
            initializeTable(colorList);
        }
        else
        {
            colorList = new ArrayList<>();
            initializeTable(colorList);
        }
    }
    
    /**
     * Initializes all table content for the first time
     * @param colorList data list that will be shown on the table
     */
    private void initializeTable(List<CorBLL> colorList)
    {
        /* Sets column variables to use entity info, empty for a button creation */
        this.idColumn.setCellValueFactory(new PropertyValueFactory<>("idcor"));
        this.nameColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));
        this.editColumn.setCellValueFactory(new PropertyValueFactory<>(""));
        
        /* Sets images for all row buttons and sets the buttons up */
        Image image = new Image(getClass().getResourceAsStream("image/edit.png"));
        Image imageHover = new Image(getClass().getResourceAsStream("image/edit_hover.png"));
        editColumn.setCellFactory(getButtonCell(image, imageHover));
        
        /* Sets the table content */
        colorObservableList = FXCollections.observableArrayList(colorList);
        setTableItems(colorObservableList);
    }
    
    /**
     * Sets the table items to be the same as the observable list items
     * @param colorObservableList list that will be set into the table
     */
    private void setTableItems(ObservableList<CorBLL> colorObservableList)
    {
        this.colorTable.setItems(colorObservableList);
    }
    
    /**
     * Creates a button for each table cell, also setting up an image for each button (with a different hover image and size)
     * @param image button image
     * @param imageHover button image on mouse hover
     * @return cell callback
     */
    private Callback getButtonCell(Image image, Image imageHover)
    {
        Callback<TableColumn<CorBLL, String>, TableCell<CorBLL, String>> cellFactory;
        cellFactory = new Callback<TableColumn<CorBLL, String>, TableCell<CorBLL, String>>()
        {
            @Override
            public TableCell call(final TableColumn<CorBLL, String> param)
            {
                final TableCell<CorBLL, String> cell = new TableCell<CorBLL, String>()
                {
                    final Button button = new Button();
                    
                    @Override
                    public void updateItem(String item, boolean empty)
                    {
                        super.updateItem(item, empty);
                        if(empty)
                        {
                            setGraphic(null);
                            setText(null);
                        }
                        else
                        {
                            /* On edit button, opens an edit category window with the row category info and the list of existent categories */
                            button.setOnAction((event) -> {
                                CorBLL color = getTableView().getItems().get(getIndex());
                                loadNewEditWindow(FXMLEditColorController.class, "FXMLEditColor.fxml", "Armazém - Editar Cor", "Não foi possível carregar o ficheiro FXMLEditColor.fxml", color);
                            });
                            
                            setGraphic(button);
                            setText(null);
                            
                            ImageView imageView = new ImageView();
                            setButtonImageView(imageView, image, 12, 12);
                            setRowButton(button, imageView, image, imageHover);
                        }
                    }
                };
                
                cell.setAlignment(Pos.CENTER);
                return cell;
            }
        };
        
        return cellFactory;
    }
    
    /**
     * Sets the button image and size
     * @param imageView button imageView
     * @param image image that will be set onto the imageView
     * @param width desired image width
     * @param height  desired image height
     */
    private void setButtonImageView(ImageView imageView, Image image, double width, double height)
    {
        imageView.setImage(image);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
    }
    
    /**
     * Sets a button image for each button and its hover
     * @param button button to set the image on
     * @param imageView button imageView
     * @param image image that will be set onto the imageView
     * @param imageHover image that will be shown on mouse hover
     */
    private void setRowButton(Button button, ImageView imageView, Image image, Image imageHover)
    {
        button.setBackground(Background.EMPTY);
        button.setGraphic(imageView);

        button.hoverProperty().addListener((ov, oldValue, newValue) -> {
            if(newValue) // On hover
            {
                setButtonImageView(imageView, imageHover, 14, 14);
                button.setGraphic(imageView);
            }
            else // Not on hover
            {
                setButtonImageView(imageView, image, 12, 12);
                button.setGraphic(imageView);
            }
        });
    }
    
    /**
     * Loads a new window on button click
     * @param event triggered event
     */
    @FXML
    void handleAddButtonAction(ActionEvent event)
    {
        loadNewAddWindow(FXMLAddColorController.class, "FXMLAddColor.fxml", "Armazém - Adicionar Cor", "Não foi possível carregar o ficheiro FXMLAddColor.fxml");
    }
    
    /**
     * Loads a new add window
     * @param controller file controller
     * @param fileName file name
     * @param title window title
     * @param message error message
     */
    private void loadNewAddWindow(Class controller, String fileName, String title, String message)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(controller.getResource(fileName));
            Parent root = (Parent) loader.load();
            
            FXMLAddColorController addController = (FXMLAddColorController) loader.getController();
            addController.initializeOnControllerCall(this, colorObservableList);
            
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch(Exception e)
        {
            System.out.println(message);
        }
    }
    
    /**
     * Loads a new edit window
     * @param controller file controller
     * @param fileName file name
     * @param title window title
     * @param message error message
     * @param color to be edited color
     */
    private void loadNewEditWindow(Class controller, String fileName, String title, String message, CorBLL color)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(controller.getResource(fileName));
            Parent root = (Parent) loader.load();
            
            FXMLEditColorController editController = (FXMLEditColorController) loader.getController();
            editController.initializeOnControllerCall(this, colorObservableList, color);
            
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch(Exception e)
        {
            System.out.println(message);
        }
    }
    
    /**
     * Searches for colors when a key is pressed
     */
    @FXML
    void getSearchList()
    {
        List<CorBLL> colorList = new ArrayList<>();
            
        /* If something has been typed, tries to find an existent color with the given name or ID */
        if(searchColorTextField.getText().length() > 0)
        {
            colorList.clear();
            
            String nonCharacters = "[^\\p{L}\\p{Nd}]";
            
            for(CorBLL color : colorObservableList)
            {
                String searchString = StringUtils.stripAccents(searchColorTextField.getText().replaceAll(nonCharacters, "").toLowerCase());
                
                String colorName = StringUtils.stripAccents(color.getNome().replaceAll(nonCharacters, "").toLowerCase());
                String colorID = String.valueOf(color.getIdcor());
                
                if(colorName.contains(searchString) || colorID.contains(searchString))
                {
                    colorList.add(color);
                }
            }
            
            setSearchedTableValues(colorList);
        }
        else /* If nothing has been typed, show full color list */
        {
            colorList.clear();
            
            colorList = colorObservableList;
            setSearchedTableValues(colorList);
        }
    }
    
    /**
     * Sets new table values
     * @param colorList list to be set on the table
     */
    public void setSearchedTableValues(List<CorBLL> colorList)
    {
        ObservableList<CorBLL> newColorObservableList;
        newColorObservableList = FXCollections.observableArrayList(colorList);
        setTableItems(newColorObservableList);
    }
}
