package com.example.faculty_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class Schedule_Review extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Button bb,sb;
    private EditText ed,ins;
    private TextView tv;
    private Spinner sp;
    String choice="";
    String datee="NA";
    private FirebaseAuth mAuth;
    //private DatabaseReference abc;
    DatePickerDialog.OnDateSetListener setListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule__review);

        getSupportActionBar().setTitle("Schedule Review");

        sp=findViewById(R.id.rNumber);
        mAuth=FirebaseAuth.getInstance();

        ArrayAdapter<String> adpp=new ArrayAdapter<String>(Schedule_Review.this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.reviewNumberArray));
        adpp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adpp);
        sp.setOnItemSelectedListener(this);

        ins=findViewById(R.id.enterInst);
        tv=findViewById(R.id.mmddyyyy);
        bb=findViewById(R.id.bckBtn);
        sb=findViewById(R.id.skeduleBtn);
        bb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ToHome=new Intent(Schedule_Review.this,Home_Page.class);
                startActivity(ToHome);
            }
        });



        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year=calendar.get(Calendar.YEAR);
                int month=calendar.get(Calendar.MONTH);
                int dayy=calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog=new DatePickerDialog(Schedule_Review.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,setListener,year,month,dayy);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });

        setListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                datee=dayOfMonth+"/"+month+"/"+year;
                tv.setText(datee);
            }
        };

        sb.setOnClickListener(new View.OnClickListener() {

            String ReviewTableKey="";
            @Override
            public void onClick(View v) {
                final DatabaseReference abc=FirebaseDatabase.getInstance().getReference();
                abc.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String grpID = String.valueOf(dataSnapshot.child("Faculty").child(mAuth.getUid()).child("group_id").getValue().toString());


                        ReviewTableKey=String.valueOf(grpID+choice);
                        Log.e("Key",ReviewTableKey);
                        abc.child("Review").child(ReviewTableKey).child("reviewDate").setValue(datee);
                        abc.child("Review").child(ReviewTableKey).child("instructions").setValue(ins.getText().toString());
                        Intent toHome=new Intent(Schedule_Review.this,Home_Page.class);
                        startActivity(toHome);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }


    @Override
    public void onBackPressed() {
        Intent ToHome=new Intent(Schedule_Review.this,Home_Page.class);
        startActivity(ToHome);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        choice = parent.getItemAtPosition(position).toString();
        Toast.makeText(getApplicationContext(),choice,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
