package sample;

import javafx.beans.property.SimpleStringProperty;

public class Host {

    private final SimpleStringProperty row1;
    private final SimpleStringProperty row2;
    private final SimpleStringProperty row3;
    private final SimpleStringProperty row4;
    private final SimpleStringProperty row5;
    private final SimpleStringProperty row6;
    private final SimpleStringProperty row7;
    private final SimpleStringProperty row8;

    public Host(String row1, String row2, String row3, String row4, String row5, String row6, String row7, String row8){
        this.row1 = new SimpleStringProperty(row1);
        this.row2 = new SimpleStringProperty(row2);
        this.row3 = new SimpleStringProperty(row3);
        this.row4 = new SimpleStringProperty(row4);
        this.row5 = new SimpleStringProperty(row5);
        this.row6 = new SimpleStringProperty(row6);
        this.row7 = new SimpleStringProperty(row7);
        this.row8 = new SimpleStringProperty(row8);
    }

    public String getRow1() {
        return row1.get();
    }

    public String getRow2() {
        return row2.get();
    }

    public String getRow3() {
        return row3.get();
    }

    public String getRow4() {
        return row4.get();
    }

    public String getRow5() {
        return row5.get();
    }

    public String getRow6() {
        return row6.get();
    }

    public String getRow7() {
        return row7.get();
    }

    public String getRow8() {
        return row8.get();
    }
}