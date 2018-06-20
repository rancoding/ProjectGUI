/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoii.design.administrator.warehouse.data.product.edit;

import dao.Cor;
import dao.Marca;
import dao.Tamanho;
import dao.Tipoproduto;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.apache.commons.lang3.text.WordUtils;
import projetoii.design.administrator.warehouse.data.brand.add.FXMLAddBrandController;
import projetoii.design.administrator.warehouse.data.category.add.FXMLAddCategoryController;
import projetoii.design.administrator.warehouse.data.color.add.FXMLAddColorController;
import projetoii.design.administrator.warehouse.data.product.list.FXMLListProductController;
import projetoii.design.administrator.warehouse.data.size.add.FXMLAddSizeController;
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
    @FXML public ComboBox brandComboBox;
    @FXML public ComboBox typeComboBox;
    @FXML public ComboBox sizeComboBox;
    @FXML public ComboBox genderComboBox;
    @FXML public ComboBox colorComboBox;
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
        List<MarcaBLL> marcaList = MarcaService.getHelperList("FROM Marca ORDER BY idmarca ASC");
        this.marcaObservableList = FXCollections.observableArrayList(marcaList);

        brandComboBox.setItems(this.marcaObservableList); 
        
      
        brandComboBox.setConverter(new StringConverter<Marca>()
        {
                    @Override
                    public String toString(Marca object) {
                        return object.getNome();
                    }

                    @Override
                    public Marca fromString(String string) {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }
        });    
        //brandComboBox.getItems().addAll(this.marcaObservableList);
    }
         
    public void fillTypeProductComboBox()
    {
        List<TipoProdutoBLL> tipoProdutoList = TipoProdutoService.getHelperList("FROM Tipoproduto ORDER BY idtipoproduto ASC");
        this.tipoProdutoObservableList = FXCollections.observableArrayList(tipoProdutoList);

        typeComboBox.setItems(this.tipoProdutoObservableList);
        typeComboBox.setConverter(new StringConverter<Tipoproduto>()
        {
                    @Override
                    public String toString(Tipoproduto object) {
                        return object.getNome();
                    }

                    @Override
                    public Tipoproduto fromString(String string) {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }
        });    
    }
    
    public void fillSizeComboBox()
    {
        List<TamanhoBLL> tamanhoList = TamanhoService.getHelperList("FROM Tamanho ORDER BY idtamanho ASC");
        this.tamanhoObservableList = FXCollections.observableArrayList(tamanhoList);

        sizeComboBox.setItems(this.tamanhoObservableList);
        sizeComboBox.setConverter(new StringConverter<Tamanho>()
        {
                    @Override
                    public String toString(Tamanho object) {
                        return object.getDescricao();
                    }

                    @Override
                    public Tamanho fromString(String string) {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }
        });  
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
        colorComboBox.setConverter(new StringConverter<Cor>()
        {
                    @Override
                    public String toString(Cor object) {
                        return object.getNome();
                    }

                    @Override
                    public Cor fromString(String string) {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }
        });  
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
        product.setMarca((Marca) brandComboBox.getSelectionModel().getSelectedItem());
        product.setTamanho((Tamanho) sizeComboBox.getSelectionModel().getSelectedItem());
        product.setTipoproduto((Tipoproduto) typeComboBox.getSelectionModel().getSelectedItem());
        product.setGenero(getComboBoxGender(genderComboBox.getSelectionModel().getSelectedIndex()));
        product.setCor((Cor) colorComboBox.getSelectionModel().getSelectedItem());
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
        if(!(product.getTipoproduto()==((Tipoproduto) typeComboBox.getSelectionModel().getSelectedItem()))){
            product.setTipoproduto((Tipoproduto) typeComboBox.getSelectionModel().getSelectedItem());
        }
        if(!(product.getGenero()==(getComboBoxGender(genderComboBox.getSelectionModel().getSelectedIndex())))){
            product.setGenero(getComboBoxGender(genderComboBox.getSelectionModel().getSelectedIndex()));
        } else {
        }
        if(!(product.getMarca()==((Marca) brandComboBox.getSelectionModel().getSelectedItem()))){
            product.setMarca((Marca) brandComboBox.getSelectionModel().getSelectedItem());
        }
        if(!(product.getCor()==((Cor) colorComboBox.getSelectionModel().getSelectedItem()))){
            product.setCor((Cor) colorComboBox.getSelectionModel().getSelectedItem());
        }
        if(!(product.getTamanho()==((Tamanho) sizeComboBox.getSelectionModel().getSelectedItem()))){
            product.setTamanho((Tamanho) sizeComboBox.getSelectionModel().getSelectedItem());
        }
        if(!(product.getPrecocompra()==(Double.parseDouble(buyPriceText.getText())))){
            product.setPrecocompra(Double.parseDouble(buyPriceText.getText()));
        }
        
        if(!(product.getPrecovenda()==(Double.parseDouble(sellPriceText.getText())))){
            product.setPrecovenda(Double.parseDouble(sellPriceText.getText()));
        }
        
        
    
    }
    
    @FXML
    private void changetoAddBrand(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(FXMLAddBrandController.class.getResource("FXMLAddBrand.fxml"));
        Parent root = (Parent) loader.load();
        FXMLAddBrandController addController = (FXMLAddBrandController) loader.getController();
        addController.initializeOnEditProductControllerCall(this, marcaObservableList);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
    
    @FXML
    private void changetoAddSize(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(FXMLAddSizeController.class.getResource("FXMLAddSize.fxml"));
        Parent root = (Parent) loader.load();
        FXMLAddSizeController addController = (FXMLAddSizeController) loader.getController();
        addController.initializeOnEditProductControllerCall(this, tamanhoObservableList);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
    
    @FXML
    private void changetoAddCategory(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(FXMLAddCategoryController.class.getResource("FXMLAddCategory.fxml"));
        Parent root = (Parent) loader.load();
        FXMLAddCategoryController addController = (FXMLAddCategoryController) loader.getController();
        addController.initializeOnEditProductControllerCall(this, tipoProdutoObservableList);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
    
    @FXML
    private void changetoAddColor(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(FXMLAddColorController.class.getResource("FXMLAddColor.fxml"));
        Parent root = (Parent) loader.load();
        FXMLAddColorController addController = (FXMLAddColorController) loader.getController();
        addController.initializeOnEditProductControllerCall(this, corObservableList);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
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
