package berthold.scrooge;

/*
 * DBChallenge.java
 *
 * Created by Berthold Fritz
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * https://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * Last modified 10/21/19 9:45 AM
 */

import android.util.Log;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBChallenge {
    public static void delete(int keyOfChallengeToDelete) {

        try {
            PreparedStatement selectPreparedStatement = null;
            selectPreparedStatement =
                    ActivityMain.conn.prepareStatement("delete from challenge where key1=" + keyOfChallengeToDelete);
                    ActivityMain.conn.prepareStatement("delete from expenses where key2="+keyOfChallengeToDelete);
            selectPreparedStatement.executeUpdate();

        } catch (
                SQLException ee) {
            Log.v("-", "SQL Error " + ee.toString());
        }
    }
}
