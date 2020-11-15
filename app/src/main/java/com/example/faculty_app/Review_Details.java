package com.example.faculty_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Review_Details extends AppCompatActivity {
    private Button bb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review__details);

        getSupportActionBar().setTitle("Review Details");

        bb=findViewById(R.id.bckBtn);
        bb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ToHome=new Intent(Review_Details.this,Home_Page.class);
                startActivity(ToHome);
            }
        });


}
    @Override
    public void onBackPressed() {
        Intent ToHome=new Intent(Review_Details.this,Home_Page.class);
        startActivity(ToHome);
    }
}
