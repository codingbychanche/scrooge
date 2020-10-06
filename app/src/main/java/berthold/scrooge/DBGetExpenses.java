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

import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBGetExpenses {

    /**
     * Returns sum of all daily expenses for the current date.
     *
     * @param primaryKeyOfCahllenge
     * @return
     */

    public static float dailySum(int primaryKeyOfCahllenge,String date) {
        int columnCount;
        float moneySpend = 0;

        try {
            PreparedStatement selectPreparedStatement = null;
            selectPreparedStatement =
                    ActivityMain.conn.prepareStatement
                            ("select spend from expenses where key2=" + primaryKeyOfCahllenge + " and date='"+date+"'");
            ResultSet rs = selectPreparedStatement.executeQuery();

            while (rs.next()) {
                columnCount = rs.getMetaData().getColumnCount();
                if (columnCount <= 1)
                    moneySpend = moneySpend + rs.getFloat(1);
            }

        } catch (SQLException ee) {
            Log.v("-", "SQL Error " + ee.toString());
        }
        return moneySpend;
    }

    /**
     * Returns the total sum of one challenge.
     */
    public static float totalSumOfExpenses(int primaryKeyOfCahllenge) {
        int columnCount;
        float moneySpend = 0;

        try {
            PreparedStatement selectPreparedStatement = null;
            selectPreparedStatement =
                    ActivityMain.conn.prepareStatement
                            ("select spend from expenses where key2=" + primaryKeyOfCahllenge);
            ResultSet rs = selectPreparedStatement.executeQuery();

            while (rs.next()) {
                columnCount = rs.getMetaData().getColumnCount();
                if (columnCount <= 1)
                    moneySpend = moneySpend + rs.getFloat(1);
            }

        } catch (SQLException ee) {
            Log.v("-", "SQL Error " + ee.toString());
        }
        return moneySpend;
    }

    /**
     * Returns a list of the daily sum of expenses for each day the challenge was running.
     */

    public static List<DataEntry> forEachDayChallengeWasRunning(int primaryKeyOfChallenge, float goal) {

        List<DataEntry> waterfallData = new ArrayList<>();
        String date;
        float dailySum;
        int columnCount, day;
        ResultSet rs;

        try {
            PreparedStatement selectPreparedStatement = null;

            // Get all dates the current challenge was running and money was spend
            selectPreparedStatement =
                    ActivityMain.conn.prepareStatement
                            ("select date from expenses where key2=" + primaryKeyOfChallenge);
            rs = selectPreparedStatement.executeQuery();

            List<String> datelist = new ArrayList<>();
            date = "empty";
            while (rs.next()) {
                columnCount = rs.getMetaData().getColumnCount();
                if (columnCount <= 1) {
                    date = rs.getString(1);
                    datelist.add(date);
                }
            }

            // Get sum of money spend for each day the challenge was running
            // and create a new entry for the waterfall chart.
            // toDO add method to formatTimeStamp which returns day/ year/ month in order to shorten date so that it can be displayed in chart
            day = 1;
            dailySum=0;

            if (datelist.isEmpty())
                waterfallData.add(new ValueDataEntry("-", goal));
            else {
                for (String currentDate : datelist) {
                    waterfallData.add(new ValueDataEntry(currentDate, goal));
                    dailySum = dailySum(primaryKeyOfChallenge, currentDate);
                    waterfallData.add(new ValueDataEntry(day, -dailySum));
                    day++;
                }
            }

        } catch (SQLException ee) {
            Log.v("-", "SQL Error " + ee.toString());
        }

        return waterfallData;
    }

    /**
     * Returns the sum by product for the selected challenge
     *
     * @return List of products and the total money spend/ product.
     * <p>
     * todo: If for the same product money was spend twice or more times, this product appears more than one time in the chart which it should not!
     */

    public static List<DataEntry> sumByProduct(int primaryKeyOfChallenge) {
        int columnCount;
        float moneySpend = 0;
        String product = "-";
        ResultSet listOfProducts, listOfExpensesPerProduct;

        List<DataEntry> resultExpensesPerProduct = new ArrayList<>();

        try {
            // Get all bought products for the challenge selected
            PreparedStatement selectProducts = null;
            selectProducts =
                    ActivityMain.conn.prepareStatement
                            ("select bought from expenses where key2=" + primaryKeyOfChallenge);
            listOfProducts = selectProducts.executeQuery();

            while (listOfProducts.next()) {

                columnCount = listOfProducts.getMetaData().getColumnCount();
                if (columnCount <= 1) {
                    product = listOfProducts.getString(1);

                    // Get money spend for this product
                    PreparedStatement selectExpenses = null;
                    selectExpenses = ActivityMain.conn.prepareStatement
                            ("select spend from expenses where bought='" + product + "' and key2=" + primaryKeyOfChallenge);
                    listOfExpensesPerProduct = selectExpenses.executeQuery();
                    columnCount = listOfExpensesPerProduct.getMetaData().getColumnCount();

                    moneySpend = 0;
                    while (listOfExpensesPerProduct.next()) {

                        if (columnCount <= 1)
                            moneySpend = moneySpend + listOfExpensesPerProduct.getFloat(1);
                    }
                    resultExpensesPerProduct.add(new ValueDataEntry(product, moneySpend));
                }
                Log.v("NAME_OF_PRODUCT", " " + product + " " + moneySpend);
            }
        } catch (SQLException ee) {
            Log.v("-", "SQL Error " + ee.toString());
        }
        return resultExpensesPerProduct;
    }

}
