package omkar.com.helpinghands.fragments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import omkar.com.FirestoreHelper.FireService;
import omkar.com.helpinghands.R;
import omkar.com.helpinghands.fragments.dummy.DummyContent.DummyItem;
import omkar.com.helpinghands.fragments.myBorrowsFragment.OnListFragmentInteractionListener;
import omkar.com.models.Lender;
import omkar.com.models.LoanGroup;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyLoangroupRecyclerViewAdapter extends RecyclerView.Adapter<MyLoangroupRecyclerViewAdapter.ViewHolder> {

    private final List<DummyItem> mValues;
    private final List<LoanGroup> itemsLoan = new ArrayList<>();
    private final OnListFragmentInteractionListener mListener;
    String TAG = "MyLoangroupRecyclerViewAdapter";
    int type;
    private FireService fs;
    private FirebaseAuth mAuth;

    public MyLoangroupRecyclerViewAdapter(int type, List<DummyItem> items, OnListFragmentInteractionListener listener, Context context) {
        Log.d(TAG, "MyLoangroupRecyclerViewAdapter: ");
        Log.d(TAG, "SIZE: " + items.size());
        mValues = items;
        mListener = listener;
        fs = new FireService().init();
        mAuth = FirebaseAuth.getInstance();
        JodaTimeAndroid.init(context);
        this.type = type;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_myborrows, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        Log.d(TAG, holder.mItem.id);
        Log.d(TAG, holder.mItem.content);
        Log.d(TAG, holder.mItem.details);
        fs.getLoansCollection().document(holder.mItem.content).get().
                addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        LoanGroup l = LoanGroup.makeFromMap(task.getResult().getData());
                        Log.d("+++++++++++++++++++", "+++++++++++++++++++");
                        Log.d("+++++++++++++++++++", "" + task.getResult().exists());
//                        Log.d("+++++++++++++++++++", task.getResult().getData().toString());
//                        Log.d("+++++++++++++++++++", task.getResult().getData().toString());
                        SimpleDateFormat sf = new SimpleDateFormat("dd.MM.yyyy");
                        List<Lender> lenders = l.getLenders();
                        for (Lender lMan : lenders) {
                            Log.d(TAG, lMan.getUserId() + " = " + mAuth.getCurrentUser().getEmail());
                            if ((type == 0) || lMan.getUserId().equals(mAuth.getCurrentUser().getEmail())) {
                                if (type == 0) {
                                    holder.amt_return.setText(String.valueOf(l.getAmountBorrowed()));
                                } else {
                                    holder.amt_return.setText(String.valueOf(lMan.getReturnAmount()));
                                }


                                holder.loan_date.setText(sf.format(new Date(l.getDate())));

                                holder.loan_amt.setText(String.valueOf(l.getAmount()));

                                DateTime dateTime = new DateTime(l.getDate());
                                dateTime.plusMonths(2);

//                                holder.due_date.setText(sf.format(dateTime.toDate()));
                                holder.due_date.setText(String.valueOf(l.getBorrowerName()));
                            } else if (type == 1) {
                                holder.amt_return.setText("You Have not borrowed");
                            } else {
                                holder.amt_return.setText("You Have not lent");
                            }
                        }

                    }
                });


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView loan_date, loan_amt, amt_return, due_date;

        public DummyItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            loan_date = (TextView) view.findViewById(R.id.loan_date);
            loan_amt = (TextView) view.findViewById(R.id.loan_amt);
            amt_return = (TextView) view.findViewById(R.id.amt_return);
            due_date = (TextView) view.findViewById(R.id.due_date);


        }

        @Override
        public String toString() {
            return super.toString() + " '" + loan_date.getText() + "'";
        }
    }
}
