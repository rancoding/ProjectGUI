/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoii.design.administrator.warehouse.box.list;

import helpers.CaixaBLL;
import helpers.ProdutoBLL;
import helpers.TipoProdutoBLL;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.commons.lang3.StringUtils;
import projetoii.design.administrator.warehouse.box.add.FXMLAddProductToBoxController;
import projetoii.design.administrator.warehouse.data.product.box.list.FXMLListProductBoxController;
import projetoii.design.administrator.warehouse.data.product.detail.FXMLProductDetailController;
import projetoii.design.administrator.warehouse.data.product.edit.FXMLEditProductController;
import services.CaixaService;

/**
 * FXML Controller class
 *
 * @author Gustavo Vieira
 */
public class FXMLListBoxController implements Initializable {

    /* Variables used for setting up the table content */
    @FXML public TableView<CaixaBLL> boxTable;
    @FXML private TableColumn<CaixaBLL, String> boxNumberColumn;
    @FXML private TableColumn<CaixaBLL, String> barCodeColumn;
    @FXML private TableColumn<CaixaBLL, String> nameColumn;
    @FXML private TableColumn<CaixaBLL, String> quantityColumn;
    @FXML private TableColumn<CaixaBLL, String> editColumn;
    @FXML private TableColumn<CaixaBLL, String> deleteColumn;
    @FXML private TreeTableColumn<CaixaBLL, String> detailColumn;
    private ObservableList<CaixaBLL> boxObservableList;
    
    
    @FXML public TextField searchBoxTextField;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        /* Retrieves all database product types to an arraylist and initializes the table values if it is not empty */
        List<CaixaBLL> boxList = CaixaService.getHelperList("FROM Caixa ORDER BY idcaixa ASC");
        
