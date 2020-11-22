package com.example.faculty_app;

import androidx.activity.OnBackPressedCallback;
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
import java.util.UUID;

public class Select_Panel extends AppCompatActivity {
    private Button proceed, reselec;
    private ListView lv;
    private TextView one[] = new TextView[2];
    private DatabaseReference abc;
    FirebaseAuth mAuth;
    private ArrayList<String> keylist = new ArrayList<>();//contains uid of all users in the listview
    private ArrayList<String> al = new ArrayList<>(); //arraylist linked to adapter
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select__panel);
        getSupportActionBar().setTitle("Select Panel Faculties");
        mAuth = FirebaseAuth.getInstance();
        one[0] = findViewById(R.id.spinner1); //TextBox Links
        one[1] = findViewById(R.id.spinner2);
        proceed = findViewById(R.id.toHomeBtn);
        reselec=findViewById(R.id.rreselectt);
        final ArrayAdapter<String> adp = new ArrayAdapter<String>(Select_Panel.this, android.R.layout.simple_list_item_1, al);//List Adapter,with al set
        abc = FirebaseDatabase.getInstance().getReference();
        lv = findViewById(R.id.FacultyList);
        lv.setAdapter(adp); //set adapter to listview

        abc.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String toDisplay="";
                for(DataSnapshot ds:snapshot.child("Faculty").getChildren()){
                    if((ds.child("panel_id").getValue().toString()).equals("NA") && !ds.getKey().equals(mAuth.getUid().toString())){
                        toDisplay=ds.child("faculty_name").getValue().toString();
                        toDisplay+="\n"+ds.child("faculty_id").getValue().toString();
                        toDisplay+="\n"+ds.child("dept_id").getValue().toString();
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

        final ArrayList<String> sal = new ArrayList<>(); //store the UID of selected item from the list
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(sal.size()<2){
                    sal.add(keylist.get((position)+sal.size()));
                    one[sal.size() - 1].setText(al.get(position));
                    al.remove(position);
                    adp.notifyDataSetChanged();
                }
                else
                    Toast.makeText(Select_Panel.this, "Max Users Added", Toast.LENGTH_SHORT).show();
            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sal.size()==2){
                    String aaaa = UUID.randomUUID().toString();
                    abc.child("Panel").child(aaaa).child("panel_id").setValue(aaaa);
                    abc.child("Panel").child(aaaa).child("panel_head").setValue(mAuth.getUid());
                    abc.child("Panel").child(aaaa).child("member_one").setValue(sal.get(0));
                    abc.child("Panel").child(aaaa).child("member_two").setValue(sal.get(1));

                    abc.child("Faculty").child(mAuth.getUid()).child("panel_id").setValue(aaaa);
                    abc.child("Faculty").child(sal.get(0)).child("panel_id").setValue(aaaa);
                    abc.child("Faculty").child(sal.get(1)).child("panel_id").setValue(aaaa);


                    Intent GoToHomeDa=new Intent(Select_Panel.this,Home_Page.class);
                    startActivity(GoToHomeDa);

                }
                else {
                    Toast.makeText(Select_Panel.this, "Not Sufficient Number of Users", Toast.LENGTH_SHORT).show();
                }
            }
        });

        reselec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reload=new Intent(Select_Panel.this,Select_Panel.class);
                startActivity(reload);
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent ToHome=new Intent(Select_Panel.this,Home_Page.class);
        startActivity(ToHome);
    }

}
