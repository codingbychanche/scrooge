package berthold.scrooge;

/*
 * DBList.java
 *
 * Provides helper methods to query the database and provide the result
 * as lists, fills the associated listView and notifies the adapter....
 *
 * Created by Berthold Fritz
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * https://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * Last modified 10/3/19 12:21 PM
 */

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBList {

    /**
     * Build a list of all challenges done from the 'challenges' table.
     *
     * @param challengeListAdapter
     * @param challengeDataList
     * @param searchQuery
     * @param orderBy
     * @param sortingOrder
     */
    public static void challenges(RecyclerView.Adapter challengeListAdapter, List<ChallengeData> challengeDataList, String searchQuery, String orderBy, String sortingOrder) {

        // Debug
        String tag = DBList.class.getSimpleName();

        int columnCount = 0;

        try {
            PreparedStatement selectPreparedStatement = null;
            selectPreparedStatement = ActivityMain.conn.prepareStatement("select key1,name,datestarted,dateended,goal,endingbalance,done from challenge where name like '%" + searchQuery + "%' and done=true order by " + orderBy + " " + sortingOrder);
            ResultSet rs = selectPreparedStatement.executeQuery();

            while (rs.next()) {

                columnCount = rs.getMetaData().getColumnCount();

                // Insert date from db into list view's data model
                int key1 = 0;
                String name = "-";
                String dateStarted = "-";
                String dateEndet = "-";
                float goal = 0;
                float endingBalance = 0;
                boolean done = false;

                if (columnCount >= 1) key1 = rs.getInt(1);
                if (columnCount >= 2) name = rs.getString(2);
                if (columnCount >= 3) dateStarted = rs.getString(3);
                if (columnCount >= 4) dateEndet = rs.getString(4);
                if (columnCount >= 5) goal = rs.getFloat(5);
                if (columnCount >= 6) endingBalance = rs.getFloat(6);
                if (columnCount >= 7) done = rs.getBoolean(7);

                // todo Debug
                Log.v("CHALLENGES:", name + " " + dateStarted + " " + dateEndet + " " + goal + " " + endingBalance + " " + done);

                // If search query was passed, mark matching part of name....
                Spannable markedName = new SpannableString(name);
                if (!searchQuery.isEmpty()) {
                    int startIndex = name.indexOf(searchQuery);
                    markedName.setSpan(new BackgroundColorSpan(Color.BLUE), startIndex, startIndex + searchQuery.length(), 0);
                    markedName.setSpan(new RelativeSizeSpan(2), startIndex, startIndex + searchQuery.length(), 0);
                }

                // Create list entry
                if (done) {
                    ChallengeData newChallenge = new ChallengeData(key1, dateStarted, dateEndet, name, goal, endingBalance, done);
                    challengeDataList.add(newChallenge);
                    challengeListAdapter.notifyDataSetChanged();
                }
            }
            if (columnCount < 1) {

                // Nothing found.....
                //ChallengeData emptyChallenge = new ChallengeData(0, "-", "No Challenges!", 0, false);
                //challengeDataList.add(emptyChallenge);
                //challengeListAdapter.notifyDataSetChanged();
            }

        } catch (
                SQLException ee) {
            Log.v(tag, "ERROR:" + ee.toString());
        }
    }

    /**
     * Builds a list of products from the 'products' table
     */
    public static String[] products() {

        String[] listOfProductsReturned = null;

        try {
            PreparedStatement selectPreparedStatement = null;
            selectPreparedStatement = ActivityMain.conn.prepareStatement
                    ("select name from products order by name DESC");
            ResultSet rs = selectPreparedStatement.executeQuery();

            int columnCount;
            List<String> listOfProducts = new ArrayList<>();

            while (rs.next()) {
                columnCount = rs.getMetaData().getColumnCount();
                if (columnCount == 1)
                    listOfProducts.add(rs.getString(1));
                else
                    listOfProducts.add("empty");
            }

            listOfProductsReturned = new String[listOfProducts.size()];
            listOfProducts.toArray(listOfProductsReturned);

        } catch (
                SQLException ee) {
            Log.v("ERROR ", ee.toString());
        }
        return listOfProductsReturned;
    }

    /**
     * Builds a list of expenses for a given challenge and the current date.
     */
    public static void expenses(int key1OfassiciatedChallenge, RecyclerView.Adapter expensesListAdapter, List<ExpensesData> expensesData) {
        // Debug
        String tag = DBList.class.getSimpleName();

        int columnCount = 0;

        try {
            PreparedStatement selectPreparedStatement = null;
            selectPreparedStatement = ActivityMain.conn.prepareStatement
                    ("select key1,key2,dateandtime,spend,bought,date from expenses where key2=" + key1OfassiciatedChallenge + " and date=CURRENT_DATE order by dateandtime DESC");
            ResultSet rs = selectPreparedStatement.executeQuery();

            while (rs.next()) {

                columnCount = rs.getMetaData().getColumnCount();

                // Insert date from db into list view's data model
                int key1 = 0;
                String date = "-";
                float spend = 0;
                String bought = "-";

                if (columnCount >= 1) key1 = rs.getInt(1);
                if (columnCount >= 2) key1OfassiciatedChallenge = rs.getInt(2);
                if (columnCount >= 3) date = rs.getString(3);
                if (columnCount >= 4) spend = rs.getFloat(4);
                if (columnCount >= 5) bought = rs.getString(5);

                String niceDate = UtilFormatTimeStamp.fromH2DataBaseTogermanDateFormat(date, UtilFormatTimeStamp.WITH_TIME);
                Log.v("DATE", date + " | " + niceDate);
                /*
                // If search query was passed, mark matching part of name....
                Spannable markedName = new SpannableString(name);
                if (!searchQuery.isEmpty()) {
                    int startIndex = name.indexOf(searchQuery);
                    markedName.setSpan(new BackgroundColorSpan(Color.BLUE), startIndex, startIndex + searchQuery.length(), 0);
                    markedName.setSpan(new RelativeSizeSpan(2), startIndex, startIndex + searchQuery.length(), 0);
                }
                */

                // Create list entry
                ExpensesData newExpense = new ExpensesData(key1, key1OfassiciatedChallenge, spend, bought, niceDate);
                expensesData.add(newExpense);
                expensesListAdapter.notifyDataSetChanged();

            }
            if (columnCount < 1) {

                // Nothing found.....
                ExpensesData emptyExpense = new ExpensesData
                        (0, 0, 0, "Keine Ausgaben...", "-");
                expensesData.add(emptyExpense);
                expensesListAdapter.notifyDataSetChanged();
            }
        } catch (
                SQLException ee) {
            Log.v(tag, "ERROR:" + ee.toString());
        }

    }
}
