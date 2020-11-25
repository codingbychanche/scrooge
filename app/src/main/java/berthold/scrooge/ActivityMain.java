package berthold.scrooge;

/*
 * ActivityMain.java
 *
 * Created by Berthold Fritz
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * https://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * Last modified 9/30/19 1:59 PM
 */

// todo When a new system date is set, carry is set for new day. If a "true" day ends, not!

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Waterfall;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ActivityMain extends AppCompatActivity
        implements FragmentSpendMoney.getDataFromSpendMoneyFrag,
        FragmentNewChallenge.getDataFromNewChallengeFrag {

    // Debug
    private String tag = ActivityMain.class.getSimpleName();

    // DB
    static public Connection conn;
    private String path;

    // Expenses List
    private RecyclerView expensesListRecyclerView;
    private RecyclerView.Adapter expensesListAdapter;
    private RecyclerView.LayoutManager expensesListLayoutManager;
    private List<ExpensesData> expensesListData = new ArrayList<>();

    // Shared prefs
    SharedPreferences sharedPreferences;

    // UI
    TextView challengeName, activeChallengeGoal, moneySpendToday, addCarryField, moneyLeftToday, targetForCurrentChallengeView, actualForCurrentChallengeView,
            totalBalanceView, dateOfActiveChallenge, challengeDaysRunningView;
    ImageButton spendMoney, closeAvtiveChallenge, listAllClosedChallanges, toggleMenu;

    boolean menuIsShown;

    AnyChartView waterfallView;
    Waterfall waterfall;
    List<DataEntry> waterfallData = new ArrayList<>();

    // Anim
    Animation fadeInAnim, fadeOutAnim;

    // Logic
    int timesBackPressed, challengeDaysRunning;
    int key1OfActiveChallenge;  // Primary key is <1 if no active challenge exists or was selected.
    String nameOfCurrentChallenge, currentDate, dateOfLastEdit;
    float goal, carry, sumOfExpenses, moneyLeft;

    // Number format
    private static String floatNumberFormatPreset = "%.2f";

    /*
     * On Create
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("LIFE ", "On Create");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        challengeName = findViewById(R.id.active_challenge_name_field);
        activeChallengeGoal = findViewById(R.id.active_challenge_goal_field);
        moneyLeftToday = findViewById(R.id.money_left_today_field);

        targetForCurrentChallengeView = findViewById(R.id.target);
        actualForCurrentChallengeView = findViewById(R.id.actual);
        totalBalanceView = findViewById(R.id.balance);

        dateOfActiveChallenge = findViewById(R.id.date_of_active_challenge);
        moneySpendToday = findViewById(R.id.money_spend_today_field);
        //addCarryField = findViewById(R.id.add_carry_field);

        toggleMenu = findViewById(R.id.fold_unfold_menu);

        spendMoney = findViewById(R.id.spend_money);
        closeAvtiveChallenge = findViewById(R.id.close_cahllenge_start_new);
        challengeDaysRunningView = findViewById(R.id.challenge_days_running);

        // Anim
        fadeInAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.menu_fade_in);
        fadeOutAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.menu_fade_out);

        // Create Database
        String dbName = "/ScroogeLedger";

        File f = getFilesDir();
        path = (f.getAbsolutePath() + dbName);

        try {
            DBCreate.make(path);
            Log.i(tag, "DB Created on:\n");
            Log.i(tag, path);
        } catch (Exception e) {
            Log.i(tag, "Error creating DB:" + e.toString());
        }

        // Read DB
        String DB_DRIVER = "org.h2.Driver";
        String DB_CONNECTION = "jdbc:h2:" + path;
        String DB_USER = "";
        String DB_PASSWORD = "";

        try {
            Log.d(tag, "Reading:" + DB_CONNECTION + "\n");
            conn = DB.read(DB_DRIVER, DB_CONNECTION, DB_USER, DB_PASSWORD);

        } catch (Exception e) {
            Log.d(tag, "Error opening DB\n");
            Log.d(tag, e.toString());
        }

        // List of expenses
        expensesListRecyclerView = (RecyclerView) findViewById(R.id.expenses_list);
        expensesListRecyclerView.setHasFixedSize(true);
        expensesListLayoutManager = new LinearLayoutManager(this);
        expensesListRecyclerView.setLayoutManager(expensesListLayoutManager);
        expensesListAdapter = new ExpensesListAdapter(expensesListData, this);
        expensesListRecyclerView.setAdapter(expensesListAdapter);

        /*
        // Waterfall Chart showing daily expenses relative to each other
        waterfallView = (AnyChartView) findViewById(R.id.any_chart_waterfall);
        APIlib.getInstance().setActiveAnyChartView(waterfallView);
        waterfall = AnyChart.waterfall();
        waterfall.background("#94BBA7");
        waterfallView.setChart(waterfall);
        */
        //
        // UI listeners
        //

        // Show/ hide menu
        toggleMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleMenue();
            }
        });

        // Spend money!
        spendMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentSpendMoney fragmentSpendMoney = FragmentSpendMoney.newInstance("Titel");
                fragmentSpendMoney.show(fm, "fragment_spend_money");
            }
        });

        // Close challenge/ start a new one....
        closeAvtiveChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                challengeStartNew();
            }
        });

        // List all closed challenges...
        listAllClosedChallanges = findViewById(R.id.list_all_closed_challenges);
        listAllClosedChallanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(), ActivityListAndEvalChallenges.class);
                startActivity(in);
            }
        });
    }

    /*
     * Resume Activity
     */
    @Override
    public void onResume() {
        super.onResume();

        menuIsShown=false;
        toggleMenue();

        timesBackPressed = 0;
        challengeDaysRunning = 1;
        toggleMenue();

        // Restores last state of the App.
        //
        // This part checks if the current date matches the date restored
        // from shared prefs. If they don't:
        //      
        //      goal        =x
        //      sum         =0  
        //      carry       =budget
        //      new budget  =goal+sum+budget
        //
        // After the new day is detected and calculated, this state is saved to
        // shared prefs.
        currentStateRestoreFromSharedPref();

        if (goal == 0)
            challengeStartNew();

        // @rem:Shows how to get system date@@
        //SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        currentDate = sdf.format(new Date());
        Log.v("DATE", " Current Date=" + currentDate);

        if (!currentDate.equals(dateOfLastEdit))
            dayStartNew();

        challengeRefresh();
        expensesRefresh();
        //expensesUpdateWaterfallChart();
    }

    /*
     * Show/ hide menu
     */
    private void toggleMenue() {
        if (menuIsShown) {
            menuIsShown = false;
            spendMoney.startAnimation(fadeOutAnim);
            spendMoney.setClickable(false);
            closeAvtiveChallenge.startAnimation(fadeOutAnim);
            closeAvtiveChallenge.setClickable(false);
            listAllClosedChallanges.startAnimation(fadeOutAnim);
            listAllClosedChallanges.setClickable(false);
        } else {
            menuIsShown = true;
            spendMoney.startAnimation(fadeInAnim);
            spendMoney.setClickable(true);
            closeAvtiveChallenge.startAnimation(fadeInAnim);
            closeAvtiveChallenge.setClickable(true);
            listAllClosedChallanges.startAnimation(fadeInAnim);
            listAllClosedChallanges.setClickable(true);
        }
    }

    /*
     * Starts a new Challenge
     */
    private void challengeStartNew() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentNewChallenge newChallenge = FragmentNewChallenge.newInstance("Titel");
        newChallenge.show(fm, "fragment_start_new_challenge");
    }

    /*
     * Callback for Fragment: New Challenge.
     * Database holding all expenses is updated from here....
     */
    @Override
    public void challengeGetDataFromFragment(String name, String newGoal, String buttonPressed) {

        if (buttonPressed.equals("OK") && !name.isEmpty() && !newGoal.isEmpty()) {

            // Old challenge is done....
            DBMarkChallenge.asDone(key1OfActiveChallenge, moneyLeft);

            // Reset....
            carry = 0;
            moneyLeft = 0;
            sumOfExpenses = 0;
            challengeDaysRunning = 1;

            // Init
            float newGoalDec = Float.valueOf(newGoal);
            int oldKey = key1OfActiveChallenge;
            key1OfActiveChallenge = DBNewChallenge.create(name, newGoalDec);

            // Refresh...
            challengeRefresh();
            expensesRefresh();
            //expensesUpdateWaterfallChart();
        }
    }

    /*
     * Updates the selected Challenge.
     *
     *
     * E.g.: App restarts or money was spend......
     */
    private void challengeRefresh() {
        String name = DBGetChallenge.name(key1OfActiveChallenge);
        challengeName.setText(name);

        challengeDaysRunningView.setText("Tag " + challengeDaysRunning);

        goal = DBGetChallenge.goal(key1OfActiveChallenge);
        String goalFormated = String.format(floatNumberFormatPreset, goal);
        activeChallengeGoal.setText(goalFormated + " €/ Day");

        sumOfExpenses = DBGetExpenses.dailySum(key1OfActiveChallenge, currentDate);
        String sumFormated = String.format(floatNumberFormatPreset, sumOfExpenses);
        Log.v("SUM", " Summe " + sumFormated);
        moneySpendToday.setText(sumFormated + " €");

        String dateFormeted = UtilFormatTimeStamp.fromSimpleDateFormatToGerman(currentDate);
        dateOfActiveChallenge.setText(dateFormeted);

        moneyLeft = goal - sumOfExpenses + carry;
        String moneyLeftFormeted = String.format(floatNumberFormatPreset, moneyLeft);

        if (moneyLeft < 0)
            moneyLeftToday.setTextColor(Color.RED);
        else
            moneyLeftToday.setTextColor(Color.GREEN);
        moneyLeftToday.setText(moneyLeftFormeted + " €");

        /*
        String carryFormated = String.format(floatNumberFormatPreset, carry);
        if (carry < 0)
            addCarryField.setTextColor(Color.RED);
        else
            addCarryField.setTextColor((Color.GREEN));

        addCarryField.setText(carryFormated + " €");
         */

        // Show target goal until this day and actual sum of money spend...
        float actualSumOfExpenses = DBGetExpenses.totalSumOfExpenses(key1OfActiveChallenge);
        String actualFormated = String.format(floatNumberFormatPreset, actualSumOfExpenses);
        actualForCurrentChallengeView.setText(actualFormated + " €");

        float targetSumOfExpenses = goal * challengeDaysRunning;
        String targetFormated = String.format(floatNumberFormatPreset, targetSumOfExpenses);
        targetForCurrentChallengeView.setText(targetFormated + " €");

        float totalBalance = targetSumOfExpenses - actualSumOfExpenses;
        String totalBalanceFormated = String.format(floatNumberFormatPreset, totalBalance);
        totalBalanceView.setText(totalBalanceFormated + " €");

        if (actualSumOfExpenses > targetSumOfExpenses) {
            totalBalanceView.setTextColor(Color.RED);
            actualForCurrentChallengeView.setTextColor(Color.RED);

        } else {
            totalBalanceView.setTextColor(Color.GREEN);
            actualForCurrentChallengeView.setTextColor(Color.GREEN);
        }
    }

    /*
     * Callback when item in challenge list was pressed
     */
    public void challengeListItemInsideWasTouched(int position, int resourceId) {
        String nameOfButtonPressedInres;
        nameOfButtonPressedInres = getResources().getResourceEntryName(resourceId);
        Log.v("Main", "Row:" + position + " touched" + " Button:" + nameOfButtonPressedInres);

        if (nameOfButtonPressedInres.equals("select_this_challenge")) {
        }
    }

    /*
     * System Callbacks
     */

    @Override
    protected void onPause() {
        super.onPause();
        Log.v("LIFE ", "Pause");
        currentStateSaveToSharedPref();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v("LIFE ", "Stop");
        currentStateSaveToSharedPref();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("LIFE ", "Destroyed");
        currentStateSaveToSharedPref();
    }


    public void expensesRefresh() {
        // Fill list containing expenses.
        expensesListData.clear();
        DBList.expenses(key1OfActiveChallenge, expensesListAdapter, expensesListData);
    }

    /*
     * Callback for Fragment: Spend Money.
     * Database holding all expenses is updated from here....
     */
    @Override
    public void expensesGetDataFromFragment(String dialogText, String spendFor, String buttonPressed) {

        if (buttonPressed.equals("OK") && !dialogText.isEmpty()) {
            // Update DB....
            float money = Float.valueOf(dialogText);
            DBInsertInto.moneySpend(key1OfActiveChallenge, money, spendFor);
            Log.v("SUM", " Insert:" + money);

            currentStateSaveToSharedPref();

            challengeRefresh();
            expensesRefresh();
        }
    }

    /*
     * Callback, item inside expenses list was pressed....
     */

    public void expensesListItemInsideWasTouched(int position, int resourcheId) {
        String nameOfButtonPressed;
        nameOfButtonPressed = getResources().getResourceEntryName(resourcheId);
    }

    /*
     * Updates the waterfall chart showing the daily expenses
     * relative to each other.
     */

    // toDo modify Tooltip. Only absolute value needs to be displayed (tells the user how much he is below or over budget...
    private void expensesUpdateWaterfallChart() {

        waterfallData.clear();
        waterfallData = DBGetExpenses.forEachDayChallengeWasRunning(key1OfActiveChallenge, goal);
        //waterfall.data(waterfallData);
        //waterfallView.invalidate();
    }

    /*
     * Starts a new day.
     */
    public void dayStartNew() {
        dateOfLastEdit = currentDate;
        sumOfExpenses = 0;
        challengeDaysRunning++;

        expensesRefresh();
        carry = moneyLeft;

        currentStateSaveToSharedPref();
        expensesUpdateWaterfallChart();
    }

    /*
     * This will be called by the Android System, before the Activity is
     * destroyed by the System itself.....
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        currentStateSaveToSharedPref();
    }

    /*
     * This will be called by the Android System, after
     * the Activity was destroyed by the system and restarted.
     */
    @Override
    public void onRestoreInstanceState(Bundle outState) {
        super.onRestoreInstanceState(outState);
        currentStateRestoreFromSharedPref();
    }


    /*
     * Save current state to sharedPreferences.
     */
    private void currentStateSaveToSharedPref() {
        sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("nameOfActiveChallenge", nameOfCurrentChallenge);
        editor.putInt("key1OfActiveChallenge", key1OfActiveChallenge);
        editor.putString("dateOfLastEdit", dateOfLastEdit);
        editor.putFloat("goal", goal);
        editor.putFloat("carry", carry);
        editor.putInt("challengeDaysRunning", challengeDaysRunning);
        editor.commit();
    }

    /*
     * Restore from shared pref's..
     */
    private void currentStateRestoreFromSharedPref() {
        sharedPreferences = getPreferences(MODE_PRIVATE);
        int key1DeffaultValue = -1;
        key1OfActiveChallenge = sharedPreferences.getInt("key1OfActiveChallenge", key1DeffaultValue);
        nameOfCurrentChallenge = sharedPreferences.getString("nameOfActiveChallenge", nameOfCurrentChallenge);

        dateOfLastEdit = sharedPreferences.getString("dateOfLastEdit", dateOfLastEdit);

        goal = sharedPreferences.getFloat("goal", goal);
        carry = sharedPreferences.getFloat("carry", carry);

        challengeDaysRunning = sharedPreferences.getInt("challengeDaysRunning", challengeDaysRunning);
    }

    /*
     * If Back button was pressed
     */
    @Override
    public void onBackPressed() {
        timesBackPressed++;
        if (timesBackPressed > 1) {
            currentStateSaveToSharedPref();
            finish();
        } else
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.leave_warning), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
