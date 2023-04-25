package lk.ijse.ikmanRental.model;

import lk.ijse.ikmanRental.crudUtil.CrudUtil;
import lk.ijse.ikmanRental.db.DBConnection;
import lk.ijse.ikmanRental.dto.DriverPayment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DriverPaymentModel {
    public static String getnextID() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();

        String sql = "SELECT PaymentID FROM payment ORDER BY PaymentID DESC LIMIT 1";

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

    public static boolean save(DriverPayment driverPay) throws SQLException {
        String sql="INSERT INTO payment(PaymentID,Status,PaymentCost,DriverNIC)" +
                "VALUES(? ,? ,? ,?)";

        return CrudUtil.execute(sql,
                driverPay.getPaymentID(),
                driverPay.getStatus(),
                driverPay.getPaymentCost(),
                driverPay.getDriverNic()
                );
    }

    public static DriverPayment getAll(String id) throws SQLException {
        String sql="SELECT * FROM payment WHERE DriverNIC =?";
        PreparedStatement pstm = DBConnection.getInstance().getConnection()
                .prepareStatement(sql);
        pstm.setString(1, id);
        ResultSet resultSet=pstm.executeQuery();

        if (resultSet.next()){
            return new DriverPayment(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDouble(3),
                    resultSet.getString(4)
            );
        }
        return null;
    }

    public static boolean delete(String driverNIC) throws SQLException {
        PreparedStatement pstm = DBConnection.getInstance().getConnection()
                .prepareStatement("DELETE FROM payment WHERE DriverNIC = ?");
        pstm.setString(1, driverNIC);
        return pstm.executeUpdate()>0;
    }

    public static boolean update(DriverPayment driverPay) throws SQLException {
        PreparedStatement pstm = DBConnection.getInstance().getConnection()
                .prepareStatement("UPDATE payment SET Status=? ,PaymentCost=? ,DriverNIC=? WHERE PaymentID=?");
        pstm.setString(1, driverPay.getStatus());
        pstm.setDouble(2, driverPay.getPaymentCost());
        pstm.setString(3, driverPay.getDriverNic());
        pstm.setString(4, driverPay.getPaymentID());
        return pstm.executeUpdate()>0;
    }

    public static String getPaymentID(String id) throws SQLException {
        PreparedStatement pstm = DBConnection.getInstance().getConnection()
                .prepareStatement("SELECT PaymentID FROM payment WHERE DriverNIC=?");
        pstm.setString(1, id);

        ResultSet resultSet=pstm.executeQuery();

       if (resultSet.next()){
           return resultSet.getString(1);
       }
       return null;
    }

    public static boolean updatePayment(String driverNic) throws SQLException {
        PreparedStatement pstm = DBConnection.getInstance().getConnection()
                .prepareStatement("UPDATE payment SET Status=? WHERE DriverNIC = ?");

        pstm.setString(1,"PAYED");
        pstm.setString(2,driverNic);

        return pstm.executeUpdate()>0;
    }

    public static List<DriverPayment> getAll() throws SQLException {
        String sql="SELECT * FROM payment";
        List<DriverPayment> payments=new ArrayList<>();
        ResultSet resultSet=null;

        try {
            resultSet=CrudUtil.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        while (resultSet.next()){
            payments.add(new DriverPayment(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDouble(3),
                    resultSet.getString(4)
            ));
        }
        return payments;
    }
}
