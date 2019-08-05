package omkar.com.other;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import omkar.com.FirestoreHelper.FireService;
import omkar.com.helpinghands.R;
import omkar.com.helpinghands.fragments.lenderInfoFragment;
import omkar.com.models.Lender;
import omkar.com.models.LoanGroup;

public class InvestorsAdapter extends ArrayAdapter<Lender> implements View.OnClickListener {

    String TAG = "InvestorsAdapter";
    Context context;
    private FragmentManager manager;
    private TextView investor_promise, investor_returns;
    private Button received, refunded, info;

    String res = null;
    Task<Void> task = null;
    private FireService fs;
    private LoanGroup loanGroup;
    private FirebaseAuth mAuth;

    public InvestorsAdapter(FragmentManager manager, Context context, List<Lender> lenders, LoanGroup loanGroup) {
        super(context, 0, lenders);
        this.TAG = TAG;
        this.context = context;
        this.manager = manager;
        this.loanGroup = loanGroup;
        fs = new FireService().init();
        mAuth = FirebaseAuth.getInstance();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
//        Toast.makeText(context, "******Loading-Investors******" + position, Toast.LENGTH_SHORT).show();
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_investor, parent, false);
        }
        final Lender lender = getItem(position);
        Log.d(TAG, "---Investor Adapter----" + lender.getUserName());
        Log.d(TAG, "lender is null--" + (lender == null));
        investor_promise = (TextView) convertView.findViewById(R.id.investor_promise);
        investor_returns = (TextView) convertView.findViewById(R.id.investor_returns);
        ;
        info = (Button) convertView.findViewById(R.id.info);
        info.setVisibility(View.VISIBLE);

        investor_promise.setText(lender.getUserName() + " has promised to lend: " + String.valueOf(lender.getAmount_Lent()));
        investor_returns.setText("Returns to " + lender.getUserName() + " are " + String.valueOf(lender.getReturnAmount()));

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new lenderInfoFragment();
                Bundle b = new Bundle();
                b.putString("JSOND", new Gson().toJson(lender));
                fragment.setArguments(b);
                FragmentTransaction fragmentTransaction = manager.beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, "lender_info");
                fragmentTransaction.commitAllowingStateLoss();
            }
        });
        Log.d(TAG, "getView: lender.isAmount_lent() " + lender.isAmount_lent());
        Log.d(TAG, "getView: lender.isAmount_refunded() " + lender.isAmount_refunded());
        if (lender.isAmount_refunded()) {

            refunded = (Button) convertView.findViewById(R.id.refunded);
            refunded.setVisibility(View.VISIBLE);
        } else {
            refunded = (Button) convertView.findViewById(R.id.refunded);
            refunded.setVisibility(View.VISIBLE);
            refunded.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view11) {
                    Toast.makeText(getContext(), "CLICK", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder askPass = new AlertDialog.Builder(getContext());
                    final View view = LayoutInflater.from(getContext()).inflate(R.layout.activity_reauthenticate, null);
                    final EditText etComments = (EditText) view.findViewById(R.id.editTextReauth);
                    final Button login = (Button) view.findViewById(R.id.buttonReauth);

                    final EditText input = new EditText(getContext());
                    input.setSingleLine();

                    input.setHint("Enter your password");
                    final AlertDialog alertDialog = askPass.create();
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    input.setLayoutParams(lp);
                    alertDialog.setView(input);
                    alertDialog.setTitle("Reauthentication ");
                    alertDialog.setCancelable(true);

                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Submit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(getContext(), "ON BEFORE REAUTH", Toast.LENGTH_SHORT).show();
                            reauthenticateUser(mAuth.getCurrentUser().getEmail(), input.getText().toString())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {


                                            lender.setAmount_refunded(true);

                                            if (loanGroup != null) {
                                                List<Lender> lenders = loanGroup.getLenders();
                                                if (!lenders.isEmpty()) {
                                                    int i = lenders.indexOf(lender);
                                                    if (i >= 0) {
                                                        lenders.set(i, lender);
                                                    } else {
                                                        lenders.add(lender);
                                                    }
                                                }
                                                Map<String, Object> loanMap = LoanGroup.makeTOMap(loanGroup);
                                                Map<String, Object> lendersMap = new HashMap<>();

                                                for (Lender l : lenders) {
                                                    lendersMap.put(l.getUserId(), new Gson().toJson(l, Lender.class));
                                                }
                                                loanMap.put("lenders", lendersMap);
                                                final String pathTOLoan = loanGroup.getBorrowerID().concat("__").concat(String.valueOf(loanGroup.getDate()));
                                                fs.getLoansCollection().document(pathTOLoan)
                                                        .set(loanMap)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Toast.makeText(getContext(), "UPDATED--LOAN-", Toast.LENGTH_SHORT);
                                                            }

                                                        });

                                            }


                                        }
                                    });

                        }
                    });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.show();
                }
            });
        }


        if (lender.isAmount_lent()) {

            received = (Button) convertView.findViewById(R.id.received);
            
            received.setVisibility(View.VISIBLE);

        } else {
            received = (Button) convertView.findViewById(R.id.received);
            received.setVisibility(View.VISIBLE);
            received.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view1) {
                    Toast.makeText(getContext(), "CLICK", Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder askPass = new AlertDialog.Builder(getContext());
                    final View view = LayoutInflater.from(getContext()).inflate(R.layout.activity_reauthenticate, null);
                    final EditText etComments = (EditText) view.findViewById(R.id.editTextReauth);
                    final Button login = (Button) view.findViewById(R.id.buttonReauth);

                    final EditText input = new EditText(getContext());
                    input.setSingleLine();

                    input.setHint("Enter your password");
                    final AlertDialog alertDialog = askPass.create();
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    input.setLayoutParams(lp);
                    alertDialog.setView(input);
                    alertDialog.setTitle("Reauthentication ");
                    alertDialog.setCancelable(true);

                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Submit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(getContext(), "ON BEFORE REAUTH", Toast.LENGTH_SHORT).show();
                            reauthenticateUser(mAuth.getCurrentUser().getEmail(), input.getText().toString())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            lender.setAmount_lent(true);
                                            loanGroup.setAmountBorrowed(lender.getAmount_Lent());
                                            if (loanGroup != null) {
                                                List<Lender> lenders = loanGroup.getLenders();
                                                if (!lenders.isEmpty()) {
                                                    int i = lenders.indexOf(lender);
                                                    if (i >= 0) {
                                                        lenders.set(i, lender);
                                                    } else {
                                                        lenders.add(lender);
                                                    }
                                                }
                                                Map<String, Object> loanMap = LoanGroup.makeTOMap(loanGroup);
                                                Map<String, Object> lendersMap = new HashMap<>();

                                                for (Lender l : lenders) {
                                                    lendersMap.put(l.getUserId(), new Gson().toJson(l, Lender.class));
                                                }
                                                loanMap.put("lenders", lendersMap);
                                                final String pathTOLoan = loanGroup.getBorrowerID().concat("__").concat(String.valueOf(loanGroup.getDate()));
                                                fs.getLoansCollection().document(pathTOLoan)
                                                        .set(loanMap)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Toast.makeText(getContext(), "UPDATED--LOAN-", Toast.LENGTH_SHORT);
                                                            }

                                                        });

                                            }
                                        }
                                    });
                            alertDialog.dismiss();
                        }
                    });

                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.show();


                }
            });
        }


        return convertView;
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick:--- ");
        int position = (Integer) view.getTag();
    }


    private Task<Void> reauth() {
//        Promise

        Log.d(TAG, "reauth: INSIDE ");
        AlertDialog.Builder askPass = new AlertDialog.Builder(getContext());
        final View view = LayoutInflater.from(getContext()).inflate(R.layout.activity_reauthenticate, null);
        final EditText etComments = (EditText) view.findViewById(R.id.editTextReauth);
        final Button login = (Button) view.findViewById(R.id.buttonReauth);

//        askPass.setMessage("Enter Password:")
//                .setTitle("Reauthentication ")
//                .setCancelable(false)
//
//        ;
        final EditText input = new EditText(getContext());

        input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        input.setHint("Enter your password");
        final AlertDialog alertDialog = askPass.create();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        alertDialog.setTitle("Reauthentication ");
        alertDialog.setCancelable(true);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getContext(), "ON BEFORE REAUTH", Toast.LENGTH_SHORT).show();
                task = reauthenticateUser(mAuth.getCurrentUser().getEmail(), input.getText().toString());
                alertDialog.dismiss();
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
        if (task.isComplete()) {
            return task;
        } else {
            return null;
        }

    }

    private Task<Void> reauthenticateUser(String email, String password) {
        Log.d(TAG, "reauthenticateUser: email " + email + " - pass " + password);
        final ProgressDialog loadingBar = new ProgressDialog(getContext());

        loadingBar.setTitle("Login");
        loadingBar.setMessage("Please wait, while we are allowing you to login into your Account...");
        loadingBar.setCanceledOnTouchOutside(true);
        loadingBar.show();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        AuthCredential authCredential = EmailAuthProvider.getCredential(email, password);
        Task<Void> task = firebaseUser.reauthenticate(authCredential);
        return task;

//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        loadingBar.hide();
//                        if (task.isSuccessful()) {
//                            Log.d(TAG, "onComplete: SUCCESS RE AUTH");
//                            Toast.makeText(getContext(), "SUCCESS RE AUTH", Toast.LENGTH_SHORT).show();
//                            res = new String("S");
//
//
//                        } else {
//                            Log.d(TAG, "RE AUTH FAILED ");
//                        }
//                    }
//                });
//        return res;
    }
}
