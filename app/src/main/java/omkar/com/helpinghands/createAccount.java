package omkar.com.helpinghands;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class createAccount extends AppCompatActivity {
    public Button create_new_account, login;
    public EditText new_mail, new_pass, new_name, new_number, new_address;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        mAuth = FirebaseAuth.getInstance();
        create_new_account = (Button) findViewById(R.id.create_new_account);
        login = (Button) findViewById(R.id.having_account_already);
        new_mail = (EditText) findViewById(R.id.new_mail);
        new_pass = (EditText) findViewById(R.id.new_pass);
        new_name = (EditText) findViewById(R.id.new_name);
        new_number = (EditText) findViewById(R.id.new_number);
        new_address = (EditText) findViewById(R.id.new_address);

        create_new_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateNewAccount();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendUserToLoginActivity();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            SendUserToMainActivity();
        }
    }

    private void CreateNewAccount() {
        final String email = new_mail.getText().toString();
        String password = new_pass.getText().toString();
        String confirmPassword = new_pass.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please write your email...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "Please confirm your password...", Toast.LENGTH_SHORT).show();
        } else if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "your password do not match with your confirm password...", Toast.LENGTH_SHORT).show();
        } else {
//            loadingBar.setTitle("Creating New Account");
//            loadingBar.setMessage("Please wait, while we are creating your new Account...");
//            loadingBar.show();
//            loadingBar.setCanceledOnTouchOutside(true);

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                SendEmailVerificationMessage();

//                                loadingBar.dismiss();
                            } else {
                                String message = task.getException().getMessage();
                                Toast.makeText(createAccount.this, "Error Occured: " + message, Toast.LENGTH_SHORT).show();
//                                loadingBar.dismiss();
                            }
                        }
                    });
        }
    }

    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(createAccount.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    private void SendEmailVerificationMessage() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {
                        Toast.makeText(createAccount.this, "Registration is Successfull, Please Check Your Email and verify the account", Toast.LENGTH_SHORT).show();
                        SendUserToLoginActivity();
                        mAuth.signOut();
                    } else {
                        String error = task.getException().getMessage();
                        Toast.makeText(createAccount.this, "Error:" + error, Toast.LENGTH_SHORT).show();
                        mAuth.signOut();
                    }

                }
            });
        }
    }

    public void SendUserToLoginActivity() {
        Intent loginIntent = new Intent(createAccount.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }
}
