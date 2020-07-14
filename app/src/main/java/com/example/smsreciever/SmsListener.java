package com.example.smsreciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.gsm.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Objects;

public class SmsListener extends BroadcastReceiver {

    //SmsMessage[] message;
    String msg, phoneNo = "";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference reference = db.collection("Details");
    //private DocumentReference reference = db.collection("Details").document("New Message");

    private static final  String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = "SMSListener";

    private String Message;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Intent Received" +intent.getAction());

        if(Objects.equals(intent.getAction(), SMS_RECEIVED)) {

            Bundle dataBundle = intent.getExtras();
            try {
                if (dataBundle != null) {
                    Object[] mypdu = (Object[]) dataBundle.get("pdus");
                    assert mypdu != null;
                    //final SmsMessage[] message = new SmsMessage[mypdu.length];

                    for (int i = 0; i < mypdu.length; i++) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            //String format = dataBundle.getString("format");
                            SmsMessage message = SmsMessage.createFromPdu((byte[]) mypdu[i]);
                            //Message += message[i].getMessageBody();
                            phoneNo = message.getDisplayOriginatingAddress();
                            msg = message.getMessageBody();
                            Log.d(TAG, Message);
                        } else {
                            //message[i] = SmsMessage.createFromPdu((byte[]) mypdu[i]);
                            //Message += message[i].getMessageBody();
                            SmsMessage message = SmsMessage.createFromPdu((byte[]) mypdu[i]);
                            //Message += message[i].getMessageBody();
                            phoneNo = message.getDisplayOriginatingAddress();
                            msg = message.getMessageBody();
                        }
                        //msg = message[i].getMessageBody();
                        Template t = new Template(msg, phoneNo);
                        //reference.set(t);
                        reference.add(t);
                    }
                    //phoneNo = message[0].getOriginatingAddress();
                    //Template t = new Template(msg, phoneNo);
                    //reference.set(t);
                    //reference.add(t);
                    Toast.makeText(context, "" + msg + "\n" + phoneNo, Toast.LENGTH_SHORT).show();

                    //addDataToDB(msg, phoneNo);
                }
            }catch (Exception e){
                Log.e("SmsReceiver", "Exception smsReceiver" +e);
            }
        }
    }
    public void addDataToDB(String msg, String phoneNo){
        Template template = new Template(msg, phoneNo);
       // reference.add(template);
    }
}
