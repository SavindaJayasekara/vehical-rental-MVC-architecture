package lk.ijse.ikmanRental.dto.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
public class BookingTM {
    String bookingID;
    String driverNIC;
    String status;
    Double cost;
    Date requiredDate;
    String distance;
    String vehicleNumber;
    String rideTo;
    String customerNIC;
}
