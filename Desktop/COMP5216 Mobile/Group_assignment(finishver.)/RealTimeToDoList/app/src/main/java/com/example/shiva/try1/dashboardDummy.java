package com.example.shiva.try1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


/**
 * The type Dashboard dummy.
 */
public class dashboardDummy extends AppCompatActivity {

    /**
     * The Email holder.
     */
    String EmailHolder;
    /**
     * The Email.
     */
    TextView Email;
    /**
     * The Log out.
     */
    Button LogOUT;
    /**
     * The constant TAG.
     */
    public static final String TAG = "LOGIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        Email = (TextView) findViewById(R.id.textView1);
        LogOUT = (Button) findViewById(R.id.button1);

        Intent intent = getIntent();

        // Receiving User Email Send By DashboardPage.
        EmailHolder = intent.getStringExtra(login.userEmail);

        // Setting up received email to TextView.
        Email.setText(Email.getText().toString() + EmailHolder);


        LogOUT.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                finish();

                Toast.makeText(dashboardDummy.this, "Log Out Successfull", Toast.LENGTH_LONG).show();


            }
        });

    }


}