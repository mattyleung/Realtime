package com.example.shiva.try1;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * The type Dashboard page.
 */
public class DashboardPage extends AppCompatActivity {

    /**
     * The Reference.
     */
    DatabaseReference reference, /**
     * The Reference 2.
     */
    reference2, /**
     * The Message ref 2.
     */
    messageRef2;

    /**
     * The Array list.
     */
    ArrayList<String> arrayList;
    /**
     * The Reset.
     */
    Button reset;
    /**
     * The E 1.
     */
    EditText e1;
    /**
     * The L 1.
     */
    ListView l1;
    /**
     * The Adapter.
     */
    ArrayAdapter<String> adapter;
    /**
     * The Name.
     */
    String name;
    /**
     * The Ee.
     */
    EditText ee;
    /**
     * The Add.
     */
    Button add;
    /**
     * The constant DATA.
     */
    public static final String DATA = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    /**
     * The Random.
     */
    Random RANDOM;
    /**
     * The User account.
     */
    HashMap<String, String> userAccount;
    /**
     * The Password copied.
     */
    String passwordCopied;
    /**
     * The Password.
     */
    final String password = "";
    /**
     * The Test array.
     */
    ArrayList<ArrayList<String>> testArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        e1 = (EditText) findViewById(R.id.editText);
        l1 = (ListView) findViewById(R.id.listView);
        arrayList = new ArrayList<>();
        reset = (Button) findViewById(R.id.reset);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        l1.setAdapter(adapter);
        reference = FirebaseDatabase.getInstance().getReference().getRoot().child("Messages");
        reference2 = FirebaseDatabase.getInstance().getReference().child("Messages");
        messageRef2 = FirebaseDatabase.getInstance().getReference().child("msg");
        testArray = new ArrayList<>();
        Log.i("dashboardDummy", reference.toString());
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference2.removeValue();
                messageRef2.removeValue();
            }
        });

        request_username();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> tempArray = new ArrayList<>();
                Set<String> set = new HashSet<String>();
                userAccount = new HashMap<String, String>();

                Iterator i = dataSnapshot.getChildren().iterator();
                while (i.hasNext()) {
                    Object j = i.next();
//                    set.add(((DataSnapshot) i.next()).getKey());
                    String temp2 = ((DataSnapshot) j).getKey();
                    set.add(((DataSnapshot) j).getKey());
                    String temp = ((DataSnapshot) j).getValue().toString();
                    userAccount.put(((DataSnapshot) j).getKey(), temp);
                    Log.i("Test to get value", "Id is:" + temp2 + "Password is: " + temp);
                }

                arrayList.clear();
                arrayList.addAll(set);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(DashboardPage.this, "No network connectivity " + databaseError, Toast.LENGTH_SHORT).show();
                Log.i("Cancel", "Error: " + databaseError);
            }
        });


        l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                AlertDialog.Builder alert = new AlertDialog.Builder(DashboardPage.this);
                final String temp = ((TextView) view).getText().toString();
                final EditText edittext = new EditText(DashboardPage.this);
                final String selectedFromList = (l1.getItemAtPosition(position).toString());
//                alert.setMessage("Enter Your Message");
                alert.setTitle("Enter the password");

                alert.setView(edittext);

                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String password = userAccount.get(selectedFromList);
                        if (edittext.getText().toString().equals(password)) {
                            Intent intent = new Intent(DashboardPage.this, todolistPage.class);
                            intent.putExtra("room_name", temp);
                            intent.putExtra("user_name", name);
                            startActivity(intent);
                        } else {
                            Toast.makeText(DashboardPage.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // what ever you want to do with No option.
                    }
                });

                alert.show();


            }
        });


    }


    /**
     * Random string string.
     *
     * @param len    the len
     * @param RANDOM the random
     * @return the string
     */
    public static String randomString(int len, Random RANDOM) {
        StringBuilder sb = new StringBuilder(len);
        RANDOM = new Random();
        for (int i = 0; i < len; i++) {
            sb.append(DATA.charAt(RANDOM.nextInt(DATA.length())));
        }

        return sb.toString();
    }

    /**
     * Request username.
     */
    public void request_username() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter your name?");
        ee = new EditText(this);
        builder.setView(ee);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                name = ee.getText().toString();


            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                request_username();
            }
        });
        builder.show();

    }


    /**
     * Insert data.
     *
     * @param v the v
     */
    public void insert_data(View v) {
        AlertDialog.Builder alert = new AlertDialog.Builder(DashboardPage.this);
        final String temp = ((TextView) v).getText().toString();
        final TextView textView = new TextView(DashboardPage.this);
        textView.setPadding(90, 0, 0, 0);
        final String generatedPassword = randomString(8, RANDOM);
        alert.setTitle("Password");
        textView.setText(generatedPassword);
        alert.setView(textView);

        alert.setPositiveButton("Copy", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                ClipboardManager clipboard = (ClipboardManager) getApplicationContext().
                        getSystemService(getApplicationContext().CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", generatedPassword);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(), "Copied!", Toast.LENGTH_LONG).show();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alert.show();
        Log.i("Insert data", "Running");
        Map<String, Object> map = new HashMap<>();
        map.put(e1.getText().toString(), generatedPassword);
        reference.updateChildren(map);

    }


}
