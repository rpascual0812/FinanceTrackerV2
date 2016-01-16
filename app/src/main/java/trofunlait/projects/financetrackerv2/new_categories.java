package trofunlait.projects.financetrackerv2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by kith on 1/6/16.
 */
public class new_categories extends Activity {
    DBTools dbTools = new DBTools(this);
    final String font1 = "fonts/handwriting.ttf";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.new_categories);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        width = (int) (width * 0.9);
        height = (int) (height * 0.6);

        getWindow().setLayout(width, height);

        final Spinner trans_type;
        String[] array_spinner = new String[] {
                "Income",
                "Expense"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.select_dialog_item, array_spinner) {

            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                Typeface externalFont=Typeface.createFromAsset(getAssets(), font1);
                ((TextView) v).setTypeface(externalFont);
                ((TextView) v).setTextSize(35);
                return v;
            }

            public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
                View v =super.getDropDownView(position, convertView, parent);
                Typeface externalFont=Typeface.createFromAsset(getAssets(), font1);
                ((TextView) v).setTypeface(externalFont);
                ((TextView) v).setTextSize(35);
                return v;
            }
        };

        trans_type = (Spinner) findViewById(R.id.spType);

        trans_type.setAdapter(adapter);

        final CustomTextView button = (CustomTextView) findViewById(R.id.btnCancel);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new_categories.this.finish();
            }
        });

        final CustomTextView save = (CustomTextView) findViewById(R.id.btnSave);
        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                HashMap<String, String> queryValuesMap =  new  HashMap<String, String>();

                Spinner type = (Spinner) findViewById(R.id.spType);
                EditText category = (EditText) findViewById(R.id.txtCategory);
                queryValuesMap.put("transactiontype", type.getSelectedItem().toString());
                queryValuesMap.put("category", category.getText().toString());

                dbTools.insertCategory(queryValuesMap);

                Toast toast = Toast.makeText(getApplicationContext(), "New " + type.getSelectedItem().toString() + "  " + category.getText().toString() + " has been saved. ", Toast.LENGTH_SHORT);
                toast.show();

                new_categories.this.finish();
            }
        });

    }
}
