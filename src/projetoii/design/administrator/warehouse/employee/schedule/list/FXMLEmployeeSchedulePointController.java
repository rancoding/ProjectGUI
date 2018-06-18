/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoii.design.administrator.warehouse.employee.schedule.list;

import helpers.FuncionarioBLL;
import helpers.PontoHorarioBLL;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import services.PontoHorarioService;

/**
 * FXML Controller class
 *
 * @author Gustavo Vieira
 */
public class FXMLEmployeeSchedulePointController implements Initializable {

    FuncionarioBLL employee;
    List<PontoHorarioBLL> pointList;
    
    /* Scene Builder Variables */
    @FXML private Label currentMonthAndYearLabel;
    @FXML private Label currentWeekDaysLabel;
    @FXML private Label prevWeekLabel;
    @FXML private Label nextWeekLabel;
    
    @FXML private Label firstScheduleHour;
    @FXML private Label secondScheduleHour;
    
    @FXML private Label firstEntranceHourLabel;
    @FXML private Label firstExitHourLabel;
    @FXML private Label secondEntranceHourLabel;
    @FXML private Label secondExitHourLabel;
    
    @FXML private Button prevWeekButton;
    @FXML private Button nextWeekButton;
    
    private List<Date> prevList;
    private List<Date> currentList;
    private List<Date> nextList;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    public void initializeOnControllerCall(FuncionarioBLL employee)
    {
        setEmployee(employee);
        initializePointList();
        getCurrentDate();
    }
    
    private void setEmployee(FuncionarioBLL employee)
    {
        this.employee = employee;
    }
    
    private void initializePointList()
    {
        pointList = PontoHorarioService.getHelperList("FROM Pontohorario WHERE idhorario = " + employee.getHorario().getIdhorario() + " AND idfuncionario = " + employee.getIdfuncionario());
        
        if(pointList.isEmpty())
        {
            pointList = new ArrayList<>();
        }
    }
    
    private void getCurrentDate()
    {
        pointList.forEach(point -> {
            System.out.println(point.getId().getData());
        });
    }
}
