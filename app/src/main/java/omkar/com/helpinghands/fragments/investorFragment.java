package omkar.com.helpinghands.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
public class investorFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView borrower_name, loan_amount, reason, loan_date, loan_interest, loan_tenure, borrower_phone_num,
            amount_return, amount_divider;
    private EditText investor_amount;
    private Button button_invest;
    private OnFragmentInteractionListener mListener;

    public investorFragment() {
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

    private String TAG = "investorFragment";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        User investor =
        LoanGroup lg;
        Bundle b = getArguments();
        Log.d(TAG, "Bundle BBB");
        Log.d(TAG, "+" + (b.getString("JSOND")));


        View viewThis = inflater.inflate(R.layout.fragment_investor, container, false);
        if ((b == null) || ((b != null) && (b.getString("JSOND").isEmpty()))) {
            Log.d(TAG, "onCreateView: nulllll");
            return viewThis;
        }
        lg = new Gson().fromJson(b.getString("JSOND"), LoanGroup.class);
        borrower_name = viewThis.findViewById(R.id.borrower_name);
        borrower_name.setText("Borrower :" + lg.getBorrowerName());

        loan_amount = viewThis.findViewById(R.id.loan_amount);
        loan_amount.setText("Loan Amount :" + String.valueOf(lg.getAmount()));

        reason = viewThis.findViewById(R.id.reason);
        reason.setText(lg.getReason());

        loan_date = viewThis.findViewById(R.id.loan_date);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        loan_date.setText(df.format(new Date(lg.getDate())));

        loan_interest = viewThis.findViewById(R.id.loan_interest);
        loan_interest.setText(String.valueOf(lg.getInterest() + "%"));

        loan_tenure = viewThis.findViewById(R.id.loan_tenure);
        loan_tenure.setText("Loan Tenure: " + String.valueOf(lg.getTenureDays()) + " Days");

        borrower_phone_num = viewThis.findViewById(R.id.borrower_phone_num);
        borrower_phone_num.setText("Phone : " + lg.getPhone());

        amount_return = viewThis.findViewById(R.id.amount_return);

        if ((lg.getAmount() - lg.getAmountBorrowed() != Float.valueOf("0.0")) && (lg.getInterest() != Float.valueOf("0.0"))) {

        }

        Float amtRt = (lg.getAmount() - lg.getAmountBorrowed()) * lg.getInterest();
        amtRt = amtRt + (amtRt * (lg.getTenureDays() / Float.valueOf("365.0"))) / Float.valueOf("100.00");

        amount_return.setText("Amount to Return :" + String.valueOf(amtRt));

        amount_divider = viewThis.findViewById(R.id.amount_divider);
        investor_amount = viewThis.findViewById(R.id.investor_amount);
        button_invest = viewThis.findViewById(R.id.button_invest);

        return viewThis;
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
