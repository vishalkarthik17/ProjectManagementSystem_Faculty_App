package com.example.faculty_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

public class Final_Eval extends AppCompatActivity {
    private Button proceed, reselec,bb;
    private ListView lv;
    private TextView one ;
    private EditText mm , rr;
    private ArrayList<String> keylist = new ArrayList<>();//contains uid of all group in the listview
    private ArrayList<String> al = new ArrayList<>(); //arraylist linked to adapter
    private DatabaseReference abc;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final__eval);
        getSupportActionBar().setTitle("Final Evaluation");
        mAuth = FirebaseAuth.getInstance();
        mm=findViewById(R.id.finMarkEnter);
        rr=findViewById(R.id.finRemarkEnter);
        one = findViewById(R.id.grpDisp); //TextBox Link
        proceed = findViewById(R.id.toHomeBtn);
        reselec=findViewById(R.id.reselect);
        final ArrayAdapter<String> adp = new ArrayAdapter<String>(Final_Eval.this, android.R.layout.simple_list_item_1, al);//List Adapter,with al set
        abc = FirebaseDatabase.getInstance().getReference();
        lv = findViewById(R.id.PanelGrouplist);
        lv.setAdapter(adp); //set adapter to listview

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

                } else
                    Toast.makeText(Final_Eval.this, "Max Users Added", Toast.LENGTH_SHORT).show();


            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sal.size()==1){
                    //Toast.makeText(Final_Eval.this, "size1", Toast.LENGTH_SHORT).show();
                    abc.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String pid=snapshot.child("Faculty").child(mAuth.getUid()).child("panel_id").getValue().toString();
                            if((snapshot.child("Panel").child(pid).child("panel_head").getValue().toString()).equals(mAuth.getUid()) ){
                                String RevKey=sal.get(0)+"Final";
                                abc.child("Review").child(RevKey).child("finalMark").setValue(mm.getText().toString());
                                abc.child("Review").child(RevKey).child("remarkHead").setValue(rr.getText().toString());
                            }
                            else if((snapshot.child("Panel").child(pid).child("member_one").getValue().toString()).equals(mAuth.getUid())){
                                String RevKey=sal.get(0)+"Final";
                                abc.child("Review").child(RevKey).child("remarkMember1").setValue(rr.getText().toString());
                            }
                            else if((snapshot.child("Panel").child(pid).child("member_two").getValue().toString()).equals(mAuth.getUid())){
                                String RevKey=sal.get(0)+"Final";
                                abc.child("Review").child(RevKey).child("remarkMember2").setValue(rr.getText().toString());
                            }
                            Intent ToHome=new Intent(Final_Eval.this,Home_Page.class);
                            startActivity(ToHome);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else {
                    Toast.makeText(Final_Eval.this, "Not Sufficient Number of Users", Toast.LENGTH_SHORT).show();
                }

            }
        });
        bb=findViewById(R.id.backBtn);
        bb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ToHome=new Intent(Final_Eval.this,Home_Page.class);
                startActivity(ToHome);
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent ToHome=new Intent(Final_Eval.this,Home_Page.class);
        startActivity(ToHome);
    }
}
