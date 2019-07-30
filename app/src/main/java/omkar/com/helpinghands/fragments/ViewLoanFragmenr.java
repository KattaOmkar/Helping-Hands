package omkar.com.helpinghands.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import omkar.com.helpinghands.R;
import omkar.com.models.LoanGroup;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link investorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link investorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewLoanFragmenr extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "ViewLoanFragmenr";
    private TextView name, loanAmount, reason, loanTenure, date, loanInterest, phone, address;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ViewLoanFragmenr() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment investorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static investorFragment newInstance(String param1, String param2) {
        investorFragment fragment = new investorFragment();
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_investor, container, false);
        name = (TextView) view.findViewById(R.id.borrower_name);
        loanAmount = (TextView) view.findViewById(R.id.loan_amount);
        reason = (TextView) view.findViewById(R.id.reason);
        date = (TextView) view.findViewById(R.id.loan_date);
        loanTenure = (TextView) view.findViewById(R.id.loan_tenure);
        loanInterest = (TextView) view.findViewById(R.id.loan_interest);
        String jsonD = getArguments().getString("jsonD");
        LoanGroup loanGroup = new Gson().fromJson(jsonD, LoanGroup.class);
        Log.d(TAG, "onCreateView: ViewLoan Fragment");
        Log.d(TAG, loanGroup.toString());
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
