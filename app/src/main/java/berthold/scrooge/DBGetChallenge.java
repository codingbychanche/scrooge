package berthold.scrooge;

/*
 * DBGetChallenge.java
 *
 * Created by Berthold Fritz
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * https://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * Last modified 10/1/19 9:58 PM
 */

import android.util.Log;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBGetChallenge {

    public static String name(int key1) {

        String name="-";

        try {
            PreparedStatement selectPreparedStatement = null;
            selectPreparedStatement =
                    ActivityMain.conn.prepareStatement("select name from challenge where key1="+key1);
            ResultSet rs = selectPreparedStatement.executeQuery();

            if (rs.next())
                name = rs.getString(1);

        } catch (SQLException ee) {
            Log.v ("-", "SQL Error " + ee.toString());
        }
        return name;
    }

    public static float goal(int key1) {

        float goal=0;

        try {
            PreparedStatement selectPreparedStatement = null;
            selectPreparedStatement =
                    ActivityMain.conn.prepareStatement("select goal from challenge where key1="+key1);
            ResultSet rs = selectPreparedStatement.executeQuery();

            if (rs.next())
                goal = rs.getFloat(1);

        } catch (SQLException ee) {
            Log.v ("-", "SQL Error " + ee.toString());
        }
        return goal;
    }

    public static boolean isActive(int key1){

        boolean isActive=false;
        try {
            PreparedStatement selectPreparedStatement = null;
            selectPreparedStatement =
                    ActivityMain.conn.prepareStatement("select goal from challenge where key1="+key1);
            ResultSet rs = selectPreparedStatement.executeQuery();

            if (rs.next())
                isActive = rs.getBoolean(1);

        } catch (SQLException ee) {
            Log.v ("-", "SQL Error " + ee.toString());
        }
        return isActive;
    }

    public String dateStarted(int key1){

        String dateStrated="-";

        try {
            PreparedStatement selectPreparedStatement = null;
            selectPreparedStatement =
                    ActivityMain.conn.prepareStatement("select datestarted from challenge where key1="+key1);
            ResultSet rs = selectPreparedStatement.executeQuery();

            if (rs.next())
               dateStrated = rs.getString(1);

        } catch (SQLException ee) {
            Log.v ("-", "SQL Error " + ee.toString());
        }
        return dateStrated;
    }
}
