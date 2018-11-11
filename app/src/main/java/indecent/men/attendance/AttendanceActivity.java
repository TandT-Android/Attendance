package indecent.men.attendance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

public class AttendanceActivity extends AppCompatActivity {
    FirebaseDatabase db;
    FirebaseAuth firebaseAuth;
    DatabaseReference rootRef;
    private FirebaseUser user;
    private TextView nameTextView;
    private TextView rollNumberTextView;
    Spinner spinner;
    String semester;
    String userRollNumber;

    Map<String,String> attendanceMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

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

        String userEmail = user.getEmail();
        userRollNumber = userEmail.substring(0,userEmail.lastIndexOf('@'));

        nameTextView.setText(String.format("Name : %s", user.getDisplayName()));
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

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,semesters);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
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
                    switch (semester) {
                        case "1" :
                            attendanceMap.put("CHEM",String.valueOf(new Random().nextInt(66) + 30));
                            attendanceMap.put("EVS",String.valueOf(new Random().nextInt(66) + 30));
                            attendanceMap.put("PC",String.valueOf(new Random().nextInt(66) + 30));
                            attendanceMap.put("PCOM",String.valueOf(new Random().nextInt(66) + 30));
                            attendanceMap.put("BETC",String.valueOf(new Random().nextInt(66) + 30));
                            attendanceMap.put("MATH 1",String.valueOf(new Random().nextInt(66) + 30));

                            semesterRef.setValue(attendanceMap);
                            break;
                        case "2" :
                            attendanceMap.put("MATH 2",String.valueOf(new Random().nextInt(66) + 30));
                            attendanceMap.put("MECH",String.valueOf(new Random().nextInt(66) + 30));
                            attendanceMap.put("BEE",String.valueOf(new Random().nextInt(66) + 30));
                            attendanceMap.put("OOPS",String.valueOf(new Random().nextInt(66) + 30));
                            attendanceMap.put("PHY",String.valueOf(new Random().nextInt(66) + 30));

                            semesterRef.setValue(attendanceMap);
                            break;
                        case "3" :
                            attendanceMap.put("DSA",String.valueOf(new Random().nextInt(66) + 30));
                            attendanceMap.put("DEC",String.valueOf(new Random().nextInt(66) + 30));
                            attendanceMap.put("WT",String.valueOf(new Random().nextInt(66) + 30));
                            attendanceMap.put("DMS",String.valueOf(new Random().nextInt(66) + 30));
                            attendanceMap.put("MATH 3",String.valueOf(new Random().nextInt(66) + 30));

                            semesterRef.setValue(attendanceMap);
                            break;
                        case "4" :
                            attendanceMap.put("PDC",String.valueOf(new Random().nextInt(66) + 30));
                            attendanceMap.put("COA",String.valueOf(new Random().nextInt(66) + 30));
                            attendanceMap.put("DBMS",String.valueOf(new Random().nextInt(66) + 30));
                            attendanceMap.put("DAA",String.valueOf(new Random().nextInt(66) + 30));
                            attendanceMap.put("MATH 4",String.valueOf(new Random().nextInt(66) + 30));
                            attendanceMap.put("ECO",String.valueOf(new Random().nextInt(66) + 30));

                            semesterRef.setValue(attendanceMap);
                            break;
                        case "5" :
                            attendanceMap.put("OS",String.valueOf(new Random().nextInt(66) + 30));
                            attendanceMap.put("CN",String.valueOf(new Random().nextInt(66) + 30));
                            attendanceMap.put("FLA",String.valueOf(new Random().nextInt(66) + 30));
                            attendanceMap.put("SE",String.valueOf(new Random().nextInt(66) + 30));
                            attendanceMap.put("HPCA",String.valueOf(new Random().nextInt(66) + 30));

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

            }
        });
    }
}
