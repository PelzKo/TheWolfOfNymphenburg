package pelz.konstantin.wolfofnymphenburg;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class StatisticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        adder = new AddOrderActivity();
        Bundle extras = getIntent().getExtras();
        drinkId = Integer.valueOf(extras.getString("drinkId"));
        TextView textView = (TextView) findViewById(R.id.preisentwicklung);
        switch (drinkId){
            case 1:
                textView.setText(String.format("Preisentwicklung von %s", getResources().getString(R.string.drink1)));
                break;
            case 2:
                textView.setText(String.format("Preisentwicklung von %s", getResources().getString(R.string.drink2)));
                break;
            case 3:
                textView.setText(String.format("Preisentwicklung von %s", getResources().getString(R.string.drink3)));
                break;
        }

        GraphView graph = (GraphView) findViewById(R.id.graph);
        initGraph(graph);
    }

    private double[] x;
    private double xMin=Double.POSITIVE_INFINITY;
    private double xMax=0;
    private double[] y;
    private AddOrderActivity adder;
    private int drinkId;
    private int hours;

    public DataPoint[] data(){
        ArrayList<String[]> pastPrices = adder.getAllDrinkOrders(drinkId);
        if (pastPrices.size()==0){
            if (drinkId==3){
                pastPrices.add(String.format("-1,%d,1.00,1,2019-06-05 21:30:00", drinkId).split(","));
            } else {
                pastPrices.add(String.format("-1,%d,2.00,1,2019-06-05 21:30:00", drinkId).split(","));
            }
        }
        /*ArrayList<String[]> pastPrices = new ArrayList<>();
        pastPrices.add("83,2,1.70,1,2019-06-02 03:38:39".split(","));
        pastPrices.add("82,2,1.60,1,2019-06-01 22:38:39".split(","));
        pastPrices.add("80,2,1.90,1,2019-06-01 21:00:39".split(","));
        pastPrices.add("81,2,2.20,1,2019-06-01 21:58:39".split(","));
        pastPrices.add("83,2,2.00,1,2019-06-01 23:38:39".split(","));
        pastPrices.add("83,2,2.20,1,2019-06-02 00:18:39".split(","));
        pastPrices.add("83,2,2.30,1,2019-06-02 00:20:39".split(","));
        pastPrices.add("83,2,2.40,1,2019-06-02 02:38:39".split(","));
        pastPrices.add("83,2,2.00,1,2019-06-02 03:20:39".split(","));
        pastPrices.add("83,2,1.40,1,2019-06-02 04:20:39".split(","));*/
        Collections.sort(pastPrices, new Comparator<String[]>() {
            @Override
            public int compare(String[] o1, String[] o2) {
                return o1[4].compareTo(o2[4]);
            }
        });



        x = new double[pastPrices.size()];
        y = new double[pastPrices.size()];
        int counter = 0;
        for (String[] e:pastPrices) {
            Date date;
            String dtStart = e[4];
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                date = format.parse(dtStart);
                double time = date.getTime();
                if (time<xMin){
                    xMin=time;
                }
                if (time>xMax){
                    xMax=time;
                }
                x[counter] = time;
                y[counter] = Double.valueOf(e[2]);
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
            counter++;
        }

        hours = (int) Math.floor((xMax-xMin)/3600000)+1;
        if (hours>5){ hours=5;}
        int n=x.length;     //to find out the no. of data-points
        DataPoint[] values = new DataPoint[n];     //creating an object of type DataPoint[] of size 'n'
        for(int i=0;i<n;i++){
            DataPoint v = new DataPoint(x[i],y[i]);
            values[i] = v;
        }
        return values;
    }
    public void initGraph(GraphView graph) {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(data());
        /*LineGraphSeries<DataPoint> series = new LineGraphSeries<>(
                new DataPoint[]{new DataPoint(2, 1),
                        new DataPoint(3, 5),
                        new DataPoint(4, 7)}
        );*/
        // styling grid/labels
        graph.getGridLabelRenderer().setHighlightZeroLines(false);
        graph.getGridLabelRenderer().setVerticalLabelsAlign(Paint.Align.LEFT);
        graph.getGridLabelRenderer().setTextSize(50);
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
        SimpleDateFormat xLabelFormat = new SimpleDateFormat("HH:mm");
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this, xLabelFormat));
        graph.getGridLabelRenderer().setHumanRounding(false);
        graph.getGridLabelRenderer().setNumHorizontalLabels(hours);
        graph.getGridLabelRenderer().setVerticalAxisTitle("Preis in Euro");
        graph.getGridLabelRenderer().setPadding(40);
        graph.getGridLabelRenderer().reloadStyles();

        graph.addSeries(series);

        graph.getViewport().setMinY(0.0);
        graph.getViewport().setMaxY(4.0);
        graph.getViewport().setYAxisBoundsManual(true);

        graph.getViewport().setMinX(xMin);
        graph.getViewport().setMaxX(xMax);
        graph.getViewport().setXAxisBoundsManual(true);

    }
}
