package lk.ijse.ikmanRental.dto.tm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class VehicleTM {
    String vehicleNumber;
    String name;
    String type;
    double fuelToKm;
    int kmh;
    String availability;
    String status;
    String condition;
}
