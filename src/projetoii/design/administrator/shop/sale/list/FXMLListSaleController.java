/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoii.design.administrator.shop.sale.list;

import helpers.ProdutoVendaBLL;
import helpers.VendaBLL;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
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
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.commons.lang3.StringUtils;
import projetoii.design.administrator.menu.top.FXMLAdministratorTopMenuController;
import projetoii.design.administrator.shop.sale.list.detail.FXMLSaleDetailController;
import services.ProdutoVendaService;
import services.VendaService;

/**
 * FXML Controller class
 *
 * @author Gustavo Vieira
 */
public class FXMLListSaleController implements Initializable {

    List<VendaBLL> saleList;

    @FXML private TextField totalCostField;
    @FXML private TextField totalSaleField;
    @FXML private TextField totalWonField;
    
    @FXML private ToggleButton dailyButton;
    @FXML private ToggleButton weeklyButton;
    @FXML private ToggleButton monthlyButton;
    @FXML private ToggleButton annualButton;
    @FXML private ToggleButton totalButton;

    @FXML public TableView<VendaBLL> saleTable;
    @FXML private TableColumn<VendaBLL, Integer> idColumn;
    @FXML private TableColumn<VendaBLL, Date> dateColumn;
    @FXML private TableColumn<VendaBLL, String> employeeColumn;
    @FXML private TableColumn<VendaBLL, String> valueColumn;
    @FXML private TableColumn<VendaBLL, Double> detailColumn;
    private ObservableList<VendaBLL> saleObservableList;
    
    /* Text field used to search brands on the table, updating as it searches */
    @FXML private TextField searchSaleTextField;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        saleList = new ArrayList<>();
        setTotalSaleList();
        
