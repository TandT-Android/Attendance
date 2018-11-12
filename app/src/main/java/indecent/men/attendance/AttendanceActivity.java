package indecent.men.attendance;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class AttendanceActivity extends AppCompatActivity {
    FirebaseDatabase db;
    FirebaseAuth firebaseAuth;
    DatabaseReference rootRef;
    FirebaseUser user;
    TextView nameTextView;
    TextView rollNumberTextView;
    Spinner spinner;
    String semester;
    String userRollNumber;
    String chId="0009";

    Map<String,String> attendanceMap;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.logout)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationGenerate(1);
            }
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(AttendanceActivity.this,SignInActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void notificationGenerate(int id)
    {String nTitle="";
        switch (id)
        {
            case 0:
                nTitle="User Signed IN";
                break;
            case 1:
                nTitle="User Logged Out";
        }

        Notification.Builder notificationBuilder=new Notification.Builder(this).setContentTitle(nTitle)
                .setAutoCancel(true).setChannelId(chId).setSmallIcon(R.drawable.kiit);
        NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel notificationChannel=new NotificationChannel(chId,"NOTIFICATION",NotificationManager.IMPORTANCE_HIGH);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(notificationChannel);
            notificationManager.notify(0,notificationBuilder.build());
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        CircleImageView circleImageView=findViewById(R.id.circleImageView);

        semester = "5";
        String[] semesters = {"Semester 5","Semester 4","Semester 3","Semester 2","Semester 1"};

        attendanceMap = new HashMap<>();

        nameTextView = findViewById(R.id.textView);
        rollNumberTextView = findViewById(R.id.textView3);
        spinner = findViewById(R.id.spinner);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        rootRef = db.getReference();

        user = firebaseAuth.getCurrentUser();

        if (user == null){
            Intent intent = new Intent(AttendanceActivity.this,SignInActivity.class);
            startActivity(intent);
            finish();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationGenerate(0);
        }
        String userEmail = user.getEmail();
        userRollNumber = userEmail.substring(0,userEmail.lastIndexOf('@'));

        nameTextView.setText(String.format("Name : %s", user.getDisplayName()));
        Glide.with(this).load(user.getPhotoUrl()).into(circleImageView);
        rollNumberTextView.setText(String.format("Roll Number : %s", userRollNumber));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(AttendanceActivity.this, "Loading attendance of semester "+ String.valueOf(5-i), Toast.LENGTH_SHORT).show();
                findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                findViewById(R.id.my_recycler_view).setVisibility(View.INVISIBLE);
                semester = String.valueOf(5-i);
                loadData(rootRef.child("Users").child(userRollNumber).child(semester));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        SpinnerAdapter aa = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,semesters);
        spinner.setAdapter(aa);

    }

    private void displayData() {
        String[] subjectData = attendanceMap.keySet().toArray(new String[0]);
        String[] attendanceData = attendanceMap.values().toArray(new String[0]);

        RecyclerView programmingList = findViewById(R.id.my_recycler_view);
        programmingList.setLayoutManager(new LinearLayoutManager(this));
        programmingList.setAdapter(new ProgrammingAdapter(subjectData,attendanceData));

        findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
        findViewById(R.id.my_recycler_view).setVisibility(View.VISIBLE);
    }

    void loadData(final DatabaseReference semesterRef){

        semesterRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                attendanceMap.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot attendance : dataSnapshot.getChildren()) {
                        attendanceMap.put(attendance.getKey(), String.valueOf(attendance.getValue()));
                        displayData();
                    }
                } else {
                    Log.i("Error","No entry found");
                    int bound = 49;
                    int startingLimit = 50;
                    switch (semester) {
                        case "1" :
                            attendanceMap.put("CHEM",String.valueOf(new Random().nextInt(bound) + startingLimit));
                            attendanceMap.put("EVS",String.valueOf(new Random().nextInt(bound) + startingLimit));
                            attendanceMap.put("PC",String.valueOf(new Random().nextInt(bound) + startingLimit));
                            attendanceMap.put("PCOM",String.valueOf(new Random().nextInt(bound) + startingLimit));
                            attendanceMap.put("BETC",String.valueOf(new Random().nextInt(bound) + startingLimit));
                            attendanceMap.put("MATH 1",String.valueOf(new Random().nextInt(bound) + startingLimit));

                            semesterRef.setValue(attendanceMap);
                            break;
                        case "2" :
                            attendanceMap.put("MATH 2",String.valueOf(new Random().nextInt(bound) + startingLimit));
                            attendanceMap.put("MECH",String.valueOf(new Random().nextInt(bound) + startingLimit));
                            attendanceMap.put("BEE",String.valueOf(new Random().nextInt(bound) + startingLimit));
                            attendanceMap.put("OOPS",String.valueOf(new Random().nextInt(bound) + startingLimit));
                            attendanceMap.put("PHY",String.valueOf(new Random().nextInt(bound) + startingLimit));

                            semesterRef.setValue(attendanceMap);
                            break;
                        case "3" :
                            attendanceMap.put("DSA",String.valueOf(new Random().nextInt(bound) + startingLimit));
                            attendanceMap.put("DEC",String.valueOf(new Random().nextInt(bound) + startingLimit));
                            attendanceMap.put("WT",String.valueOf(new Random().nextInt(bound) + startingLimit));
                            attendanceMap.put("DMS",String.valueOf(new Random().nextInt(bound) + startingLimit));
                            attendanceMap.put("MATH 3",String.valueOf(new Random().nextInt(bound) + startingLimit));

                            semesterRef.setValue(attendanceMap);
                            break;
                        case "4" :
                            attendanceMap.put("PDC",String.valueOf(new Random().nextInt(bound) + startingLimit));
                            attendanceMap.put("COA",String.valueOf(new Random().nextInt(bound) + startingLimit));
                            attendanceMap.put("DBMS",String.valueOf(new Random().nextInt(bound) + startingLimit));
                            attendanceMap.put("DAA",String.valueOf(new Random().nextInt(bound) + startingLimit));
                            attendanceMap.put("MATH 4",String.valueOf(new Random().nextInt(bound) + startingLimit));
                            attendanceMap.put("ECO",String.valueOf(new Random().nextInt(bound) + startingLimit));

                            semesterRef.setValue(attendanceMap);
                            break;
                        case "5" :
                            attendanceMap.put("OS",String.valueOf(new Random().nextInt(bound) + startingLimit));
                            attendanceMap.put("CN",String.valueOf(new Random().nextInt(bound) + startingLimit));
                            attendanceMap.put("FLA",String.valueOf(new Random().nextInt(bound) + startingLimit));
                            attendanceMap.put("SE",String.valueOf(new Random().nextInt(bound) + startingLimit));
                            attendanceMap.put("HPCA",String.valueOf(new Random().nextInt(bound) + startingLimit));

                            semesterRef.setValue(attendanceMap);
                            break;
                        default :
                            break;
                    }
                    displayData();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("Database ","Error");
            }
        });
    }
}
