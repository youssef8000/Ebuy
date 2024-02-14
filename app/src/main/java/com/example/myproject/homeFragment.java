package com.example.myproject;

import android.graphics.Color;
import android.os.Bundle;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;


public class homeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        DatabaseHelper databaseHelper = new DatabaseHelper(requireContext());
        List<BestSellingProduct> bestSellingProducts = databaseHelper.getBestSellingProducts();

        BarChart barChart = view.findViewById(R.id.bestSellingChart);

        List<BarEntry> entries = new ArrayList<>();

        for (int i = 0; i < bestSellingProducts.size(); i++) {
            Product product=databaseHelper.getOneProduct(bestSellingProducts.get(i).getProductId());
            entries.add(new BarEntry(i, bestSellingProducts.get(i).getTotalQuantity(), product.getName()));
        }

        BarDataSet barDataSet = new BarDataSet(entries, "Best Selling Products");

        int[] colors = new int[]{
                Color.BLUE,
                Color.GREEN,
                Color.RED,
        };
        barDataSet.setColors(colors);
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
        barChart.getDescription().setEnabled(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new XAxisValueFormatter(bestSellingProducts));
        barChart.invalidate();


        PieChart pieChart = view.findViewById(R.id.bestSellingPieChart);

        List<PieEntry> entriess = new ArrayList<>();

        for (int i = 0; i < bestSellingProducts.size(); i++) {
            Product product = databaseHelper.getOneProduct(bestSellingProducts.get(i).getProductId());
            entriess.add(new PieEntry(bestSellingProducts.get(i).getTotalQuantity(), product.getName()));
        }

        PieDataSet pieDataSet = new PieDataSet(entriess, "Best Selling Products");


        pieDataSet.setColors(colors);

        PieData pieData = new PieData(pieDataSet);

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.invalidate();
        return view;
    }

    private class XAxisValueFormatter extends ValueFormatter {

        private final List<BestSellingProduct> bestSellingProducts;

        XAxisValueFormatter(List<BestSellingProduct> bestSellingProducts) {
            this.bestSellingProducts = bestSellingProducts;
        }

        @Override
        public String getFormattedValue(float value) {
            int index = (int) value;
            DatabaseHelper databaseHelper = new DatabaseHelper(requireContext());
            if (index >= 0 && index < bestSellingProducts.size()) {
                Product product= databaseHelper.getOneProduct(bestSellingProducts.get(index).getProductId());
                return product.getName();
            }
            return "";
        }
    }
}