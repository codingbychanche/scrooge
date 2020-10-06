package berthold.scrooge;

/*
 * FragmentSpendMoney.java
 *
 * Created by Berthold Fritz
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * https://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * Last modified 3/28/18 9:19 PM
 */

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class FragmentSpendMoney extends DialogFragment {

    // Fragments UI components
    EditText moneySpend;
    EditText moneySpendFor;
    Button okButton;
    Button cancelButton;


    getDataFromSpendMoneyFrag gf;

    public FragmentSpendMoney() {
        // Constructor must be empty....
    }

    public static FragmentSpendMoney newInstance(String titel) {
        FragmentSpendMoney frag = new FragmentSpendMoney();
        Bundle args = new Bundle();
        args.putString("titel", titel);
        frag.setArguments(args);
        return frag;
    }

    // THE INTERFACE
    //
    // This is the interface used to pass data from the
    // this fragment to it's activity
    //
    public interface getDataFromSpendMoneyFrag {
        void expensesGetDataFromFragment(String dialogText, String moneySpendFor, String buttonPressed);
    }

    // get interface Object...
    // You may use any method defined in the interface through the
    // object 'gf'

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        gf = (getDataFromSpendMoneyFrag) activity;
    }

    // Inflate fragment layout
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_spend_money, container);
    }

    // This fills the layout with data
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // These are the fragments UI components
        // Gets all objects (Buttons, EditText etc..) and set's them on
        // their listeners.....
        moneySpend = (EditText) view.findViewById(R.id.name);
        moneySpendFor = (EditText) view.findViewById(R.id.spend_for);


        // Fill product list
        ListView itemListView = (ListView) view.findViewById(R.id.product_list);
        final String[] values = DBList.products();

        ArrayAdapter<String> itemListAdapter =
                new ArrayAdapter<String>(getActivity(), R.layout.simple_list_row, R.id.text1, values);
        itemListView.setAdapter(itemListAdapter);

        /*
         * @rem:RadioButtonGroup, selectable list of items...@@
         * Nice piece of code, showing how to handle a Radio- Button Group like a list of items
         * which can be selected. Name of the Item (text displayed) is returned as an value.
        // Inner Class contains callback method which will be executed, when
        // a checkbox was changed.
        CompoundButton.OnCheckedChangeListener myOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                // Insert associated text- string into editText....
                // @rem:Shows how one can get compoundButtons associated text- string@@

                // Funny: If ou do not check if b is true, string is from the last
                // selected button, but not from the actual
                if (b)
                    moneySpendFor.setText(compoundButton.getText());
            }
        };

        // This initializes the list of goods to choose from
        // Get all radio buttons
        // Name convention in XML: 'selcted_#' where '#' is the consecutive number of the checkbox
        int tag = 0; // Consecutive number of checkbox

        for (int i = 1; i <= 5; i++) { // # of checkboxes per area
            int id = getResources().getIdentifier("selected_" + i, "id", getActivity().getPackageName());

            RadioButton rb = (RadioButton) view.findViewById(id);
            // Set tag which acts as an unique id for this checkbox
            // Set this checkbox on listener....
            rb.setTag(++tag); // First checkbox=1....
            rb.setOnCheckedChangeListener(myOnCheckedChangeListener);
            Log.v("LIST ", "" + id);
        }
        */

        // When item inside list was pressed...
        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Log.v("FRAGMENT_ITEM", "" + position);
                moneySpendFor.setText(values[position]);
            }
        };
        itemListView.setOnItemClickListener(listener);

        // When Ok Button is pressed, finish fragment and return text....
        okButton = (Button) view.findViewById(R.id.ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // When the button is pressed, pass entered text via the interface
                // to the activity which started this fragment.
                //
                // The caling activity must implement this fragments interface!


                DBInsertInto.products(moneySpendFor.getText().toString());


                gf.expensesGetDataFromFragment(moneySpend.getText().toString(), moneySpendFor.getText().toString(), "OK");
                dismiss();
            }
        });

        // Cancel button was pressed
        cancelButton = (Button) view.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gf.expensesGetDataFromFragment("-", "-", "CANCEL");
                dismiss();
            }
        });
    }

    /**
     * Fills the list with data
     * <p>
     * todo Replace by database query!
     */
    private String[] getValues() {

        String[] values = new String[]{"Berthold",
                "Donald",
                "Fuck",
                "Another Fuck",
                "Shit",
                "Damit",
                "End"};
        return values;
    }
}
