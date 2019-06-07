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
 * {@link Bar_Manuell.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Bar_Manuell#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Bar_Manuell extends Fragment {
    // TODOS: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODOS: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private AddOrderActivity adder;
    private double fix1;
    private double fix2;
    private double fix3;

    private OnFragmentInteractionListener mListener;

    public Bar_Manuell() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Bar_Manuell.
     */
    // TODOS: Rename and change types and number of parameters
    public static Bar_Manuell newInstance(String param1, String param2) {
        Bar_Manuell fragment = new Bar_Manuell();
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
        View view = inflater.inflate(R.layout.fragment_bar__manuell, container, false);
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

    public void upDrink1(View view){
        adder.editFix(1,0.1);
        refreshAll();
    }
    public void upDrink2(View view){
        adder.editFix(2,0.1);
        refreshAll();
    }
    public void upDrink3(View view){
        adder.editFix(3,0.1);
        refreshAll();
    }
    public void downDrink1(View view){
        adder.editFix(1,-0.1);
        refreshAll();
    }
    public void downDrink2(View view){
        adder.editFix(2,-0.1);
        refreshAll();
    }
    public void downDrink3(View view){
        adder.editFix(3,-0.1);
        refreshAll();
    }

    public void refreshAll(){
        View view = getView();
        loadFixes();
        TextView textView1 = (TextView) view.findViewById(R.id.textView2);
        textView1.setText(String.format("%s: %.2f€", getString(R.string.drink1), fix1));
        TextView textView2 = (TextView) view.findViewById(R.id.textView4);
        textView2.setText(String.format("%s: %.2f€", getString(R.string.drink2), fix2));
        TextView textView3 = (TextView) view.findViewById(R.id.textView5);
        textView3.setText(String.format("%s: %.2f€", getString(R.string.drink3), fix3));
    }
    public void refreshAll(View view){
        loadFixes();
        TextView textView1 = (TextView) view.findViewById(R.id.textView2);
        textView1.setText(String.format("%s: %.2f€", getString(R.string.drink1), fix1));
        TextView textView2 = (TextView) view.findViewById(R.id.textView4);
        textView2.setText(String.format("%s: %.2f€", getString(R.string.drink2), fix2));
        TextView textView3 = (TextView) view.findViewById(R.id.textView5);
        textView3.setText(String.format("%s: %.2f€", getString(R.string.drink3), fix3));
    }
    public void loadFixes(){
        fix1 = adder.getFix(1);
        fix2 = adder.getFix(2);
        fix3 = adder.getFix(3);
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
}