package lk.ijse.ikmanRental.model;

import lk.ijse.ikmanRental.crudUtil.CrudUtil;
import lk.ijse.ikmanRental.db.DBConnection;
import lk.ijse.ikmanRental.dto.Bill;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BillModel {
    public static String getNextID() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();

        String sql = "SELECT BillID FROM billing ORDER BY BillID DESC LIMIT 1";

        ResultSet resultSet = con.createStatement().executeQuery(sql);

        if (resultSet.next()) {
            return splitOrderId(resultSet.getString(1));
        }
        return splitOrderId(null);
    }

    private static String splitOrderId(String currentId) {
        if(currentId != null) {
            String[] strings = currentId.split("O0");
            int id = Integer.parseInt(strings[0]);
            id++;
            return "00" + id;
        }
        return "00";
    }

    public static boolean save(Bill bill) throws SQLException {
        String sql="INSERT INTO billing(BillID,BookingID,CustomerNIC,DriverNIC,Cost,VehicleNumber,Date)" +
                "VALUES(?, ?, ?, ?, ?, ?, ?)";

        return CrudUtil.execute(sql,
                bill.getBillID(),
                bill.getBookingID(),
                bill.getCustomerNIC(),
                bill.getDriverNic(),
                bill.getCost(),
                bill.getVehicleNumber(),
                bill.getCurrentDate()
                );

    }

    public static Bill getAll(String id) throws SQLException {
        String sql="SELECT * FROM billing WHERE BookingID =?";
        PreparedStatement pstm = DBConnection.getInstance().getConnection()
                .prepareStatement(sql);
        pstm.setString(1, id);
        ResultSet resultSet=pstm.executeQuery();

        if (resultSet.next()){
            return new Bill(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getDouble(5),
                    resultSet.getString(6),
                    resultSet.getDate(7)
            );
        }
        return null;
    }

    public static boolean delete(String id) throws SQLException {
        PreparedStatement pstm = DBConnection.getInstance().getConnection()
                .prepareStatement("DELETE FROM billing WHERE BookingID = ?");
        pstm.setString(1, id);
        return pstm.executeUpdate()>0;
    }

    public static boolean update(Bill bill) throws SQLException {

        String sql="UPDATE billing SET BookingID=? ,CustomerNIC=? ,DriverNIC=? ,Cost=? ,VehicleNumber=? ,Date=? WHERE BillID=?";
        PreparedStatement pstm = DBConnection.getInstance().getConnection()
                .prepareStatement(sql);
        pstm.setString(1, bill.getBookingID());
        pstm.setString(2, bill.getCustomerNIC());
        pstm.setString(3, bill.getDriverNic());
        pstm.setDouble(4, bill.getCost());
        pstm.setString(5, bill.getVehicleNumber());
        pstm.setDate(6, bill.getCurrentDate());
        pstm.setString(7, bill.getBillID());
        return pstm.executeUpdate()>0;

    }

    public static String getBillid(String bookingID) throws SQLException {
        PreparedStatement pstm = DBConnection.getInstance().getConnection()
                .prepareStatement("SELECT BillID FROM billing WHERE BookingID=?");
        pstm.setString(1, bookingID);

        ResultSet resultSet= pstm.executeQuery();

        if (resultSet.next()){
            return resultSet.getString(1);
        }
        return null;
    }
}
