package trofunlait.projects.financetrackerv2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kith on 1/12/16.
 */
public class Reports extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reports_main,container,false);

        DBTools dbTools = new DBTools(getActivity());

        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<String>();

        List<String> array_list = dbTools.getTransactionsGrouped();
        //1~Food - 100.0
        for(int i=0;i<array_list.size();i++){
            String[] a = array_list.get(i).split("~");

            labels.add(a[0].toString());
            entries.add(new BarEntry(Float.parseFloat(Float.toString(Float.parseFloat(a[1])) + "f"), i));
        }

        BarDataSet dataset = new BarDataSet(entries, "List of Transactions");

        BarChart chart = new BarChart(getContext());

        BarData data = new BarData(labels, dataset);
        chart.setData(data);

        dataset.setColors(ColorTemplate.COLORFUL_COLORS);

        chart.animateY(3000);
        return chart;
    }
}