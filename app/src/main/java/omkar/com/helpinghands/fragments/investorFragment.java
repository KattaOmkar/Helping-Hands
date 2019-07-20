package omkar.com.helpinghands.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import omkar.com.FirestoreHelper.FireService;
import omkar.com.helpinghands.R;
import omkar.com.models.Lender;
import omkar.com.models.LoanGroup;
import omkar.com.models.User;
import omkar.com.other.InvestorsAdapter;

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
    List<Lender> lenderArrayList;
    private FireService fs;
    private FirebaseAuth mAuth;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView borrower_name, loan_amount, reason, loan_date, loan_interest, loan_tenure, borrower_phone_num,
            amount_return, amount_divider;
    private EditText investor_amount;
    private Button button_invest;
    private ListView loan_lenders;
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
        mAuth = FirebaseAuth.getInstance();
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
        fs = new FireService().init();

        Bundle b = getArguments();
        Log.d(TAG, "Bundle BBB");
        Log.d(TAG, "+" + (b.getString("JSOND")));


        final View viewThis = inflater.inflate(R.layout.fragment_investor, container, false);
        loan_lenders = (ListView) viewThis.findViewById(R.id.loan_lenders);


        if ((b == null) || ((b != null) && (b.getString("JSOND").isEmpty()))) {
            Log.d(TAG, "onCreateView: nulllll");
            return viewThis;
        }
        final LoanGroup lg = new Gson().fromJson(b.getString("JSOND"), LoanGroup.class);
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
        loan_tenure.setText("Loan Tenure: " + String.valueOf(lg.getTenureMonths()) + " Days");

        borrower_phone_num = viewThis.findViewById(R.id.borrower_phone_num);
        borrower_phone_num.setText("Phone : " + lg.getPhone());

        amount_return = viewThis.findViewById(R.id.amount_return);


        if ((lg.getAmount() - lg.getAmountBorrowed() != Float.valueOf("0.0")) && (lg.getInterest() != Float.valueOf("0.0"))) {

        }

        Float amtRt = lg.getAmount();
        amtRt = amtRt + (amtRt * lg.getInterest() * lg.getTenureMonths()) / ((Float.valueOf("12")) * Float.valueOf("100.00"));
        final Float amtRtCopy = amtRt;
        amount_return.setText("Amount to Return :" + String.valueOf(amtRt));


        if (lg.getLenders() != null) {


            lenderArrayList = lg.getLenders();
            InvestorsAdapter investorsAdapter = new InvestorsAdapter
                    (getFragmentManager(), getContext(), lenderArrayList);
            loan_lenders.setAdapter(investorsAdapter);

        } else {
            Log.d(TAG, "______________________LENDERS IS NULL");
        }


        amount_divider = viewThis.findViewById(R.id.amount_divider);
        amount_divider.setText(String.valueOf(lg.getAmount() - lg.getAmountBorrowed()));
        investor_amount = viewThis.findViewById(R.id.investor_amount);
        button_invest = viewThis.findViewById(R.id.button_invest);

        button_invest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ((investor_amount.getText() != null) && (!investor_amount.getText().toString().isEmpty())) {
                    final Float f = Float.valueOf(investor_amount.getText().toString());
                    if (f.equals(Float.valueOf("0.0"))) {
                        Toast.makeText(getContext(), "invalid", Toast.LENGTH_SHORT).show();
                    } else {

                        DocumentReference ds = fs.getUserDocumentByUserId(mAuth.getCurrentUser().getEmail());
                        Task<DocumentSnapshot> tGetUser = ds.get();
                        tGetUser.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> userD) {
                                User userHolder;
                                if ((userD.isComplete()) && (userD.getResult() != null)) {
                                    userHolder = User.setDataFromDocument(userD.getResult().getData());
                                    final Lender lender = new Lender();
                                    lender.setUserId(userHolder.email);
                                    lender.setUserName(userHolder.name);
                                    lender.setAmount_Lent(f);
                                    lender.setAmount_lent(true);
                                    lender.setAmount_refunded(false);
                                    lender.setReturnAmount(amtRtCopy);
                                    lenderArrayList.add(lender);
                                    final String pathTOLoan = lg.getBorrowerID().concat("__").concat(String.valueOf(lg.getDate()));
                                    Log.d(TAG, "///////////////////////");
                                    Log.d(TAG, pathTOLoan);
                                    fs.getLoansCollection().document(pathTOLoan).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> loanData) {
                                            LoanGroup loanGroupH;
                                            if ((loanData.isComplete()) && (loanData.getResult() != null)) {
                                                loanGroupH = LoanGroup.makeFromMap(loanData.getResult().getData());

                                                ArrayList<Lender> list = loanGroupH.getLenders();
                                                list.add(lender);
                                                loanGroupH.setAmountBorrowed(lender.getAmount_Lent());
                                                loanGroupH.setLenders(list);
                                                Gson json = new Gson();
                                                final CollectionReference myBorrows = fs.getMyBorrowings(loanGroupH.getBorrowerID());
                                                Map<String, String> mapOfPath = new HashMap<>();
                                                mapOfPath.put("pathTOLoan", pathTOLoan);
                                                myBorrows.add(mapOfPath);
                                                final CollectionReference myLendings = fs.getMyLendings(lender.getUserId());
                                                myLendings.add(mapOfPath);
                                                Map<String, Object> loanMap = LoanGroup.makeTOMap(loanGroupH);
                                                Map<String, Object> lendersMap = new HashMap<>();

                                                for (Lender l : lenderArrayList) {
                                                    lendersMap.put(l.getUserId(), new Gson().toJson(l, Lender.class));
                                                }
                                                loanMap.put("lenders", lendersMap);
                                                fs.getLoansCollection().document(pathTOLoan)
                                                        .set(loanMap)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Toast.makeText(getContext(), "UPDATED", Toast.LENGTH_SHORT);
                                                            }

                                                        });

                                            }
                                        }
                                    });

                                    Toast.makeText(getContext(), "username " + userHolder.email, Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }
                } else {
                    Toast.makeText(getContext(), "Fill Carefully", Toast.LENGTH_SHORT).show();
                }


            }
        });
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
