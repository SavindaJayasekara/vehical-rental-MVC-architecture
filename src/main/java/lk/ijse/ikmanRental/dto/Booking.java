package lk.ijse.ikmanRental.dto;

import lombok.*;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Booking {
    String bookingID;
    String status;
    Double amountsCost;
    Date requiredDate;
    String rideTo;
    String distance;
    String customerNic;

   public java.util.Date getDate(){
        return requiredDate;
    }
}
