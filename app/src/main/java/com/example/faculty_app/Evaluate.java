package com.example.faculty_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Evaluate extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Button bb,hh;
    private FirebaseAuth mAuth;
    private EditText mm,rm;
    private Spinner sp;
    String choice="NA";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate);

        getSupportActionBar().setTitle("Evaluate");

        mAuth=FirebaseAuth.getInstance();
        mm=findViewById(R.id.enterMark);
        rm=findViewById(R.id.enterRemark);
        sp=findViewById(R.id.rNumber_eval);

        ArrayAdapter<CharSequence> adpp=ArrayAdapter.createFromResource(Evaluate.this,R.array.reviewNumberArray,android.R.layout.simple_spinner_item);
        adpp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adpp);
        sp.setOnItemSelectedListener(this);

        hh=findViewById(R.id.Finish);
        hh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!choice.equals("NA")){
                    final DatabaseReference abc= FirebaseDatabase.getInstance().getReference();
                    abc.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String ggg=snapshot.child("Faculty").child(mAuth.getUid()).child("group_id").getValue().toString();
                            String one=ggg+choice;

                            abc.child("Review").child(one).child("marks").setValue(mm.getText().toString());
                            abc.child("Review").child(one).child("remark").setValue(rm.getText().toString());
                            Intent toH=new Intent(Evaluate.this,Home_Page.class);
                            startActivity(toH);
                            finish();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else{
                    Toast.makeText(Evaluate.this, "Select Review No.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        bb=findViewById(R.id.bckBtn);
        bb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ToHome=new Intent(Evaluate.this,Home_Page.class);
                startActivity(ToHome);
            }
        });



    }
    @Override
    public void onBackPressed() {
        Intent ToHome=new Intent(Evaluate.this,Home_Page.class);
        startActivity(ToHome);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        choice=parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
