package berthold.scrooge;

/*
 * ChallengeListAdapter.java
 *
 * Created by Berthold Fritz
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * https://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * Last modified 4/23/19 9:20 AM
 */

import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ChallengeListAdapter extends RecyclerView.Adapter<ChallengeListAdapter.MyViewHolder> {
    private List<ChallengeData> challengeList=new ArrayList<>();
    private ActivityListAndEvalChallenges listAndEvalChallenges;

    // Ui
    private ImageButton thisChallengeWasSelected;

    // Provide a suitable constructor (depends on the kind of dataset)
    public ChallengeListAdapter(List<ChallengeData> challengeList, ActivityListAndEvalChallenges listAndEvalChallenges) {
        this.challengeList=challengeList;
        this.listAndEvalChallenges=listAndEvalChallenges;
        Log.v("Adapter:","OK");
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mView;
        public MyViewHolder(View v) {
            super(v);
            Log.v("Constructor","-");
            mView= v;
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ChallengeListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.challenges_row_view, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        Log.v("onCreateViewHolder","-");
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        TextView challengeName=(TextView)holder.mView.findViewById(R.id.challenge_name);
        TextView dateStarted=(TextView) holder.mView.findViewById(R.id.date_started);
        TextView dateEndetView=(TextView)holder.mView.findViewById(R.id.date_ended);
        TextView goalView=(TextView)holder.mView.findViewById(R.id.goal);
        TextView endingBalanceView=(TextView)holder.mView.findViewById(R.id.ending_balance);

        thisChallengeWasSelected=(ImageButton) holder.mView.findViewById(R.id.info_about_challenge);

        // Put data into view
        String name=challengeList.get(position).getChallengeName();
        String date=challengeList.get(position).getDateStarted();
        String dateEnded=challengeList.get(position).getDateEnded();
        float goal=challengeList.get(position).getChallengeGoal();
        float endingBalance=challengeList.get(position).getEndingBalance();

        challengeName.setText(name);
        goalView.setText(String.valueOf(goal)+"€/ Tag");

        endingBalanceView.setText(String.valueOf(endingBalance)+"€");
        if (endingBalance<0)
            endingBalanceView.setTextColor(Color.RED);
        else
            endingBalanceView.setTextColor(Color.GREEN);

        String niceDate=UtilFormatTimeStamp.german(date,UtilFormatTimeStamp.WITH_TIME);
        dateStarted.setText(niceDate);

        niceDate=UtilFormatTimeStamp.german(dateEnded,UtilFormatTimeStamp.WITH_TIME);
        dateEndetView.setText(niceDate);
        // Listen to click events
        // Delete row?
        /*
        thisChallengeWasSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               listAndEvalChallenges.challengesListItemTouched(position, thisChallengeWasSelected.getId());
            }
        });
        /*
        includeThisLoadIntoCalculation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean includehisLoadIntoCalculation) {
                Log.v("Checked:"," Include checked for item "+includehisLoadIntoCalculation);
                loadList.get(position).setIncludeIntoCalculation(includehisLoadIntoCalculation);
                main.itemInsideLoadListWasPressed(position,includeThisLoadIntoCalculation.getId());
            }
        });
        */
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if(challengeList!=null)
            return challengeList.size();
        return 0;
    }
}