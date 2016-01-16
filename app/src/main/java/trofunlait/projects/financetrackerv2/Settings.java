package trofunlait.projects.financetrackerv2;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by kith on 1/12/16.
 */
public class Settings extends Fragment {
    final String font1 = "fonts/handwriting.ttf";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings,container,false);

        String[] array_spinner = new String[] {
                "Weekly",
                "Monthly",
                "Yearly"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.select_dialog_item, array_spinner){

            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                Typeface externalFont=Typeface.createFromAsset(getActivity().getAssets(), font1);
                ((TextView) v).setTypeface(externalFont);
                ((TextView) v).setTextSize(35);
                return v;
            }

            public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
                View v =super.getDropDownView(position, convertView, parent);
                Typeface externalFont=Typeface.createFromAsset(getActivity().getAssets(), font1);
                ((TextView) v).setTypeface(externalFont);
                ((TextView) v).setTextSize(35);
                return v;
            }
        };

        final Spinner category = (Spinner) view.findViewById(R.id.spinner_categories);
        category.setAdapter(adapter);

        DBTools dbTools = new DBTools(getActivity());
        HashMap<String, String> settings = (HashMap<String, String>) dbTools.getSettings();
        //Spinner type = (Spinner) view.findViewById(R.id.spinner_categories);

        int spinnerPosition = adapter.getPosition(settings.get("date_type"));
        category.setSelection(spinnerPosition);

        final TextView button_income = (TextView) view.findViewById(R.id.btnSave);
        button_income.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DBTools dbTools = new DBTools(getActivity());

                HashMap<String, String> queryValuesMap =  new  HashMap<String, String>();

                //Spinner type1 = (Spinner) v.findViewById(R.id.spinner_categories);

                queryValuesMap.put("date_type", category.getSelectedItem().toString());

                dbTools.updateSettings(queryValuesMap);

                Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Settings saved. ", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        return view;
    }
}