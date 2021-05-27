package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

public class Main {



    public static void main(String[] args) {
        Timer timer = new Timer();

        if(args.length<6){
            System.out.println("\n");
            System.out.println("Please make sure you app instruction has following parameters accordingly");
            System.out.println("java -jar filename period/interval servicename ip dbname dbuser dbpass no-of-item-process");
            System.exit(0);
        }

        long period = Long.valueOf(args[0]);

        timer.schedule(new MyTask(
                    args[1],        //service name i.e daily-loan-saving, bkash
                    args[2],
                    args[3],
                    args[4],
                    args[5],
                    args[6]
                ),0,period);
    }
}



class MyTask extends TimerTask{

    String serviceName;
    String ip;
    String db;
    String user;
    String pass;
    String limit;

    public MyTask(String sName,String ip, String dbName, String user, String pass,String limit){
        this.ip = ip;
        this.db = dbName;
        this.user = user;
        this.pass = pass;
        this.serviceName = sName;
        this.limit = limit;
    }

    @Override
    public void run() {
        Connection conn = null;
        //"jdbc:sqlserver://192.192.192.171\\MFI;databaseName=gBankerBUROSouth";
        String URL = "jdbc:sqlserver://"+ip+";databaseName="+db;
        String user = this.user; //live buroapk
        String password = this.pass; //"buroapk@#$2019"; // buroapk@#$2019

        System.out.println("Service Starting ");
        System.out.println("DSN: "+URL);
        System.out.println("User: "+user);
        System.out.println("Password: "+pass);
        System.out.println("No of process in one execution:"+limit);

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(URL,user,password);
            if(conn != null){

                switch (serviceName){
                    case "daily-loan":
                        System.out.println("Processing Daily Loan collection");
                        DailyProcessLoan dailyProcessLoan = new DailyProcessLoan(conn,limit);
                        dailyProcessLoan.execute();
                        break;
                    case "daily-loan-saving":
                        System.out.println("Processing Daily Loan Saving collection");
                        DailyProcessLoanSaving dailyProcessLoanSaving = new DailyProcessLoanSaving(conn,limit);
                        dailyProcessLoanSaving.execute();
                        System.out.println("End of Execution");
                        break;
                    case "bkash":
                        System.out.println("Processing Bkash");
                        BkashApiLogProcess bkashApiLogProcess = new BkashApiLogProcess(conn);
                        bkashApiLogProcess.execute();
                        System.out.println("End of Execution");
                        break;
                }



                //conn.setAutoCommit(true);
                conn.close();
                System.out.println("End Session");
                //executor.shutdown();



            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
//            System.out.println("Success");
        }

    }
}
