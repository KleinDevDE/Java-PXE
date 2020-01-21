package de.kleindev.tftpserver.utils;

import java.sql.*;

public class MySQLConnection {
    private Connection connection;

    public MySQLConnection(String host, String port, String database, String username, String password, boolean ssl, boolean readOnly) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        connection=DriverManager.getConnection(
                "jdbc:mysql://"+host+":"+port+"/"+database,username,password);
        connection.setSavepoint(String.valueOf(System.currentTimeMillis()));
        connection.setReadOnly(readOnly);
    }

    public void disconnect(){
        try {
            connection.close();
        } catch (SQLException e) {
            }
    }

    public PreparedStatement preparedStatement(String sql){
        try {
            return connection.prepareStatement(sql);
        } catch (SQLException e) {
            return null;
        }
    }

    public SQLWarning getWarnings(){
        try {
            return connection.getWarnings();
        } catch (SQLException e) {
            return null;
        }
    }

    protected Connection getConnection(){
        return this.connection;
    }


}
