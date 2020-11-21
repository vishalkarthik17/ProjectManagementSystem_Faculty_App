package com.example.faculty_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Approve_Request_Bud extends AppCompatActivity {
    private Button bb;
    private Button proceed, reselec;
    private ListView lv;
    private TextView one ;
    private ArrayList<String> keylist = new ArrayList<>();//contains uid of all group in the listview
    private ArrayList<String> al = new ArrayList<>(); //arraylist linked to adapter
    private DatabaseReference abc;
    private DatabaseReference gg;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve__request__bud);

        getSupportActionBar().setTitle("Approve Budget Request");

        mAuth = FirebaseAuth.getInstance();
        one = findViewById(R.id.disp_sel2); //TextBox Link
        proceed = findViewById(R.id.ApproveB);
        reselec=findViewById(R.id.reselectt);
        final ArrayAdapter<String> adp = new ArrayAdapter<String>(Approve_Request_Bud.this, android.R.layout.simple_list_item_1, al);//List Adapter,with al set
        abc = FirebaseDatabase.getInstance().getReference();
        gg = FirebaseDatabase.getInstance().getReference();

        lv = findViewById(R.id.BudList);
        lv.setAdapter(adp); //set adapter to listview

        final ArrayList<String> sal = new ArrayList<>(); //store the UID of selected item from the list

        abc.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String toDisplay="";
                String ggg=snapshot.child("Faculty").child(mAuth.getUid()).child("group_id").getValue().toString();
                for (DataSnapshot ds : snapshot.child("Budget").getChildren()){
                    toDisplay="";
                    if(ds.getKey().contains(ggg) && !(ds.child("status").getValue().toString() ).equals("Y") ){
                        toDisplay="BUDGET : "+ds.child("budget").getValue().toString();
                        toDisplay+="\n"+"DESCRIPTION : "+ds.child("budDescription").getValue().toString();
                        toDisplay+="\n"+"APPROVAL STATUS (Y/N) : "+ds.child("status").getValue().toString();
                        al.add(toDisplay);
                        keylist.add(ds.getKey());
                        adp.notifyDataSetChanged();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (sal.size() == 0) {
                    sal.add(keylist.get(position+sal.size()));
                    one.setText(al.get(position));
                    al.remove(position);
                    adp.notifyDataSetChanged();

                } else
                    Toast.makeText(Approve_Request_Bud.this, "You can only Approve One at a time", Toast.LENGTH_SHORT).show();
            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sal.size()==1) {
                    gg.child("Budget").child(sal.get(0)).child("status").setValue("Y");
                    Intent rel = new Intent(Approve_Request_Bud.this, Approve_Request_Bud.class);
                    startActivity(rel);
                    finish();
                }
                else {
                    Toast.makeText(Approve_Request_Bud.this, "You Have to Select Atleat 1 to approve", Toast.LENGTH_SHORT).show();
                }
            }
        });


        bb=findViewById(R.id.bckBtn);
        bb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ToHome=new Intent(Approve_Request_Bud.this,Home_Page.class);
                startActivity(ToHome);
            }
        });

        reselec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reload=new Intent(Approve_Request_Bud.this,Approve_Request_Bud.class);
                startActivity(reload);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent ToHome=new Intent(Approve_Request_Bud.this,Home_Page.class);
        startActivity(ToHome);
    }
}

