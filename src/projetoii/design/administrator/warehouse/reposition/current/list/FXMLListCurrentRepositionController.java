/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoii.design.administrator.warehouse.reposition.current.list;

import helpers.EntregaBLL;
import helpers.ProdutoEntregaBLL;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import projetoii.design.administrator.warehouse.menu.top.FXMLWarehouseTopMenuController;
import projetoii.design.administrator.warehouse.reposition.current.restock.FXMLRestockProductController;
import projetoii.design.administrator.warehouse.reposition.history.list.FXMLListRepositionHistoryController;
import services.EntregaService;
import services.ProdutoEntregaService;

/**
 * FXML Controller class
 *
 * @author Gustavo Vieira
 */
public class FXMLListCurrentRepositionController implements Initializable {

    @FXML public TableView<ProdutoEntregaBLL> deliveryProductTable;
    @FXML private TableColumn<ProdutoEntregaBLL, Long> barCodeColumn;
    @FXML private TableColumn<ProdutoEntregaBLL, String> nameColumn;
    @FXML private TableColumn<Integer, Integer> soldColumn;
    private ObservableList<ProdutoEntregaBLL> deliveryProductObservableList;
    
    List<ProdutoEntregaBLL> productList = new ArrayList<>();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        List<EntregaBLL> restockList = EntregaService.getHelperList("FROM Entrega WHERE dataentrega is null AND dataenvio is null AND enviado = 0");
        
        if(!(restockList.isEmpty()))
        {
            productList = ProdutoEntregaService.getHelperList("FROM Produtoentrega WHERE entrega.identrega = " + restockList.get(0).getIdentrega());
            initializeTable(productList);
        }
    }    

    /**
     * Initializes all table content for the first time
     * @param productList data that will be shown in the table
     */
    private void initializeTable(List<ProdutoEntregaBLL> productList) {
        /* Sets column variables to use entity info, empty for a button creation */
        this.barCodeColumn.setCellValueFactory((TableColumn.CellDataFeatures<ProdutoEntregaBLL, Long> param) -> new SimpleObjectProperty<>(param.getValue().getProduto().getCodbarras()));
        this.nameColumn.setCellValueFactory((TableColumn.CellDataFeatures<ProdutoEntregaBLL, String> param) -> new SimpleObjectProperty<>(param.getValue().getProduto().getDescricao()));  
        this.soldColumn.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        
        /* Sets the table content */
        deliveryProductObservableList = FXCollections.observableArrayList(productList);
        setTableItems(deliveryProductObservableList);
    }

    private void setTableItems(ObservableList<ProdutoEntregaBLL> deliveryProductObservableList) {
        this.deliveryProductTable.setItems(deliveryProductObservableList);
    }
    
    /**
     * Changes to the history panel
     */
    @FXML private void onHistoryButtonClick()
    {
        try
        {
            Pane newPane = FXMLLoader.load(FXMLListRepositionHistoryController.class.getResource("FXMLListRepositionHistory.fxml"));
            FXMLWarehouseTopMenuController.getStaticPane().setCenter(newPane);
        }
        catch(Exception e)
        {
        }
    }
    
    /**
     * Opens the restock panel
     */
    @FXML private void onRestockButtonClick()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(FXMLRestockProductController.class.getResource("FXMLRestockProduct.fxml"));
            Parent root = (Parent) loader.load();
            
            FXMLRestockProductController repositionController = (FXMLRestockProductController) loader.getController();
            
            Stage stage = new Stage();
            stage.setTitle("Armazém - Repor Produtos");
            stage.setScene(new Scene(root));
            
            repositionController.initializeOnContorllerCall(this, productList, stage);
            
            stage.show();
        }
        catch(Exception e)
        {
        }
    }
}
