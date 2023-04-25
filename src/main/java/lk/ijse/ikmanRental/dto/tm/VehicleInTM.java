package lk.ijse.ikmanRental.dto.tm;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;
import java.time.LocalDate;

@AllArgsConstructor
@Data
public class VehicleInTM {
    String vehicleNumber;
    String driverNic;
    Date currentDate;
    String bookingId;
}
