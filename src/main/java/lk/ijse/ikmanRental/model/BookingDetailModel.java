package lk.ijse.ikmanRental.model;

import lk.ijse.ikmanRental.crudUtil.CrudUtil;
import lk.ijse.ikmanRental.db.DBConnection;
import lk.ijse.ikmanRental.dto.BookingDetail;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookingDetailModel {
    public static boolean save(BookingDetail bookingDetail) throws SQLException {
        String sql="INSERT INTO bookingdetail(BookingID,VehicleNumber,fuel)" +
                "VALUES(?, ?, ?)";

        return CrudUtil.execute(sql,
                bookingDetail.getBookingId(),
                bookingDetail.getVehicleNumber(),
                bookingDetail.getFuel()
                );
    }

    public static String getVehicleNumber(String bookingID) throws SQLException {
        String sql="SELECT VehicleNumber FROM bookingdetail WHERE BookingID =?";
        PreparedStatement pstm = DBConnection.getInstance().getConnection()
                .prepareStatement(sql);
        pstm.setString(1, bookingID);
        ResultSet resultSet=pstm.executeQuery();

        if (resultSet.next()){
            return resultSet.getString(1);
        }
        return null;
    }

    public static BookingDetail getAll(String id) throws SQLException {
        String sql="SELECT * FROM bookingdetail WHERE BookingID =?";
        PreparedStatement pstm = DBConnection.getInstance().getConnection()
                .prepareStatement(sql);
        pstm.setString(1, id);

        ResultSet resultSet=pstm.executeQuery();

        if (resultSet.next()){
            return new BookingDetail(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDouble(3)
            );
        }
        return null;
    }

    public static boolean delete(String id) throws SQLException {
        PreparedStatement pstm = DBConnection.getInstance().getConnection()
                .prepareStatement("DELETE FROM bookingdetail WHERE BookingID = ?");
        pstm.setString(1, id);
        return pstm.executeUpdate()>0;
    }

    public static boolean update(BookingDetail bookingDetail) throws SQLException {
        PreparedStatement pstm = DBConnection.getInstance().getConnection()
                .prepareStatement("UPDATE bookingdetail SET VehicleNumber=? ,fuel=? WHERE BookingID=?");
        pstm.setString(1, bookingDetail.getVehicleNumber());
        pstm.setDouble(2, bookingDetail.getFuel());
        pstm.setString(3, bookingDetail.getBookingId());
        return pstm.executeUpdate()>0;
    }

    public static String getVehicleNUmberInRunning(String id) throws SQLException {
        String sql="SELECT VehicleNumber FROM bookingdetail WHERE BookingID=?";
        PreparedStatement pstm = DBConnection.getInstance().getConnection()
                .prepareStatement(sql);
        pstm.setString(1, id);

        ResultSet resultSet=pstm.executeQuery();

        if (resultSet.next()){
            return resultSet.getString(1);
        }
        return null;
    }

    public static boolean setStatus(String id) throws SQLException {
        PreparedStatement pstm = DBConnection.getInstance().getConnection()
                .prepareStatement("UPDATE booking SET Status='FINISHED' WHERE BookingID=?");
        pstm.setString(1, id);
        return pstm.executeUpdate()>0;
    }
}
