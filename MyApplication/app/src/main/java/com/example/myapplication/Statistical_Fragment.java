package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieDataSet.ValuePosition;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Statistical_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Statistical_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View v;
    private PieChart stat_count;
    private BarChart stat_computer, stat_phone, stat_access;
    private SQLite_Product sqLite_product;
    private Button save_stat_count_price, save_stat_computer, save_stat_phone, save_stat_access;
    private Locale localeEN;
    private NumberFormat en;
    public Statistical_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CallLog_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Statistical_Fragment newInstance(String param1, String param2) {
        Statistical_Fragment fragment = new Statistical_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_statistical, container, false);
        initView();
        localeEN = new Locale("en", "EN");
        en = NumberFormat.getInstance(localeEN);
        sqLite_product = new SQLite_Product(getContext());
        List<Product> list = sqLite_product.getAllProduct();
        addDataStatCount(stat_count, list);
        addDataStatType(stat_computer, list, "M??y t??nh");
        addDataStatType(stat_phone, list, "??i???n tho???i");
        addDataStatType(stat_access, list, "Ph??? ki???n");
        // export ???nh t???ng ti???n
        save_stat_count_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stat_count.saveToGallery("Count_Price.jpg", 100);
                Toast.makeText(getContext(), "Tr??ch xu???t th???ng k?? th??nh c??ng!", Toast.LENGTH_LONG).show();
            }
        });
        // export ???nh m??y t??nh
        save_stat_computer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stat_computer.saveToGallery("Count_Price_Computer.jpg", 100);
                Toast.makeText(getContext(), "Tr??ch xu???t th???ng k?? th??nh c??ng!", Toast.LENGTH_LONG).show();
            }
        });
        // export ???nh ??i???n tho???i
        save_stat_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stat_phone.saveToGallery("Count_Price_Phone.jpg", 100);
                Toast.makeText(getContext(), "Tr??ch xu???t th???ng k?? th??nh c??ng!", Toast.LENGTH_LONG).show();
            }
        });
        // export ???nh ph??? ki???n
        save_stat_access.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stat_access.saveToGallery("Count_Price_Accessories.jpg", 100);
                Toast.makeText(getContext(), "Tr??ch xu???t th???ng k?? th??nh c??ng!", Toast.LENGTH_LONG).show();
            }
        });
        return v;
    }

    private void initView() {
        stat_count = v.findViewById(R.id.stat_count);
        stat_computer = v.findViewById(R.id.stat_computer);
        stat_phone = v.findViewById(R.id.stat_phone);
        stat_access = v.findViewById(R.id.stat_access);
        save_stat_computer = v.findViewById(R.id.save_stat_computer);
        save_stat_phone = v.findViewById(R.id.save_stat_phone);
        save_stat_access = v.findViewById(R.id.save_stat_access);
        save_stat_count_price = v.findViewById(R.id.save_stat_count_price);
    }

    private void addDataStatType(BarChart barChart, List<Product> list, String type) {
        ArrayList<BarEntry> yEntrys = new ArrayList<>();
        long [] yData = new long[100];
        String [] xData = new String[100];
        int dem = 0;
        // l???c lo???i sp
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).getType().equals(type)){
                yData[dem] = (long) (list.get(i).getCount()*Double.parseDouble(list.get(i).getPrice()));
                System.out.println(yData[dem]);
                xData[dem] = list.get(i).getName();
                yEntrys.add(new BarEntry(dem, yData[dem]));
                dem++;
            }
        }
        // t??nh t???ng ti???n
        long count = 0;
        for(int i = 0; i < dem; i++)
            count += yData[i];
        // th??m d??? li???u v??o dataset
        BarDataSet dataSet = new BarDataSet(yEntrys, type + ": " + en.format(count) + " ?????ng");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(12);
        //
        dataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return en.format((long) entry.getY()) + "??";
            }
        });
        // th??m t??n sp xu???ng d?????i tr???c x
        XAxis bottom = barChart.getXAxis();
        bottom.setPosition(XAxis.XAxisPosition.BOTTOM);
        bottom.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xData[(int) value];
            }
        });
        bottom.setTextColor(Color.RED);
        bottom.setGranularityEnabled(true);
        // th??m d??? li???u v??o bi???u ?????
        BarData barData = new BarData(dataSet);

        barChart.getAxisLeft().setAxisMinimum(0);
        barChart.getAxisRight().setEnabled(false);
        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.animateY(2000);
        barChart.getDescription().setEnabled(false);
    }

    private void addDataStatCount(PieChart pieChart, List<Product> list) {
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        float [] yData = { 0, 0, 0 };
        // t??nh t???ng ti???n t???ng lo???i sp
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).getType().equals("M??y t??nh"))
                yData[0]+= list.get(i).getCount()*Double.parseDouble(list.get(i).getPrice());
            if(list.get(i).getType().equals("??i???n tho???i"))
                yData[1]+= list.get(i).getCount()*Double.parseDouble(list.get(i).getPrice());
            if(list.get(i).getType().equals("Ph??? ki???n"))
                yData[2]+= list.get(i).getCount()*Double.parseDouble(list.get(i).getPrice());
        }
        // t??nh ph???n tr??m t???ng lo???i
        int [] percent = {0, 0, 0};
        percent[0] = (int) ((yData[0]/(yData[0] + yData[1] + yData[2])) * 100);
        percent[1] = (int) ((yData[1]/(yData[0] + yData[1] + yData[2])) * 100);
        percent[2] = 100 - percent[0] - percent[1];
        String [] xData = { "M??y t??nh", "??i???n tho???i", "Ph??? ki???n" };
        // t??nh t???ng ti???n
        long count = (long) (yData[0] + yData[1] + yData[2]);
        for (int i = 0; i < yData.length; i++){
            yEntrys.add(new PieEntry(yData[i], xData[i] + ": " + percent[i] + "%"));
        }
        // th??m d??? li???u v??o dataset
        PieDataSet pieDataSet=new PieDataSet(yEntrys, null);
        pieDataSet.setYValuePosition(ValuePosition.OUTSIDE_SLICE);
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);

        pieDataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return en.format((long) entry.getY()) + "??";
            }
        });
        // th??m d??? li???u v??o bi???u ?????
        PieData pieData = new PieData(pieDataSet);

        pieChart.setData(pieData);
        pieChart.setCenterText("T???ng ti???n: \n" + en.format(count) + "\n ?????ng");
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setHoleRadius(35f);
        pieChart.getDescription().setEnabled(false);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.animateY(2000);
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        // th??m ghi ch?? b??n tr??i ph??a tr??n
        Legend legend = pieChart.getLegend();
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_search_option, menu);
        MenuItem item = menu.findItem(R.id.mSearch);
        item.setVisible(false);
    }
}