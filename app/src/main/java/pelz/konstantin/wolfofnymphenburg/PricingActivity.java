package pelz.konstantin.wolfofnymphenburg;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class PricingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pricing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rightNetwork) {
                    //Intent nextScreen = new Intent(PricingActivity.this, BarActivity.class);
                    Intent nextScreen = new Intent(PricingActivity.this, LoginActivity.class);
                    startActivity(nextScreen);
                }
            }
        });
        adder = new AddOrderActivity();
        checkNetworkConnection();
        refreshPrices();
        final TextView mTextField = (TextView) findViewById(R.id.timer);
        mTextField.setVisibility(View.INVISIBLE);
        final Handler handler = new Handler();
        final Handler connectionHandler = new Handler();
        final int delay = 3000; //milliseconds

        handler.postDelayed(new Runnable(){
            public void run(){
                refreshPrices();
                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    private boolean event = false;
    private String[] currentEvent;
    private boolean rightNetwork = false;
    private int remainingTime = 120000;
    private boolean isRunning = false;
    private boolean justRan = false;
    private AddOrderActivity adder;
    double price1;
    double price2;
    double price3;
    double fix1;
    double fix2;
    double fix3;

    public void goToStatistics(View view){
        if (rightNetwork) {
            Intent intent = new Intent(this, StatisticsActivity.class);
            intent.putExtra("drinkId", "1");
            startActivity(intent);
        }
    }

    public void goToTester(View view){
        if (rightNetwork) {
            Intent intent = new Intent(this, StatisticsActivity.class);
            intent.putExtra("drinkId", "2");
            startActivity(intent);
        }
    }

    public void goToAddOrder(View view){
        if (rightNetwork) {
            Intent intent = new Intent(this, StatisticsActivity.class);
            intent.putExtra("drinkId", "3");
            startActivity(intent);
        }
    }

    public void startTimer(int remainingTimeTimer){
        isRunning = true;
        final TextView mTextField = (TextView) findViewById(R.id.timer);
        mTextField.setVisibility(View.VISIBLE);
        CountDownTimer test = new CountDownTimer(remainingTimeTimer, 1000) {

            public void onTick(long millisUntilFinished) {
                int time=(int) millisUntilFinished;
                int minutes = time / (60 * 1000);
                int seconds = (time / 1000) % 60;
                String str = String.format("Das Event läuft noch: %d:%02d", minutes, seconds);
                mTextField.setText(str);
            }

            public void onFinish() {
                mTextField.setVisibility(View.INVISIBLE);
                isRunning = false;
                justRan=true;
                TextView textView = findViewById(R.id.eventText);
                textView.setText("");
            }
        }.start();
    }

    public void refreshPrices(){
        if (rightNetwork) {
            try {
                ArrayList<String[]> eventsDB = adder.getAllEvents();
                currentEvent = new String[5];
                for (String[] e:eventsDB){
                    if (e[4].equals("1")){
                        currentEvent=e;
                        break;
                    }
                }
                if (currentEvent[0]!=null){
                    event=true;
                } else {
                    event=false;
                }
                if (event && !isRunning && !justRan) {
                    startTimer(remainingTime);
                    TextView textView = findViewById(R.id.eventText);
                    textView.setText(currentEvent[2]);
                    textView.setSelected(true);

                    //TODO: Mark the buttons with a red or green arrow
                } else {
                    justRan=false;
                }

                int[] counts = new int[3];
                counts[0] = adder.getAmount(1);
                counts[1] = adder.getAmount(2);
                counts[2] = adder.getAmount(3);
                double[] prices = Tester.calculatePricesAnfang(counts);
                price1 = prices[0];
                price2 = prices[1];
                price3 = prices[2];
                fix1 = adder.getFix(1);
                fix2 = adder.getFix(2);
                fix3 = adder.getFix(3);
                TextView textView1 = (TextView) findViewById(R.id.drink1_btn);
                textView1.setText(String.format("%s\n %.2f€", getString(R.string.drink1), (price1 + fix1)));
                TextView textView2 = (TextView) findViewById(R.id.drink2_btn);
                textView2.setText(String.format("%s\n %.2f€", getString(R.string.drink2), (price2 + fix2)));
                TextView textView3 = (TextView) findViewById(R.id.drink3_btn);
                textView3.setText(String.format("%s\n %.2f€", getString(R.string.drink3), (price3 + fix3)));
                if (isRunning) {
                    String[] effects = currentEvent[3].split("_");
                    for (String e:effects) {
                        switch (Character.getNumericValue(e.charAt(0))){
                            case 1:
                                SpannableString text = new SpannableString(getString(R.string.drink1)+String.format("\n%.2f€ %.2f€",(price1 + fix1)-Double.valueOf(e.substring(1)),(price1 + fix1)));
                                int prefix = getString(R.string.drink1).length()+1;
                                text.setSpan(new ForegroundColorSpan(Color.RED), prefix+0, prefix+5, 0);
                                text.setSpan(new StrikethroughSpan(), prefix+0, prefix+5, 0);

                                text.setSpan(new RelativeSizeSpan(1.5f), prefix+6, prefix+11, 0);
                                text.setSpan(new ForegroundColorSpan(Color.GREEN), prefix+6, prefix+11, 0);

                                textView1.setText(text, TextView.BufferType.SPANNABLE);
                                break;
                            case 2:
                                SpannableString text2 = new SpannableString(getString(R.string.drink2)+String.format("\n%.2f€ %.2f€",(price2 + fix2)-Double.valueOf(e.substring(1)),(price2 + fix2)));
                                int prefix2 = getString(R.string.drink2).length()+1;
                                text2.setSpan(new ForegroundColorSpan(Color.RED), prefix2+0, prefix2+5, 0);
                                text2.setSpan(new StrikethroughSpan(), prefix2+0, prefix2+5, 0);

                                text2.setSpan(new RelativeSizeSpan(1.5f), prefix2+6, prefix2+11, 0);
                                text2.setSpan(new ForegroundColorSpan(Color.GREEN), prefix2+6, prefix2+11, 0);

                                textView2.setText(text2, TextView.BufferType.SPANNABLE);
                                break;
                            case 3:
                                SpannableString text3 = new SpannableString(getString(R.string.drink3)+String.format("\n%.2f€ %.2f€",(price3 + fix3)-Double.valueOf(e.substring(1)),(price3 + fix3)));
                                int prefix3 = getString(R.string.drink3).length()+1;
                                text3.setSpan(new ForegroundColorSpan(Color.RED), prefix3+0, prefix3+5, 0);
                                text3.setSpan(new StrikethroughSpan(), prefix3+0, prefix3+5, 0);

                                text3.setSpan(new RelativeSizeSpan(1.5f), prefix3+6, prefix3+11, 0);
                                text3.setSpan(new ForegroundColorSpan(Color.GREEN), prefix3+6, prefix3+11, 0);

                                textView3.setText(text3, TextView.BufferType.SPANNABLE);
                                break;

                        }
                    }
                }
            } catch (Exception ex){
                checkNetworkConnection();
            }
        } else {
            //Popup "Please connect to the WiFi \"WolfOfNymphenburg\"\nBitte in das WLAN \"WolfOfNymphenburg\" verbinden"
            String message = "Please connect to the WiFi \"WolfOfNymphenburg\"\n--------\n" +
                    "Bitte mit dem WLAN \"WolfOfNymphenburg\" verbinden";
            String title = "Connection issue";
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(message)
                    .setTitle(title)
                    .setPositiveButton("OK", null);
            AlertDialog dialog = builder.create();
            dialog.show();
            checkNetworkConnection();
        }

    }

    public void checkNetworkConnection(){
        String exitCode = adder.testConnection();
        if (exitCode.equals("true")){
            rightNetwork = true;
        } else {
            rightNetwork = false;
        }
    }

    //TODO: Tiefpreisalarm (Tiefster Preis des Tages), eventuelle einfach als Popup für Barteam
    // und die läuten die Glocke

}
