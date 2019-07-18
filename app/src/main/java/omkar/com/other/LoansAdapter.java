package omkar.com.other;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import omkar.com.helpinghands.R;
import omkar.com.models.LoanGroup;

public class LoansAdapter extends ArrayAdapter<LoanGroup> implements View.OnClickListener {
    String TAG = "LoansAdapter";
    private TextView name, loanAmount, reason, loanTenure, date, loanInterest, phone, address;

    public LoansAdapter(Context context, List<LoanGroup> loans) {
        super(context, 0, loans);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Toast.makeText(getContext(), "INSIDE LOANS ADAPTER", Toast.LENGTH_SHORT).show();
        // Get the data item for this position
        Log.d(TAG, "---------------getView:-------------------- ");
        Log.d(TAG, "position---" + position);
        Log.d(TAG, "View is empty -" + (convertView == null));
        Log.d(TAG, "Parent is empty -" + (parent == null));


        LoanGroup loan = getItem(position);

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
        // Lookup view for data population


//         Populate the data into the template view using the data object
        name.setText(loan.getBorrowerName());
        loanAmount.setText(String.valueOf(loan.getAmount()));
        reason.setText(loan.getReason());
        date.setText(String.valueOf(loan.getDate()));
//        loanTenure.setText(loan.getTenureDays());
        loanInterest.setText(String.valueOf(loan.getInterest()));

        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick:--- ");
        int position = (Integer) view.getTag();
        Object object = getItem(position);
        LoanGroup item = (LoanGroup) object;

//        switch (view.getId())
//        {
//            case R.id.reason:
//                Toast.makeText(getContext(), dataModel.toString(), Toast.LENGTH_SHORT).show();
//                break;
//        }


//        Fragment fragment = new newLoanFragment();
//        Bundle b = new Bundle();
//        b.putString("jsonD",new Gson().toJson(item,LoanGroup.class));
//        fragment.setArguments(b);
//        FragmentTransaction fragmentTransaction = view.getParent()
//
//        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
//                android.R.anim.fade_out);
//        fragmentTransaction.replace(R.id.frame, fragment, "new_loans");
//        fragmentTransaction.commitAllowingStateLoss();


    }

}