package pelz.konstantin.wolfofnymphenburg;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import pelz.konstantin.wolfofnymphenburg.helper.CheckNetworkStatus;
import pelz.konstantin.wolfofnymphenburg.helper.HttpJsonParser;

public class AddOrderActivity extends AppCompatActivity {
    private static final String KEY_SUCCESS = "success";
    private static final String KEY_DRINK = "drink";
    private static final String KEY_PRICE = "price";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_DRINKID = "drink_id";
    private static final String IP_ADRESS = "192.168.1.80";
    //private static final String IP_ADRESS = "10.0.10.48";
    private static final String BASE_URL = "http://"+IP_ADRESS+"/android_connect/";
    private static String STRING_EMPTY = "";
    private EditText drinkEditText;
    private EditText priceEditText;
    private EditText amountEditText;
    //private String drink;
    //private String price;
    //private String amount;
    private Button addButton;
    private int success;
    //private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);
        drinkEditText = (EditText) findViewById(R.id.txtDrinkAdd);
        priceEditText = (EditText) findViewById(R.id.txtPriceAdd);
        amountEditText = (EditText) findViewById(R.id.txtAmountAdd);
        addButton = (Button) findViewById(R.id.btnAdd);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                    addOrder();
                } else {
                    Toast.makeText(AddOrderActivity.this,
                            "Unable to connect to internet",
                            Toast.LENGTH_LONG).show();

                }

            }
        });

    }

    /**
     * Checks whether all files are filled. If so then calls AddMovieAsyncTask.
     * Otherwise displays Toast message informing one or more fields left empty
     */
    private void addOrder() {
        if (!STRING_EMPTY.equals(drinkEditText.getText().toString()) &&
                !STRING_EMPTY.equals(priceEditText.getText().toString()) &&
                !STRING_EMPTY.equals(amountEditText.getText().toString())) {

            //drink = drinkEditText.getText().toString();
            //price = priceEditText.getText().toString();
            //amount = amountEditText.getText().toString();
            new AddOrderAsyncTask(drinkEditText.getText().toString(),priceEditText.getText().toString(),amountEditText.getText().toString(),"createNew").execute();
        } else {
            Toast.makeText(AddOrderActivity.this,
                    "One or more fields left empty!",
                    Toast.LENGTH_LONG).show();

        }


    }

    public void addOrder(int drink, double price, int amount){
        //this.drink = Integer.toString(drink);
        //this.price = Double.toString(Math.round(price * 100) / 100.0);
        //this.amount = Integer.toString(amount);
        new AddOrderAsyncTask(drink,price,amount,"createNew").execute();
    }

    public int getAmount(int drinkId){
        AddOrderAsyncTask asyn = new AddOrderAsyncTask(drinkId,0,0,"getAmount");
        try {
            return Integer.valueOf(asyn.execute().get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return 999999999;
    }
    public double getFix(int drinkId){
        AddOrderAsyncTask asyn = new AddOrderAsyncTask(drinkId,0,0,"getFix");
        try {
            return Double.valueOf(asyn.execute().get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return 999999999;
    }
    public void editFix(int drinkId, double amount){
        new AddOrderAsyncTask(drinkId,amount,0,"editFix").execute();
    }
    public String testConnection(){
        AddOrderAsyncTask asyn = new AddOrderAsyncTask(0,0,0,"testConnection");
        try {
            return asyn.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return "999";
    }

    public ArrayList<String[]> getAllEvents(){
        AddOrderAsyncTask asyn = new AddOrderAsyncTask(0,0,0,"getAllEvents");
        try {
            String result = asyn.execute().get();
            ArrayList<String[]> resultList = asyn.getEvents();
            return resultList;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return new ArrayList<String[]>();
    }

    public void changeEventActivation(int id, int activation){
        new AddOrderAsyncTask(id,activation,"switchEventActivation").execute();
    }

    public ArrayList<String[]> getAllDrinkOrders(int drinkId){
        AddOrderAsyncTask asyn = new AddOrderAsyncTask(drinkId,0,0,"getAllDrinkOrders");
        try {
            String result = asyn.execute().get();
            ArrayList<String[]> resultList = asyn.getEvents();
            return resultList;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return new ArrayList<String[]>();
    }

    /**l
     * AsyncTask for adding a movie
     */
    private class AddOrderAsyncTask extends AsyncTask<String, String, String> {
        private String drink;
        private String price;
        private String amount;
        private String task;
        private String id;
        private String activation;
        private ArrayList<String[]> events = new ArrayList<>();

        public AddOrderAsyncTask(int drink, double price, int amount, String task){
            this.drink = Integer.toString(drink);
            this.price = Double.toString(Math.round(price * 10) / 10.00);
            this.amount = Integer.toString(amount);
            this.task = task;
        }


        public AddOrderAsyncTask(int id, int activation, String task){
            this.id = Integer.toString(id);
            this.activation = Integer.toString(activation);
            this.task = task;
        }

        public AddOrderAsyncTask(String drink, String price, String amount, String task){
            this.drink = drink;
            this.price = price;
            this.amount = amount;
            this.task = task;
        }

        public ArrayList<String[]> getEvents(){
            return events;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display proggress bar
            //pDialog = new ProgressDialog(AddOrderActivity.this);
            //pDialog.setMessage("Adding Order. Please wait...");
            //pDialog.setIndeterminate(false);
            //pDialog.setCancelable(false);
            //pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            //Populating request parameters
            if (task.equals("createNew")) {
                httpParams.put(KEY_DRINK, drink);
                httpParams.put(KEY_PRICE, price);
                httpParams.put(KEY_AMOUNT, amount);
                JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                        BASE_URL + "create_order.php","POST", httpParams);
                try {
                    success = jsonObject.getInt(KEY_SUCCESS);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else if (task.equals("getAmount")){
                httpParams.put(KEY_DRINKID, drink);
                JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                        BASE_URL + "get_drink_amount.php","GET", httpParams);
                try {
                    success = jsonObject.getInt(KEY_SUCCESS);
                    String result = jsonObject.getJSONObject("data").getString(KEY_AMOUNT);
                    if (result.equals("null")){
                        result = "0";
                    }
                    return result;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else if (task.equals("getFix")){
                httpParams.put(KEY_DRINKID, drink);
                JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                        BASE_URL + "get_fix.php","GET", httpParams);
                try {
                    success = jsonObject.getInt(KEY_SUCCESS);
                    String result = jsonObject.getJSONObject("data").getString("fix");
                    if (result.equals("null")){
                        result = "0";
                    }
                    return result;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else if (task.equals("editFix")){
                httpParams.put(KEY_DRINK, drink);
                httpParams.put(KEY_AMOUNT, price);
                JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                        BASE_URL + "edit_fix.php","POST", httpParams);
                try {
                    success = jsonObject.getInt(KEY_SUCCESS);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else if (task.equals("testConnection")){
                boolean reachable = false;
                try {
                    reachable = InetAddress.getByName(IP_ADRESS).isReachable(2000);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return Boolean.toString(reachable);
                //return "true";
            }
            else if (task.equals("switchEventActivation")){
                httpParams.put("id", id);
                httpParams.put("activation", activation);
                JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                        BASE_URL + "switch_event_status.php","POST", httpParams);
                try {
                    success = jsonObject.getInt(KEY_SUCCESS);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else if (task.equals("getAllEvents")){
                JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                        BASE_URL + "get_all_events.php","GET", httpParams);
                try {
                    success = jsonObject.getInt(KEY_SUCCESS);
                    for (int i=0;i<jsonObject.getJSONArray("data").length();i++) {
                        String[] resultArray = new String[5];
                        resultArray[0] = jsonObject.getJSONArray("data").getJSONObject(i).getString("id");
                        resultArray[1] = jsonObject.getJSONArray("data").getJSONObject(i).getString("short");
                        resultArray[2] = jsonObject.getJSONArray("data").getJSONObject(i).getString("text");
                        resultArray[3] = jsonObject.getJSONArray("data").getJSONObject(i).getString("effect");
                        resultArray[4] = jsonObject.getJSONArray("data").getJSONObject(i).getString("active");
                        events.add(resultArray);
                    }
                    return "done";
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else if (task.equals("getAllDrinkOrders")){
                httpParams.put(KEY_DRINKID, drink);
                JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                        BASE_URL + "get_all_drink_orders.php","GET", httpParams);
                try {
                    success = jsonObject.getInt(KEY_SUCCESS);
                    for (int i=0;i<jsonObject.getJSONArray("data").length();i++) {
                        String[] resultArray = new String[5];
                        resultArray[0] = jsonObject.getJSONArray("data").getJSONObject(i).getString("pid");
                        resultArray[1] = jsonObject.getJSONArray("data").getJSONObject(i).getString("drink");
                        resultArray[2] = jsonObject.getJSONArray("data").getJSONObject(i).getString("price");
                        resultArray[3] = jsonObject.getJSONArray("data").getJSONObject(i).getString("amount");
                        resultArray[4] = jsonObject.getJSONArray("data").getJSONObject(i).getString("created_at");
                        events.add(resultArray);
                    }
                    return "done";
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        protected void onPostExecute(String result) {
            /*pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    if (success == 1) {
                        //Display success message
                        Toast.makeText(AddOrderActivity.this,
                                "Order Added", Toast.LENGTH_LONG).show();
                        Intent i = getIntent();
                        //send result code 20 to notify about movie update
                        setResult(20, i);
                        //Finish ths activity and go back to listing activity
                        finish();

                    } else {
                        Toast.makeText(AddOrderActivity.this,
                                "Some error occurred while adding order",
                                Toast.LENGTH_LONG).show();

                    }
                }
            });*/
        }
    }
}