        setButtonSelection(false, false, false, false, true);
        setTotalValues(saleList);
        initializeTable(saleList);
    }    
    
    /**
     * Gets all the sales into a list
     */
    private void setTotalSaleList()
    {
        saleList.clear();
        saleList = VendaService.getHelperList("FROM Venda WHERE loja.idloja = " + FXMLAdministratorTopMenuController.getWorkLocationId());
    }
    
    /**
     * Gets today sales list
     */
    private void setDailySaleList()
    {
        Date date = new Date();
        
        saleList.clear();
        setTotalSaleList();
        saleList = this.getTodaySaleList(date);
    }
    
    /**
     * Gets sales made in the last week
     */
    private void setWeeklySaleList()
    {
        Date prevDate = getDate(-7);
        Date nextDate = getDate(7);
        
        saleList.clear();
        setTotalSaleList();
        saleList = getSaleListBetweenDates(prevDate, nextDate);
    }
    
    /**
     * Gets sales made in the last month
     */
    private void setMonthlySaleList()
    {
        Date prevDate = getDate(-31);
        Date nextDate = getDate(31);
        
        saleList.clear();
        setTotalSaleList();
        saleList = getSaleListBetweenDates(prevDate, nextDate);
    }
    
    /**
     * Gets sales made in the last year
     */
    private void setAnualSaleList()
    {
        Date prevDate = getDate(-365);
        Date nextDate = getDate(365);
        
        saleList.clear();
        setTotalSaleList();
        saleList = getSaleListBetweenDates(prevDate, nextDate);
    }
    
    /**
     * Gets a date at a given time
     * @param number days to add or to take from todays date
     * @return wanted date
     */
    private Date getDate(int number)
    {
        Date date = getCalendar(number).getTime();
        return date;
    }
    
    /**
     * Gets a calendar to set onto the date
     * @param number days to add or to take from todays date
     * @return desired calendar
     */
    private Calendar getCalendar(int number)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, number);
        return calendar;
    }
    
    private LocalDate getLocalDateFromDate(Date date)
    {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate;
    }
    
    private List<VendaBLL> getSaleListBetweenDates(Date prevDate, Date nextDate)
    {
        List<VendaBLL> list = new ArrayList<>();
        
        for(VendaBLL sale : saleList)
        {
            if(sale.getDatavenda().after(prevDate) && sale.getDatavenda().before(nextDate))
            {
                list.add(sale);
            }
        } 
        
        return list;
    }
    
    private List<VendaBLL> getTodaySaleList(Date date)
    {
        List<VendaBLL> list = new ArrayList<>();
        
        for(VendaBLL sale : saleList)
        {
            if(sale.getDatavenda().getDay() == date.getDay() && sale.getDatavenda().getMonth() == date.getMonth() && sale.getDatavenda().getYear() == date.getYear())
            {
                list.add(sale);
            }
        } 
        
        return list;
    }
    
    /**
     * Gets sales total cost
     * @return sales total cost
     */
    private double getTotalCostValue(List<VendaBLL> list)
    {
        double total = 0;
        
        List<ProdutoVendaBLL> saleProductList = new ArrayList<>();
        
        for(VendaBLL sale : list)
        {
            List<ProdutoVendaBLL> sales = ProdutoVendaService.getHelperList("FROM Produtovenda WHERE venda.idvenda = " + sale.getIdvenda());
            
            for(ProdutoVendaBLL product : sales) {
                saleProductList.add(product);
            }
        }
        
        for(ProdutoVendaBLL product : saleProductList)
        {
            total += product.getProduto().getPrecocompra();
        }
        
        return total;
    }
    
    /**
     * Gets sales total value
     * @return sales total value
     */
    private double getTotalSaleValue(List<VendaBLL> list)
    {
        double total = 0;
        
        for(VendaBLL sale : list) {
            total = total + sale.getValortotal();
        }
        
        return total;
    }
    /**
     * Gets sales total won value
     * @return sales total won value
     */
    private double getTotalWonValue(List<VendaBLL> list)
    {
        double total = 0;
        
        total = getTotalSaleValue(list) - getTotalCostValue(list);
        
        return total;
    }
    
    /**
     * Sets text fields to the correct values
     */
    private void setTotalValues(List<VendaBLL> list)
    {
        this.totalCostField.setText(String.valueOf(getTotalCostValue(list)) + "€");;
        this.totalSaleField.setText(String.valueOf(getTotalSaleValue(list)) + "€");;
        this.totalWonField.setText(String.valueOf(getTotalWonValue(list)) + "€");;
    }
    
    /**
     * Disables toggle buttons
     * @param daily daily button
     * @param weekly weekly button
     * @param monthly monthly button
     * @param annualy annually button
     * @param total total button
     */
    private void setButtonSelection(boolean daily, boolean weekly, boolean monthly, boolean annualy, boolean total)
    {
        this.dailyButton.setSelected(daily);
        this.weeklyButton.setSelected(weekly);
        this.monthlyButton.setSelected(monthly);
        this.annualButton.setSelected(annualy);
        this.totalButton.setSelected(total);
    }
    
    /**
     * On daily button click
     */
    @FXML private void onDailyClick() {
        setDailySaleList();
        setTotalValues(saleList);
        setButtonSelection(true, false, false, false, false);
        initializeTable(saleList);
    }
    
    /**
     * On weekly button click
     */
    @FXML private void onWeeklyClick() {
        setWeeklySaleList();
        setTotalValues(saleList);
        setButtonSelection(false, true, false, false, false);
        initializeTable(saleList);
    }

    /**
     * On monthly button click
     */
    @FXML private void onMonthlyClick() {
        setMonthlySaleList();
        setTotalValues(saleList);
        setButtonSelection(false, false, true, false, false);
        initializeTable(saleList);
    }
    
    /**
     * On annual button click
     */
    @FXML private void onAnnualClick() {
        setAnualSaleList();
        setTotalValues(saleList);
        setButtonSelection(false, false, false, true, false);
        initializeTable(saleList);
    }
    
    /**
     * On total button click
     */
    @FXML private void onTotalClick() {
        setTotalSaleList();
        setTotalValues(saleList);
        setButtonSelection(false, false, false, false, true);
        initializeTable(saleList);
    }

    /**
     * Initializes the table with the sale list
     * @param saleList data to set on the table
     */
    private void initializeTable(List<VendaBLL> saleList) {
        this.idColumn.setCellValueFactory(new PropertyValueFactory<>("idvenda"));
        this.dateColumn.setCellValueFactory(new PropertyValueFactory<>("datavenda"));
        this.employeeColumn.setCellValueFactory((TableColumn.CellDataFeatures<VendaBLL, String> param) -> new SimpleObjectProperty<>(param.getValue().getFuncionario().getNome()));
        this.valueColumn.setCellValueFactory((TableColumn.CellDataFeatures<VendaBLL, String> param) -> new SimpleObjectProperty<>(param.getValue().getValortotal() + "€"));
        this.detailColumn.setCellValueFactory(new PropertyValueFactory<>(""));
        
        /* Sets images for all row buttons and sets the buttons up */
        Image image = new Image(getClass().getResourceAsStream("image/detail.png"));
        Image imageHover = new Image(getClass().getResourceAsStream("image/detail_hover.png"));
        detailColumn.setCellFactory(getButtonCell(image, imageHover));
        
        /* Sets the table content */
        saleObservableList = FXCollections.observableArrayList(saleList);
        setTableItems(saleObservableList);
    }

    /**
     * Sets table items
     * @param saleObservableList data that will be set onto the table
     */
    private void setTableItems(ObservableList<VendaBLL> saleObservableList) {
        this.saleTable.setItems(saleObservableList);
    }
    
    
    /**
     * Searches for sale when a key is pressed
     */
    @FXML
    void getSearchList()
    {
        List<VendaBLL> newSaleList = new ArrayList<>();
            
        /* If something has been typed, tries to find an existent sale with the given name or ID */
        if(searchSaleTextField.getText().length() > 0)
        {
            newSaleList.clear();
            
            String nonCharacters = "[^\\p{L}\\p{Nd}]";
            
            for(VendaBLL sale : saleObservableList)
            {
                String searchString = StringUtils.stripAccents(searchSaleTextField.getText().replaceAll(nonCharacters, "").toLowerCase());
                
                String saleDate = StringUtils.stripAccents(String.valueOf(sale.getDatavenda()).replaceAll(nonCharacters, "").toLowerCase());
                String saleEmployee = StringUtils.stripAccents(String.valueOf(sale.getFuncionario().getNome()).replaceAll(nonCharacters, "").toLowerCase());
                String saleValue = StringUtils.stripAccents(String.valueOf(sale.getValortotal()).replaceAll(nonCharacters, "").toLowerCase());
                String saleID = String.valueOf(sale.getIdvenda());
                
                if(saleDate.contains(searchString) || saleEmployee.contains(searchString) || saleValue.contains(searchString) || saleID.contains(searchString))
                {
                    newSaleList.add(sale);
                }
            }
            
            setSearchedTableValues(newSaleList);
            setTotalValues(newSaleList);
        }
        else /* If nothing has been typed, show full sale list */
        {
            newSaleList.clear();
            
            newSaleList = saleObservableList;
            setSearchedTableValues(newSaleList);
            setTotalValues(newSaleList);
        }
    }
    
    /**
     * Sets new table values
     * @param newSaleList new sale list data to show on table
     */
    public void setSearchedTableValues(List<VendaBLL> newSaleList)
    {
        ObservableList<VendaBLL> newSaleObservableList;
        newSaleObservableList = FXCollections.observableArrayList(newSaleList);
        setTableItems(newSaleObservableList);
    }
    
    /**
     * Creates a button for each table cell, also setting up an image for each button (with a different hover image and size)
     * @param image button image
     * @param imageHover button image on mouse hover
     * @return 
     */
    private Callback getButtonCell(Image image, Image imageHover)
    {
        Callback<TableColumn<VendaBLL, String>, TableCell<VendaBLL, String>> cellFactory;
        cellFactory = new Callback<TableColumn<VendaBLL, String>, TableCell<VendaBLL, String>>()
        {
            @Override
            public TableCell call(final TableColumn<VendaBLL, String> param)
            {
                final TableCell<VendaBLL, String> cell = new TableCell<VendaBLL, String>()
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
                            /* On edit button, opens a detailed sale window  */
                            button.setOnAction((event) -> {
                                VendaBLL sale = getTableView().getItems().get(getIndex());
                                loadNewDetailWindow(FXMLSaleDetailController.class, "FXMLSaleDetail.fxml", "Armazém - Detalhes de venda", "Não foi possível carregar o ficheiro FXMLSaleDetail.fxml", sale);
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
     * @param controller New window Controller
     * @param fileName Window file to be loaded
     * @param title Window title
     * @param message Error message
     * @param sale Sale to be detailed
     */
    private void loadNewDetailWindow(Class controller, String fileName, String title, String message, VendaBLL sale)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(controller.getResource(fileName));
            Parent root = (Parent) loader.load();
            
            FXMLSaleDetailController detailController = (FXMLSaleDetailController) loader.getController();
            detailController.initializeOnControllerCall(this, sale);
            
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
}
