package pelz.konstantin.wolfofnymphenburg;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Tester extends AppCompatActivity {

    public static double PREISDRINK1 = 2;
    public static double PREISDRINK2 = 2;
    public static double PREISDRINK3 = 1;

    public static double CAP1 = 1;
    public static double CAP2 = 1;
    public static double CAP3 = 0.4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tester);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void buttonTry (View view) {
        EditText editText = (EditText) findViewById(R.id.testAmmounts);
        String ammounts = editText.getText().toString();
        String[] ammountSplit = ammounts.split(",");
        if (ammountSplit.length==3) {
            try {
                int input[] = new int[3];
                input[0] = Integer.parseInt(ammountSplit[0]);
                input[1] = Integer.parseInt(ammountSplit[1]);
                input[2] = Integer.parseInt(ammountSplit[2]);
                double result[] = calculatePricesAnfang(input);
                String resultString = String.format("Drink1: %.2f€ | Drink2: %.2f€ | Drink3: %.2f€", result[0], result[1], result[2]);
                TextView textView = (TextView) findViewById(R.id.resultView);
                textView.setText(resultString);
            } catch (NumberFormatException e){
                Context context = getApplicationContext();
                CharSequence text = "Please only use whole numbers and the format 'N,N,N'!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }

        }
    }

    public static double[] calculatePricesAnfang(int[] drinkAmmounts){
        double gesamtzahl = drinkAmmounts[0]+drinkAmmounts[1]+drinkAmmounts[2];
        double niedriger = Math.floor(gesamtzahl/30);
        double fullRounds = Math.floor(niedriger/3);
        double rest = niedriger%3;

        double preise[] = new double[3];
        preise[0] = Tester.PREISDRINK1-fullRounds*0.1;
        preise[1] = Tester.PREISDRINK2-fullRounds*0.1;
        preise[2] = Tester.PREISDRINK3-fullRounds*0.1;

        for (int i=0;i<rest;i++){
            preise[i]-=0.1;
        }

        for (int drinks=0;drinks<3;drinks++){
            double ammountChanges = Math.floor(drinkAmmounts[drinks]/8);
            double changesOne = Math.ceil(ammountChanges/2);
            preise[drinks]+=ammountChanges*0.1;
            if (drinks==0){
                preise[1]-=changesOne*0.1;
                preise[2]-=(ammountChanges-changesOne)*0.1;
            }else if (drinks==1){
                preise[0]-=changesOne*0.1;
                preise[2]-=(ammountChanges-changesOne)*0.1;
            }else if (drinks==2){
                preise[0]-=changesOne*0.1;
                preise[1]-=(ammountChanges-changesOne)*0.1;
            }
        }

        if (preise[0]<Tester.CAP1) preise[0]=Tester.CAP1;
        if (preise[1]<Tester.CAP2) preise[1]=Tester.CAP2;
        if (preise[2]<Tester.CAP3) preise[2]=Tester.CAP3;

        return preise;
    }

    public double[] calculatePricesEnde(int[] drinkAmmounts){
        double gesamtzahl = drinkAmmounts[0]+drinkAmmounts[1]+drinkAmmounts[2];
        double niedriger = Math.floor(gesamtzahl/25);
        double fullRounds = Math.floor(niedriger/3);
        double rest = niedriger%3;

        double preise[] = new double[3];
        preise[0] = Tester.PREISDRINK1+fullRounds*0.1;
        preise[1] = Tester.PREISDRINK2+fullRounds*0.1;
        preise[2] = Tester.PREISDRINK3+fullRounds*0.1;

        for (int i=0;i<rest;i++){
            preise[i]+=0.1;
        }

        for (int drinks=0;drinks<3;drinks++){
            double ammountChanges = Math.floor(drinkAmmounts[drinks]/8);
            double changesOne = Math.ceil(ammountChanges/2);
            preise[drinks]+=ammountChanges*0.1;
            if (drinks==0){
                preise[1]-=changesOne*0.1;
                preise[2]-=(ammountChanges-changesOne)*0.1;
            }else if (drinks==1){
                preise[0]-=changesOne*0.1;
                preise[2]-=(ammountChanges-changesOne)*0.1;
            }else if (drinks==2){
                preise[0]-=changesOne*0.1;
                preise[1]-=(ammountChanges-changesOne)*0.1;
            }
        }

        if (preise[0]<Tester.CAP1) preise[0]=Tester.CAP1;
        if (preise[1]<Tester.CAP2) preise[1]=Tester.CAP2;
        if (preise[2]<Tester.CAP3) preise[2]=Tester.CAP3;

        return preise;
    }

}
