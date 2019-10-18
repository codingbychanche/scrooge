package berthold.scrooge;

/*
 * DBInsertIntoExpeneses.java
 *
 * Created by Berthold Fritz
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * https://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * Last modified 10/4/19 5:30 AM
 */

import android.util.Log;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBInsertIntoExpeneses {

    public static void moneySpend (int key1OfAssociatedChallenge,float moneySpend,String moneySpendFor){
        PreparedStatement selectPreparedStatement = null;
        try {
            selectPreparedStatement =
                    ActivityMain.conn.prepareStatement
                            ("insert into expenses (key2,date,dateandtime,spend,bought) values ("+key1OfAssociatedChallenge + ",CURRENT_DATE,CURRENT_TIMESTAMP," + moneySpend + ",'"+moneySpendFor+"')");
            selectPreparedStatement.executeUpdate();
        } catch(SQLException ee){
            Log.v("SQLERROR",ee.toString());
        }
    }
}
