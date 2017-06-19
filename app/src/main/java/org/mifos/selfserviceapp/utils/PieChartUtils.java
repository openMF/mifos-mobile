package org.mifos.selfserviceapp.utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;

import org.mifos.selfserviceapp.R;

import java.util.ArrayList;

/**
 * Created by dilpreet on 19/6/17.
 */

public class PieChartUtils {
    private ArrayList<PieEntry> yValues = new ArrayList<>();
    private ArrayList<Integer> colors = new ArrayList<Integer>();
    private Context context;

    public PieChartUtils(Context context, Legend legend) {
        this.context = context;
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
    }

    public void addDataSet(int count, String label, int color) {
        if (count > 0) {
            colors.add(color);
            yValues.add(new PieEntry(count, label));
        }
    }

    public PieData getPieData() {
        PieDataSet pieDataSet = new PieDataSet(yValues, "");
        pieDataSet.setColors(colors);
        pieDataSet.setValueTextColor(ContextCompat.getColor(context, R.color.white));

        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new DefaultValueFormatter(0));
        return pieData;
    }
}
