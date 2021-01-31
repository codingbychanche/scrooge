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

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class ActivityListAndEvalChallenges extends AppCompatActivity implements FragmentDialogYesNo.getDataFromFragment{

    // Challenges list
    private RecyclerView challengesListRecyclerView;
    private RecyclerView.Adapter challengesListViewAdapter;
    private RecyclerView.LayoutManager challengesListLayoutManager;
    private List<ChallengeData> challengesListData = new ArrayList<>();

    // Request code for confirm dialog
    private static final int DELETE_CHALLENGE=1;

    // This is used to pass the selected items list- pos to the callback method of @see FragmentYesNoDialog
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_and_eval_challenges);

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
        String nameOfButtonPressed;
        this.position=position;
        nameOfButtonPressed = getResources().getResourceEntryName(resourcheId);
        Log.v("NAME",nameOfButtonPressed);

        if (nameOfButtonPressed.equals("challenge_row_view_delete_challenge")) {
            String dialogText=getResources().getString(R.string.dialog_delete_challenges);
            String noButton=getResources().getString(R.string.dialog_no_button);
            String yesButton=getResources().getString(R.string.dialog_yes_button);
            showConfirmDialog(DELETE_CHALLENGE,FragmentDialogYesNo.SHOW_AS_YES_NO_DIALOG,dialogText,noButton,yesButton);
        }

        if (nameOfButtonPressed.equals("challenge_row_view_info_button")){
            Intent in = new Intent(getApplicationContext(), ActivityChallengeInfo.class);
            in.putExtra("key1OfChallengeSelected",challengesListData.get(position).getKey1());
            startActivity(in);
        }

        if (nameOfButtonPressed.equals("export_this_challenge")){
            Intent in = new Intent(this, ActivityExportData.class);
            in.putExtra("keyOfChallengeToExport",challengesListData.get(position).getKey1());
            startActivity(in);
        }
    }

    /*
     * Callback for delete confirm Dialog.
     */
    @Override
    public void getDialogInput(int reqCode,int data,String empty,String buttonPressed){
        int keyOfChallengeToDelete = challengesListData.get(position).getKey1();

        if(reqCode==DELETE_CHALLENGE){
            if (buttonPressed.equals(FragmentDialogYesNo.BUTTON_OK_PRESSED)){
                DBChallenge.delete(keyOfChallengeToDelete);
                challengesListData.clear();
                challengesListViewAdapter.notifyDataSetChanged();
                DBList.challenges(challengesListViewAdapter, challengesListData, "", "datestarted", "DESC");
            }
        }
    }

    /*
     * Show confirm at dialog.
     *
     * @see FragmentDialogYesNo
     */
    private void showConfirmDialog(int reqCode, int kindOfDialog, String dialogText, String confirmButton, String cancelButton) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentDialogYesNo confirm = FragmentDialogYesNo.newInstance(reqCode,0,0,null,dialogText,"OK","nein");
        confirm.show(fm, "fragment_dialog_yes_no");
    }

}
