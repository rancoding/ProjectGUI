/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoii.design.user.work.sale.current.list;

import dao.Produtovenda;
import dao.Venda;
import helpers.ProdutoBLL;
import helpers.VendaBLL;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import projetoii.design.user.work.menu.top.FXMLUserTopMenuController;
import services.VendaService;

/**
 * FXML Controller class
 *
 * @author Gustavo Vieira
 */
public class FXMLListCurrentSaleController implements Initializable {

    
    /* Variables used for setting up the table content */
    @FXML public TableView<VendaBLL> saleTable;
    @FXML private TableColumn<Produtovenda, Long> barCodeColumn;
    @FXML private TableColumn<Produtovenda, String> nameColumn;
    @FXML private TableColumn<Produtovenda, String> sizeColumn;
    @FXML private TableColumn<Produtovenda, Integer> quantityColumn;
    @FXML private TableColumn<Produtovenda, Float> priceColumn;
    private ObservableList<VendaBLL> saleObservableList;
    
    /* Text field used to search sales on the table, updating as it searches */
    @FXML private TextField searchSaleTextField;
    
    @FXML private Button accountBtn;
    @FXML private Button closePointBtn;
    @FXML private Button cancelBtn;
    @FXML private Button saveSaleBtn;
    @FXML private Button payBtn;
    
    @FXML private Label totalLabel;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        setFields();
        /* Retrieves all database sales to an arraylist and initializes the table values if it is not empty */
        List<VendaBLL> saleList = new ArrayList<>();
        saleList = VendaService.getHelperList("FROM Venda WHERE funcionario.idfuncionario = " + FXMLUserTopMenuController.employee.getIdfuncionario() + " ORDER BY datavenda ASC");  
        saleList = VendaService.getHelperList("FROM Venda WHERE funcionario.idfuncionario = " + FXMLUserTopMenuController.employee.getIdfuncionario() + " ORDER BY datavenda ASC"); 
    }   
    
    /**
     * Initializes all table content for the first time
     * @param brandList data that will be shown in the table
     */
    private void initializeTable(List<VendaBLL> saleList)
    {
        /* Sets column variables to use entity info, empty for a button creation */
        this.barCodeColumn.setCellValueFactory(new PropertyValueFactory<>("codbarras"));
        this.nameColumn.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        this.sizeColumn.setCellValueFactory(new PropertyValueFactory<>("tamanho.descricao"));
        this.quantityColumn.setCellValueFactory(new PropertyValueFactory<>(""));
        this.priceColumn.setCellValueFactory(new PropertyValueFactory<>(""));
        
        /* Sets the table content */
        saleObservableList = FXCollections.observableArrayList(saleList);
        //setTableItems(saleObservableList);
    }
    
    private void setFields()
    {
        accountBtn.setText("<< " + FXMLUserTopMenuController.employee.getNome() + " >>");
    }
}
