/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoii.design.administrator.warehouse.reposition.current.restock;

import helpers.FuncionarioBLL;
import helpers.ProdutoEntregaBLL;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import projetoii.design.administrator.menu.top.FXMLAdministratorTopMenuController;
import projetoii.design.administrator.warehouse.reposition.current.list.FXMLListCurrentRepositionController;
import projetoii.design.administrator.warehouse.reposition.current.restock.last.FXMLRestockLastProductController;
import projetoii.design.administrator.warehouse.reposition.current.restock.next.FXMLRestockNextProductController;
import projetoii.design.administrator.warehouse.reposition.current.restock.only.FXMLRestockOnlyProductController;
import projetoii.design.administrator.warehouse.reposition.current.restock.previous.FXMLRestockPreviousProductController;
import services.FuncionarioService;

/**
 * FXML Controller class
 *
 * @author Gustavo Vieira
 */
public class FXMLRestockProductController implements Initializable {

    /* Info Variables */
    @FXML private TextField barCodeField;
    @FXML private TextField nameField;
    @FXML private TextField soldField;
    @FXML private TextField stockField;
    @FXML private TextField restockField;
    @FXML private ComboBox employeeBox;
    @FXML private DatePicker dateField;
    
    FXMLListCurrentRepositionController repositionController;
    List<ProdutoEntregaBLL> productList;
    
    @FXML private BorderPane currentProductBorderPane;
    
    private static int count = 1;
    private static int size;
    private static BorderPane staticPane;
    private static Stage stage;

    public static Stage getStage() {
        return stage;
    }
    
    public static void setStage(Stage stage) {
        FXMLRestockProductController.stage = stage;
    }
    
    public static BorderPane getStaticPane() {
        return staticPane;
    }

    public static void setStaticPane(BorderPane staticPane) {
        FXMLRestockProductController.staticPane = staticPane;
    }

    public static int getSize() {
        return size;
    }

    public static void setSize(int size) {
        FXMLRestockProductController.size = size;
    }

    public static int getCount() {
        return count;
    }

    public static void setCount(int count) {
        FXMLRestockProductController.count = count;
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    /**
     * Initializes this controller when called from another controller
     * @param repositionController controller who called this controller
     * @param productList all products
     * @param stage stage to set window name
     */
    public void initializeOnContorllerCall(FXMLListCurrentRepositionController repositionController, List<ProdutoEntregaBLL> productList, Stage stage) {
        setCurrentRepositionController(repositionController);
        setProductList(productList);
        setStage(stage);
        
        setStaticPane(currentProductBorderPane);
        if(!(productList.isEmpty())) { setSize(productList.size()); }
        if(!(productList.isEmpty())) { setBottomBar(); }
        if(!(productList.isEmpty())) { showProduct(); }
        if(!(productList.isEmpty())) { setStageTitle(); }
    }

    private void setProductList(List<ProdutoEntregaBLL> productList) {
        this.productList = productList;
    }

    private void setCurrentRepositionController(FXMLListCurrentRepositionController repositionController) {
        this.repositionController = repositionController;
    }
    
    /**
     * Sets the bottom bar depending on the current product count
     */
    public static void setBottomBar()
    {
        if(getSize() == 1)
        {
            switchBottom(FXMLRestockOnlyProductController.class, "FXMLRestockOnlyProduct.fxml");
        }
        else
        {
            if(getCount() == 1)
            {
                switchBottom(FXMLRestockNextProductController.class, "FXMLRestockNextProduct.fxml");
            }
            else if(getCount() == getSize())
            {
                switchBottom(FXMLRestockLastProductController.class, "FXMLRestockLastProduct.fxml");
            }
            else
            {
                switchBottom(FXMLRestockPreviousProductController.class, "FXMLRestockPreviousProduct.fxml");
            }
        }
    }
    
    /**
     * Sets the border pane bottom
     * @param controller To be displayed controller
     * @param file To be opened file
     */
    private static void switchBottom(Class controller, String file)
    {
        try
        {
            Pane newPane = FXMLLoader.load(controller.getResource(file));
            getStaticPane().setBottom(newPane);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Shows all product information
     */
    private void showProduct()
    {
        setFields();
    }
    
    /**
     * Sets all fields
     */
    public void setFields()
    {
        ProdutoEntregaBLL product = productList.get(getCount() - 1);
        this.barCodeField.setText(String.valueOf(product.getProduto().getCodbarras()));
        this.nameField.setText(product.getProduto().getDescricao());
        this.soldField.setText(String.valueOf(product.getQuantidade()));
        
        List<FuncionarioBLL> employeeList = FuncionarioService.getHelperList("FROM Funcionario WHERE localtrabalho.idlocaltrabalho = " + FXMLAdministratorTopMenuController.getWorkLocationId());
        this.employeeBox.getItems().addAll(employeeList);
        this.employeeBox.getSelectionModel().select(0);
    }
    
    /**
     * Sets the stage title
     */
    public static void setStageTitle()
    {
        getStage().setTitle("[Armazém] Reposição - " + getCount() + " de " + getSize());
    }
}
