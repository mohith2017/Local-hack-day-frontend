package simulation.msrit.mohith.simulation;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import simulation.msrit.mohith.simulation.Adapter.CustomAdapter;
import simulation.msrit.mohith.simulation.Helper.HttpDataHandler;
import simulation.msrit.mohith.simulation.Models.Chatmodel;
import simulation.msrit.mohith.simulation.Models.SimsimiModel;

public class Chat extends AppCompatActivity {
    ListView listView;
    EditText editText;
    private DatabaseReference mDatabase;
    List<Chatmodel> list_chat = new ArrayList<>();
    FloatingActionButton btn_send_message;
    String yes;

//    private Notification.Builder builder;
//    private NotificationManager manager;
//    private int not_id;
//    private RemoteViews remoteViews;
//    private Context context;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        listView =(ListView)

                findViewById(R.id.list_of_message);
        mDatabase= FirebaseDatabase.getInstance().getReference("messages");


        editText =(EditText)

                findViewById(R.id.user_message);

        btn_send_message =(FloatingActionButton)

                findViewById(R.id.fab);

//        context=this;
//        manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
//        remoteViews=new RemoteViews(getPackageName(),R.layout.custom_notification);
//
//        remoteViews.setImageViewResource(R.id.imageView2,android.R.drawable.ic_dialog_alert);
//
//        not_id=(int)System.currentTimeMillis();
//        Intent danger=new Intent("danger");
//        danger.putExtra("id",not_id);
//
//        PendingIntent button_intent= PendingIntent.getBroadcast(context,123,danger,0);
//        remoteViews.setOnClickPendingIntent(R.id.button3,button_intent);



        btn_send_message.setOnClickListener(new View.OnClickListener()

        {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick (View v){
                String text = editText.getText().toString();
                 writeNewUser(text);

                Chatmodel model = new Chatmodel(text, true); // user send message
                list_chat.add(model);
                new SimsimiAPI().execute(list_chat);

                //remove user message
                editText.setText("");

//                Intent notification_intent=new Intent(context,Chat.class);
//                PendingIntent pendingintent=PendingIntent.getActivity(context,0,notification_intent,0);
//
//                builder = new Notification.Builder(context);
//                builder.setSmallIcon(R.mipmap.ic_launcher)
//                        .setAutoCancel(true)
//                        .setCustomBigContentView(remoteViews)
//                        .setContentIntent(pendingintent);
//
//                manager.notify(not_id,builder.build());
              checkUser();


            }
        });
    }


    private class SimsimiAPI extends AsyncTask<List<Chatmodel>,Void,String> {
        String stream = null;
        List<Chatmodel> models;
        String text = editText.getText().toString();

        @Override
        protected String doInBackground(List<Chatmodel>... params) {
            String url = String.format("http://sandbox.api.simsimi.com/request.p?key=%s&lc=en&ft=1.0&text=%s",getString(R.string.simsimi_api),text);
            models = params[0];
            HttpDataHandler httpDataHandler = new HttpDataHandler();
            stream = httpDataHandler.GetHTTPData(url);
            return stream;
        }

        @Override
        protected void onPostExecute(String s) {
            Gson gson = new Gson();
            SimsimiModel response = gson.fromJson(s,SimsimiModel.class);

            Chatmodel chatModel = new Chatmodel(response.getResponse(),false); // get response from simsimi
            models.add(chatModel);
            CustomAdapter adapter = new CustomAdapter(models,getApplicationContext());
            listView.setAdapter(adapter);

        }
    }
    private void writeNewUser(String userId) {

        mDatabase.child("isCyberBullying").setValue("NO");
        mDatabase.child("msg").setValue(userId).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(),"added",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkUser() {

        mDatabase.child("isCyberBullying").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot.getValue().toString()=="NO")
                {
                    yes="This is an accepted message";
                }
                else
                {
                    yes="Do you want help?";
                    Toast.makeText(getApplicationContext(),yes,Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }
}
