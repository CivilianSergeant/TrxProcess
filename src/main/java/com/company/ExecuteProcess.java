package com.company;

import java.sql.CallableStatement;
import java.sql.Connection;

public class ExecuteProcess implements Runnable {

//    private TabCollection tabCollection;
//    private String tabData;
//    private String rowId;
    Connection conn;
    CollectionLoanSavings collectionLoanSavings;
    private boolean isError=false;
    public ExecuteProcess(Connection c, CollectionLoanSavings collectionLoanSavings){
        this.conn = c;
        this.collectionLoanSavings = collectionLoanSavings;

    }

    @Override
    public void run() {

        if(this.conn == null){

            return;
        }

        long start = System.currentTimeMillis();

        try {
            //conn.setAutoCommit(false);


            String data = collectionLoanSavings.getOfficeID() + ",'" + collectionLoanSavings.getCreateUser() + "','" + collectionLoanSavings.getCollectionDate().substring(0, 10) + "'," +
                    collectionLoanSavings.getCenterID() + "," +
                    collectionLoanSavings.getSummaryID() + "," +
                    collectionLoanSavings.getProductID() + "," +
                    collectionLoanSavings.getID()+","+
                    collectionLoanSavings.getTransType();

            //String command = "{call SPTESTRais("+data+")}";
            String command = "{call UpLoanTabCollectionForServiceSaving(" + data + ")}";

            CallableStatement cstmt = conn.prepareCall(command);
            boolean executed = cstmt.execute();
            //conn.commit();
            //conn.setAutoCommit(true);
            if(executed){
                System.out.println("Executed: "+collectionLoanSavings.getID()+", status: "+executed);
            }else{
                System.out.println("Executed: "+collectionLoanSavings.getID()+", status: "+executed);
            }
            //this.conn.close();
        } catch (Exception e) {
            System.out.printf("ERROR:"+e.getMessage());
            //return;
            //e.printStackTrace();
        }finally {

        }


        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println("Execution Completed by : " + timeElapsed + "ms");
    }
}
