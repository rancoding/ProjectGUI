/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoii.design.administrator.warehouse.data.product.box.list;

import helpers.ProdutoBLL;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import projetoii.design.administrator.warehouse.data.product.list.FXMLListProductController;

/**
 * FXML Controller class
 *
 * @author Gustavo Vieira
 */
public class FXMLListProductBoxController implements Initializable {

    
    private FXMLListProductController listProductController;
    private ObservableList<ProdutoBLL> productList;
    private ProdutoBLL product;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void initializeOnControllerCall(FXMLListProductController listProductController, ObservableList<ProdutoBLL> productList, ProdutoBLL product)
    {
        /* Sets all variables accordingly to received parameters */
        setListCategoryController(listProductController);
        setProductTypeList(productList);
        setProductType(product);
        setField();
    }
    
    private void setListCategoryController(FXMLListProductController listProductController)
    {
        this.listProductController = listProductController;
    }
    
    private void setProductTypeList(ObservableList<ProdutoBLL> productList)
    {
        this.productList = productList;
    }
    
    private void setProductType(ProdutoBLL product)
    {
        this.product = product;
    }
    
    private void setField()
    {
        
    }
    
    
    
}
