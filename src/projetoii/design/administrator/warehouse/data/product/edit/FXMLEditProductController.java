/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoii.design.administrator.warehouse.data.product.edit;

import helpers.CorBLL;
import helpers.MarcaBLL;
import helpers.ProdutoBLL;
import helpers.TamanhoBLL;
import helpers.TipoProdutoBLL;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.commons.lang3.text.WordUtils;
import projetoii.design.administrator.warehouse.data.product.list.FXMLListProductController;
import services.CorService;
import services.MarcaService;
import services.TamanhoService;
import services.TipoProdutoService;

/**
 * FXML Controller class
 *
 * @author Gustavo Vieira
 */
public class FXMLEditProductController implements Initializable {

    @FXML private TextField barCodeText;
    @FXML private TextField nameText;
    @FXML private ComboBox brandComboBox;
    @FXML private ComboBox typeComboBox;
    @FXML private ComboBox sizeComboBox;
    @FXML private ComboBox genderComboBox;
    @FXML private ComboBox colorComboBox;
    @FXML private TextField buyPriceText;
    @FXML private TextField sellPriceText;
    @FXML private Button updateButton;
    @FXML private Button CancelButton;
    
    private FXMLListProductController listProductController;
    private ObservableList<ProdutoBLL> productList;
    private ProdutoBLL product;
    
    ObservableList<MarcaBLL> marcaObservableList;
    ObservableList<TipoProdutoBLL> tipoProdutoObservableList;
    ObservableList<TamanhoBLL> tamanhoObservableList;
    ObservableList<CorBLL> corObservableList;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        List<MarcaBLL> marcaList = MarcaService.getHelperList("FROM Marca ORDER BY idmarca ASC");
        this.marcaObservableList = FXCollections.observableArrayList(marcaList);

