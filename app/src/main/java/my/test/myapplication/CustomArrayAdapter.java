package my.test.myapplication;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by kith on 1/12/16.
 */
public class CustomArrayAdapter extends ArrayAdapter {
    Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/handwriting.ttf");

    public CustomArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public TextView getView(int position, View convertView, ViewGroup parent) {
        TextView v = (TextView) super.getView(position, convertView, parent);
        v.setTypeface(tf);
        return v;
    }

    public TextView getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView v = (TextView) super.getView(position, convertView, parent);
        v.setTypeface(tf);
        return v;
    }
}
