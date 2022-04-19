package cinema.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Theater implements Serializable {

    private int total_rows = 9;
    private int total_columns = 9;
    private ArrayList<Seat>  available_seats = new ArrayList<>();

    public Theater() {
        for ( int i = 0; i < total_rows; i++) {
            for (int j = 0; j < total_columns; j++) {
                available_seats.add(new Seat(i+1,j+1));
            }
        }
    }

    public int getTotal_rows() {
        return total_rows;
    }

    public void setTotal_rows(int total_rows) {
        this.total_rows = total_rows;
    }

    public int getTotal_columns() {
        return total_columns;
    }

    public void setTotal_columns(int total_columns) {
        this.total_columns = total_columns;
    }

    public ArrayList<Seat> getAvailable_seats() {
        return available_seats;
    }

    public void setAvailable_seats(ArrayList<Seat> available_seats) {
        this.available_seats = available_seats;
    }
}
