package berthold.scrooge;

/*
 * ActivityListAndEvalChallenges.java
 *
 * Created by Berthold Fritz
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * https://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * Last modified 10/16/19 8:57 AM
 */

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class ActivityListAndEvalChallenges extends AppCompatActivity {

    // Challenges list
    private RecyclerView challengesListRecyclerView;
    private RecyclerView.Adapter challengesListViewAdapter;
    private RecyclerView.LayoutManager challengesListLayoutManager;
    private List<ChallengeData> challengesListData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_and_eval_challenges);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Init recycler view's
        challengesListRecyclerView = (RecyclerView) findViewById(R.id.challenges_list);
        challengesListRecyclerView.setHasFixedSize(true);
        challengesListLayoutManager = new LinearLayoutManager(this);
        challengesListRecyclerView.setLayoutManager(challengesListLayoutManager);
        challengesListViewAdapter = new ChallengeListAdapter(challengesListData, this);
        challengesListRecyclerView.setAdapter(challengesListViewAdapter);

        // List Challenges
        DBList.challenges(challengesListViewAdapter, challengesListData, "", "datestarted", "DESC");

        // UI
        FloatingActionButton fab = findViewById(R.id.close_list_an_eval_challenges);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /*
     * Callback, item inside challenges list was pressed....
     */

    public void challengesListItemTouched(int position, int resourcheId) {

    }

}
