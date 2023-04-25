package lk.ijse.ikmanRental.model;

import lk.ijse.ikmanRental.crudUtil.CrudUtil;
import lk.ijse.ikmanRental.db.DBConnection;
import lk.ijse.ikmanRental.dto.Driver;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DriverModel {

    public static boolean save(Driver driver) throws SQLException {
        String sql="INSERT INTO driver (DriverNIC,Gamil,Name,Gender,Status)" +
                "VALUES(?, ?, ?, ?, ?)";

        return CrudUtil.execute(sql,
                driver.getNic(),
                driver.getGmail(),
                driver.getName(),
                driver.getGender(),
                driver.getStatus()
        );
    }

    public static Driver getAllDrivers(String nic) throws SQLException {
        PreparedStatement pstm = DBConnection.getInstance().getConnection()
                .prepareStatement("SELECT * FROM driver WHERE DriverNIC = ?");
        pstm.setString(1, nic);
        ResultSet resultSet = pstm.executeQuery();

        if (resultSet.next()){
            return new Driver(resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
                    );
        }
    return null;
    }

    public static List<String> loadNic() throws SQLException {
        List<String> nic=new ArrayList<>();

        ResultSet resultSet = null;
        String sql="SELECT DriverNIC FROM driver";

        try {
            resultSet=CrudUtil.execute(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        while (resultSet.next()){
            nic.add(resultSet.getString(1));
        }
        return nic;
    }

    public static boolean update(Driver driver) throws SQLException {

        PreparedStatement pstm = DBConnection.getInstance().getConnection()
                .prepareStatement("UPDATE driver SET Gamil = ?, Name = ?, " +
                        "Gender = ?,Status =? WHERE DriverNIC = ?");

        pstm.setString(1,driver.getGmail());
        pstm.setString(2,driver.getName());
        pstm.setString(3,driver.getGender());
        pstm.setString(4,driver.getStatus());
        pstm.setString(5,driver.getNic());

        return pstm.executeUpdate()>0;
    }

    public static List<Driver> getAll() throws SQLException {
        List<Driver> drivers=new ArrayList<>();

        String sql="SELECT * FROM driver";

        ResultSet resultSet=null;

        try {
            resultSet=CrudUtil.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        while (resultSet.next()){
            drivers.add(new Driver(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
            ));
        }
        return drivers;
    }

    public static boolean delete(String nicValue) throws SQLException {
        PreparedStatement pstm = DBConnection.getInstance().getConnection()
                .prepareStatement("DELETE FROM driver WHERE DriverNIC = ?");
        pstm.setString(1, nicValue);
        return pstm.executeUpdate()>0;
    }

    public static String getName(String driverNic) throws SQLException {
        PreparedStatement pstm = DBConnection.getInstance().getConnection()
                .prepareStatement("SELECT Name FROM driver WHERE DriverNIC=?");
        pstm.setString(1, driverNic);

        ResultSet resultSet=pstm.executeQuery();
        if (resultSet.next()){
            return resultSet.getString(1);
        }
        return null;
    }

    public static Double getStatus(String driverNic) throws SQLException {
        PreparedStatement pstm = DBConnection.getInstance().getConnection()
                .prepareStatement("SELECT Status FROM driver WHERE DriverNIC=?");
        pstm.setString(1, driverNic);

        ResultSet resultSet=pstm.executeQuery();

        if (resultSet.next()) {
            Double status= Double.valueOf(resultSet.getString(1));
            return status;
        }
        return null;
    }

    public static String getType(String vehicleNumber) throws SQLException {
        PreparedStatement pstm = DBConnection.getInstance().getConnection()
                .prepareStatement("SELECT Type FROM vehicle WHERE VehicleNumber=?");
        pstm.setString(1, vehicleNumber);

        ResultSet resultSet=pstm.executeQuery();
        if (resultSet.next()){
            return resultSet.getString(1);
        }
        return null;
    }

    public static int count() throws SQLException {
        String sql="SELECT count(*) from driver;";
        PreparedStatement pstm = DBConnection.getInstance().getConnection()
                .prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        if (resultSet.next()){
            return resultSet.getInt(1);
        }
        return 0;
    }

    public static String getGmail(String nic) throws SQLException {
        String sql="SELECT Gamil FROM driver WHERE DriverNIC=?";
        PreparedStatement pstm = DBConnection.getInstance().getConnection()
                .prepareStatement(sql);
        pstm.setString(1, nic);
        ResultSet resultSet = pstm.executeQuery();

        if (resultSet.next()){
            return resultSet.getString(1);
        }
        return null;
    }
}
