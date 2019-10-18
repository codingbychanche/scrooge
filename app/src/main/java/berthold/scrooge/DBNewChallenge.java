package berthold.scrooge;

/*
 * DBNewChallenge.java
 *
 * Created by Berthold Fritz
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * https://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * Last modified 10/6/19 2:59 PM
 */

import android.util.Log;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBNewChallenge {

    /**
     * Creates a new Challenge
     *
     * @param challengeName
     * @param goal
     * @return key1 of challenge. <0 means error (e.g. challenge with the same name already exists).
     */
    public static int create (String challengeName,float goal){
        PreparedStatement selectPreparedStatement = null;
        int key1OfChallengeJustCreated=-1;

        try {
            // Create new Entry
            selectPreparedStatement =
                    ActivityMain.conn.prepareStatement
                            ("insert into challenge (name,datestarted,goal,done) values ('" + challengeName + "',CURRENT_DATE," + goal + ",false)");
            selectPreparedStatement.executeUpdate();

            // Get Key1 Value of entry just created
            selectPreparedStatement =
                    ActivityMain.conn.prepareStatement
                            ("select key1 from challenge where name='"+challengeName+"'");
            ResultSet rs = selectPreparedStatement.executeQuery();

            int columnCount = rs.getMetaData().getColumnCount();
            if(rs.next())
                key1OfChallengeJustCreated=rs.getInt(1);

        } catch(SQLException ee){
            Log.v("SQLERROR",ee.toString());
        }
        return key1OfChallengeJustCreated;
    }
}
