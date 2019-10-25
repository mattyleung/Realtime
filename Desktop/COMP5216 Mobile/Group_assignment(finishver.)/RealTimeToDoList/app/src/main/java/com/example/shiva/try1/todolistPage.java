package com.example.shiva.try1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The type Todolist page.
 */
public class todolistPage extends AppCompatActivity {
    /**
     * The Simple adapter.
     */
    SimpleAdapter simpleAdapter;
    /**
     * The E 1.
     */
    EditText e1;
    /**
     * The T 1.
     */
    TextView t1;
    /**
     * The List view.
     */
    ListView listView;
    private String user_name, room_name;
    /**
     * The Reference.
     */
    DatabaseReference reference, /**
     * The Messages ref.
     */
    messagesRef, /**
     * The Message ref 2.
     */
    messageRef2;
    /**
     * The Temp key.
     */
    String temp_key;
    /**
     * The Item date.
     */
    LinkedHashMap<String, String> itemDate;
    /**
     * The List items.
     */
    ArrayList<LinkedHashMap<String, String>> listItems;
    /**
     * The Photo.
     */
    ImageView photo;
    /**
     * The Request image capture.
     */
    static final int REQUEST_IMAGE_CAPTURE = 1;
    /**
     * The My camera request code.
     */
    final int MY_CAMERA_REQUEST_CODE = 100;
    /**
     * The My permissions request read photos.
     */
    final int MY_PERMISSIONS_REQUEST_READ_PHOTOS = 101;
    /**
     * The View.
     */
    View view;
    /**
     * The Dialog.
     */
    android.support.v7.app.AlertDialog dialog;
    /**
     * The Adapter.
     */
    SimpleAdapter adapter;
    /**
     * The Remove position.
     */
    int removePosition;
    /**
     * The Photo bit map.
     */
    ArrayList<String> photoBitMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /**
         *This function is going to run once when the app is installed.
         */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);
        setContentView(R.layout.viewlistcontents_layout);
        Log.i("todoListPage", "we are in todolistPage.java");
        e1 = (EditText) findViewById(R.id.editText2);
        listItems = new ArrayList<>();
        photoBitMap = new ArrayList<>();
        listView = findViewById(R.id.listView123);

        user_name = getIntent().getExtras().get("user_name").toString();
        room_name = getIntent().getExtras().get("room_name").toString();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        messagesRef = rootRef.child("Messages").child(room_name);
        messageRef2 = rootRef.child("msg").child(room_name);
        photo = findViewById(R.id.button3);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(todolistPage.this);
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.
                        AlertDialog.Builder(todolistPage.this);
