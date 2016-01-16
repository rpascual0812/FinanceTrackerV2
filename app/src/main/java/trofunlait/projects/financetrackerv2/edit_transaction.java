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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kith on 1/6/16.
 */
public class edit_transaction extends Activity {
    DBTools dbTools = new DBTools(this);
    String trans_id;
    final String font1 = "fonts/handwriting.ttf";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit_transaction);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        width = (int) (width * 0.9);
        height = (int) (height * 0.8);

        getWindow().setLayout(width, height);

        trans_id = getIntent().getExtras().getString("id");

        List<String> array_list = dbTools.getEditTransaction(trans_id);

        String[] data = array_list.get(0).split("~");


        List<String> array_spinner = dbTools.getCategories(data[4]);

        ArrayAdapter adapter = new ArrayAdapter(this,
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

        Spinner spinnercategory = (Spinner) findViewById(R.id.spinner_categories);
        spinnercategory.setAdapter(adapter);

        EditText txtamount = (EditText) findViewById(R.id.txt_amount);
        Spinner spcategory = (Spinner) findViewById(R.id.spinner_categories);
        DatePicker datecreated = (DatePicker) findViewById(R.id.datePicker);

        String id = data[0];
        String category = data[1];
        String amount = data[2];
        String[] date = data[3].split("-");

        txtamount.setText(amount);

        int spinnerPosition = adapter.getPosition(category);
        spcategory.setSelection(spinnerPosition);
        datecreated.updateDate(Integer.parseInt(date[0]), Integer.parseInt(date[1]) + 1, Integer.parseInt(date[2]));


        final CustomTextView button_cancel = (CustomTextView) findViewById(R.id.btn_cancel);
        button_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                setResult(RESULT_OK, null);
                finish();
                //edit_transaction.this.finish();
            }
        });

        final CustomTextView button_update = (CustomTextView) findViewById(R.id.btn_savetransaction);
        button_update.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                EditText txtamount = (EditText) findViewById(R.id.txt_amount);
                Spinner spcategory = (Spinner) findViewById(R.id.spinner_categories);
                DatePicker datecreated = (DatePicker) findViewById(R.id.datePicker);

                HashMap<String, String> queryValuesMap =  new  HashMap<String, String>();

                queryValuesMap.put("id", trans_id);
                queryValuesMap.put("amount", txtamount.getText().toString());
                queryValuesMap.put("category", spcategory.getSelectedItem().toString());

                int y = datecreated.getYear();
                int m = datecreated.getMonth() + 1;
                String m2 = String.format("%2s", Integer.toString(m)).replace(' ', '0');

                int d = datecreated.getDayOfMonth();
                String d2 = String.format("%2s", Integer.toString(d)).replace(' ', '0');

                queryValuesMap.put("datecreated", Integer.toString(y) + "-" + m2 + "-" + d2);

                dbTools.updateTransaction(queryValuesMap);

                EditText amount = (EditText)findViewById(R.id.txt_amount);
                Toast toast;
                String cat = spcategory.getSelectedItem().toString();

                toast = Toast.makeText(getApplicationContext(), "Transaction saved.", Toast.LENGTH_SHORT);
                toast.show();


            }
        });
    }


}
