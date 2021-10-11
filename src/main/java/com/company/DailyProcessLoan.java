package com.company;

import java.sql.*;

public class DailyProcessLoan {

    Connection conn;
    String limit;
    public DailyProcessLoan(Connection conn, String limit){
        this.conn = conn;
        this.limit = limit;
    }
    public void execute() throws SQLException {
        long start = System.currentTimeMillis();
        Statement stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT TOP "+this.limit+" * FROM vw_Collection_LoanSavingsLoan WHERE IsUploaded=0 ORDER BY ID ASC");

        conn.setAutoCommit(false);
        while (resultSet.next()) {

            CollectionLoanSavings collectionLoanSavings = new CollectionLoanSavings();
            collectionLoanSavings.setID(resultSet.getLong(1));
            collectionLoanSavings.setSummaryID(resultSet.getLong(2));
            collectionLoanSavings.setOfficeID(resultSet.getInt(3));
            collectionLoanSavings.setCenterID(resultSet.getInt(4));
            collectionLoanSavings.setMemberID(resultSet.getInt(5));
            collectionLoanSavings.setProductID(resultSet.getInt(6));
            collectionLoanSavings.setProductCode(resultSet.getString(7));
            collectionLoanSavings.setLoanPaid(resultSet.getDouble(8));
            collectionLoanSavings.setDeposit(resultSet.getDouble(9));
            collectionLoanSavings.setWithDrawal(resultSet.getDouble(10));
            collectionLoanSavings.setCollectionDate(resultSet.getString(11));
            collectionLoanSavings.setCreateUser(resultSet.getString(12));
            collectionLoanSavings.setCreateDate(resultSet.getString(13));
            collectionLoanSavings.setIsUploaded(resultSet.getInt(14));
            collectionLoanSavings.setLoanDue(resultSet.getDouble(15));
            collectionLoanSavings.setIntDue(resultSet.getDouble(16));
            collectionLoanSavings.setInstallmentNo(resultSet.getInt(17));
            collectionLoanSavings.setPrincipalLoan(resultSet.getDouble(18));
            collectionLoanSavings.setLoanRepaid(resultSet.getDouble(19));
            collectionLoanSavings.setIntCharge(resultSet.getDouble(20));
            collectionLoanSavings.setIntPaid(resultSet.getDouble(21));
            collectionLoanSavings.setBalance(resultSet.getDouble(22));
            collectionLoanSavings.setCollectionGUID(resultSet.getString(23));
            collectionLoanSavings.setTransType(resultSet.getInt(24));
            try {
                String data = collectionLoanSavings.getOfficeID() + ",'" + collectionLoanSavings.getCreateUser() + "','" + collectionLoanSavings.getCollectionDate().substring(0, 10) + "'," +
                        collectionLoanSavings.getCenterID() + "," +
                        collectionLoanSavings.getSummaryID() + "," +
                        collectionLoanSavings.getProductID() + "," +
                        collectionLoanSavings.getID()+","+
                        collectionLoanSavings.getTransType();

                //String command = "{call SPTESTRais("+data+")}";
                String command = "{call UpLoanTabCollectionForServiceLOan(" + data + ")}";

                CallableStatement cstmt = conn.prepareCall(command);
                boolean executed = cstmt.execute();
                conn.commit();
                if (executed) {
                    System.out.println("Executed: " + collectionLoanSavings.getID() + ", status: " + executed);
                } else {
                    System.out.println("Executed: " + collectionLoanSavings.getID() + ", status: " + executed);
                }

            } catch (Exception e) {

                conn.rollback();

                System.out.println(e.getMessage());
            } finally {


            }

            long finish = System.currentTimeMillis();
            long timeElapsed = finish - start;
            System.out.println("Elapsed Time: "+ timeElapsed +"ms");

        }
        conn.setAutoCommit(true);
    }
}
