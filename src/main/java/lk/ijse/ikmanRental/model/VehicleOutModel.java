package lk.ijse.ikmanRental.model;

import lk.ijse.ikmanRental.crudUtil.CrudUtil;
import lk.ijse.ikmanRental.dto.VehicleOut;

import java.sql.SQLException;

public class VehicleOutModel {
    public static boolean save(VehicleOut vehicle) throws SQLException {
        String sql="INSERT INTO vehicleout (VehicleNumber,DriverNIC,Distance,BookingID)" +
                "VALUES(?, ?, ? ,?)";
        return CrudUtil.execute(sql,
                vehicle.getVehicleNumber(),
                vehicle.getDriverNic(),
                vehicle.getDistance(),
                vehicle.getBookingId()
        );
    }
}
