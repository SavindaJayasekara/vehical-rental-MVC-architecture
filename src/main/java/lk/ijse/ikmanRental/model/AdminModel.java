package lk.ijse.ikmanRental.model;

import lk.ijse.ikmanRental.crudUtil.CrudUtil;
import lk.ijse.ikmanRental.db.DBConnection;
import lk.ijse.ikmanRental.dto.Admin;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminModel {

    public static boolean save(Admin admin) throws SQLException {

        try(Connection con = DBConnection.getInstance().getConnection();){

            String sql="INSERT INTO admin (AdminNIC,FirsName,LastName,Gamil,Password)" +
                    "VALUES(?, ?, ?, ?, ?)";

            PreparedStatement pstm = con.prepareStatement(sql);

            pstm.setString(1, admin.getNIC());
            pstm.setString(2,admin.getFirstName());
            pstm.setString(3, admin.getLastName());
            pstm.setString(4, admin.getGmail());
            pstm.setString(5, admin.getPassword());

           try{
               return pstm.executeUpdate()>0;

           } catch (Exception e){
               new Alert(Alert.AlertType.ERROR,"Duplicate Detail, Please check your Account and forget Passwords").show();
               return false;
           }
        }catch (Exception e){
            return false;
        }
    }

    public static void delete(String nic){}

    public static boolean update(Admin admin) throws SQLException {
        PreparedStatement pstm = DBConnection.getInstance().getConnection()
                .prepareStatement("UPDATE admin SET FirsName  = ?, LastName = ?, " +
                        "Gamil = ?,Password=?  WHERE AdminNIC = ?");

        pstm.setString(1, admin.getFirstName());
        pstm.setString(2,admin.getLastName());
        pstm.setString(3, admin.getGmail());
        pstm.setString(4, admin.getPassword());
        pstm.setString(5, admin.getNIC());

        return pstm.executeUpdate()>0;
    }

    public static Admin getloginDetail(String gmail) throws SQLException {

        Admin admin1 = new Admin();

        Connection connection=DBConnection.getInstance().getConnection();

            String sql="SELECT Gamil,Password,FirsName,LastName,AdminNIC FROM admin WHERE Gamil=?";
            String sql1="SELECT * FROM admin";

            PreparedStatement pstm = connection.prepareStatement(sql);
            pstm.setString(1, gmail);

            ResultSet resultSet=pstm.executeQuery();

            if (resultSet.next()) {
                admin1.setGmail(resultSet.getString(1));
                admin1.setPassword(resultSet.getString(2));
                admin1.setFirstName(resultSet.getString(3));
                admin1.setLastName(resultSet.getString(4));
                admin1.setNIC(resultSet.getString(5));
            }
        return admin1;
    }

    public static List<String> loadIds() throws SQLException {

        List<String> data = new ArrayList<>();

        String sql="SELECT AdminNIC FROM admin";

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
}
