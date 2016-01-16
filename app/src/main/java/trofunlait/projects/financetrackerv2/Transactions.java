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
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kith on 1/12/16.
 */
public class Transactions extends Fragment {
    List<String> array_list;
    final String font1 = "fonts/handwriting.ttf";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.transactions_main,container,false);

        DBTools dbTools = new DBTools(getActivity());
        ListView category_listview;

        array_list = dbTools.getTransactions();

        List<String> new_transactions = new ArrayList<String>();

        for(int i=0;i<array_list.size();i++){
            String[] a = array_list.get(i).split("~");

            new_transactions.add(a[1].toString());
        }

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, new_transactions) {

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

        category_listview = (ListView) view.findViewById(R.id.listview_transactions);
        category_listview.setAdapter(adapter2);

        category_listview.setOnItemClickListener(onTransactionClickListener);

        return view;
    }

    private AdapterView.OnItemClickListener onTransactionClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
            String[] a = array_list.get((int)id).split("~");

            Intent edit_transaction = new Intent(getActivity().getApplication(), edit_transaction.class);
            edit_transaction.putExtra("id", a[0].toString());
            startActivity(edit_transaction);
        }
    };
}