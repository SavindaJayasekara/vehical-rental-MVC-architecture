package lk.ijse.ikmanRental.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;

@AllArgsConstructor
@Data
public class VehicleIn {
    String vehicleNumber;
    String driverNic;
    Date date;
    String bookingId;
}
