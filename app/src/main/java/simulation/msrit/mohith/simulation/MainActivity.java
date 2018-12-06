package simulation.msrit.mohith.simulation;

import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private final String Channel="personal_notifications";
    private final int Channel_id=001;
    Button a,b,c,n;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        a=(Button)findViewById(R.id.button);
        b=(Button)findViewById(R.id.button2);
        n=(Button)findViewById(R.id.button4);



        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,Chat.class);
                startActivity(i);
            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,Timeline.class);
                startActivity(i);
            }
        });

        n.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationCompat.Builder builder=new NotificationCompat.Builder(getApplicationContext(),Channel);
                builder.setSmallIcon(R.drawable.ic_phone_black_24dp);
                builder.setContentTitle("Warning!");
                builder.setContentText("Are you bullying?");
                builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

                NotificationManagerCompat n1= NotificationManagerCompat.from(getApplicationContext());
                n1.notify(Channel_id,builder.build());

            }
        });






    }


}
