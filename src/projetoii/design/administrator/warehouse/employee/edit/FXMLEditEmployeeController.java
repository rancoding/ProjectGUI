/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoii.design.administrator.warehouse.employee.edit;

import helpers.FuncionarioBLL;
import helpers.HorarioBLL;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
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
import projetoii.design.administrator.warehouse.employee.list.FXMLListEmployeeController;
import services.HorarioService;

public class FXMLEditEmployeeController implements Initializable {

    @FXML private TextField nameField;
    @FXML private TextField addressField;
    @FXML private TextField firstEntranceField;
    @FXML private TextField firstExitField;
    @FXML private TextField secondEntranceField;
    @FXML private TextField secondExitField;
    @FXML private DatePicker birthdayDate;
    @FXML private ComboBox genderBox;
    @FXML private ComboBox typeBox;
    @FXML private ComboBox activeBox;
    
    /* Controller to be able to refresh the table on edit button click, and color list to be able to edit and search for existent color */
    private FXMLListEmployeeController listEmployeeController;
    private ObservableList<FuncionarioBLL> employeeList;
    private FuncionarioBLL employee;
    
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
    public void initialize(URL url, ResourceBundle rb)
    {
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
    
    /* * Initializes variables when called from other controller * */
    public void initializeOnControllerCall(FXMLListEmployeeController listEmployeeController, ObservableList<FuncionarioBLL> employeeList, FuncionarioBLL employee)
    {
        setListEmployeeController(listEmployeeController);
        setEmployeeList(employeeList);
        setEmployee(employee);
        setFields();
    }
    
    private void setListEmployeeController(FXMLListEmployeeController listEmployeeController)
    {
        this.listEmployeeController = listEmployeeController;
    }
    
    private void setEmployeeList(ObservableList<FuncionarioBLL> employeeList)
    {
        this.employeeList = employeeList;
    }
    
    private void setEmployee(FuncionarioBLL employee)
    {
        this.employee = employee;
    }
    
    /* * Sets FXML fields to contain the correct employee information * */
    private void setFields()
    {
        nameField.setText(employee.getNome());
        addressField.setText(employee.getMorada());
        
        LocalDate localDate = getLocalDateFromDate(employee.getDatanascimento());
        birthdayDate.setValue(localDate);
        
        String gender = getGenderFullName((employee.getSexo()));
        genderBox.setValue(gender);
        
        String typeText = getTypeFullName(employee.isTipo());
        typeBox.setValue(typeText);
        
        String activeText = getActiveFullText(employee.isActivo());
        activeBox.setValue(activeText);
        
        if(employee.getHorario() != null)
        {
            List<HorarioBLL> employeeSchedule = HorarioService.getHelperList("FROM Horario WHERE idhorario = " + employee.getHorario().getIdhorario());
            HorarioBLL schedule = employeeSchedule.get(0);

            this.firstEntranceField.setText( getDateTime(schedule.getHoraprimeiraentrada()) );
            this.firstExitField.setText( getDateTime(schedule.getHoraprimeirasaida()) );
            this.secondEntranceField.setText( getDateTime(schedule.getHorasegundaentrada()) );
            this.secondExitField.setText( getDateTime(schedule.getHorasegundasaida()) );
        }
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
    
    /* * Gets the gender full name (Male, Female, Undefined) depending on the given character * */
    private String getGenderFullName(char gender)
    {
        switch(gender)
        {
            case 'u':
            case 'U':
            {
                return "Uniforme";
            }
            
            case 'm':
            case 'M':
            {
                return "Masculino";
            }
            
            case 'f':
            case 'F':
            {
                return "Feminino";
            }
            
            default:
            {
                return "Indefinido";
            }
        }
    }
    
    /* * Returns if an employee is still working or not * */
    private String getActiveFullText(boolean active)
    {
        if(active)
        {
            return "Sim";
        }
        else
        {
            return "Não";
        }
    }
    
    /* * Returns if an employee is still working or not * */
    private String getTypeFullName(boolean type)
    {
        if(type)
        {
            return "Administrador";
        }
        else
        {
            return "Utilizador";
        }
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