        fillGenderComboBox();
        fillColorComboBox();
        fillTypeProductComboBox();
        fillSizeComboBox();
        fillBrandComboBox();
        brandComboBox.getSelectionModel().select(0);
    }    
    
    public void initializeOnControllerCall(FXMLListProductController listProductsController, ObservableList<ProdutoBLL> productList, ProdutoBLL product)
    {
         /* Sets all variables accordingly to received parameters */
        setListProductController(listProductsController);
        setProductList(productList);
        setProduct(product);
        setField();
                
        setSelectedGenderComboBox(product.getGenero());
        
    }
    
    
    
    private void setListProductController(FXMLListProductController listProductController)
    {
        this.listProductController = listProductController;
    }
    
    private void setProductList(ObservableList<ProdutoBLL> productList)
    {
        this.productList = productList;
    }
    
    private void setProduct(ProdutoBLL product)
    {
        this.product = product;
    }
    
    private void setField()
    {
        this.barCodeText.setText(String.valueOf(product.getCodbarras()));
        this.nameText.setText(product.getDescricao());
        this.brandComboBox.setValue(product.getMarca());
        this.sizeComboBox.setValue(product.getTamanho());
        this.typeComboBox.setValue(product.getTipoproduto());
        this.genderComboBox.setValue(product.getGenero());
        this.colorComboBox.setValue(product.getCor());
        this.buyPriceText.setText(String.valueOf(product.getPrecocompra()));
        this.sellPriceText.setText(String.valueOf(product.getPrecovenda()));     
    }
    
  ///////////////////////////// Fill Combo Boxes ///////////////////////////////
    
    public void fillBrandComboBox(){
        brandComboBox.getItems().addAll(this.marcaObservableList);
    }
         
    public void fillTypeProductComboBox()
    {
        List<TipoProdutoBLL> tipoProdutoList = TipoProdutoService.getHelperList("FROM Tipoproduto ORDER BY idtipoproduto ASC");
        this.tipoProdutoObservableList = FXCollections.observableArrayList(tipoProdutoList);

        typeComboBox.setItems(this.tipoProdutoObservableList);
    }
    
    public void fillSizeComboBox()
    {
        List<TamanhoBLL> tamanhoList = TamanhoService.getHelperList("FROM Tamanho ORDER BY idtamanho ASC");
        this.tamanhoObservableList = FXCollections.observableArrayList(tamanhoList);

        sizeComboBox.setItems(this.tamanhoObservableList);
    }
    
    public void fillGenderComboBox()
    {
        ObservableList genderObservableList;
        List genderList = new ArrayList<>();
        
        genderList.add("UniSexo");
        genderList.add("Masculino");
        genderList.add("Feminino");
        
        genderObservableList = FXCollections.observableArrayList(genderList);
        
        genderComboBox.setItems(genderObservableList);
    }
    
    public void fillColorComboBox()
    {
        List<CorBLL> corList = CorService.getHelperList("FROM Cor ORDER BY idcor ASC");
        this.corObservableList = FXCollections.observableArrayList(corList);

        colorComboBox.setItems(this.corObservableList);
    }
    
    ////////////////////////////////////////////////////////////////////////////
    
    private char getComboBoxGender(int posicao) {
        switch(posicao){
            case 0 : 
                return 'U';
            case 1: 
                return 'M';
            case 2: 
                return 'F';
        }    
        return 0;
    }
     
    private void setSelectedGenderComboBox(char genero) {
        switch(genero){
            case 'U' : 
                genderComboBox.getSelectionModel().select(0);
            case 'M': 
                genderComboBox.getSelectionModel().select(1);
            case 'F': 
                genderComboBox.getSelectionModel().select(2);
        }    
        
    }
    
    /* * Sets the new product name, updates in the database, refreshes the list controller table and closes current window * */
    @FXML
    void onEditButtonClick(ActionEvent event) throws IOException
    {

        updateProductList();
        updateProduct();
        
        this.listProductController.productTable.refresh();
        closeStage(event);
    }
    
    /* * Updates entity on database * */
    private void updateProduct()
    {
        product.setCodbarras(Long.parseLong(barCodeText.getText()));
        product.setDescricao(WordUtils.capitalizeFully(nameText.getText()));
        product.setMarca((MarcaBLL) brandComboBox.getSelectionModel().getSelectedItem());
        product.setTamanho((TamanhoBLL) sizeComboBox.getSelectionModel().getSelectedItem());
        product.setTipoproduto((TipoProdutoBLL) typeComboBox.getSelectionModel().getSelectedItem());
        product.setGenero(getComboBoxGender(genderComboBox.getSelectionModel().getSelectedIndex()));
        product.setCor((CorBLL) colorComboBox.getSelectionModel().getSelectedItem());
        product.setPrecocompra(Double.parseDouble(buyPriceText.getText()));
        product.setPrecovenda(Double.parseDouble(sellPriceText.getText()));
        
        product.update();
    }
    
    private void updateProductList(){
        if(!(product.getDescricao().equals(nameText.getText()))){
            product.setDescricao(WordUtils.capitalizeFully(nameText.getText()));
        }
        if(!(product.getCodbarras()==(Long.valueOf(barCodeText.getText())))){
           for(ProdutoBLL prod : productList){
               if(prod.getCodbarras()==(Long.valueOf(barCodeText.getText()))){
                   
               }else{   
                   product.setCodbarras(Long.valueOf(barCodeText.getText()));
               }
           }    
        }
        if(!(product.getTipoproduto()==((TipoProdutoBLL) typeComboBox.getSelectionModel().getSelectedItem()))){
            product.setTipoproduto((TipoProdutoBLL) typeComboBox.getSelectionModel().getSelectedItem());
        }
        if(!(product.getGenero()==(getComboBoxGender(genderComboBox.getSelectionModel().getSelectedIndex())))){
            product.setGenero(getComboBoxGender(genderComboBox.getSelectionModel().getSelectedIndex()));
        } else {
        }
        if(!(product.getMarca()==((MarcaBLL) brandComboBox.getSelectionModel().getSelectedItem()))){
            product.setMarca((MarcaBLL) brandComboBox.getSelectionModel().getSelectedItem());
        }
        if(!(product.getCor()==((CorBLL) colorComboBox.getSelectionModel().getSelectedItem()))){
            product.setCor((CorBLL) colorComboBox.getSelectionModel().getSelectedItem());
        }
        if(!(product.getTamanho()==((TamanhoBLL) sizeComboBox.getSelectionModel().getSelectedItem()))){
            product.setTamanho((TamanhoBLL) sizeComboBox.getSelectionModel().getSelectedItem());
        }
        if(!(product.getPrecocompra()==(Double.parseDouble(buyPriceText.getText())))){
            product.setPrecocompra(Double.parseDouble(buyPriceText.getText()));
        }
        
        if(!(product.getPrecovenda()==(Double.parseDouble(sellPriceText.getText())))){
            product.setPrecovenda(Double.parseDouble(sellPriceText.getText()));
        }
        
        
    
    }
    
    /* * Closes the stage on cancel button click * */
    @FXML void onCancelClick(ActionEvent event)
    {
        closeStage(event);
    }
    
    /* * Closes current window * */
    private void closeStage(ActionEvent event)
    {
        Node node = (Node)event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }
    
}
