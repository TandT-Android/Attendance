package indecent.men.attendance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AttendanceActivity extends AppCompatActivity {
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference rootRef=db.getReference();
    FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private TextView nameTextView;
    private TextView rollNumberTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        nameTextView = findViewById(R.id.textView);
        rollNumberTextView = findViewById(R.id.textView3);

        firebaseAuth = FirebaseAuth.getInstance();

        user = firebaseAuth.getCurrentUser();

        if (user == null){
            Intent intent = new Intent(AttendanceActivity.this,SignInActivity.class);
            startActivity(intent);
            finish();
        }

        String userEmail = user.getEmail();
        String userRollNumber = userEmail.substring(0,userEmail.lastIndexOf('@'));

        nameTextView.setText(String.format("Name : %s", user.getDisplayName()));
        rollNumberTextView.setText(String.format("Roll Number : %s", userRollNumber));


        String[] subjectData={  "Physics",
                                "Chemistry",
                                "Biology"};
        String[] attendanceData={   "75%",
                                    "80%",
                                    "40%"};
        rootRef.child("user1").child("Name").setValue(" Abhijeet ");
        rootRef.child("user1").child("semester").setValue(" 2nd ");
        rootRef.child("user1").child("Rollno").setValue(" 1705257 ");
        rootRef.child("user1").child("subjects").child(" Mathematic-II ").setValue(" 72% ");
        rootRef.child("user1").child("subjects").child(" BEE ").setValue(" 78% ");
        rootRef.child("user1").child("subjects").child(" Mechanics ").setValue(" 82% ");
        rootRef.child("user1").child("subjects").child(" Physics ").setValue(" 89% ");
        rootRef.child("user1").child("subjects").child(" OOPs- ").setValue(" 76% ");

        rootRef.child("user2").child("Name").setValue(" Abhay ");
        rootRef.child("user2").child("semester").setValue(" 2nd ");
        rootRef.child("user2").child("Rollno").setValue(" 1705277 ");
        rootRef.child("user2").child("subjects").child(" Mathematic-II ").setValue(" 55% ");
        rootRef.child("user2").child("subjects").child(" BEE ").setValue(" 58% ");
        rootRef.child("user2").child("subjects").child(" Mechanics ").setValue(" 52% ");
        rootRef.child("user2").child("subjects").child(" Physics ").setValue(" 59% ");
        rootRef.child("user2").child("subjects").child(" OOPs- ").setValue(" 76% ");

        rootRef.child("user3").child("Name").setValue(" Aryan ");
        rootRef.child("user3").child("semester").setValue(" 5th ");
        rootRef.child("user3").child("Rollno").setValue(" 1605345 ");
        rootRef.child("user3").child("subjects").child("OS").setValue("72.55%");
        rootRef.child("user3").child("subjects").child("SE").setValue("76.3%");
        rootRef.child("user3").child("subjects").child("CN").setValue("82.7%");
        rootRef.child("user3").child("subjects").child("FLA").setValue("76%");
        rootRef.child("user3").child("subjects").child("HPCA").setValue("77%");

        rootRef.child("user4").child("Name").setValue("Amulya ");
        rootRef.child("user4").child("semester").setValue("3th");
        rootRef.child("user4").child("Rollno").setValue("1706237");
        rootRef.child("user4").child("subjects").child("Numerical").setValue(" 65% ");
        rootRef.child("user4").child("subjects").child("Structural Analysis").setValue(" 78% ");
        rootRef.child("user4").child("subjects").child("Advanced Surveying").setValue(" 56% ");
        rootRef.child("user4").child("subjects").child("Fluid Mechanics").setValue(" 79% ");
        rootRef.child("user4").child("subjects").child("Transportation Engineering").setValue(" 76% ");


        RecyclerView programmingList = findViewById(R.id.my_recycler_view);
        programmingList.setLayoutManager(new LinearLayoutManager(this));
        programmingList.setAdapter(new ProgrammingAdapter(subjectData,attendanceData));
    }
}
