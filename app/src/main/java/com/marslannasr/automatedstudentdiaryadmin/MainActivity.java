package com.marslannasr.automatedstudentdiaryadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

public class MainActivity extends AppCompatActivity {
    private EditText username_text, username_password;
    //private TextView create_newAccountId;
    private Button login_btn_id;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mUSer;
    private ProgressDialog progressDialog;
    private ConnectionDetect connectionDetect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectionDetect = new ConnectionDetect(this);
        if (!connectionDetect.isConnected()) {
            Toast.makeText(this, "Please Turn On Wifi Connection", Toast.LENGTH_SHORT).show();
        }

        username_text = (EditText) findViewById(R.id.username_text);
        username_password = (EditText) findViewById(R.id.username_password);
        username_text.setText("admin@gmail.com");
        username_text.setSelection(username_text.getText().length());
        username_password.setText("admin123");
        username_password.setSelection(username_password.getText().length());
        login_btn_id = (Button) findViewById(R.id.login_btn_id);

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUSer = firebaseAuth.getCurrentUser();
                if (mUSer != null) {
                    Toast.makeText(MainActivity.this, "Signed In", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, DrawerActivity.class));
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "Not Signed In", Toast.LENGTH_SHORT).show();
                }
            }
        };


        login_btn_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(username_text.getText().toString())
                        && !TextUtils.isEmpty(username_password.getText().toString())) {
                    if (connectionDetect.isConnected()) {
                        String email = username_text.getText().toString();
                        String pass = username_password.getText().toString();
                        login(email, pass);
                    } else {
                        Toast.makeText(MainActivity.this, "Please Turn On Wifi Connection", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(MainActivity.this, "Plz Fill Data!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void login(final String email, final String pass) {
        progressDialog.setMessage("Checking Credentials....");
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            //SharedPrefManager.getInstance(getApplicationContext()).userLogin(mAuth.getCurrentUser().getUid(),
                            //      email,pass);
                            Toast.makeText(MainActivity.this, "Signed In", Toast.LENGTH_SHORT).show();
                            //Toast.makeText(MainActivity.this, currentUser.getPhotoUrl().toString(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            startActivity(new Intent(MainActivity.this, DrawerActivity.class));
                            finish();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Signed Failed Please check credentials", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthListener !=null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}
