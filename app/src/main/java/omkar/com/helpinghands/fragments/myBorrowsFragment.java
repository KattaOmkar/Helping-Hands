package omkar.com.helpinghands.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import omkar.com.FirestoreHelper.FireService;
import omkar.com.helpinghands.R;
import omkar.com.helpinghands.fragments.dummy.DummyContent.DummyItem;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class myBorrowsFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static int Type;
    List<DummyItem> items = new ArrayList<>();
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public myBorrowsFragment() {

    }


    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static myBorrowsFragment newInstance(int columnCount) {
        myBorrowsFragment fragment = new myBorrowsFragment();
        Bundle args = new Bundle();
        Type = Integer.valueOf((args.getString("TYPE")));
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle b = getArguments();
        Type = Integer.valueOf((b.getString("TYPE")));
        Log.d("-*-*-*--*-", "onCreateView: type " + Type);
        View view = inflater.inflate(R.layout.fragment_myborrows_list, container, false);
        final FireService fs = new FireService().init();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            final RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            final List<String> loans = new ArrayList<>();
            if (Type == 0) {
                Log.d("MY BRO", "mAuth.getCurrentUser().getEmail() " + mAuth.getCurrentUser().getEmail());
                fs.getMyBorrowings(mAuth.getCurrentUser().getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        List<DocumentSnapshot> list = task.getResult().getDocuments();
                        if (!list.isEmpty()) {
                            for (DocumentSnapshot ds : list) {
                                Map<String, Object> map = ds.getData();
                                Log.d("MY BRO", map.get("pathTOLoan").toString());
                                loans.add(map.get("pathTOLoan").toString());

                                items.add(new DummyItem(ds.getId(), map.get("pathTOLoan").toString(), ""));
                            }

                            recyclerView.setAdapter(new MyLoangroupRecyclerViewAdapter(Type, items, mListener, getContext()));
                        } else {
                            Log.d("MY BRO", "LIST is empty ");
                        }

                    }
                });
            } else {
                Log.d("MY BRO", "mAuth.getCurrentUser().getEmail() " + mAuth.getCurrentUser().getEmail());
                fs.getMyLendings(mAuth.getCurrentUser().getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        List<DocumentSnapshot> list = task.getResult().getDocuments();
                        if (!list.isEmpty()) {
                            for (DocumentSnapshot ds : list) {
                                Map<String, Object> map = ds.getData();
                                Log.d("MY BRO", map.get("pathTOLoan").toString());
                                loans.add(map.get("pathTOLoan").toString());

                                items.add(new DummyItem(ds.getId(), map.get("pathTOLoan").toString(), ""));
                            }

                            recyclerView.setAdapter(new MyLoangroupRecyclerViewAdapter(Type, items, mListener, getContext()));
                        } else {
                            Log.d("MY BRO", "LIST is empty ");
                        }

                    }
                });
            }

//            for (String path: loans){
//                fs.getLoansCollection().document(path).get().
//                        addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        LoanGroup l = LoanGroup.makeFromMap(task.getResult().getData());
//                        items.add(new DummyItem(l.getBorrowerID(),String.valueOf(l.getAmount()),l.toString()));
//                    }
//                });
//            }
//            recyclerView.setAdapter(new MyLoangroupRecyclerViewAdapter(items, mListener));


        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyItem item);
    }
}
