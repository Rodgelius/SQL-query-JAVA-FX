package sample;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Host {

    private final SimpleIntegerProperty id;
    private final SimpleStringProperty hostname;
    private final SimpleStringProperty status;

    public Host(Integer id, String hostname, String status){
        super();
        this.id = new SimpleIntegerProperty(id);
        this.hostname = new SimpleStringProperty(hostname);
        this.status = new SimpleStringProperty(status);
    }
    public Integer getId() {
        return id.get();
    }

    public String getHostname() {
        return hostname.get();
    }

    public String getStatus() {
        return status.get();
    }

}