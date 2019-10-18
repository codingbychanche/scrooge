package berthold.scrooge;

/*
 * FragmentNewChallenge.java
 *
 * Created by Berthold Fritz
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * https://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * Last modified 10/7/19 1:37 PM
 */

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class FragmentNewChallenge extends DialogFragment {

    // Fragments UI components
    EditText    challengeName,challengeGoal;
   
    Button      okButton;
    Button      cancelButton;

    getDataFromNewChallengeFrag gf;

    public FragmentNewChallenge(){
        // Constructor must be empty....
    }

    public static FragmentNewChallenge newInstance (String titel){
        FragmentNewChallenge frag=new FragmentNewChallenge();
        Bundle args=new Bundle();
        args.putString("titel",titel);
        frag.setArguments(args);
        return frag;
    }

    // THE INTERFACE
    //
    // This is the interface used to pass data from the
    // this fragment to it's activity
    //
    public interface getDataFromNewChallengeFrag{
        void challengeGetDataFromFragment(String name, String goal, String buttonPressed);
    }

    // get interface Object...
    // You may use any method defined in the interface through the
    // object 'gf'

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        gf=(getDataFromNewChallengeFrag) activity;
    }

    // Inflate fragment layout
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_start_new_challenge,container);
    }

    // This fills the layout with data
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // These are the fragments UI components
        // Gets all objects (Buttons, EditText etc..) and set's them on
        // their listeners.....
        challengeName=(EditText) view.findViewById(R.id.challenge_name);
        challengeGoal=view.findViewById(R.id.challenge_new_goal);

        // When Ok Button is pressed, finish fragment and return text....
        okButton=(Button) view.findViewById(R.id.ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // When the button is pressed, pass entered text via the interface
                // to the activity which started this fragment.
                //
                // The caling activity must implement this fragments interface!

                gf.challengeGetDataFromFragment(challengeName.getText().toString(),challengeGoal.getText().toString(),"OK");
                dismiss();
            }
        });

        // Cancel button was pressed
        cancelButton=(Button)view.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gf.challengeGetDataFromFragment("Cancel Button was pressed.....","-","CANCEL");
                dismiss();
            }
        });
    }
}
