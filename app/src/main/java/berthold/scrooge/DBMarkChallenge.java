package berthold.scrooge;

/*
 * DBMarkChallenge.java
 *
 * Created by Berthold Fritz
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * https://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * Last modified 10/15/19 8:51 AM
 */

import android.util.Log;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBMarkChallenge {

    public static void asDone(int keyOfChallengeToMark,float endingBalance){
        PreparedStatement selectPreparedStatement = null;
        try {
            selectPreparedStatement =
                    ActivityMain.conn.prepareStatement
                            ("update challenge set done=true, dateended=CURRENT_DATE, endingbalance="+endingBalance+" where key1="+keyOfChallengeToMark);
            //update regex set regexstring='"+regex+"',description='"+coment+"',rating=" +rating + ",date=CURRENT_TIMESTAMP where key1=" + key1
            selectPreparedStatement.executeUpdate();
        } catch(SQLException ee){
            Log.v("SQLERROR",ee.toString());
        }
    }
}
