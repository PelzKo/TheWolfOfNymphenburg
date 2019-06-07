package pelz.konstantin.wolfofnymphenburg;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Bar_Events.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Bar_Events#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Bar_Events extends Fragment implements AdapterView.OnItemSelectedListener{
    // TODOS: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private HashMap<String,Integer> events = new HashMap<>();
    private HashMap<String,String> plusMinusMap = new HashMap<>();
    private String[] eventsShort;
    private AddOrderActivity adder;
    private ArrayList<String[]> eventsDB;
    private int currentId;
    private boolean eventRunning = false;

    // TODOS: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Bar_Events() {
        // Required empty public constructor
        /*events.put("Matze, Shots +0.3€","Matze kommt, dadurch steigen alle Shots um 30 ct");
        events.put("Black Friday, Drinks -0.2€","ES IST BLACK FRIDAY!! Alle Getränke sind 20 ct billiger");
        events.put("Hopfendürre, Longdrinks +0.2€","Wegen einer Hopfendürre kaufen nun alle Leute Longdrinks. Die sind alle 20 ct teurer geworden");
        eventsShort = events.keySet().toArray(new String[events.size()]);*/
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Bar_Events.
     */
    // TODOS: Rename and change types and number of parameters
    public static Bar_Events newInstance(String param1, String param2) {
        Bar_Events fragment = new Bar_Events();
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
        eventsDB = adder.getAllEvents();
        eventsShort = new String[eventsDB.size()];
        int counter = 0;
        for (String[] e:eventsDB){
            eventsShort[counter]=e[1];
            events.put(e[1],counter);
            counter++;
        }
        plusMinusMap.put("+","-");
        plusMinusMap.put("-","+");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bar__events, container, false);
        Spinner spinner = view.findViewById(R.id.effectSpinner);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(getContext(),
                android.R.layout.simple_spinner_item, eventsShort);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        TextView textView = (TextView) getView().findViewById(R.id.effectText);
        currentId = events.get(text);
        textView.setText(eventsDB.get(currentId)[2]);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void applyEvent(View view){
        if (!eventRunning) {
            MyRunnable myRunnable = new MyRunnable();
            Thread t = new Thread(myRunnable);
            t.start();

        } else{
            Toast.makeText(getActivity(),
                    "Ein Event läuft schon", Toast.LENGTH_SHORT).show();
        }
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
        void onFragmentInteraction(Uri uri);
    }

    public class MyRunnable implements Runnable {

        public MyRunnable() {
        }

        public void run() {
            eventRunning = true;
            String[] effects = eventsDB.get(currentId)[3].split("_");
            int eventID = Integer.valueOf(eventsDB.get(currentId)[0]);
            adder.changeEventActivation(eventID,1);
            for (String e:effects) {
                adder.editFix(Character.getNumericValue(e.charAt(0)),Double.valueOf(e.substring(1)));
            }
            try {
                TimeUnit.MINUTES.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (String e:effects) {
                adder.editFix(Character.getNumericValue(e.charAt(0)),Double.valueOf(plusMinusMap.get(e.substring(1,2))+e.substring(2)));
            }
            adder.changeEventActivation(eventID,0);
            eventRunning = false;
        }
    }
}
