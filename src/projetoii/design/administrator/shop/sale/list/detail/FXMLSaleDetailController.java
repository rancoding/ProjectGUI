/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoii.design.administrator.shop.sale.list.detail;

import helpers.ProdutoVendaBLL;
import helpers.VendaBLL;
import java.net.URL;
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
import projetoii.design.administrator.shop.sale.list.FXMLListSaleController;
import services.ProdutoVendaService;

/**
 * FXML Controller class
 *
 * @author Gustavo Vieira
 */
public class FXMLSaleDetailController implements Initializable {

    FXMLListSaleController listSaleController;
    VendaBLL sale;
    
    @FXML private TableView<ProdutoVendaBLL> productTable;
    @FXML private TableColumn<ProdutoVendaBLL, Long> barCodeColumn;
    @FXML private TableColumn<ProdutoVendaBLL, String> nameColumn;
    @FXML private TableColumn<ProdutoVendaBLL, Integer> quantityColumn;
    @FXML private TableColumn<ProdutoVendaBLL, String> valueColumn;
    private ObservableList<ProdutoVendaBLL> productObservableList; 
    
    @FXML private TextField dateField;
    @FXML private TextField employeeField;
    @FXML private TextField valueField;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    public void initializeOnControllerCall(FXMLListSaleController listSaleController, VendaBLL sale) {
        setListSaleController(listSaleController);
        setSale(sale);
        
        setFields();
        List<ProdutoVendaBLL> productSaleList = ProdutoVendaService.getHelperList("FROM Produtovenda WHERE venda.idvenda = " + sale.getIdvenda());
        initializeTable(productSaleList);
    }
    
    private void setListSaleController(FXMLListSaleController listSaleController) {
        this.listSaleController = listSaleController;
    }

    private void setSale(VendaBLL sale) {
        this.sale = sale;
    }

    private void initializeTable(List<ProdutoVendaBLL> productSaleList) {
        this.barCodeColumn.setCellValueFactory((TableColumn.CellDataFeatures<ProdutoVendaBLL, Long> param) -> new SimpleObjectProperty<>(param.getValue().getProduto().getCodbarras()));
        this.nameColumn.setCellValueFactory((TableColumn.CellDataFeatures<ProdutoVendaBLL, String> param) -> new SimpleObjectProperty<>(param.getValue().getProduto().getDescricao()));
        this.quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        this.valueColumn.setCellValueFactory((TableColumn.CellDataFeatures<ProdutoVendaBLL, String> param) -> new SimpleObjectProperty<>(String.valueOf(param.getValue().getProduto().getPrecovenda()) + "€"));
    
        /* Sets the table content */
        productObservableList = FXCollections.observableArrayList(productSaleList);
        setTableItems(productObservableList);
    }

    private void setTableItems(ObservableList<ProdutoVendaBLL> productObservableList) {
        this.productTable.setItems(productObservableList);
    }
    
    private void setFields() {
        this.dateField.setText(String.valueOf(sale.getDatavenda()));
        this.employeeField.setText(sale.getFuncionario().getNome());
        this.valueField.setText(String.valueOf(sale.getValortotal()) + "€");
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
