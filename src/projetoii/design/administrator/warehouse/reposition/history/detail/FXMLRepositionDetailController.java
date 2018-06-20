/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoii.design.administrator.warehouse.reposition.history.detail;

import helpers.EntregaBLL;
import helpers.ProdutoEntregaBLL;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import services.ProdutoEntregaService;

/**
 * FXML Controller class
 *
 * @author Gustavo Vieira
 */
public class FXMLRepositionDetailController implements Initializable {

    /* Variables used for setting up the table content */
    @FXML public TableView<ProdutoEntregaBLL> productTable;
    @FXML private TableColumn<ProdutoEntregaBLL, Long> barCodeColumn;
    @FXML private TableColumn<ProdutoEntregaBLL, String> nameColumn;
    @FXML private TableColumn<ProdutoEntregaBLL, Integer> quantityColumn;
    @FXML private TableColumn<ProdutoEntregaBLL, String> sentEmployeeColumn;
    @FXML private TableColumn<ProdutoEntregaBLL, String> receivedEmployeeColumn;
    private ObservableList<ProdutoEntregaBLL> deliveryProductObservableList;
    
    @FXML private TextField warehouseField;
    @FXML private TextField shopField;
    @FXML private TextField sentDateField;
    @FXML private TextField receivedDateField;
    
    private EntregaBLL delivery;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    /**
     * Initializes this controller when called from another controller
     * @param delivery delivery to show details from
     */
    public void initializeOnControllerCall(EntregaBLL delivery) {
        setDelivery(delivery);
        setFields();
        
        List<ProdutoEntregaBLL> productList = new ArrayList<>();
        productList = ProdutoEntregaService.getHelperList("FROM Produtoentrega WHERE entrega.identrega = " + delivery.getIdentrega() + " ORDER BY quantidade DESC");
        
        initializeTable(productList);
    }
    
    private void setDelivery(EntregaBLL delivery)
    {
        this.delivery = delivery;
    }
    
    /**
     * Sets all fields information
     */
    private void setFields()
    {
        warehouseField.setText(delivery.getArmazem().getNome());
        shopField.setText(delivery.getLoja().getNome());
        sentDateField.setText(delivery.getDataenvio().toString());
        receivedDateField.setText(delivery.getDataentrega().toString());
    }
    
    /**
     * Initializes the table values
     * @param productList data that will be set onto the table
     */
    private void initializeTable(List<ProdutoEntregaBLL> productList)
    {
        this.barCodeColumn.setCellValueFactory((TableColumn.CellDataFeatures<ProdutoEntregaBLL, Long> param) -> new SimpleObjectProperty<>(param.getValue().getProduto().getCodbarras()));
        this.nameColumn.setCellValueFactory((TableColumn.CellDataFeatures<ProdutoEntregaBLL, String> param) -> new SimpleObjectProperty<>(param.getValue().getProduto().getDescricao()));
        this.quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        this.sentEmployeeColumn.setCellValueFactory((TableColumn.CellDataFeatures<ProdutoEntregaBLL, String> param) -> new SimpleObjectProperty<>(param.getValue().getEntrega().getFuncionarioByIdfuncionarioentrega().getNome()));
        this.receivedEmployeeColumn.setCellValueFactory((TableColumn.CellDataFeatures<ProdutoEntregaBLL, String> param) -> new SimpleObjectProperty<>(param.getValue().getEntrega().getFuncionarioByIdfuncionariorecebe().getNome()));
    
        /* Sets the table content */
        deliveryProductObservableList = FXCollections.observableArrayList(productList);
        setTableItems(deliveryProductObservableList);
    }
    
    /**
     * Sets the table items to be the same as the observable list items
     * @param deliveryProductObservableList data that will be set to the table
     */
    private void setTableItems(ObservableList<ProdutoEntregaBLL> deliveryProductObservableList)
    {
        this.productTable.setItems(deliveryProductObservableList);
    }
    
    /**
     * Closes the stage on cancel button click
     * @param event triggered event
     */
    @FXML void onBackClick(ActionEvent event)
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
