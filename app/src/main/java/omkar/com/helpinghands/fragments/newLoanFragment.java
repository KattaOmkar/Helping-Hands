package omkar.com.helpinghands.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import omkar.com.FirestoreHelper.FireService;
import omkar.com.helpinghands.R;
import omkar.com.models.LoanGroup;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link newLoanFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link newLoanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class newLoanFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String TAG = "newLoanFragment";
    public LoanGroup loanGroup;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    boolean isValid = false;
    private FirebaseAuth mAuth;
    private EditText nameEdit, loanAmount, reason, loanTenure, loanInterest, phone, address;
    private OnFragmentInteractionListener mListener;
    private Button createLoanGroup;
    private FireService fireService;
    public newLoanFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment newLoanFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static newLoanFragment newInstance(String param1, String param2) {
        newLoanFragment fragment = new newLoanFragment();
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
        fireService = new FireService().init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        // Inflate the layout for this fragment
        Toast.makeText(getContext(), mAuth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
        View newLoanView = inflater.inflate(R.layout.fragment_new_loan, container, false);
        nameEdit = (EditText) newLoanView.findViewById(R.id.loan_borrower_name);
        loanAmount = (EditText) newLoanView.findViewById(R.id.borrower_req_amount);
        loanTenure = (EditText) newLoanView.findViewById(R.id.borrower_req_tenure);
        loanInterest = (EditText) newLoanView.findViewById(R.id.borrower_int);
        phone = (EditText) newLoanView.findViewById(R.id.borrower_phone_number);
        address = (EditText) newLoanView.findViewById(R.id.borrower_address);
        reason = (EditText) newLoanView.findViewById(R.id.borrower_reason);
        createLoanGroup = (Button) newLoanView.findViewById(R.id.create_loan_group);
        loanGroup = new LoanGroup();


        createLoanGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isValidData();
                if (isValid) {

                    loanGroup.setBorrowerID(mAuth.getCurrentUser().getEmail());

                    loanGroup.setBorrowerName(nameEdit.getText().toString());
                    loanGroup.setBorrowerAddress(address.getText().toString());
                    loanGroup.setPhone(phone.getText().toString());
                    loanGroup.setReason(reason.getText().toString());
                    loanGroup.setDate(System.currentTimeMillis());
                    loanGroup.setAmount(Float.parseFloat(loanAmount.getText().toString()));
                    loanGroup.setTenureMonths(Integer.parseInt(loanTenure.getText().toString()));
                    loanGroup.setInterest(Float.parseFloat(loanInterest.getText().toString()));
                    Task<Void> res = fireService.createNewLoan(loanGroup);
                    res.addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getContext(), "Successfully created loan", Toast.LENGTH_SHORT).show();
                            Fragment fragment = new loansFragment();
                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                                    android.R.anim.fade_out);
                            fragmentTransaction.replace(R.id.frame, fragment, "loans");
                            fragmentTransaction.commitAllowingStateLoss();
                        }
                    });

                } else {
                    Log.d(TAG, "INVALID INPUTS ");
                }

            }
        });

        return newLoanView;
    }

    private void isValidData() {
        EditText[] arr = new EditText[6];
        arr[0] = nameEdit;
        arr[1] = loanAmount;
        arr[2] = loanTenure;
        arr[3] = loanInterest;
        arr[4] = phone;
        arr[5] = address;

        for (EditText e : arr) {
            if ((e.getText() == null) || (e.getText().toString().isEmpty())) {
                isValid = false;
                Toast.makeText(getContext(), "please fill " + e.getId(), Toast.LENGTH_SHORT).show();
            } else {
                isValid = true;
            }
        }
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
