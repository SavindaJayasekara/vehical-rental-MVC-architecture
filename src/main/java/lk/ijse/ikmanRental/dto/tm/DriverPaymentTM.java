package lk.ijse.ikmanRental.dto.tm;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DriverPaymentTM {
    String paymentId;
    String status;
    Double cost;
    String driverNic;
}
