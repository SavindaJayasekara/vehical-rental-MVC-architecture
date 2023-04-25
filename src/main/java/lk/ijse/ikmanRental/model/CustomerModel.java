package lk.ijse.ikmanRental.model;

import lk.ijse.ikmanRental.crudUtil.CrudUtil;
import lk.ijse.ikmanRental.db.DBConnection;
import lk.ijse.ikmanRental.dto.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerModel {

    public static boolean save(Customer customer) throws SQLException {

            String sql ="INSERT INTO customer (CustomerNIC,Gamil,Contact,Name,AdminNIC)" +
                    "VALUES(?, ?, ?, ?, ?)";
            return CrudUtil.execute(sql,customer.getNic(),
                    customer.getGmail(),
                    customer.getContact(),
                    customer.getName(),
                    customer.getAdminNic()
            );
    }

    public static List<Customer> getAll() throws SQLException {
        List<Customer> data = new ArrayList<>();

        String sql = "SELECT * FROM Customer";
        ResultSet resultSet = null;
        try {
            resultSet = CrudUtil.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        while (resultSet.next()) {
            data.add(new Customer(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
            ));
        }
        return data;
    }

    public static List<String> getCustomerNic() throws SQLException {
        List<String> data = new ArrayList<>();

        String sql="SELECT CustomerNIC FROM customer";

        ResultSet resultSet = null;
        try {
            resultSet = CrudUtil.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        while (resultSet.next()) {
            data.add(resultSet.getString(1));
        }
        return data;
    }

    public static Customer getAllCustomer(String nic) throws SQLException {
//
//        try {
//            ResultSet resultSet=CrudUtil.execute(sql,nic);
//
//            if (resultSet.next())
//            return  new Customer(
//                    resultSet.getString(1),
//                    resultSet.getString(2),
//                    resultSet.getString(3),
//                    resultSet.getString(4),
//                    resultSet.getString(5)
//            );
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;

        PreparedStatement pstm = DBConnection.getInstance().getConnection()
                .prepareStatement("SELECT * FROM customer WHERE CustomerNIC = ?");
        pstm.setString(1, nic);
        ResultSet resultSet = pstm.executeQuery();

        if(resultSet.next()) {
            return new Customer(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)
            );
        }
        return null;
    }

    public static boolean delete(String nic) throws SQLException {
        PreparedStatement pstm = DBConnection.getInstance().getConnection()
                .prepareStatement("DELETE FROM customer WHERE CustomerNIC = ?");
        pstm.setString(1, nic);
        return pstm.executeUpdate()>0;
    }

    public static boolean update(Customer customer) throws SQLException {
        PreparedStatement pstm = DBConnection.getInstance().getConnection()
                .prepareStatement("UPDATE customer SET Name = ?, Contact = ?, " +
                        "Gamil = ? WHERE CustomerNIC = ?");

        pstm.setString(1, customer.getName());
        pstm.setString(2,customer.getContact());
        pstm.setString(3,customer.getGmail());
        pstm.setString(4,customer.getNic());

        return pstm.executeUpdate()>0;
    }

    public static String getName(String driverNic) throws SQLException {
        PreparedStatement pstm = DBConnection.getInstance().getConnection()
                .prepareStatement("SELECT Name FROM customer WHERE CustomerNIC=?");

        pstm.setString(1, driverNic);

        ResultSet resultSet=pstm.executeQuery();

        if (resultSet.next()){
            return resultSet.getString(1);
        }

        return null;
    }

    public static int countCustomer() throws SQLException {
        String sql="SELECT count(*) from customer;";
        PreparedStatement pstm = DBConnection.getInstance().getConnection()
                .prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        if (resultSet.next()){
            return resultSet.getInt(1);
        }
        return 0;
    }
}
