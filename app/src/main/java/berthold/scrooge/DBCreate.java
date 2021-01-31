package berthold.scrooge;
/**
 * Create.
 * <p>
 * This creates a new, empty, database if no database of the same
 * name already exists.
 *
 * @author Berthold Fritz 2016
 */

import android.util.Log;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


public class DBCreate {

    public static void make(String name) throws Exception {

        // DB- Connection
        String DB_DRIVER = "org.h2.Driver";
        String DB_CONNECTION = "jdbc:h2:";
        String DB_USER = "";
        String DB_PASSWORD = "";
        Connection conn;

        // Debug
        String tag = ActivityMain.class.getSimpleName();

        try {
            conn = DB.read(DB_DRIVER, DB_CONNECTION + name, DB_USER, DB_PASSWORD);
        } catch (SQLException se) {
            Log.v(tag, "Error while creating database:" + DB_CONNECTION);
            Log.v(tag, " " + se);
            return;
        }

        try {
            Statement stmt = null;

            stmt = conn.createStatement();

            // Create Tables
            stmt.executeUpdate("create table"
                    + "	challenge"
                    + " (key1 identity,"
                    + "name char(255),"
                    + "datestarted datetime,"
                    + "dateended datetime,"
                    + "done boolean,"
                    + "endingbalance float,"
                    + "goal float)");

            stmt.executeUpdate("create table"
                    + "	expenses"
                    + " (key1 identity,"
                    + "key2 int,"
                    + "date datetime," // Date without time (in order to check if a new Day started)...
                    + "dateandtime datetime," // Used to order expenses per day by time....
                    + "spend float,"
                    + "bought char (255))");

            stmt.executeUpdate("create table"
                    + "	products"
                    + " (key1 identity,"
                    + " name char (255))");

        } catch (SQLException se) {
            Log.v(tag, "Error while creating table " + se.toString());
            return;
        }
        // Close database
        DB.close(conn);
        Log.i("---------", "Closed connection");
    }
}
