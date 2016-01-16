package trofunlait.projects.financetrackerv2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by kith on 1/12/16.
 */
public class Spending extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.spending, container, false);

        DBTools dbTools = new DBTools(getActivity());

        HashMap<String, String> settings_db = (HashMap<String, String>) dbTools.getSettings();
        String settings = settings_db.get("date_type");

        TextView income = (TextView) view.findViewById(R.id.income_textview);
        TextView savings = (TextView) view.findViewById(R.id.savings_textview);
        TextView expense = (TextView) view.findViewById(R.id.expense_textview);
        TextView balance = (TextView) view.findViewById(R.id.balance_textview);

        Toast toast = Toast.makeText(getActivity().getApplicationContext(), settings, Toast.LENGTH_SHORT);
        toast.show();

        HashMap<String, String> income_db = dbTools.getSpending("Income", settings);
        HashMap<String, String> expense_db = dbTools.getSpending("Expense", settings);

        HashMap<String, String> savings1 = dbTools.getSavings("Income", settings);
        HashMap<String, String> savings2 = dbTools.getSavings("Expense", settings);

        income.setText(income_db.get("Income"));
        expense.setText(expense_db.get("Expense"));

        int save = Integer.parseInt(savings1.get("Income")) - Integer.parseInt(savings2.get("Expense"));
        savings.setText(Integer.toString(save));

        int inc = Integer.parseInt(income_db.get("Income"));
        //int sav = Integer.parseInt(savings_db.get("Savings"));
        int exp = Integer.parseInt(expense_db.get("Expense"));
        float bal = (inc + save) - exp;

        String str_bal = Float.toString(bal);
        balance.setText(str_bal);

        final TextView button_income = (TextView) view.findViewById(R.id.btn_income);
        button_income.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent new_transaction = new Intent(getActivity().getApplication(), new_transaction.class);
                new_transaction.putExtra("type", "Income");
                startActivity(new_transaction);
            }
        });

        TextView button_expense = (TextView) view.findViewById(R.id.btn_expense);
        button_expense.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent new_transaction = new Intent(getActivity().getApplication(), new_transaction.class);
                new_transaction.putExtra("type", "Expense");
                startActivity(new_transaction);
            }
        });

        TextView tv =(TextView) view.findViewById(R.id.page_title);
        tv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent dbmanager = new Intent(getActivity().getApplication(), AndroidDatabaseManager.class);
                startActivity(dbmanager);
                return false;
            }
        });

        return view;
    }
}