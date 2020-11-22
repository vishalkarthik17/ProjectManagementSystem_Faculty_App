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
import java.util.UUID;

public class Group_Select extends AppCompatActivity {
    private Button proceed, reselec;
    private ListView lv;
    private TextView one ;
    private ArrayList<String> keylist = new ArrayList<>();//contains uid of all group in the listview
    private ArrayList<String> al = new ArrayList<>(); //arraylist linked to adapter
    //GroupClasss grp;
    private DatabaseReference abc;
    private DatabaseReference gg;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group__select);
        getSupportActionBar().setTitle("Group Select");
        mAuth = FirebaseAuth.getInstance();
        one = findViewById(R.id.spinner1); //TextBox Link
        proceed = findViewById(R.id.toHomeBtn);
        reselec=findViewById(R.id.reselect);
        final ArrayAdapter<String> adp = new ArrayAdapter<String>(Group_Select.this, android.R.layout.simple_list_item_1, al);//List Adapter,with al set
        abc = FirebaseDatabase.getInstance().getReference().child("Groups");
        gg = FirebaseDatabase.getInstance().getReference().child("Faculty");//

        lv = findViewById(R.id.studentlist);
        lv.setAdapter(adp); //set adapter to listview
        String summa;

        final ArrayList<String> sal = new ArrayList<>(); //store the UID of selected item from the list


        abc.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String toDisplay = "";
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    toDisplay = "";
                    if (  !ds.getKey().equals("Test") && (ds.child("facultyid").getValue().toString()).equals("NA")) {
                        toDisplay = ds.child("title").getValue().toString();
                        toDisplay += "\n" + ds.child("mainarea").getValue().toString();
                        toDisplay +=" , subarea : "+ds.child("subarea").getValue().toString();
                        al.add(toDisplay);
                        keylist.add(ds.getKey());
                        adp.notifyDataSetChanged();
                    }

                }
                if(keylist.size()==0)
                {
                    Intent veedu=new Intent(Group_Select.this,Home_Page.class);
                    startActivity(veedu);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //OnClick of List
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (sal.size() == 0) {
                    sal.add(keylist.get(position+sal.size()));
                    one.setText(al.get(position));
                    al.remove(position);
                    adp.notifyDataSetChanged();

                } else
                    Toast.makeText(Group_Select.this, "Max Users Added", Toast.LENGTH_SHORT).show();


            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            String facid="Poda";
            String grid="";
            String grpp="";
            @Override
            public void onClick(View v) {
            //gg faculty abc grp

               // String facuid=mAuth.getUid();
                if (sal.size() == 1) {



                    gg.child(mAuth.getUid()).child("group_id").setValue(sal.get(0));
                    gg.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            facid=String.valueOf(dataSnapshot.child(mAuth.getUid()).child("faculty_id").getValue().toString());
                            abc.child(sal.get(0)).child("facultyid").setValue(facid);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(Group_Select.this, "Nahi Beta", Toast.LENGTH_SHORT).show();
                        }
                    });
                    abc.child(sal.get(0)).child("facultyid").setValue(facid);


                    Intent homego=new Intent(Group_Select.this,Home_Page.class);
                    startActivity(homego);

                } else {
                    Toast.makeText(Group_Select.this, "Not Sufficient Number of Users", Toast.LENGTH_SHORT).show();
                }


            }
        });


        //reselec.findViewById(R.id.reselect);
        reselec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reload=new Intent(Group_Select.this,Group_Select.class);
                startActivity(reload);
            }
        });



        //Intent homego=new Intent(Group_Select.this,Home_Page.class);
        //startActivity(homego);
    }
    @Override
    public void onBackPressed() {
        Intent ToHome=new Intent(Group_Select.this,Home_Page.class);
        startActivity(ToHome);
    }
}
