package com.example.ekene.hexaBlog;

import android.content.Intent;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ekene.hexaBlog.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private EditText loginEmail, loginPass;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private Button loginBtn;
    private String Tag = "Login_Activity";
    private TextView toRegister ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBtn = (Button)findViewById(R.id.loginBtn);
        loginEmail = (EditText)findViewById(R.id.login_email);
        loginPass = (EditText)findViewById(R.id.login_password);
        toRegister = (TextView)findViewById(R.id.signUpTxtView) ;

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "PROCESSING....", Toast.LENGTH_LONG).show();
                String email = loginEmail.getText().toString().trim();
                String password = loginPass.getText().toString().trim();
                Log.i(Tag,"login password and username " + email + " "+password);

                if (!TextUtils.isEmpty(email)&& !TextUtils.isEmpty(password)){

                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.i(Tag,"login password and username " + task.toString());

                            if (task.isSuccessful()){
                                checkUserExistence();
                                Log.i(Tag,"login password and username " + task.isSuccessful());

                            }else {
                                Toast.makeText(LoginActivity.this, "Couldn't login, User not found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    Log.i(Tag,"problem login" );

                    Toast.makeText(LoginActivity.this, "Complete all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void checkUserExistence(){

        final String user_id = mAuth.getCurrentUser().getUid();
        Log.i(Tag,"login user_id" + user_id);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(Tag,"login password and username onDataChange");
                Log.i(Tag,"login password and username " + dataSnapshot.hasChild(user_id));

                if (dataSnapshot.hasChild(user_id)){
                    Log.i(Tag,"login password and username " + dataSnapshot.hasChild(user_id));
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }else {
                    Log.i(Tag,"login password and username " + dataSnapshot.hasChild(user_id));

                    Toast.makeText(LoginActivity.this, "User not registered!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
