package berthold.scrooge;

/*
 * ExpensesListAdapter.java
 *
 * Created by Berthold Fritz
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * https://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * Last modified 10/6/19 10:02 PM
 */

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ExpensesListAdapter extends RecyclerView.Adapter<ExpensesListAdapter.MyViewHolder> {
    private List<ExpensesData> expensesList = new ArrayList<>();
    private ActivityMain main;

    // Ui
    private ImageButton thisExpenseWasSelected;

    // Number format
    private static String floatNumberFormatPreset = "%.2f";

    // Provide a suitable constructor (depends on the kind of dataset)
    public ExpensesListAdapter(List<ExpensesData> expensesList, ActivityMain main) {
        this.expensesList = expensesList;
        this.main = main;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mView;

        public MyViewHolder(View v) {
            super(v);
            Log.v("Constructor", "-");
            mView = v;
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ExpensesListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.expenses_row_view, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        Log.v("onCreateViewHolder", "-");
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        TextView dateOfExpense = (TextView) holder.mView.findViewById(R.id.date_of_expense);
        TextView amountOfMoneySpend = (TextView) holder.mView.findViewById(R.id.amount_of_money_spend);
        TextView moneySpendFor = (TextView) holder.mView.findViewById(R.id.money_spend_for);

        //thisExpenseWasSelected = (ImageButton) holder.mView.findViewById(R.id.info_about_challenge);

        // Show data
        String date = expensesList.get(position).getDate();
        Log.v("DATE ", date);
        dateOfExpense.setText(date);

        float amountSpend = expensesList.get(position).getAmountOfMoneySpend();
        String amountSpendFormated = String.format(floatNumberFormatPreset, amountSpend);
        amountOfMoneySpend.setText(amountSpendFormated + " €");

        String spendFor = expensesList.get(position).getMoneySpendFor();
        moneySpendFor.setText(spendFor);

        // Listen to click events
        // Delete row?
        /*
        thisExpenseWasSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main.expensesListItemInsideWasTouched(position, thisExpenseWasSelected.getId());
            }
        });
        */
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (expensesList != null)
            return expensesList.size();
        return 0;
    }
}