package com.example.faculty_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private Button Login_Button;
    private Button br;
    private FirebaseAuth mAuth;
    private ProgressBar loading;
    private EditText userid,pword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        Login_Button= findViewById(R.id.loginBtn);
        loading=findViewById(R.id.progressBar);
        userid=findViewById(R.id.UID);
        pword=findViewById(R.id.PW);

        Login_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loading.setVisibility(View.VISIBLE);
                Login_Button.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                final String uids= userid.getText().toString().trim();
                final String pws=pword.getText().toString().trim();
                if(TextUtils.isEmpty(uids)){
                    userid.setError("Email is Required");
                    return;
                }
                if(TextUtils.isEmpty(pws)){
                    pword.setError("Password is Required");
                    return;
                }

                mAuth.signInWithEmailAndPassword(uids,pws).addOnCompleteListener(new OnCompleteListener<AuthResult>() {


                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {


                        if(task.isSuccessful()&& uids!="" && pws!="" ){


                            Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                            DatabaseReference abc= FirebaseDatabase.getInstance().getReference();
                            abc.child("Faculty").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Toast.makeText(MainActivity.this, "Data Snap On Data Change", Toast.LENGTH_SHORT).show();
                                    if((dataSnapshot.child("group_id").getValue().toString()).equals("NA"))
                                    {
                                        Toast.makeText(MainActivity.this, "Group not assigned", Toast.LENGTH_SHORT).show();
                                        Intent ToGroup=new Intent(MainActivity.this,Group_Select.class);
                                        startActivity(ToGroup);
                                        finish();
                                    }
                                    else
                                    {
                                        Toast.makeText(MainActivity.this, "group assigned", Toast.LENGTH_SHORT).show();
                                        Intent loginToHome=new Intent(MainActivity.this,Home_Page.class);
                                        startActivity(loginToHome);
                                        finish();

                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });




                          /*Intent loginToHome=new Intent(MainActivity.this,Home_Page.class);
                          startActivity(loginToHome);
                          finish();*/

                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "Username/Password Dont Match!", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });
    }
}
