package com.app.elisoft.guru.Services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.app.elisoft.guru.Utils.Keys;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class SendMessageToDevice  extends IntentService {

    private final static String TAG = "SendMessageToDevice";

    private String FCM_API_KEY = "AAAAK_9HT5E:APA91bHdVCtHBj8p6ovE8kGQmTWZdrLSE6b9WGH58Cio-GXI7umvkfGe8B4nG4v3At2a486iV_vUh65pOMMIr87K876QyTWKZZ2ZodZCpHBqqAvDTyO2Ux-oDJtvjA2lxNipfP87_6Mr";
    String message = null ;

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "SendMessageToDevice - onHandleIntent");

        String host_name, host_uid, address_prefix, client_uid, game_room, request_type;
        host_name = intent.getStringExtra("host_name");
        host_uid = intent.getStringExtra("host_uid");
        client_uid = intent.getStringExtra("client_uid");
        game_room = intent.getStringExtra("game_room");
        request_type = intent.getStringExtra("request_type");
        address_prefix = intent.getStringExtra("address_prefix");

        if ( intent.getStringExtra("message") != null ) {
            message = intent.getStringExtra("message");
        }

        Log.d(TAG, "Message: " + host_name + host_uid + client_uid + request_type);
        sendMessage(host_name, host_uid, address_prefix, client_uid, game_room, request_type);

    }

    public void stopService(){
        Log.d(TAG, "Stopping SendMessageToDevice");
        this.stopSelf();
    }

    private void sendMessage(String host_name, String senderId, String address_prefix, String receiverId, String game_room, String request_type) {
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
            data.put("game_room", game_room);
            data.put(Keys.REQUEST_TYPE, request_type);
            if (message != null) {
                data.put("message" , message);
            }

            root.put("data", data);
            root.put("to", "/topics/" + address_prefix + receiverId);
            Log.d(TAG, "Sending message to To: " + "/topics/" + address_prefix + receiverId);

            byte[] outputBytes = root.toString().getBytes("UTF-8");
            OutputStream os = connection.getOutputStream();
            Log.d(TAG, "Request send: " + os.toString());
            os.write(outputBytes);
            os.flush();
            os.close();
            connection.getInputStream(); //do not remove this line. request will not work without it gg
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Log.d(TAG, "Successfully send");
            } else {
                Log.d(TAG, "request not send, error: " + String.valueOf(connection.getResponseCode()));
            }

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
