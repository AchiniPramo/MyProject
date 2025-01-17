package com.example.layeredarchitecture.dao;

import com.example.layeredarchitecture.db.DBConnection;

import java.sql.*;
import java.time.LocalDate;

public class OrderDAOImpl {
    public String generateNewOrderId() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getDbConnection().getConnection();
        Statement stm = connection.createStatement();
        ResultSet rst = stm.executeQuery("SELECT oid FROM `Orders` ORDER BY oid DESC LIMIT 1;");

        if (rst.next()) {
            int lastId = Integer.parseInt(rst.getString("oid").replace("OID-", ""));
            return String.format("OID-%03d", lastId + 1);
        } else {
            return "OID-001";
        }
    }

    public void selectOrderId(String orderId) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getDbConnection().getConnection();
        PreparedStatement stm = connection.prepareStatement("SELECT oid FROM Orders WHERE oid=?");
        stm.setString(1, orderId);
        //if order id already exist//
        if (stm.executeQuery().next()) {

        }
    }

    public boolean saveOrder(String orderId, LocalDate orderDate, String customerId) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getDbConnection().getConnection();

        connection.setAutoCommit(false);
        PreparedStatement stm = connection.prepareStatement("INSERT INTO Orders (oid, date, customerID) VALUES (?,?,?)");
        stm.setString(1, orderId);
        stm.setDate(2, Date.valueOf(orderDate));
        stm.setString(3, customerId);

        if (stm.executeUpdate() != 1) {
            connection.rollback();
            connection.setAutoCommit(true);
            return false;
        }
        return true;
    }
}
