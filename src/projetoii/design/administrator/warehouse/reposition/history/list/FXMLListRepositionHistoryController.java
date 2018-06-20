/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoii.design.administrator.warehouse.reposition.history.list;

import helpers.EntregaBLL;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import projetoii.design.administrator.menu.top.FXMLAdministratorTopMenuController;
import projetoii.design.administrator.warehouse.reposition.history.detail.FXMLRepositionDetailController;
import services.EntregaService;

/**
 * FXML Controller class
 *
 * @author Gustavo Vieira
 */
public class FXMLListRepositionHistoryController implements Initializable {

    @FXML public TableView<EntregaBLL> deliveryTable;
    @FXML private TableColumn<EntregaBLL, String> warehouseColumn;
    @FXML private TableColumn<EntregaBLL, String> shopColumn;
    @FXML private TableColumn<EntregaBLL, Date> sentDateColumn;
    @FXML private TableColumn<EntregaBLL, Date> deliveredDateColumn;
    @FXML private TableColumn<EntregaBLL, String> detailColumn;
    private ObservableList<EntregaBLL> deliveryProductObservableList;
    
    /* Text field used to search repositions on the table, updating as it searches */
    @FXML private TextField searchRepositionTextField;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        List<EntregaBLL> deliveryList = new ArrayList<>();
        deliveryList = EntregaService.getHelperList("FROM Entrega WHERE enviado = 1 AND armazem.idarmazem = " + FXMLAdministratorTopMenuController.getWorkLocationId());
        
        initializeTable(deliveryList);
    }    

    /**
     * Initializes all table content
     * @param deliveryList data to be set onto the table
     */
    private void initializeTable(List<EntregaBLL> deliveryList) {
        this.warehouseColumn.setCellValueFactory((TableColumn.CellDataFeatures<EntregaBLL, String> param) -> new SimpleObjectProperty<>(param.getValue().getArmazem().getNome()));
        this.shopColumn.setCellValueFactory((TableColumn.CellDataFeatures<EntregaBLL, String> param) -> new SimpleObjectProperty<>(param.getValue().getLoja().getNome()));
        this.sentDateColumn.setCellValueFactory(new PropertyValueFactory<>("dataenvio"));
        this.deliveredDateColumn.setCellValueFactory(new PropertyValueFactory<>("dataentrega"));
        this.detailColumn.setCellValueFactory(new PropertyValueFactory<>(""));
        
        /* Sets images for all row buttons and sets the buttons up */
        Image image = new Image(getClass().getResourceAsStream("image/detail.png"));
        Image imageHover = new Image(getClass().getResourceAsStream("image/detail_hover.png"));
        detailColumn.setCellFactory(getButtonCell(image, imageHover));
        
        /* Sets the table content */
        deliveryProductObservableList = FXCollections.observableArrayList(deliveryList);
        setTableItems(deliveryProductObservableList);
    }
    
    /**
     * Creates a button for each table cell, also setting up an image for each button (with a different hover image and size)
     * @param image image to be set onto the button
     * @param imageHover image to show on mouse hover
     * @return cell callback
     */
    private Callback getButtonCell(Image image, Image imageHover)
    {
        Callback<TableColumn<EntregaBLL, String>, TableCell<EntregaBLL, String>> cellFactory;
        cellFactory = new Callback<TableColumn<EntregaBLL, String>, TableCell<EntregaBLL, String>>()
        {
            @Override
            public TableCell call(final TableColumn<EntregaBLL, String> param)
            {
                final TableCell<EntregaBLL, String> cell = new TableCell<EntregaBLL, String>()
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
                            /* Sets an action depending on the passed controller */
                            button.setOnAction((event) -> {
                                EntregaBLL delivery = getTableView().getItems().get(getIndex());
                                loadNewDetailWindow(FXMLRepositionDetailController.class, "FXMLRepositionDetail.fxml", "Armazém - Histórico de Reposições", "Não foi possível carregar o ficheiro FXMLEmployeeDetail.fxml", delivery);
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
     * @param imageView imageView to set the image on
     * @param image image to be shown
     * @param width desired image width
     * @param height desired image height
     */
    private void setButtonImageView(ImageView imageView, Image image, double width, double height)
    {
        imageView.setImage(image);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
    }
    
    /**
     * Sets a button image for each button and its hover
     * @param button button to set an imageView on
     * @param imageView imageView to set the image on
     * @param image button image
     * @param imageHover button image on mouse hover
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
     * Loads a new detail window
     * @param controller file controller
     * @param fileName file name
     * @param title window title
     * @param message error message
     * @param delivery delivery to be detailed
     */
    private void loadNewDetailWindow(Class controller, String fileName, String title, String message, EntregaBLL delivery)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(controller.getResource(fileName));
            Parent root = (Parent) loader.load();
            
            FXMLRepositionDetailController detailController = (FXMLRepositionDetailController) loader.getController();
            detailController.initializeOnControllerCall(delivery);
            
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch(Exception e)
        {
        }
    }

    /**
     * Updates table items
     * @param deliveryProductObservableList data to be set onto the table
     */
    private void setTableItems(ObservableList<EntregaBLL> deliveryProductObservableList) {
        this.deliveryTable.setItems(deliveryProductObservableList);
    }
    
    /**
     * Searches for deliveries when a key is pressed
     */
    @FXML
    void getSearchList()
    {
        List<EntregaBLL> deliveryList = new ArrayList<>();
            
        /* If something has been typed, tries to find an existent delivery with the given name or ID */
        if(searchRepositionTextField.getText().length() > 0)
        {
            deliveryList.clear();
            
            String nonCharacters = "[^\\p{L}\\p{Nd}]";
            
            for(EntregaBLL delivery : deliveryProductObservableList)
            {
                String searchString = StringUtils.stripAccents(searchRepositionTextField.getText().replaceAll(nonCharacters, "").toLowerCase());
                
                String warehouseName = StringUtils.stripAccents(delivery.getArmazem().getNome().replaceAll(nonCharacters, "").toLowerCase());
                String shopName = StringUtils.stripAccents(delivery.getLoja().getNome().replaceAll(nonCharacters, "").toLowerCase());
                String date1 = StringUtils.stripAccents(String.valueOf(delivery.getDataenvio()).replaceAll(nonCharacters, "").toLowerCase());
                String date2 = StringUtils.stripAccents(String.valueOf(delivery.getDataentrega()).replaceAll(nonCharacters, "").toLowerCase());
                String deliveryID = String.valueOf(delivery.getIdentrega());
                
                if(warehouseName.contains(searchString) || deliveryID.contains(searchString) || shopName.contains(searchString) || date1.contains(searchString) || date2.contains(searchString))
                {
                    deliveryList.add(delivery);
                }
            }
            
            setSearchedTableValues(deliveryList);
        }
        else /* If nothing has been typed, show full category list */
        {
            deliveryList.clear();
            
            deliveryList = deliveryProductObservableList;
            setSearchedTableValues(deliveryList);
        }
    }
    
    /**
     * Sets new table values
     * @param deliveryList new delivery list data to shown on table
     */
    public void setSearchedTableValues(List<EntregaBLL> deliveryList)
    {
        ObservableList<EntregaBLL> newDeliveryObservableList;
        newDeliveryObservableList = FXCollections.observableArrayList(deliveryList);
        setTableItems(newDeliveryObservableList);
    }
}
