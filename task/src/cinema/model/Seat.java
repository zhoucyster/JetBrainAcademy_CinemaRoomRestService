package cinema.model;

import java.io.Serializable;

public class Seat implements Serializable {
    private int row;
    private int column;
    private final int price;
    //private transient boolean availability = true;

    public Seat(int row, int column) {
        this.row = row;
        this.column = column;
        this.price = (row <= 4) ? 10 : 8 ;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getPrice() {
        return price;
    }

}
