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

public class Select_Groups_Panel extends AppCompatActivity {
    private Button proceed, reselec;
    private ListView lv;
    private TextView one ;
    private ArrayList<String> keylist = new ArrayList<>();//contains uid of all group in the listview
    private ArrayList<String> al = new ArrayList<>(); //arraylist linked to adapter
    private FirebaseAuth mAuth;
    private DatabaseReference abc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select__groups__panel);
        getSupportActionBar().setTitle("Group Select");

        mAuth = FirebaseAuth.getInstance();
        one = findViewById(R.id.spinner1); //TextBox Link
        proceed = findViewById(R.id.toHomeBtn);
        reselec=findViewById(R.id.reselect);
        final ArrayAdapter<String> adp = new ArrayAdapter<String>(Select_Groups_Panel.this, android.R.layout.simple_list_item_1, al);//List Adapter,with al set
        abc = FirebaseDatabase.getInstance().getReference();
        lv = findViewById(R.id.GroupList);
        lv.setAdapter(adp); //set adapter to listview


        final ArrayList<String> sal = new ArrayList<>(); //store the UID of selected item from the list

        abc.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String toDisplay = "";
                for(DataSnapshot ds : snapshot.child("Groups").getChildren()){
                    if (  !ds.getKey().equals("Test") && (ds.child("panelAssign").getValue().toString()).equals("NA")){
                        toDisplay = ds.child("title").getValue().toString();
                        toDisplay += "\n" + ds.child("mainarea").getValue().toString();
                        toDisplay +=" , subarea : "+ds.child("subarea").getValue().toString();
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
                    Toast.makeText(Select_Groups_Panel.this, "Max Users Added", Toast.LENGTH_SHORT).show();


            }
        });
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sal.size()==1){
                    abc.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String ppp=snapshot.child("Faculty").child(mAuth.getUid()).child("panel_id").getValue().toString();
                            String ggg=sal.get(0);
                            String kkk=ppp+ggg;
                            abc.child("Groups").child(ggg).child("panelAssign").setValue(ppp);
                            abc.child("Panel_Group").child(kkk).child("panel_id").setValue(ppp);
                            abc.child("Panel_Group").child(kkk).child("group_id").setValue(ggg);
                            Intent homego=new Intent(Select_Groups_Panel.this,Home_Page.class);
                            startActivity(homego);
                            finish();


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else {
                    Toast.makeText(Select_Groups_Panel.this, "Not Sufficient Number of Users", Toast.LENGTH_SHORT).show();
                }
            }
        });
        reselec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reload=new Intent(Select_Groups_Panel.this,Select_Groups_Panel.class);
                startActivity(reload);
            }
        });
    }
}
