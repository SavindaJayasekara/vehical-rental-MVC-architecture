package lk.ijse.ikmanRental.model;

import lk.ijse.ikmanRental.crudUtil.CrudUtil;
import lk.ijse.ikmanRental.db.DBConnection;
import lk.ijse.ikmanRental.dto.DriverSchedule;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DriverScheduleModel {
    public static boolean save(DriverSchedule driverSchedule) throws SQLException {
        String sql="INSERT INTO drivershedeul(BookingID,DriverNIC)" +
                "VALUES(?, ?)";

        return CrudUtil.execute(sql,driverSchedule.getBookingID(),
                driverSchedule.getDriverNic()
        );
    }

    public static String getDriverNic(String bookingId) throws SQLException {
        String sql="SELECT DriverNIC FROM drivershedeul WHERE BookingID =?";
        PreparedStatement pstm = DBConnection.getInstance().getConnection()
                .prepareStatement(sql);
        pstm.setString(1, bookingId);
        ResultSet resultSet=pstm.executeQuery();

        if (resultSet.next()){
            return resultSet.getString(1);
        }
        return null;
    }

    public static DriverSchedule getAll(String id) throws SQLException {
        String sql="SELECT * FROM drivershedeul WHERE BookingID =?";
        PreparedStatement pstm = DBConnection.getInstance().getConnection()
                .prepareStatement(sql);
        pstm.setString(1, id);
        ResultSet resultSet=pstm.executeQuery();

        if (resultSet.next()){
            return new DriverSchedule(
                    resultSet.getString(1),
                    resultSet.getString(2)
            );
        }
        return null;
    }

    public static boolean delete(String id) throws SQLException {
        PreparedStatement pstm = DBConnection.getInstance().getConnection()
                .prepareStatement("DELETE FROM drivershedeul WHERE BookingID = ?");
        pstm.setString(1, id);
        return pstm.executeUpdate()>0;
    }

    public static boolean update(DriverSchedule driverSchedule) throws SQLException {
        PreparedStatement pstm = DBConnection.getInstance().getConnection()
                .prepareStatement("UPDATE drivershedeul SET DriverNIC=? WHERE BookingID=?");
        pstm.setString(1, driverSchedule.getDriverNic());
        pstm.setString(2, driverSchedule.getBookingID());
        return pstm.executeUpdate()>0;
    }

    public static String getDriverNicInRunning(String id) throws SQLException {
        PreparedStatement pstm = DBConnection.getInstance().getConnection()
                .prepareStatement("SELECT DriverNIC FROM drivershedeul WHERE BookingID=?");
        pstm.setString(1, id);

        ResultSet resultSet=pstm.executeQuery();

        if (resultSet.next()){
            return resultSet.getString(1);
        }
        return null;
    }
}
