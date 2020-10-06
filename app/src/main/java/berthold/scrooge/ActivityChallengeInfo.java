package berthold.scrooge;

/*
 * ActivityChallengeInfo.java
 *
 * Created by Berthold Fritz
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * https://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * Last modified 10/27/19 5:03 PM
 */

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.charts.Pie;
import com.anychart.core.ui.Tooltip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ActivityChallengeInfo extends AppCompatActivity {

    private int key1OfChallengeSelected;

    // Number format
    private static String floatNumberFormatPreset = "%.2f";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_info);

        Bundle extra=getIntent().getExtras();
        key1OfChallengeSelected=extra.getInt("key1OfChallengeSelected");

        // UI
        TextView nameOfChallengeView=(TextView)findViewById(R.id.challenge_name);
        String challengeName=DBGetChallenge.name(key1OfChallengeSelected);
        nameOfChallengeView.setText(challengeName);

        TextView challengeSpendTotalView=(TextView)findViewById(R.id.challenge_spend_total);
        float spendTotal=DBGetExpenses.totalSumOfExpenses(key1OfChallengeSelected);
        String spendTotalFormated=String.format(floatNumberFormatPreset, spendTotal);
        challengeSpendTotalView.setText(spendTotalFormated+ " €");

        // Listeners
        FloatingActionButton fab = findViewById(R.id.close_challenge_info);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();

        // DB
        List <DataEntry>sumPerProduct=new ArrayList<>();
        sumPerProduct=DBGetExpenses.sumByProduct(key1OfChallengeSelected);

        // Pie Chart
        Pie pie = AnyChart.pie3d();
        pie.background().fill("#94BBB7");
        pie.stroke();

        Tooltip tooltip=pie.tooltip();

        // ToDo Create String Resource!
        tooltip.format("Ausgegeben: {%value} €");

        pie.data(sumPerProduct);

        AnyChartView anyChartView = (AnyChartView) findViewById(R.id.any_chart_view);
        anyChartView.setChart(pie);
    }
}
