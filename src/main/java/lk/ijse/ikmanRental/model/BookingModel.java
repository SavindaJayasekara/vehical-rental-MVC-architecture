package lk.ijse.ikmanRental.model;

import lk.ijse.ikmanRental.crudUtil.CrudUtil;
import lk.ijse.ikmanRental.db.DBConnection;

import javafx.scene.control.Alert;
import lk.ijse.ikmanRental.dto.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookingModel {
    public static String getNextBookingID() throws SQLException {

        Connection con = DBConnection.getInstance().getConnection();

        String sql = "SELECT BookingID FROM booking ORDER BY BookingID DESC LIMIT 1";

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
        return "001";
    }

    public static boolean addBooking(Booking booking, Bill bill, DriverPayment driverPay, DriverSchedule driverSchedule, BookingDetail bookingDetail) throws SQLException {
        Connection connection=null;

        try {
            connection=DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            boolean isSave=save(booking);
            if (isSave){
                boolean isSaveBill=BillModel.save(bill);
                if (isSaveBill){
                    boolean isSavePayment=DriverPaymentModel.save(driverPay);
                    if (isSavePayment){
                        boolean isVehicleUpdate=VehicleModel.updateAvailability(bookingDetail.getVehicleNumber());
                        if (isVehicleUpdate){
                            boolean isSaveDriverSchedule=DriverScheduleModel.save(driverSchedule);
                            if (isSaveDriverSchedule){
                                boolean isBookingDetailSave=BookingDetailModel.save(bookingDetail);
                                if (isBookingDetailSave){
                                    connection.commit();
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            connection.rollback();
            new Alert(Alert.AlertType.ERROR,"Transaction SQL Error !").show();
            e.printStackTrace();
        }finally {
            System.out.println("finally");
            connection.setAutoCommit(true);
        }
        return false;
    }

    private static boolean save(Booking booking) throws SQLException {
        String sql="INSERT INTO booking (BookingID,Status,AmmountCost,RequriedDate,RideTO,Distance,CustomerNIC)" +
                "VALUES(?, ?, ?, ?, ?, ?, ?)";

        return CrudUtil.execute(sql,
                booking.getBookingID(),
                booking.getStatus(),
                booking.getAmountsCost(),
                booking.getRequiredDate(),
                booking.getRideTo(),
                booking.getDistance(),
                booking.getCustomerNic()
                );
    }

    public static List<Booking> getAll() throws SQLException {
        List<Booking> bookings=new ArrayList<>();
        String sql = "SELECT * FROM booking";

        ResultSet resultSet = null;

        try {
            resultSet = CrudUtil.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        while (resultSet.next()){
            bookings.add(new Booking(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDouble(3),
                    resultSet.getDate(4),
                    resultSet.getString(5),
                    resultSet.getString(6),
                    resultSet.getString(7)
            ));
        }
        return bookings;
    }

    public static List<String> getBookingIds() throws SQLException {
        List<String> ids=new ArrayList<>();
        String sql="SELECT BookingID FROM booking";
        ResultSet resultSet=null;

        try {
            resultSet=CrudUtil.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        while (resultSet.next()){
            ids.add(resultSet.getString(1));
        }
        return ids;
    }

    public static Booking getAllFromID(String id) throws SQLException {
        PreparedStatement pstm = DBConnection.getInstance().getConnection()
                .prepareStatement("SELECT * FROM booking WHERE BookingID = ?");
        pstm.setString(1, id);
        ResultSet resultSet = pstm.executeQuery();

        if (resultSet.next()){
            return new Booking(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDouble(3),
                    resultSet.getDate(4),
                    resultSet.getString(5),
                    resultSet.getString(6),
                    resultSet.getString(7)
            );
        }
        return null;
    }

    public static boolean delete(String id) throws SQLException {

        DriverSchedule driverSchedule=DriverScheduleModel.getAll(id);
        BookingDetail bookingDetail=BookingDetailModel.getAll(id);

        Connection connection=null;

        try {
            connection=DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            boolean isDeleteDriver=DriverScheduleModel.delete(id);
            if (isDeleteDriver){
                boolean isDeleteDetail=BookingDetailModel.delete(id);
                if (isDeleteDetail){
                    boolean isDeleteBill=BillModel.delete(id);
                    if (isDeleteBill){
                        boolean isDeletePayment=DriverPaymentModel.delete(driverSchedule.getDriverNic());
                        if (isDeletePayment){
                           boolean isDeleteBooking=deleteBooking(id);
                           if (isDeleteBooking){
                               boolean isUpdate=VehicleModel.updateAvailabilityDelete(bookingDetail.getVehicleNumber());
                               if (isUpdate){
                                   connection.commit();
                                   return true;
                               }
                           }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            connection.rollback();
            e.printStackTrace();
        }finally {
            System.out.println("delete trans !");
            connection.setAutoCommit(true);
        }
        return false;
    }

    private static boolean deleteBooking(String id) throws SQLException {
        PreparedStatement pstm = DBConnection.getInstance().getConnection()
                .prepareStatement("DELETE FROM booking WHERE BookingID = ?");
        pstm.setString(1, id);
        return pstm.executeUpdate()>0;
    }


    public static boolean update(Booking booking, Bill bill, DriverPayment driverPay, DriverSchedule driverSchedule, BookingDetail bookingDetail) throws SQLException {

        Connection connection=null;

        try {
            connection=DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            boolean isUpdateBooking=updateBooking(booking);
            if (isUpdateBooking){
                boolean isUpdateBill=BillModel.update(bill);
                if (isUpdateBill){
                    boolean isUpdatePayment=DriverPaymentModel.update(driverPay);
                    if (isUpdatePayment){
                        boolean isUpdateSchedule=DriverScheduleModel.update(driverSchedule);
                        if (isUpdateSchedule){
                            boolean isUpdateBookingDetail=BookingDetailModel.update(bookingDetail);
                            if (isUpdateBookingDetail){
                                connection.commit();
                                return true;
                            }
                        }
                    }
                }
            }

        } catch (SQLException e) {
            connection.rollback();
            e.printStackTrace();
        }finally {
            System.out.println("finally");
            connection.setAutoCommit(true);
        }
        return false;
    }

    private static boolean updateBooking(Booking booking) throws SQLException {

        PreparedStatement pstm = DBConnection.getInstance().getConnection()
                .prepareStatement("UPDATE booking SET Status=?, AmmountCost=? ,RequriedDate=? ,RideTO=? ,Distance=? ,CustomerNIC=? WHERE BookingID=?");
        pstm.setString(1, booking.getStatus());
        pstm.setDouble(2, booking.getAmountsCost());
        pstm.setDate(3, booking.getRequiredDate());
        pstm.setString(4, booking.getRideTo());
        pstm.setString(5, booking.getDistance());
        pstm.setString(6, booking.getCustomerNic());
        pstm.setString(7, booking.getBookingID());
        return pstm.executeUpdate()>0;
    }

    public static List<String> getRunningIds() throws SQLException {
        List<String> bookings=new ArrayList<>();
        String sql = "SELECT BookingID FROM booking WHERE Status='RUNNING'";

        ResultSet resultSet = null;

        resultSet=CrudUtil.execute(sql);

        while (resultSet.next()){
            bookings.add(resultSet.getString(1));
        }
        return bookings;
    }

    public static boolean setStatus(String id) throws SQLException {
        PreparedStatement pstm = DBConnection.getInstance().getConnection()
                .prepareStatement("UPDATE booking SET Status='FINISHED' WHERE BookingID=?");
        pstm.setString(1, id);
        return pstm.executeUpdate()>0;
    }

    public static List<String> getPendinngIds() throws SQLException {
        List<String> bookings=new ArrayList<>();
        String sql = "SELECT BookingID FROM booking WHERE Status='PENDING'";

        ResultSet resultSet = null;

        resultSet=CrudUtil.execute(sql);

        while (resultSet.next()){
            bookings.add(resultSet.getString(1));
        }
        return bookings;
    }

    public static Double getDistance(String id) throws SQLException {
        PreparedStatement pstm = DBConnection.getInstance().getConnection()
                .prepareStatement("SELECT Distance FROM booking WHERE BookingID = ?");
        pstm.setString(1, id);
        ResultSet resultSet = pstm.executeQuery();

        if (resultSet.next()){
            return resultSet.getDouble(1);
        }
        return null;
    }

    public static boolean updateVehicleOut(String id) throws SQLException {
        PreparedStatement pstm = DBConnection.getInstance().getConnection()
                .prepareStatement("UPDATE booking SET Status='RUNNING' WHERE BookingID=?");
        pstm.setString(1, id);
        return pstm.executeUpdate()>0;
    }

    public static int count() throws SQLException {
        String sql="SELECT count(*) from booking;";
        PreparedStatement pstm = DBConnection.getInstance().getConnection()
                .prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        if (resultSet.next()){
            return resultSet.getInt(1);
        }
        return 0;
    }

    public static int countRides() throws SQLException {
        Date now = Date.valueOf(LocalDate.now());

        PreparedStatement pstm = DBConnection.getInstance().getConnection()
                .prepareStatement("SELECT count(*) from booking WHERE RequriedDate=?");
        pstm.setDate(1, now);
        ResultSet resultSet = pstm.executeQuery();

        if (resultSet.next()){
            return resultSet.getInt(1);
        }
        return 0;
    }
}
