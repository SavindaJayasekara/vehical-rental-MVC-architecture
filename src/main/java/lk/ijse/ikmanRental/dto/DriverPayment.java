package lk.ijse.ikmanRental.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class DriverPayment {
    String paymentID;
    String status;
    Double paymentCost;
    String DriverNic;
}
