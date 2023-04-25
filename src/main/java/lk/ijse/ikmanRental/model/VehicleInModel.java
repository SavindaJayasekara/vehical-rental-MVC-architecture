package lk.ijse.ikmanRental.model;

import lk.ijse.ikmanRental.crudUtil.CrudUtil;
import lk.ijse.ikmanRental.dto.VehicleIn;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VehicleInModel {

    public static boolean save(VehicleIn vehicleIn) throws SQLException {
        String sql="INSERT INTO vehiclein (VehicleNumber,DriverNIC,CurrentDate,BookingID)" +
                "VALUES (?, ?, ?, ?)";
        return CrudUtil.execute(sql,
                vehicleIn.getVehicleNumber(),
                vehicleIn.getDriverNic(),
                vehicleIn.getDate(),
                vehicleIn.getBookingId()
        );
    }

    public static List<VehicleIn> getAll() throws SQLException {
        List<VehicleIn> inList=new ArrayList<>();
        String sql = "SELECT * FROM vehiclein";
        ResultSet resultSet=null;

        try {
             resultSet= CrudUtil.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        while (resultSet.next()){
            inList.add(new VehicleIn(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDate(3),
                    resultSet.getString(4)
                    )
            );
        }
        return inList;
    }
}
