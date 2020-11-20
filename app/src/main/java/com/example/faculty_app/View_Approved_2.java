package com.example.faculty_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class View_Approved_2 extends AppCompatActivity {
    private Button bb;
    private ListView lv;
    private DatabaseReference abc;
    FirebaseAuth mAuth;
    private ArrayList<String> al = new ArrayList<>(); //arraylist linked to adapter
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__approved_2);
        getSupportActionBar().setTitle("Budget Details");

        mAuth = FirebaseAuth.getInstance();
        final ArrayAdapter<String> adp = new ArrayAdapter<String>(View_Approved_2.this, android.R.layout.simple_list_item_1, al);//List Adapter,with al set
        lv = findViewById(R.id.ResList);
        lv.setAdapter(adp); //set adapter to listview

        abc = FirebaseDatabase.getInstance().getReference();
        abc.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String toDisplay="";
                String ggg=snapshot.child("Faculty").child(mAuth.getUid()).child("group_id").getValue().toString();
                for (DataSnapshot ds : snapshot.child("Budget").getChildren()){
                    toDisplay="";
                    if(ds.getKey().contains(ggg)){
                        toDisplay="BUDGET : "+ds.child("budget").getValue().toString();
                        toDisplay+="\n"+"DESCRIPTION : "+ds.child("budDescription").getValue().toString();
                        toDisplay+="\n"+"APPROVAL STATUS (Y/N) : "+ds.child("status").getValue().toString();
                        al.add(toDisplay);
                        adp.notifyDataSetChanged();

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        bb=findViewById(R.id.backBtn);
        bb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ToHome=new Intent(View_Approved_2.this,Home_Page.class);
                startActivity(ToHome);
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent ToHome=new Intent(View_Approved_2.this,Home_Page.class);
        startActivity(ToHome);
    }
}