//                builder.setTitle("Name");
                final View customLayout = getLayoutInflater().inflate(R.layout.custom_layout, null);
                builder.setView(customLayout);
                dialog = builder.create();
                dialog.show();
            }
        });
        setTitle(room_name);


        messageRef2.addChildEventListener(new ChildEventListener() {
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

                append_chat(dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                append_chat(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    /**
     * Go to camera.
     *
     * @param v the v
     */
    public void goToCamera(View v) {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, MY_CAMERA_REQUEST_CODE);
        }
    }

    /**
     * Go to photo.
     *
     * @param v the v
     */
    public void goToPhoto(View v) {
        // Create intent for picking a photo from the gallery
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, MY_PERMISSIONS_REQUEST_READ_PHOTOS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            final Bitmap imageBitmap = (Bitmap) extras.get("data");
//            imageView.setImageBitmap(imageBitmap);
            Log.i("Result", "successful");
            dialog.dismiss();
            LayoutInflater layoutInflater = LayoutInflater.from(this);
            final View alertDialog = layoutInflater.inflate(R.layout.imagepreview, null);

            ImageView imageView = (ImageView) alertDialog.findViewById(R.id.previewImage);
            imageView.setImageBitmap(imageBitmap);


            final android.support.v7.app.AlertDialog.Builder alertadd = new android.support.v7.app.AlertDialog.Builder(todolistPage.this);
            alertadd.setView(alertDialog);
            alertadd.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(todolistPage.this);
                    final EditText edittext = new EditText(todolistPage.this);
                    alert.setTitle("What is your message");

                    alert.setView(edittext);

                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String message = edittext.getText().toString();
                            if (message.isEmpty()) {
                                Toast.makeText(todolistPage.this, "Please input message", Toast.LENGTH_SHORT).show();
                            } else {
                                sendWithPhoto(imageBitmap, message);
                                Log.i("Testing mess", "this is the message: " + message);
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
            alertadd.setNegativeButton("No", null);
            alertadd.show();
        }
        else if(requestCode == MY_PERMISSIONS_REQUEST_READ_PHOTOS && resultCode == RESULT_OK){
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                LayoutInflater layoutInflater = LayoutInflater.from(this);
                final View alertDialog = layoutInflater.inflate(R.layout.imagepreview, null);

                ImageView imageView = (ImageView) alertDialog.findViewById(R.id.previewImage);
                imageView.setImageBitmap(selectedImage);


                final android.support.v7.app.AlertDialog.Builder alertadd = new android.support.v7.app.AlertDialog.Builder(todolistPage.this);
                alertadd.setView(alertDialog);
                alertadd.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(todolistPage.this);
                        final EditText edittext = new EditText(todolistPage.this);
                        alert.setTitle("What is your message");

                        alert.setView(edittext);

                        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String message = edittext.getText().toString();
                                if (message.isEmpty()) {
                                    Toast.makeText(todolistPage.this, "Please input message", Toast.LENGTH_SHORT).show();
                                } else {
                                    sendWithPhoto(selectedImage, message);
//                                    dialog.dismiss();
                                    Log.i("Testing mess", "this is the message: " + message);
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
                alertadd.setNegativeButton("No", null);
                alertadd.show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(todolistPage.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }
    }

    /**
     * Send.
     *
     * @param v the v
     */
    public void send(View v) {
        Log.i("send", "we are starting send function");
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> map2 = new HashMap<>();
        map2.put("name", user_name);
        map2.put("msg", e1.getText().toString());
        map2.put("date", formattedDate);
        map2.put("photo", "null");
//        messagesRef.child(room_name).push().setValue(map2);
//        messageRef2.push().setValue(map2);
        messageRef2.child(e1.getText().toString()).setValue(map2);
        Log.i("dashboardDummy", messageRef2.toString());
        Log.i("dashboardDummy", messageRef2.child("name").toString());
        Log.i("dashboardDummy", messageRef2.child("msg").toString());
        Log.i("dashboardDummy", messageRef2.child("date").toString());
        e1.setText("");

    }

    /**
     * Send with photo.
     *
     * @param bitmap  the bitmap
     * @param message the message
     */
    public void sendWithPhoto(Bitmap bitmap, String message) {
        Log.i("send", "we are starting sendwithPhoto function");
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> map2 = new HashMap<>();
        map2.put("name", user_name);
        map2.put("msg", message);
        map2.put("date", formattedDate);
        String bitmapTemp = BitMapToString(bitmap);
        map2.put("photo", bitmapTemp);
//        messagesRef.child(room_name).push().setValue(map2);
//        messageRef2.push().setValue(map2);
        messageRef2.child(message).setValue(map2);
        Log.i("dashboardDummy", messageRef2.toString());
        Log.i("dashboardDummy", messageRef2.child("name").toString());
        Log.i("dashboardDummy", messageRef2.child("msg").toString());
        Log.i("dashboardDummy", messageRef2.child("date").toString());
        e1.setText("");

    }

    /**
     * Bit map to string string.
     *
     * @param bitmap the bitmap
     * @return the string
     */
    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    /**
     * String to bit map bitmap.
     *
     * @param encodedString the encoded string
     * @return the bitmap
     */
    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
    /**
     * Append chat.
     *
     * @param date the date
     * @param msg  the msg
     * @param name the name
     */

    /**
     * Append chat.
     *
     * @param ss the ss
     */
    public void append_chat(DataSnapshot ss) {
        itemDate = new LinkedHashMap<>();


        String chat_msg, chat_username, chat_date, chat_pic;
        Iterator i = ss.getChildren().iterator();
        while (i.hasNext()) {

            chat_date = ((DataSnapshot) i.next()).getValue().toString();
            chat_msg = ((DataSnapshot) i.next()).getValue().toString();
            chat_username = ((DataSnapshot) i.next()).getValue().toString();
            chat_pic = ((DataSnapshot) i.next()).getValue().toString();
            photoBitMap.add(chat_pic);
            String secondLine = chat_username + " " + chat_date;
            Log.i("appending", "firstLine is: " + chat_msg);
            Log.i("appending", "secondLine is: " + secondLine);
            itemDate.put(chat_msg, secondLine);
        }
        Iterator it = itemDate.entrySet().iterator();
        while (it.hasNext()) {
            LinkedHashMap<String, String> resultMap = new LinkedHashMap<>();
            Map.Entry pair = (Map.Entry) it.next();
            resultMap.put("First Line", pair.getKey().toString());
            resultMap.put("Second Line", pair.getValue().toString());
            listItems.add(resultMap);
        }
        Log.i("Check perform", "listItems length is: " + listItems.size());
        adapter = new SimpleAdapter(this, listItems,
                R.layout.list_item2,
                new String[]{"First Line", "Second Line"},
                new int[]{R.id.text1, R.id.text2});
        listView.setAdapter(adapter);
        setupListViewListener();
    }

    private void setupListViewListener() {
        /**
         * This function will be called with activities related to clicking on a listView items.
         */

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /**
                 * This function will be called when user click an item in listview
                 */
                AlertDialog.Builder alert = new AlertDialog.Builder(todolistPage.this);
                alert.setTitle("Please select actions");
                removePosition = position;
//                alert.setView(edittext);

                alert.setPositiveButton("Completed item", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String temp = listView.getItemAtPosition(removePosition).toString();
                        String[] tempArray = temp.split(",");
                        String[] tempArray2 = tempArray[0].split("=");
                        Log.i("Testing", "tempArray2[1] is: " + tempArray2[1]);
                        messageRef2.child(tempArray2[1]).removeValue();
                        listItems.remove(removePosition);
                        photoBitMap.remove(removePosition);
                        Log.i("MainActivity", "Clicked item " + removePosition + ": ");
                        adapter.notifyDataSetChanged();
                    }
                });

                alert.setNegativeButton("View photo", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // what ever you want to do with No option.
                        String photoBitmapString = photoBitMap.get(removePosition);
                        if (!photoBitmapString.equals("null")) {
                            LayoutInflater layoutInflater = LayoutInflater.from(todolistPage.this);
                            final View alertDialog = layoutInflater.inflate(R.layout.imagepreview, null);

                            ImageView imageView = (ImageView) alertDialog.findViewById(R.id.previewImage);
                            Bitmap imageBitmap = StringToBitMap(photoBitMap.get(removePosition));
                            imageView.setImageBitmap(imageBitmap);


                            final android.support.v7.app.AlertDialog.Builder alertadd = new android.support.v7.app.AlertDialog.Builder(todolistPage.this);
                            alertadd.setView(alertDialog);
                            alertadd.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                }
                            });
                            alertadd.setNegativeButton("No", null);
                            alertadd.show();
                        } else {
                            Toast.makeText(todolistPage.this, "There is no photo for this item", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                alert.show();
                Log.i("teting", "This is: " + listView.getItemAtPosition(position).toString());


            }
        });
    }
}
