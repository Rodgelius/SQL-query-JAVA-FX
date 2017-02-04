package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import kotlin.reflect.jvm.internal.impl.descriptors.EffectiveVisibility;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.CancellationException;

import static java.time.format.DateTimeFormatter.ISO_INSTANT;

public class Controller implements Initializable{

    private static final String url = "jdbc:mysql://zabbix2.cod.sz.rt.ru:3306/zabbix";
    private static final String user = "read";
    private static final String password = "654321";

    private static Connection con;
    private static Statement stmt;
    private static ResultSet rs;

    @FXML private TableView tableHosts;
    @FXML private TableColumn<Host, String> row1;
    @FXML private TableColumn<Host, String> row2;
    @FXML private TableColumn<Host, String> row3;
    @FXML private TableColumn<Host, String> row4;
    @FXML private TableColumn<Host, String> row5;
    @FXML private TableColumn<Host, String> row6;
    @FXML private TableColumn<Host, String> row7;
    @FXML private TableColumn<Host, String> row8;
    @FXML private TextField textfield = new TextField();
    @FXML private DateTimePicker timefrom = null;
    @FXML private DateTimePicker timeto = null;
    @FXML private Button btn = new Button();
    @FXML private Button btn1 = new Button();
    String query;
    String datatocsv = "id;event_time;trigger;item;status;priority;name;ip" + '\r';

    public ObservableList list = FXCollections.observableArrayList();

    private void initData(){
        try {
            con = DriverManager.getConnection(url, user, password);
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);

            while (rs.next()) {                                         //reading from mysql results
                String row1 = rs.getString("id");
                String row2 = rs.getString("event_time");
                String row3 = rs.getString("trigger");
                String row4 = rs.getString("item");
                String row5 = rs.getString("status");
                String row6 = rs.getString("priority");
                String row7 = rs.getString("name");
                String row8 = rs.getString("ip");
                list.add(new Host (row1, row2, row3, row4, row5, row6, row7, row8));
                datatocsv+= row1.trim() + ";" + row2.trim() + ";" + row3.trim() + ";" + row4.trim() + ";" +
                        row5.trim() + ";" + row6.trim() + ";" + row7.trim() + ";" + row8.trim() + '\r';
                tableHosts.setEditable(true);
            }
            System.out.println(datatocsv);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            //close connection ,stmt and resultset here
            try { con.close(); } catch(SQLException se) { /*can't do anything */ }
            try { stmt.close(); } catch(SQLException se) { /*can't do anything */ }
            try { rs.close(); } catch(SQLException se) { /*can't do anything */ }
        }
    }

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        row1.setCellValueFactory(new PropertyValueFactory<Host, String>("row1"));
        row2.setCellValueFactory(new PropertyValueFactory<Host, String>("row2"));
        row3.setCellValueFactory(new PropertyValueFactory<Host, String>("row3"));
        row4.setCellValueFactory(new PropertyValueFactory<Host, String>("row4"));
        row5.setCellValueFactory(new PropertyValueFactory<Host, String>("row5"));
        row6.setCellValueFactory(new PropertyValueFactory<Host, String>("row6"));
        row7.setCellValueFactory(new PropertyValueFactory<Host, String>("row7"));
        row8.setCellValueFactory(new PropertyValueFactory<Host, String>("row8"));
        tableHosts.setItems(list);

            btn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
//                    if (textfield != null){
                        list.clear();

                    LocalDateTime date = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH.mm.ss");
                    String timetotxt = date.format(formatter);                              //default TOTIME now
                    String timefromtxt = date.minusDays(3).format(formatter);               //default FROMTIME -3 days till now
                        try {
                            if (!timefrom.getValue().equals(null)) timefromtxt = timefrom.getDateTimeValue().format(formatter);         //if timefrom not blank
                            if (!timeto.getValue().equals(null)) timetotxt = timeto.getDateTimeValue().format(formatter);               //if timeto not blank
                            if (LocalDateTime.parse(timefromtxt, formatter).isAfter(LocalDateTime.parse(timetotxt, formatter))){        // timefrom <-> timeto
                                String t = timefromtxt;
                                timefromtxt = timetotxt;
                                timetotxt = t;
                            }
                        }
                      catch (Exception e){
                            System.out.println("catched");
                      }
                    System.out.println(timetotxt);
                    System.out.println(timefromtxt);
                        query = "SELECT events.eventid AS id, " +
                                "from_unixtime( clock, '%Y-%m-%d %H.%i.%s' ) AS event_time, " +
                                "triggers.description AS 'trigger', " +
                                "items.name AS item, " +
                                "events.value AS status, triggers.priority, hosts.name, interface.ip " +
                                "FROM zabbix.triggers AS triggers, " +
                                "zabbix.events AS events, " +
                                "zabbix.functions AS functions, " +
                                "zabbix.items AS items, " +
                                "zabbix.hosts AS hosts, " +
                                "zabbix.interface AS interface " +
                                "WHERE triggers.triggerid = events.objectid AND " +
                                "functions.triggerid = triggers.triggerid AND " +
                                "functions.itemid = items.itemid AND " +
                                "items.hostid = hosts.hostid AND " +
                                "interface.hostid = hosts.hostid AND " +
                                "events.source = 0 AND " +
                                "interface.main = 1 AND " +
                                "hosts.name LIKE '%" + textfield.getText() + "%' AND " +
                                "events.clock " +
                                "BETWEEN UNIX_TIMESTAMP( '" + timefromtxt + "' ) AND " +
                                "UNIX_TIMESTAMP( '" + timetotxt + "' ) " +
                                "ORDER BY id ASC LIMIT 10";
                        initData();
                    }
 //               }
            });

            btn1.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {

                    Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
                    alert1.setTitle("CONFIRM SAVE");
                    alert1.setHeaderText("");
                    alert1.setContentText("Select OK to save csv");

                    Optional<ButtonType> result = alert1.showAndWait();

                    if (result.get() == ButtonType.OK){
                        try {
                            FileWriter fileWriter = new FileWriter("/home/user/out.csv",true);
                            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                            bufferedWriter.write(datatocsv);

                            bufferedWriter.flush();
                            bufferedWriter.close();
                            fileWriter.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
    }
}