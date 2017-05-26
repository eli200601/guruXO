package com.app.elisoft.guru.Services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class SendMessageToDevice  extends IntentService {

    private final static String TAG = "SendMessageToDevice";

    private String FCM_API_KEY = "AAAAK_9HT5E:APA91bHdVCtHBj8p6ovE8kGQmTWZdrLSE6b9WGH58Cio-GXI7umvkfGe8B4nG4v3At2a486iV_vUh65pOMMIr87K876QyTWKZZ2ZodZCpHBqqAvDTyO2Ux-oDJtvjA2lxNipfP87_6Mr";


    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "SendMessageToDevice - onHandleIntent");

        String host_name, host_uid, client_uid;
        host_name = intent.getStringExtra("host_name");
        host_uid = intent.getStringExtra("host_uid");
        client_uid = intent.getStringExtra("client_uid");

        Log.d(TAG, "Message: " + host_name + host_uid + client_uid);
        sendMessage(host_name, host_uid, client_uid);

    }

    public void stopService(){
        Log.d(TAG, "Stopping SendMessageToDevice");
        this.stopSelf();
    }

    private void sendMessage(String host_name, String senderId, String receiverId) {
        //send Push Notification
        Log.d(TAG, "Starting to sendMessage");

        HttpsURLConnection connection = null;
        String KEY_FCM_SENDER_ID = "host_id";
        String sender = senderId;
        try {

            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            Log.d(TAG, "New URL: " + url.getPath() );
            connection = (HttpsURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            //Put below you FCM API Key instead
            connection.setRequestProperty("Authorization", "key="
                    + FCM_API_KEY);

            JSONObject root = new JSONObject();
            JSONObject data = new JSONObject();

            data.put("host_name", host_name);
            data.put(KEY_FCM_SENDER_ID, sender);//"host_id"

            root.put("data", data);
            root.put("to", "/topics/user_" + receiverId);

            byte[] outputBytes = root.toString().getBytes("UTF-8");
            OutputStream os = connection.getOutputStream();
            os.write(outputBytes);
            os.flush();
            os.close();
            connection.getInputStream(); //do not remove this line. request will not work without it gg

        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null) connection.disconnect();
        }
        stopService();
    }

    public SendMessageToDevice() {
        super("");
    }

    public SendMessageToDevice(String name) {
        super(name);
    }

}
