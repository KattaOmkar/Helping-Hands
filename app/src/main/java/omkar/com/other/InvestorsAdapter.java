package omkar.com.other;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import omkar.com.helpinghands.R;
import omkar.com.models.Lender;

public class InvestorsAdapter extends ArrayAdapter<Lender> implements View.OnClickListener {

    String TAG = "InvestorsAdapter";
    Context context;
    private FragmentManager manager;
    private TextView investor_promise, investor_returns;
    private Button received, refunded, info;

    public InvestorsAdapter(FragmentManager manager, Context context, List<Lender> lenders) {
        super(context, 0, lenders);
        this.TAG = TAG;
        this.context = context;
        this.manager = manager;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Toast.makeText(context, "******Loading-Investors******", Toast.LENGTH_SHORT).show();
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_investor, parent, false);
        }
        final Lender lender = getItem(position);
        Log.d(TAG, "---Investor Adapter----");
        Log.d(TAG, "lender is null--" + (lender == null));
        investor_promise = (TextView) convertView.findViewById(R.id.investor_promise);
        investor_returns = (TextView) convertView.findViewById(R.id.investor_returns);
        ;
        received = (Button) convertView.findViewById(R.id.received);
        refunded = (Button) convertView.findViewById(R.id.refunded);
        info = (Button) convertView.findViewById(R.id.info);

        investor_promise.setText(lender.getUserName() + "has promised to lend: " + String.valueOf(lender.getAmount_Lent()));
        investor_returns.setText("Returns to " + lender.getLender_Name() + " are " + String.valueOf(lender.getReturnAmount()));


        return convertView;
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick:--- ");
        int position = (Integer) view.getTag();
    }


}
