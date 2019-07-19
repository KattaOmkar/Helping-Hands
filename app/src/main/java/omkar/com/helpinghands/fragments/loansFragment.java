package omkar.com.helpinghands.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import omkar.com.FirestoreHelper.FireService;
import omkar.com.helpinghands.R;
import omkar.com.models.LoanGroup;
import omkar.com.other.LoansAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link loansFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link loansFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class loansFragment extends Fragment implements newLoanFragment.OnFragmentInteractionListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<LoanGroup> listOfLoans = new ArrayList<>();
    private FireService fs;
    private FloatingActionButton fab;
    private OnFragmentInteractionListener mListener;

    public loansFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment loansFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static loansFragment newInstance(String param1, String param2) {
        loansFragment fragment = new loansFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoanGroup h = new LoanGroup();
        h.setDate(System.currentTimeMillis());
        h.setBorrowerId("DEMO@GMAIL>COM");
        h.setReason("DEMO");
        h.setTenureDays(Integer.parseInt("10"));
        h.setInterest(Float.valueOf(10));
        h.setAmount(Float.valueOf(100));
        h.setPhone("987456321");
        h.setBorrowerAddress("ASD ASD ASD ASD");
        h.setBorrowerName("DEMO PERSON");
        listOfLoans.add(h);



        Toast.makeText(getContext(), "length is " + listOfLoans.size(), Toast.LENGTH_SHORT).show();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_loans, container, false);
        fab = (FloatingActionButton) view.findViewById(R.id.fab_newSpends);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new newLoanFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, "new_loans");
                fragmentTransaction.commitAllowingStateLoss();
            }
        });
        fs = new FireService().init();
        Task<QuerySnapshot> task = fs.getLoansCollection().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                QuerySnapshot qsnap = task.getResult();
                List<DocumentSnapshot> ds = qsnap.getDocuments();
                for (DocumentSnapshot data : ds) {
                    Map<String, Object> jsonD = data.getData();
                    Log.d("loansFrag", jsonD.toString());
                    final LoanGroup holder = LoanGroup.makeFromMap(jsonD);
                    listOfLoans.add(holder);
                    Log.d("loansFrag", "onSuccess: loanGroups count " + listOfLoans.size());
                }
                Toast.makeText(getContext(), "ONCREATEVIEW-Loans", Toast.LENGTH_SHORT).show();

                // Inflate the layout for this fragment

                Toast.makeText(getContext(), "listOfLoans.size() " + listOfLoans.size(), Toast.LENGTH_SHORT).show();
                ListView listView = (ListView) view.findViewById(R.id.loan_list);


                LoansAdapter adapter = new LoansAdapter(getFragmentManager(), getContext(), listOfLoans);
                listView.setAdapter(adapter);


            }
        });



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

    @Override
    public void onFragmentInteraction(Uri uri) {

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

//    private void addRealtimeUpdate() {
//        DocumentReference contactListener = fs.getLoansCollection().docu
//        contactListener.addSnapshotListener(new EventListener < DocumentSnapshot > () {
//            @Override
//            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
//                if (e != null) {
//                    Log.d("ERROR", e.getMessage());
//                    return;
//                }
//                if (documentSnapshot != null && documentSnapshot.exists()) {
//                    Toast.makeText(MainActivity.this, "Current data:" + documentSnapshot.getData(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
}
