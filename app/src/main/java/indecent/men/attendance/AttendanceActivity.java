package indecent.men.attendance;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class AttendanceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] subjectData={  "Physics",
                                "Chemistry",
                                "Biology"};
        String[] attendanceData={   "75%",
                                    "80%",
                                    "40%"};
        setContentView(R.layout.activity_attendance);
        RecyclerView programmingList = findViewById(R.id.my_recycler_view);
        programmingList.setLayoutManager(new LinearLayoutManager(this));
        programmingList.setAdapter(new ProgrammingAdapter(subjectData,attendanceData));
    }
}
