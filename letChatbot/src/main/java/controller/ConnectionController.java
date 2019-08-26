package controller;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionController {
    Connection connect = null;
    Statement stmt = null;
    ResultSet result = null;
    CallableStatement cs = null;
    String dbUrl, username, password;
    public ConnectionController() {
    	connect = null;
    	stmt = null;
        result = null;
        cs = null;
        dbUrl = "YOUR_SQL_URL_HERE";
        username = "YOUR_SQL_USERNAME";
        password = "YOUR_SQL_PASSWORD";
    }
    public void connection(){
        try{
            // conexão com o banco de dados ------------------------------------
            connect = DriverManager.getConnection(dbUrl, username, password);
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    } //metodo conexão ---------------------------------------------------------
    public void disconection(){
        try{//desconectando do banco de dados ----------------------------------              
            if(stmt!=null){
                stmt.close();
            }
            if(result!=null){
                result.close();
            }
            if(connect!=null){
                connect.close();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }//metodo desconexão -------------------------------------------------------
}
