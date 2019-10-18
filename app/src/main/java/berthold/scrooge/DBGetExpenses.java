package berthold.scrooge;

/*
 * DBGetExpenses.java
 *
 * Created by Berthold Fritz
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * https://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * Last modified 10/4/19 5:40 AM
 */

import android.util.Log;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBGetExpenses {

    /**
     * Returns sum of all daily expenses for the current date.
     *
     * @param primaryKeyOfCahllenge
     * @return
     */

    public static float dailySum(int primaryKeyOfCahllenge){
        int columnCount;
        float moneySpend=0;

        try {
            PreparedStatement selectPreparedStatement = null;
            selectPreparedStatement =
                    ActivityMain.conn.prepareStatement
                            ("select spend from expenses where key2="+primaryKeyOfCahllenge+" and date=CURRENT_DATE");
            ResultSet rs = selectPreparedStatement.executeQuery();

            while (rs.next()) {
              columnCount = rs.getMetaData().getColumnCount();
              if (columnCount<=1)
                  moneySpend = moneySpend+rs.getFloat(1);
            }

        } catch (SQLException ee) {
            Log.v ("-", "SQL Error " + ee.toString());
        }
        return moneySpend;
    }
}
