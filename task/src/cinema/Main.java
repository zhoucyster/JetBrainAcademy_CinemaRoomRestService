package cinema;

import cinema.model.Position;
import cinema.model.Seat;
import cinema.model.Theater;
import cinema.model.Token;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.lang.Exception;
import java.util.*;

@SpringBootApplication
@RestController
public class Main {

    Set<Position> bookedPositionSet;
    Theater theater;
    Map<Position,Seat> positionSeatMap;
    Map<Token,Seat> tokenSeatMap;

    {
        theater = new Theater();
        bookedPositionSet = new HashSet<>();
        positionSeatMap = new HashMap<>();
        tokenSeatMap = new HashMap<>();

        //put position:seat pair into a map. In order to get the seat object faster.
        for (Seat seat : theater.getAvailable_seats()){
            positionSeatMap.put(new Position(seat.getRow(),seat.getColumn()),seat);
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @GetMapping("/seats")
    public String hello(){

        ObjectMapper Obj = new ObjectMapper();
        String jsonStr = "";
        try {
            jsonStr = Obj.writeValueAsString(theater);

        }
        catch(IOException e) {
            e.printStackTrace();
            jsonStr = "error found";
        }
        return jsonStr;

    }

    @PostMapping("/purchase")
    public ResponseEntity purchase(@RequestBody Position reqPosition){
        Token token = new Token();
        Seat seat = positionSeatMap.getOrDefault(reqPosition,new Seat(0,0));
        System.out.println("row: "+ reqPosition.getRow());
        System.out.println("column: " + reqPosition.getColumn());
        if (seat.getRow() == 0){//row or column out of bounds.
            return new ResponseEntity(Map.of("error", "The number of a row or a column is out of bounds!"), HttpStatus.BAD_REQUEST);
        }
        if (bookedPositionSet.contains(reqPosition)){//seat already been purchased
            return new ResponseEntity(Map.of("error", "The ticket has been already purchased!"), HttpStatus.BAD_REQUEST);
        }
        else{ //not yet been purchased, add to set.
            bookedPositionSet.add(reqPosition);
            //add to tokenSeat map
            tokenSeatMap.put(token,seat);
        }

        return  new ResponseEntity(Map.of("token",token.getToken(),"ticket",seat), HttpStatus.OK);

    }

    @PostMapping("/return")
    public ResponseEntity returnTicket (@RequestBody Token reqToken) {

        Seat seat = tokenSeatMap.get(reqToken);
        if (seat != null){
            // correct token was provided. remove seat from bookedPositionSet
            // remove seat from tokenSeatMap.
            bookedPositionSet.remove(new Position(seat.getRow(),seat.getColumn()));
            tokenSeatMap.remove(reqToken);
            return new ResponseEntity(Map.of("returned_ticket", seat), HttpStatus.OK);
        }
        else {
            // incorrect token was provide.
            return new ResponseEntity(Map.of("error","Wrong token!"), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/stats")
    public ResponseEntity stats(@RequestParam(name = "password", required = false) String password){

        if (password==null)
            return new ResponseEntity(Map.of("error","The password is wrong!"),HttpStatus.UNAUTHORIZED);
        if (password.equals("super_secret")){
            return new ResponseEntity(Map.of("current_income", calIncome(),
                    "number_of_available_seats", positionSeatMap.size() - bookedPositionSet.size() ,
                    "number_of_purchased_tickets",bookedPositionSet.size()), HttpStatus.OK);
        }
        else
            return new ResponseEntity(Map.of("error","The password is wrong!"),HttpStatus.UNAUTHORIZED);

    }

    private int calIncome(){
        int sum = 0;
        for(Seat seat: tokenSeatMap.values()){
            sum += seat.getPrice();
        }
        return sum;
    }
}
