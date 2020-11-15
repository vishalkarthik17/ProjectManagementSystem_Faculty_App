package com.example.faculty_app;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Home_Page extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private TextView namee;
    private TextView facid;
    private TextView dd;
    private TextView gtitle,gmainarea,gsubarea;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home__page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        final FirebaseAuth mAuth=FirebaseAuth.getInstance();
        //final FirebaseAuth mAuth=FirebaseAuth.getInstance();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        namee=(TextView)findViewById(R.id.home_name);
        facid=(TextView)findViewById(R.id.home_studentid);
        dd=(TextView)findViewById(R.id.home_dept);

        gtitle=(TextView)findViewById(R.id.home_title);
        gmainarea=(TextView)findViewById(R.id.home_mainarea);
        gsubarea=(TextView)findViewById(R.id.home_subarea);


        final DatabaseReference rrr=FirebaseDatabase.getInstance().getReference();
        Log.e("Vishal",mAuth.getUid());
        rrr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               // Log.e("Vishal",dataSnapshot.child("faculty_name").getValue().toString());

                    namee.setText(dataSnapshot.child("Faculty").child(mAuth.getUid()).child("faculty_name").getValue().toString());
                    facid.setText(dataSnapshot.child("Faculty").child(mAuth.getUid()).child("faculty_id").getValue().toString());
                    String did=dataSnapshot.child("Faculty").child(mAuth.getUid()).child("dept_id").getValue().toString();
                    dd.setText(dataSnapshot.child("Department").child(did).getValue().toString());
                    String groupdisp= dataSnapshot.child("Faculty").child(mAuth.getUid()).child("group_id").getValue().toString();

                    if(!groupdisp.equals("NA")){
                        gtitle.setText(dataSnapshot.child("Groups").child(groupdisp).child("title").getValue().toString());
                        gmainarea.setText(dataSnapshot.child("Groups").child(groupdisp).child("mainarea").getValue().toString());
                        gsubarea.setText(dataSnapshot.child("Groups").child(groupdisp).child("subarea").getValue().toString());
                    }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home__page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            Intent Sched = new Intent(Home_Page.this,Schedule_Review.class);
            startActivity(Sched);
            finish();

        } else if (id == R.id.nav_slideshow) {
            Intent Sched = new Intent(Home_Page.this,Evaluate.class);
            startActivity(Sched);
            finish();

        } else if (id == R.id.nav_tools) {
            Intent Sched = new Intent(Home_Page.this,Approve_Request.class);
            startActivity(Sched);
            finish();

        }
        else if(id==R.id.nav_reviewdet){
            Intent Sched = new Intent(Home_Page.this,Approve_Request.class);
            startActivity(Sched);
            finish();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
