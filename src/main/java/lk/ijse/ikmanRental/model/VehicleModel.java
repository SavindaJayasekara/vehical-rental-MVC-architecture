package lk.ijse.ikmanRental.model;

import lk.ijse.ikmanRental.crudUtil.CrudUtil;
import lk.ijse.ikmanRental.db.DBConnection;
import lk.ijse.ikmanRental.dto.Vehicle;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VehicleModel {

    public static boolean save(Vehicle vehicle) throws SQLException {
        String sql="INSERT INTO vehicle(VehicleNumber,Name,Type,FuelToKm,KMH,Availability,Status,Conditions)" +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        return CrudUtil.execute(sql,
                vehicle.getVehicleNumber(),
                vehicle.getName(),
                vehicle.getType(),
                vehicle.getFuelToKm(),
                vehicle.getKmh(),
                vehicle.getAvailability(),
                vehicle.getStatus(),
                vehicle.getCondition()
        );
    }

    public static List<Vehicle> getAll() throws SQLException {
        List<Vehicle> vehicles=new ArrayList<>();
        String sql = "SELECT * FROM vehicle";

        ResultSet resultSet = null;

        try {
            resultSet = CrudUtil.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        while (resultSet.next()){
            vehicles.add(new Vehicle(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getInt(4),
                    resultSet.getInt(5),
                    resultSet.getString(6),
                    resultSet.getString(7),
                    resultSet.getString(8)

            ));
        }
        return vehicles;
    }

    public static List<String> loadNumbers() throws SQLException {
        List<String> numbers = new ArrayList<>();

        String sql="SELECT VehicleNumber FROM vehicle";

        ResultSet resultSet = null;

        try {
            resultSet=CrudUtil.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        while (resultSet.next()){
            numbers.add(resultSet.getString(1));
        }
    return numbers;
    }

    public static Vehicle getAll(String number) throws SQLException {
        PreparedStatement pstm = DBConnection.getInstance().getConnection()
                .prepareStatement("SELECT * FROM vehicle WHERE VehicleNumber = ?");
        pstm.setString(1, number);
        ResultSet resultSet = pstm.executeQuery();

        while (resultSet.next()){
            return new Vehicle(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getInt(4),
                    resultSet.getInt(5),
                    resultSet.getString(6),
                    resultSet.getString(7),
                    resultSet.getString(8)
            );
        }
        return null;
    }

    public static boolean update(Vehicle vehicle) throws SQLException {
        PreparedStatement pstm = DBConnection.getInstance().getConnection()
                .prepareStatement("UPDATE vehicle SET Name = ?, Type = ?, " +
                        "FuelToKm = ?,KMH =?,Availability =?,Status=?,Conditions=? WHERE VehicleNumber = ?");

        pstm.setString(1,vehicle.getName());
        pstm.setString(2,vehicle.getType());
        pstm.setDouble(3, vehicle.getFuelToKm());
        pstm.setInt(4,vehicle.getKmh());
        pstm.setString(5,vehicle.getAvailability());
        pstm.setString(6,vehicle.getStatus());
        pstm.setString(7,vehicle.getCondition());
        pstm.setString(8,vehicle.getVehicleNumber());

        return pstm.executeUpdate()>0;

    }

    public static boolean delete(String number) throws SQLException {
        PreparedStatement pstm = DBConnection.getInstance().getConnection()
                .prepareStatement("DELETE FROM vehicle WHERE VehicleNumber = ?");
        pstm.setString(1, number);
        return pstm.executeUpdate()>0;
    }

    public static List<String> getAvailbleType(String type) throws SQLException {
        List<String> numbers = new ArrayList<>();
        PreparedStatement pstm = DBConnection.getInstance().getConnection()
                .prepareStatement("SELECT VehicleNumber FROM vehicle WHERE Type=? && Availability='AVAILABLE';");
        pstm.setString(1, type);

        ResultSet resultSet=pstm.executeQuery();

        while (resultSet.next()){
            numbers.add(resultSet.getString(1));
        }
        return numbers;
    }

    public static Vehicle getFuelToKm(String number) throws SQLException {
        String sql="SELECT FuelToKm, Status FROM vehicle WHERE VehicleNumber=?";
        PreparedStatement pstm = DBConnection.getInstance().getConnection()
                .prepareStatement(sql);
        pstm.setString(1, number);
        Vehicle vehicle = new Vehicle();
        ResultSet resultSet=pstm.executeQuery();
        if (resultSet.next()){
            vehicle.setFuelToKm(resultSet.getDouble(1));
            vehicle.setStatus(resultSet.getString(2));
        }
        return vehicle;
    }

    public static boolean updateAvailability(String vehicleNumber) throws SQLException {
        PreparedStatement pstm = DBConnection.getInstance().getConnection()
                .prepareStatement("UPDATE vehicle SET Availability=? WHERE VehicleNumber = ?");

        pstm.setString(1,"BOOKED");
        pstm.setString(2,vehicleNumber);

        return pstm.executeUpdate()>0;
    }

    public static boolean updateAvailabilityDelete(String vehicleNumber) throws SQLException {
        PreparedStatement pstm = DBConnection.getInstance().getConnection()
                .prepareStatement("UPDATE vehicle SET Availability=? WHERE VehicleNumber = ?");

        pstm.setString(1,"AVAILABLE");
        pstm.setString(2,vehicleNumber);

        return pstm.executeUpdate()>0;
    }

    public static int count() throws SQLException {
        String sql="SELECT count(*) from vehicle WHERE Availability='AVAILABLE';";
        PreparedStatement pstm = DBConnection.getInstance().getConnection()
                .prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        if (resultSet.next()){
            return resultSet.getInt(1);
        }
        return 0;
    }

    public static boolean updateAvailabilityRunning(String vehicleNumber,String status) throws SQLException {
        PreparedStatement pstm = DBConnection.getInstance().getConnection()
                .prepareStatement("UPDATE vehicle SET Availability=? WHERE VehicleNumber = ?");

        pstm.setString(1,status);
        pstm.setString(2,vehicleNumber);

        return pstm.executeUpdate()>0;
    }
}
