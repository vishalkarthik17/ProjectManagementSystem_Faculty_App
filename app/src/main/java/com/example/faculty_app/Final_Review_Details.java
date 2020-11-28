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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Final_Review_Details extends AppCompatActivity {

    private Button  reselec,bb;
    private ListView lv;
    private TextView dt,ins,r1,r2,r3,fm;
    private TextView one ;
    private ArrayList<String> keylist = new ArrayList<>();//contains uid of all group in the listview
    private ArrayList<String> al = new ArrayList<>(); //arraylist linked to adapter
    private DatabaseReference abc;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final__review__details);
        getSupportActionBar().setTitle("Final Review Details");
        mAuth = FirebaseAuth.getInstance();
        one = findViewById(R.id.grpDisp); //TextBox Link
        reselec=findViewById(R.id.reselectGrp);
        final ArrayAdapter<String> adp = new ArrayAdapter<String>(Final_Review_Details.this, android.R.layout.simple_list_item_1, al);//List Adapter,with al set
        abc = FirebaseDatabase.getInstance().getReference();
        lv = findViewById(R.id.PanelGrouplist);
        lv.setAdapter(adp); //set adapter to listview

        dt=findViewById(R.id.finDate);
        ins=findViewById(R.id.FinIns);
        r1=findViewById(R.id.rem1);
        r2=findViewById(R.id.rem2);
        r3=findViewById(R.id.rem3);
        fm=findViewById(R.id.FinalMarks);


        final ArrayList<String> sal = new ArrayList<>(); //store the UID of selected item from the list


        abc.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String toDisplay= "";
                String pid=snapshot.child("Faculty").child(mAuth.getUid()).child("panel_id").getValue().toString();
                for (DataSnapshot ds : snapshot.child("Panel_Group").getChildren())
                {
                    if(ds.getKey().contains(pid))
                    {
                        String gid=ds.child("group_id").getValue().toString();
                        toDisplay=snapshot.child("Groups").child(gid).child("title").getValue().toString();
                        toDisplay+="\n"+snapshot.child("Groups").child(gid).child("mainarea").getValue().toString();
                        toDisplay+=" , Subarea : "+snapshot.child("Groups").child(gid).child("subarea").getValue().toString();
                        al.add(toDisplay);
                        keylist.add(gid);
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
                    abc.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                           String gid=sal.get(0)+"Final" ;
                            fm.setText(snapshot.child("Review").child(gid).child("finalMark").getValue().toString());
                            r1.setText(snapshot.child("Review").child(gid).child("remarkHead").getValue().toString());
                            r2.setText(snapshot.child("Review").child(gid).child("remarkMember1").getValue().toString());
                            r3.setText(snapshot.child("Review").child(gid).child("remarkMember2").getValue().toString());
                            dt.setText(snapshot.child("Review").child(gid).child("reviewDate").getValue().toString());
                            ins.setText(snapshot.child("Review").child(gid).child("instructions").getValue().toString());


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    lv.setVisibility(View.INVISIBLE);

                }
            }
        });

        bb=findViewById(R.id.backBtn);
        bb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ToHome=new Intent(Final_Review_Details.this,Home_Page.class);
                startActivity(ToHome);
            }
        });

        reselec=findViewById(R.id.reselectGrp);
        reselec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ToHome=new Intent(Final_Review_Details.this,Final_Review_Details.class);
                startActivity(ToHome);
            }
        });

    }
    @Override
    public void onBackPressed() {
        Intent ToHome=new Intent(Final_Review_Details.this,Home_Page.class);
        startActivity(ToHome);
    }
}
