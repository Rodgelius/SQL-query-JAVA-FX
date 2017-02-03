package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

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

    public ObservableList list = FXCollections.observableArrayList();

    private void initOnDemand(String txt){
        

    }

    private void initData(){
        String query = "SELECT events.eventid AS id, from_unixtime( clock, '%Y-%m-%d %H.%i.%s' ) AS event_time, triggers.description AS 'trigger', items.name AS item, events.value AS status, triggers.priority, hosts.name, interface.ip FROM zabbix.triggers AS triggers, zabbix.events AS events, zabbix.functions AS functions, zabbix.items AS items, zabbix.hosts AS hosts, zabbix.interface AS interface WHERE triggers.triggerid = events.objectid AND functions.triggerid = triggers.triggerid AND functions.itemid = items.itemid AND items.hostid = hosts.hostid AND interface.hostid = hosts.hostid AND events.source = 0 AND interface.main = 1 AND events.clock BETWEEN UNIX_TIMESTAMP( '2017-01-14 00:00:00' ) AND UNIX_TIMESTAMP( '2017-01-17 00:00:00' ) ORDER BY id ASC LIMIT 10";

        try {
            con = DriverManager.getConnection(url, user, password);
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);

            while (rs.next()) {
                String row1 = rs.getString("id");
                String row2 = rs.getString("event_time");
                String row3 = rs.getString("trigger");
                String row4 = rs.getString("item");
                String row5 = rs.getString("status");
                String row6 = rs.getString("priority");
                String row7 = rs.getString("name");
                String row8 = rs.getString("ip");
                list.add(new Host (row1, row2, row3, row4, row5, row6, row7, row8));
                tableHosts.setEditable(true);
            }
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
        initData();
        row1.setCellValueFactory(new PropertyValueFactory<Host, String>("row1"));
        row2.setCellValueFactory(new PropertyValueFactory<Host, String>("row2"));
        row3.setCellValueFactory(new PropertyValueFactory<Host, String>("row3"));
        row4.setCellValueFactory(new PropertyValueFactory<Host, String>("row4"));
        row5.setCellValueFactory(new PropertyValueFactory<Host, String>("row5"));
        row6.setCellValueFactory(new PropertyValueFactory<Host, String>("row6"));
        row7.setCellValueFactory(new PropertyValueFactory<Host, String>("row7"));
        row8.setCellValueFactory(new PropertyValueFactory<Host, String>("row8"));
        tableHosts.setItems(list);

    }
}