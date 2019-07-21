package omkar.com.helpinghands.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.gson.Gson;

import omkar.com.FirestoreHelper.FireService;
import omkar.com.helpinghands.R;
import omkar.com.models.Lender;
import omkar.com.models.User;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link lenderInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link lenderInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class lenderInfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static String TAG = "lenderInfoFragment";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    User user;
    private TextView lender_name, lender_amount, lender_returns, lender_mobile_number, lender_address;
    public lenderInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment lenderInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static lenderInfoFragment newInstance(String param1, String param2) {
        lenderInfoFragment fragment = new lenderInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private FireService fireService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.fireService = new FireService().init();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lender_info, container, false);
        Bundle b = getArguments();
        Lender lender = new Gson().fromJson(b.getString("JSOND"), Lender.class);
        Log.d(TAG, "lenderInfoFragment: ");
        Log.d(TAG, "+" + (lender == null));
        lender_name = (TextView) view.findViewById(R.id.lender_name);
        lender_amount = (TextView) view.findViewById(R.id.lender_amount);
        lender_returns = (TextView) view.findViewById(R.id.lender_returns);
        lender_mobile_number = (TextView) view.findViewById(R.id.lender_mobile_number);
        lender_address = (TextView) view.findViewById(R.id.lender_address);
        lender_name.setText("Lender: " + lender.getUserName());
        lender_amount.setText("Amount lent: " + String.valueOf(lender.getAmount_Lent()));
        lender_returns.setText("Returns: " + String.valueOf(lender.getReturnAmount()));
        final Intent callIntent = new Intent(Intent.ACTION_CALL);

        this.fireService.getUsersRef().document(lender.getUserId()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        user = User.setDataFromDocument(task.getResult().getData());
                        if ((user != null) && (!user.getNumber().isEmpty())) {

                            lender_address.setText("Address: " + user.getAddress());
                            Log.i(TAG, "USR-phone " + user.getNumber());
                            lender_mobile_number.setText("Mobile Number: " + user.getNumber());
                            callIntent.setData(Uri.parse("tel:" + user.getNumber()));
                        } else {
                            lender_mobile_number.setText("Not Provided");
                            lender_address.setText("Not Provided");
                        }


                    }
                });
        lender_mobile_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(callIntent);
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
