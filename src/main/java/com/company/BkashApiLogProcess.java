package com.company;

import java.sql.*;

public class BkashApiLogProcess {

    Connection conn;
    public BkashApiLogProcess(Connection conn){
        this.conn = conn;
    }

    public void execute() throws SQLException {
        long start = System.currentTimeMillis();
        Statement stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT TOP 50 ID,BusinessDate,OfficeID FROM vw_bKashAPILOG WHERE IsUploaded=0 ORDER BY ID ASC");

        conn.setAutoCommit(false);
        while (resultSet.next()) {

            System.out.println("ID: "+resultSet.getLong("ID")+" OFFICE_ID: "+resultSet.getLong("OfficeID"));

            BkashApiLog bkashApiLog = new BkashApiLog(resultSet.getInt("ID"));
            bkashApiLog.setBusinessDate(resultSet.getString("BusinessDate"));
            bkashApiLog.setOfficeID(resultSet.getLong("OfficeID"));
            try {
                String data =  bkashApiLog.getOfficeID()+","+bkashApiLog.getID()+",'"+bkashApiLog.getBusinessDate().substring(0,10)+"'";

                //String command = "{call SPTESTRais("+data+")}";
                String command = "{call setbKashUploadForService(" + data + ")}";

                CallableStatement cstmt = conn.prepareCall(command);
                boolean executed = cstmt.execute();
                conn.commit();
                if (executed) {
                    System.out.println("Executed: " + bkashApiLog.getID() + ", status: " + executed);
                } else {
                    System.out.println("Executed: " + bkashApiLog.getID() + ", status: " + executed);
                }

            } catch (Exception e) {
                System.out.println("Exception Cause:[ID-"+bkashApiLog.getID()+"]: "+e.getMessage());
                conn.rollback();

            } finally {
                conn.setAutoCommit(true);
            }

            long finish = System.currentTimeMillis();
            long timeElapsed = finish - start;
            System.out.println("Elapsed Time: "+ timeElapsed +"ms");
        }

        conn.setAutoCommit(true);
    }
}
