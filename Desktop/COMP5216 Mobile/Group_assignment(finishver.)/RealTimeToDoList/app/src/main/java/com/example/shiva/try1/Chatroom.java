package com.example.shiva.try1;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Chatroom.
 */
public class Chatroom extends AppCompatActivity {
    /**
     * The E 1.
     */
    EditText e1;
    /**
     * The T 1.
     */
    TextView t1;

    private String user_name, room_name;
    /**
     * The Adapter.
     */
    SimpleAdapter adapter;
    /**
     * The Reference.
     */
    DatabaseReference reference;
    /**
     * The Temp key.
     */
    String temp_key;
    /**
     * The Item date.
     */
    LinkedHashMap<String, String> itemDate;
    /**
     * The List view 123.
     */
    ListView listView123;
    /**
     * The Test.
     */
    TextView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);
        Log.i("Chatroom", "we are in chatroom.java");
        e1 = (EditText) findViewById(R.id.editText2);
        t1 = (TextView) findViewById(R.id.textView);
        listView123 = (ListView) findViewById(R.id.listView);
        test = (TextView) findViewById(R.id.text1);


        user_name = getIntent().getExtras().get("user_name").toString();
        room_name = getIntent().getExtras().get("room_name").toString();
        reference = FirebaseDatabase.getInstance().getReference().child(room_name);
        setTitle(" Category: " + room_name);


        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                append_chat(dataSnapshot);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                append_chat(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                append_chat(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        setupListViewListener();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }

    /**
     * Send.
     *
     * @param v the v
     */
    public void send(View v) {
        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        Map<String, Object> map = new HashMap<String, Object>();
        temp_key = reference.push().getKey();
        reference.updateChildren(map);

        DatabaseReference child_ref = reference.child(temp_key);
        Map<String, Object> map2 = new HashMap<>();
        map2.put("name", user_name);
        map2.put("msg", e1.getText().toString());
        map2.put("date", formattedDate);
        child_ref.updateChildren(map2).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        e1.setText("");

    }

    /**
     * Append chat.
     *
     * @param ss the ss
     */
    public void append_chat(DataSnapshot ss) {
        itemDate = new LinkedHashMap<>();
        Iterator it = itemDate.entrySet().iterator();
        List<LinkedHashMap<String, String>> listItems = new ArrayList<>();
        String chat_msg, chat_username, chat_date;
        Iterator i = ss.getChildren().iterator();
        while (i.hasNext()) {
            LinkedHashMap<String, String> resultMap = new LinkedHashMap<>();

            chat_date = ((DataSnapshot) i.next()).getValue().toString();
            chat_msg = ((DataSnapshot) i.next()).getValue().toString();
            chat_username = ((DataSnapshot) i.next()).getValue().toString();

            String secondLine = chat_username + " " + chat_date;

            resultMap.put("First Line", chat_msg);
            resultMap.put("Second Line", secondLine);
            adapter = new SimpleAdapter(this, listItems,
                    R.layout.list_item,
                    new String[]{"First Line", "Second Line", "Third Line"},
                    new int[]{R.id.text1, R.id.text2});

        }
    }


    private void setupListViewListener() {
        Log.i("Chatroom", "running setUplistView ");
        listView123.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.i("MainActivity", "Long Clicked item " + position);
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Chatroom.this);
                String yes = "YES";
                String no = "NO";
                builder.setTitle(R.string.dialog_delete_title)
                        .setMessage(R.string.dialog_delete_msg)
                        .setPositiveButton(yes, new
                                DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                })
                        .setNegativeButton(R.string.cancel, new
                                DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                });
                builder.create().show();
            }
        });
    }
}