        if(!(boxList.isEmpty()))
        {
            initializeTable(boxList);
        }
        else
        {
            boxList = new ArrayList<>();
            initializeTable(boxList);
        }
    }    
    
    /** Initializes all table content for the first time **/
    private void initializeTable(List<CaixaBLL> boxList)
    {
        /* Sets column variables to use entity info, empty for a button creation */
        this.boxNumberColumn.setCellValueFactory(new PropertyValueFactory<>("idcaixa"));
        this.barCodeColumn.setCellValueFactory(new PropertyValueFactory<>("codbarras"));
        this.nameColumn.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        this.quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantidade"));

        this.editColumn.setCellValueFactory(new PropertyValueFactory<>(""));
//        this.deleteColumn.setCellValueFactory(new PropertyValueFactory<>(""));
//        this.detailColumn.setCellValueFactory(new PropertyValueFactory<>(""));
        
        /* Sets images for all row buttons and sets the buttons up */
        Image image = new Image(getClass().getResourceAsStream("image/edit.png"));
        Image imageHover = new Image(getClass().getResourceAsStream("image/edit_hover.png"));
        editColumn.setCellFactory(getButtonEditCell(image, imageHover));
        
//        /* Sets images for all row buttons and sets the buttons up */
//        Image imageDelete = new Image(getClass().getResourceAsStream("image/delete.png"));
//        Image imageHoverDelete = new Image(getClass().getResourceAsStream("image/box_hover.png"));
//        deleteColumn.setCellFactory(getDeleteProductCell(imageDelete, imageHoverDelete));
//        
//        /* Sets images for all row buttons and sets the buttons up */
//        Image imageDetail = new Image(getClass().getResourceAsStream("image/detail.png"));
//        Image imageHoverDetail = new Image(getClass().getResourceAsStream("image/detail_hover.png"));
//        detailColumn.setCellFactory(getButtonDetailCell(imageDetail, imageHoverDetail));
        
        /* Sets the table content */
        boxObservableList = FXCollections.observableArrayList(boxList);
        setTableItems(boxObservableList);
    }
    
    /* * Sets the table items to be the same as the observable list items * */
    private void setTableItems(ObservableList<CaixaBLL> boxObservableList)
    {
        this.boxTable.setItems(boxObservableList);
    }
    
    /* Creates a button for each table cell, also setting up an image for each button (with a different hover image and size) */
    private Callback getButtonEditCell(Image image, Image imageHover)
    {
        Callback<TableColumn<CaixaBLL, String>, TableCell<CaixaBLL, String>> cellFactory;
        cellFactory = new Callback<TableColumn<CaixaBLL, String>, TableCell<CaixaBLL, String>>()
        {
            @Override
            public TableCell call(final TableColumn<CaixaBLL, String> param)
            {
                final TableCell<CaixaBLL, String> cell = new TableCell<CaixaBLL, String>()
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
                                CaixaBLL type = getTableView().getItems().get(getIndex());
                                //loadNewEditWindow(FXMLEditProductController.class, "FXMLEditBoxProduct.fxml", "Armazém - Editar Caixa de Produto", "Não foi possível carregar o ficheiro FXMLEditBoxProduct.fxml", type);
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
    
     /* Creates a button for each table cell, also setting up an image for each button (with a different hover image and size) */
    private Callback getDeleteProductCell(Image image, Image imageHover)
    {
        Callback<TableColumn<CaixaBLL, String>, TableCell<CaixaBLL, String>> cellFactory;
        cellFactory = new Callback<TableColumn<CaixaBLL, String>, TableCell<CaixaBLL, String>>()
        {
            @Override
            public TableCell call(final TableColumn<CaixaBLL, String> param)
            {
                final TableCell<CaixaBLL, String> cell = new TableCell<CaixaBLL, String>()
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
                                CaixaBLL type = getTableView().getItems().get(getIndex());
                                //loadNewBoxWindow(FXMLListProductBoxController.class, "FXMLListProductBox.fxml", "Armazém - Lista de Caixas do Produto", "Não foi possível carregar o ficheiro FXMLListProductBox.fxml", type);
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
    
     /* Creates a button for each table cell, also setting up an image for each button (with a different hover image and size) */
    private Callback getButtonDetailCell(Image image, Image imageHover)
    {
        Callback<TableColumn<CaixaBLL, String>, TableCell<CaixaBLL, String>> cellFactory;
        cellFactory = new Callback<TableColumn<CaixaBLL, String>, TableCell<CaixaBLL, String>>()
        {
            @Override
            public TableCell call(final TableColumn<CaixaBLL, String> param)
            {
                final TableCell<CaixaBLL, String> cell = new TableCell<CaixaBLL, String>()
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
                                CaixaBLL box = getTableView().getItems().get(getIndex());
                                System.out.println(box);
                                //loadNewDetailWindow(FXMLProductDetailController.class, "FXMLProductDetail.fxml", "Armazém - Detalhe Produto", "Não foi possível carregar o ficheiro FXMLProductDetail.fxml", product);
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
    
     /* * Sets the button image and size * */
    private void setButtonImageView(ImageView imageView, Image image, double width, double height)
    {
        imageView.setImage(image);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
    }
    
    /* * Sets a button image for each button and its hover * */
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
    
    /* * Loads a new window on button click * */
    @FXML
    void handleAddButtonAction(ActionEvent event)
    {
        loadNewAddWindow(FXMLAddProductToBoxController.class, "FXMLAddToBoxProduct.fxml", "Armazém - Adicionar Produto", "Não foi possível carregar o ficheiro FXMLAddToBoxProduct.fxml");
    }
    
    /* * Loads a new add window * */
    private void loadNewAddWindow(Class controller, String fileName, String title, String message)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(controller.getResource(fileName));
            Parent root = (Parent) loader.load();
            
             FXMLAddProductToBoxController addController = (FXMLAddProductToBoxController) loader.getController();
            //addController.initializeOnControllerCall(this, boxObservableList);
           // addController.initialize(this, boxObservableList);
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
    
    /* * Loads a new edit window * */
    private void loadNewEditWindow(Class controller, String fileName, String title, String message, ProdutoBLL type)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(controller.getResource(fileName));
            Parent root = (Parent) loader.load();
            
            FXMLEditProductController editController = (FXMLEditProductController) loader.getController();
            //editController.initializeOnControllerCall(this, boxObservableList, type);
            
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
    
    /* * Loads a new edit window * */
    private void loadNewBoxWindow(Class controller, String fileName, String title, String message, ProdutoBLL type)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(controller.getResource(fileName));
            Parent root = (Parent) loader.load();
            
            FXMLListProductBoxController listController = (FXMLListProductBoxController) loader.getController();
            //listController.initializeOnControllerCall(this, productObservableList, type);
            
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
    
    /* * Loads a new detail window * */
    private void loadNewDetailWindow(Class controller, String fileName, String title, String message, ProdutoBLL product)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(controller.getResource(fileName));
            Parent root = (Parent) loader.load();
            
            FXMLProductDetailController detailController = (FXMLProductDetailController) loader.getController();
            detailController.initializeOnControllerCall(product);
            
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
    
    /* * Searches for categories when a key is released * */
    @FXML
    void getSearchList()
    {
        List<CaixaBLL> typeList = new ArrayList<>();
            
        /* If something has been typed, tries to find an existent category with the given name or ID */
        if(searchBoxTextField.getText().length() > 0)
        {
            typeList.clear();
            TipoProdutoBLL type = new TipoProdutoBLL();
            
            String nonCharacters = "[^\\p{L}\\p{Nd}]";
            
            for(CaixaBLL box : boxObservableList)
            {
                String searchString = StringUtils.stripAccents(searchBoxTextField.getText().replaceAll(nonCharacters, "").toLowerCase());
                
                String boxNumber = StringUtils.stripAccents(String.valueOf(box.getId()).replaceAll(nonCharacters, "").toLowerCase());
                //String productCategory = StringUtils.stripAccents(type.toString().replaceAll(nonCharacters, "").toLowerCase());
                
                if(boxNumber.equals(searchString))
                {
                    typeList.add(box);
                }
            }
            
            setSearchedTableValues(typeList);
        }
        else /* If nothing has been typed, show full category list */
        {
            typeList.clear();
            
            typeList = boxObservableList;
            setSearchedTableValues(typeList);
        }
    }
    
    /* * Sets new table values * */
    public void setSearchedTableValues(List<CaixaBLL> boxList)
    {
        ObservableList<CaixaBLL> newBoxObservableList;
        newBoxObservableList = FXCollections.observableArrayList(boxList);
        setTableItems(newBoxObservableList);
    }
   
}
