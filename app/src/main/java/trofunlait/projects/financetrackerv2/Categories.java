package trofunlait.projects.financetrackerv2;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

/**
 * Created by kith on 1/12/16.
 */
public class Categories extends Fragment {
    DBTools dbTools;
    List<String> array_list;
    final String font1 = "fonts/handwriting.ttf";
    final String font2 = "fonts/tomshandwriting.ttf";
    ListView category_listview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.categories_main,container,false);

        dbTools = new DBTools(getActivity());
        final Spinner category;

        //set_list("Income");

        List<String> array_listview = dbTools.getCategories("Income");

        ArrayAdapter adapter2 = new ArrayAdapter(getContext(),
                android.R.layout.simple_list_item_1, array_listview);

        category_listview = (ListView) view.findViewById(R.id.listview_categories);
        category_listview.setAdapter(adapter2);

        String[] array_spinner = new String[] {
                "Income",
                "Expense"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.select_dialog_item, array_spinner) {

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

        category = (Spinner) view.findViewById(R.id.spinner_categories);
        category.setAdapter(adapter);

        final Spinner spinner_type = (Spinner) view.findViewById(R.id.spinner_categories);
        spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View view, int position, long id) {
                String text = spinner_type.getSelectedItem().toString();

                List<String> array_listview = dbTools.getCategories(text);

                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_list_item_1, array_listview){

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

                category_listview = (ListView) getView().findViewById(R.id.listview_categories);
                category_listview.setAdapter(adapter2);

//                dbTools = new DBTools(getActivity());
//                set_list(text);
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }

        });

        final Button button_addcategory = (Button) view.findViewById(R.id.btn_addcategory);
        button_addcategory.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplication(), new_categories.class);
                startActivity(intent);
            }
        });

        return view;
    }

//    private void set_list(String trans_type){
//
//        final ListView category_listview;
//
//        List<String> array_listview = dbTools.getCategories(trans_type);
//
//        ArrayAdapter adapter2 = new ArrayAdapter(getContext(),
//                android.R.layout.simple_list_item_1, array_listview);
//
//        category_listview = (ListView) getView().findViewById(R.id.listview_categories);
//        category_listview.setAdapter(adapter2);
//    }
}