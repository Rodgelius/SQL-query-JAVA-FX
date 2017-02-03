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
    @FXML private TableColumn<Host, Integer> id;
    @FXML private TableColumn<Host, String> host;
    @FXML private TableColumn<Host, String> status;

    public ObservableList list = FXCollections.observableArrayList(
            new Host(1, "test", "test")
    );

//    @FXML private TableView<String> hostStatus;

    private void initData(){
        String query = "select host, status from hosts LIMIT 10";

        try {
            con = DriverManager.getConnection(url, user, password);
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);

            while (rs.next()) {
                String hostdb = rs.getString("host");
                String statusdb = rs.getString("status");
                list.add(new Host (1, hostdb, statusdb));
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        id.setCellValueFactory(new PropertyValueFactory<Host, Integer>("id"));
        host.setCellValueFactory(new PropertyValueFactory<Host, String>("hostname"));
        status.setCellValueFactory(new PropertyValueFactory<Host, String>("status"));
        tableHosts.setItems(list);
        initData();
    }
}