package pelz.konstantin.wolfofnymphenburg;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Bar_Verkauf.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Bar_Verkauf#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Bar_Verkauf extends Fragment{
    // TODOS: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODOS: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int count1 = 0;
    private int count2 = 0;
    private int count3 = 0;
    private double price1 = 2.0;
    private double price2 = 2.0;
    private double price3 = 1.0;
    private double fix1;
    private double fix2;
    private double fix3;
    private AddOrderActivity adder;

    private OnFragmentInteractionListener mListener;

    public Bar_Verkauf() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Bar_Verkauf.
     */
    // TODOS: Rename and change types and number of parameters
    public static Bar_Verkauf newInstance(String param1, String param2) {
        Bar_Verkauf fragment = new Bar_Verkauf();
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
        adder = new AddOrderActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bar__verkauf, container, false);
        refreshAll(view);
        return view;
    }

    // TODOS: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //mListener = (OnFragmentInteractionListener) context;
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODOS: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void plusDrink1(View view){
        count1 += 1;
        refreshDrink(1);
        refresh();
    }
    public void plusDrink2(View view){
        count2 += 1;
        refreshDrink(2);
        refresh();
    }
    public void plusDrink3(View view){
        count3 += 1;
        refreshDrink(3);
        refresh();
    }
    public void minusDrink1(View view){
        if (count1>0){
            count1 -= 1;
            refreshDrink(1);
            refresh();
        }
    }
    public void minusDrink2(View view){
        if (count2>0){
            count2 -= 1;
            refreshDrink(2);
            refresh();
        }
    }
    public void minusDrink3(View view){
        if (count3>0){
            count3 -= 1;
            refreshDrink(3);
            refresh();
        }
    }

    public void buy(View view){
        if (count1>0){
            adder.addOrder(1,price1+fix1,count1);
        }
        if (count2>0){
            adder.addOrder(2,price2+fix2,count2);
        }
        if (count3>0){
            adder.addOrder(3,price3+fix3,count3);
        }
        count1=0;
        count2=0;
        count3=0;
        refreshAll(getView());
        refresh();
    }

    public void refresh(){
        double result = (price1 + fix1)*count1+ (price2 + fix2)*count2+ (price3 + fix3)*count3;
        TextView textView = (TextView) getView().findViewById(R.id.total);
        textView.setText(String.format("Total: %.2f€", result));
    }

    public void refreshDrink(int number){
        switch (number){
            case 1:
                TextView textView1 = (TextView) getView().findViewById(R.id.textView2);
                textView1.setText(String.format("%s - %d\n %.2f€", getString(R.string.drink1), count1, (price1+fix1)));
                break;
            case 2:
                TextView textView2 = (TextView) getView().findViewById(R.id.textView4);
                textView2.setText(String.format("%s - %d\n %.2f€", getString(R.string.drink2), count2, (price2+fix2)));
                break;
            case 3:
                TextView textView3 = (TextView) getView().findViewById(R.id.textView5);
                textView3.setText(String.format("%s - %d\n %.2f€", getString(R.string.drink3), count3, (price3+fix3)));
                break;
        }
    }

    public void refreshAll(){
        View view = getView();
        loadPrices();
        TextView textView1 = (TextView) view.findViewById(R.id.textView2);
        textView1.setText(String.format("%s - %d\n %.2f€", getString(R.string.drink1), count1, (price1+fix1)));
        TextView textView2 = (TextView) view.findViewById(R.id.textView4);
        textView2.setText(String.format("%s - %d\n %.2f€", getString(R.string.drink2), count2, (price2+fix2)));
        TextView textView3 = (TextView) view.findViewById(R.id.textView5);
        textView3.setText(String.format("%s - %d\n %.2f€", getString(R.string.drink3), count3, (price3+fix3)));

        double result = (price1 + fix1)*count1+ (price2 + fix2)*count2+ (price3 + fix3)*count3;
        TextView textView = (TextView) view.findViewById(R.id.total);
        textView.setText(String.format("Total: %.2f€", result));
    }

    public void refreshAll(View view){
        loadPrices();
        TextView textView1 = (TextView) view.findViewById(R.id.textView2);
        textView1.setText(String.format("%s - %d\n %.2f€", getString(R.string.drink1), count1, (price1+fix1)));
        TextView textView2 = (TextView) view.findViewById(R.id.textView4);
        textView2.setText(String.format("%s - %d\n %.2f€", getString(R.string.drink2), count2, (price2+fix2)));
        TextView textView3 = (TextView) view.findViewById(R.id.textView5);
        textView3.setText(String.format("%s - %d\n %.2f€", getString(R.string.drink3), count3, (price3+fix3)));

        double result = (price1 + fix1)*count1+ (price2 + fix2)*count2+ (price3 + fix3)*count3;
        TextView textView = (TextView) view.findViewById(R.id.total);
        textView.setText(String.format("Total: %.2f€", result));
    }

    public void loadPrices(){
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
    }

}
