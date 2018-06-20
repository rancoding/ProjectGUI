/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoii.design.administrator.warehouse.employee.add;

import helpers.FuncionarioBLL;
import helpers.HorarioBLL;
import helpers.LocalTrabalhoBLL;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import projetoii.design.administrator.menu.top.FXMLAdministratorTopMenuController;
import services.HorarioService;
import services.LocalTrabalhoService;

/**
 * FXML Controller class
 *
 * @author Gustavo Vieira
 */
public class FXMLAddEmployeeController implements Initializable {

    @FXML private TextField nameField;
    @FXML private TextField addressField;
    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
    @FXML private TextField firstEntranceField;
    @FXML private TextField firstExitField;
    @FXML private TextField secondEntranceField;
    @FXML private TextField secondExitField;
    @FXML private ComboBox genderBox;
    @FXML private ComboBox typeBox;
    @FXML private ComboBox activeBox;
    @FXML private DatePicker birthdayDate;
    
    /* Variables used for setting up the table content */
    @FXML public TableView<HorarioBLL> scheduleTable;
    @FXML private TableColumn<HorarioBLL, Date> firstEntranceColumn;
    @FXML private TableColumn<HorarioBLL, Date> firstExitColumn;
    @FXML private TableColumn<HorarioBLL, Date> secondEntranceColumn;
    @FXML private TableColumn<HorarioBLL, Date> secondExitColumn;
    private ObservableList<HorarioBLL> scheduleObservableList; 
    private List<HorarioBLL> scheduleList;
    
    private BigDecimal scheduleID;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        /* Retrieves all database product types to an arraylist and initializes the table values if it is not empty */
        scheduleList = HorarioService.getHelperList("FROM Horario ORDER BY idhorario ASC");
        
        genderBox.setItems(FXCollections.observableArrayList("Uniforme", "Masculino", "Feminino"));
        typeBox.setItems(FXCollections.observableArrayList("Administrador", "Utilizador"));
        activeBox.setItems(FXCollections.observableArrayList("Sim", "Não"));
        
        setScheduleRowFactory();
        
        if(!(scheduleList.isEmpty()))
        {
            initializeTable(scheduleList);
        }
        else
        {
            scheduleList = new ArrayList<>();
            initializeTable(scheduleList);
        }
    }    
    
    private void setScheduleRowFactory()
    {
        scheduleTable.setRowFactory((schedule) -> {
            TableRow<HorarioBLL> row = new TableRow<>();
            
            row.setOnMouseClicked((event) -> {
                if((!(row.isEmpty()))) {
                    firstEntranceField.setText( getDateTime(row.getItem().getHoraprimeiraentrada()) );
                    firstExitField.setText( getDateTime(row.getItem().getHoraprimeirasaida()) );
                    secondEntranceField.setText( getDateTime(row.getItem().getHorasegundaentrada()) );
                    secondExitField.setText( getDateTime(row.getItem().getHorasegundasaida()) );
                    scheduleID = row.getItem().getIdhorario();
                }
            });
            
            return row;
        });
    }
    
    private LocalTime getLocalTimeFromDate(String date)
    {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalTime time = LocalTime.parse(date, dateTimeFormatter);
        return time;
    }
    
    /* * Converts the employee date to a local date * */
    private LocalDate getLocalDateFromDate(Date date)
    {
        LocalDate localDate = LocalDate.parse(date.toString());
        // LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate;
    }
    
    /* * Returns the local time of a given date * */
    private String getDateLocalTime(Date date)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }
    
    /* * Returns the hour and minutes of a given date * */
    private String getDateTime(Date date)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        return simpleDateFormat.format(date);
    }
    
    /* * Sets the maximum date one can pick to today * */
    private void setDateUpperBound()
    {
    }
    
    /** Initializes all table content for the first time **/
    private void initializeTable(List<HorarioBLL> scheduleList)
    {
        /* Sets column variables to use entity info, empty for a button creation */
        this.firstEntranceColumn.setCellValueFactory(new PropertyValueFactory<>("horaprimeiraentrada"));
        this.firstExitColumn.setCellValueFactory(new PropertyValueFactory<>("horaprimeirasaida"));
        this.secondEntranceColumn.setCellValueFactory(new PropertyValueFactory<>("horasegundaentrada"));
        this.secondExitColumn.setCellValueFactory(new PropertyValueFactory<>("horasegundasaida"));
        
        setColumnDateToDateTime();
        
        /* Sets the table content */
        scheduleObservableList = FXCollections.observableArrayList(scheduleList);
        setTableItems(scheduleObservableList);
    }
    
    /* * Sets the table items to be the same as the observable list items * */
    private void setTableItems(ObservableList<HorarioBLL> scheduleObservableList)
    {
        this.scheduleTable.setItems(scheduleObservableList);
    }
    
    private void setColumnDateToDateTime()
    {
        firstEntranceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Date>() {
            
            @Override
            public String toString(Date object) {
                return getDateTime(object);
            }

            @Override
            public Date fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
            
        }));
        
        firstExitColumn.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Date>() {
            
            @Override
            public String toString(Date object) {
                return getDateTime(object);
            }

            @Override
            public Date fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
            
        }));
        
        secondEntranceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Date>() {
            
            @Override
            public String toString(Date object) {
                return getDateTime(object);
            }

            @Override
            public Date fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
            
        }));
        
        secondExitColumn.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Date>() {
            
            @Override
            public String toString(Date object) {
                return getDateTime(object);
            }

            @Override
            public Date fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
            
        }));
    }
    
    private Date getDateFromLocalDate(LocalDate localDate)
    {
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        Date date = Date.from(instant);
        return date;
    }
    
    @FXML private void onAddClick()
    {
        FuncionarioBLL employee = new FuncionarioBLL();
        employee.setNome(nameField.getText());
        employee.setUsername(usernameField.getText());
        employee.setPassword(Short.parseShort(passwordField.getText()));
        employee.setDatanascimento(getDateFromLocalDate(birthdayDate.getValue()));
        employee.setSexo(getGender(genderBox.getSelectionModel().getSelectedIndex()));
        employee.setActivo(getActive());
        employee.setTipo(getType());
        employee.setMorada(addressField.getText());
        
        scheduleList.forEach((schedule) -> {
            if(schedule.getIdhorario() == scheduleID)
            {
                employee.setHorario(schedule);
            }
        });
        
        List<LocalTrabalhoBLL> workingLocation = LocalTrabalhoService.getHelperList("FROM Localtrabalho WHERE idlocaltrabalho = " + FXMLAdministratorTopMenuController.getWorkLocationId());
        employee.setLocaltrabalho(workingLocation.get(0));
        
        insertEmployee(employee);
    }
    
    private void insertEmployee(FuncionarioBLL employee)
    {
        employee.create();
    }
    
    private boolean getType()
    {
        return typeBox.getSelectionModel().getSelectedIndex() == 0;
    }
    
    private char getGender(int index)
    {
        switch(index)
        {
            case 0: { return 'U'; }
            case 1: { return 'M'; }
            case 2: { return 'F'; }
        }
        
        return 'U';
    }
    
    private boolean getActive()
    {
        return activeBox.getSelectionModel().getSelectedIndex() == 0;
    }
    
    /* * Closes the stage on cancel button click * */
    @FXML void onCancelClick(ActionEvent event)
    {
        closeStage(event);
    }
    
    /* * Closes current window * */
    private void closeStage(ActionEvent event)
    {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }
}
