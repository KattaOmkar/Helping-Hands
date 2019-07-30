package omkar.com.other;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import omkar.com.helpinghands.R;
import omkar.com.helpinghands.fragments.investorFragment;
import omkar.com.models.LoanGroup;

public class LoansAdapter extends ArrayAdapter<LoanGroup> implements View.OnClickListener {
    String TAG = "LoansAdapter";
    private TextView name, loanAmount, reason, loanTenure, date, loanInterest, phone, address;
    private FragmentManager manager;

    public LoansAdapter(FragmentManager manager, Context context, List<LoanGroup> loans) {
        super(context, 0, loans);
        this.manager = manager;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
//        Toast.makeText(getContext(), "INSIDE LOANS ADAPTER", Toast.LENGTH_SHORT).show();
        // Get the data item for this position
        Log.d(TAG, "---------------getView:-------------------- ");
        Log.d(TAG, "position---" + position);
        Log.d(TAG, "View is empty -" + (convertView == null));
        Log.d(TAG, "Parent is empty -" + (parent == null));


        final LoanGroup loan = getItem(position);

        Log.d(TAG, "-----------------------" + (loan == null));
//        Log.d(TAG, loan.getBorrowerId());
//        Log.d(TAG, loan.getBorrowerAddress());

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_loans, parent, false);
        }
        name = (TextView) convertView.findViewById(R.id.borrower_name);
        loanAmount = (TextView) convertView.findViewById(R.id.loan_amount);
        reason = (TextView) convertView.findViewById(R.id.reason);
        date = (TextView) convertView.findViewById(R.id.loan_date);
        loanTenure = (TextView) convertView.findViewById(R.id.loan_tenure);
        loanInterest = (TextView) convertView.findViewById(R.id.loan_interest);


//         Populate the data into the template view using the data object
        name.setText(loan.getBorrowerName());
        loanAmount.setText(String.valueOf(loan.getAmount()));
        reason.setText(loan.getReason());
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        date.setText(df.format(new Date(loan.getDate())));
        loanTenure.setText(String.valueOf(loan.getTenureMonths()));
        loanInterest.setText(String.valueOf(loan.getInterest()));
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: OF row .........................");
                Fragment fragment = new investorFragment();
                Bundle b = new Bundle();
                b.putString("JSOND", new Gson().toJson(loan, LoanGroup.class));
                fragment.setArguments(b);
                FragmentTransaction fragmentTransaction = manager.beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, "investor");
                fragmentTransaction.commitAllowingStateLoss();

            }
        });

        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick:--- ");
        int position = (Integer) view.getTag();
        Object object = getItem(position);
        LoanGroup item = (LoanGroup) object;



    }

}