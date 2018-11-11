package indecent.men.attendance;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ProgrammingAdapter extends RecyclerView.Adapter<ProgrammingAdapter.ProgrammingViewHolder> {

    private String[] data1,data2;
    ProgrammingAdapter(String[] data1, String[] data2)
        {
            this.data1 = data1;
            this.data2 = data2;
        }
    @NonNull
    @Override
    public ProgrammingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        View view = inflater.inflate(R.layout.list_item_layout,viewGroup,false );
        return new ProgrammingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgrammingViewHolder programmingViewHolder, int i) {
        String subjectData = data1[i];
        String attendanceData=data2[i];
        programmingViewHolder.t1.setText("  "+subjectData);
        programmingViewHolder.t2.setText(attendanceData+" %");
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) programmingViewHolder.helper.getLayoutParams();
        params.guidePercent = Float.parseFloat(attendanceData)/100; // 45% // range: 0 <-> 1
        programmingViewHolder.helper.setLayoutParams(params);    }

    @Override
    public int getItemCount() {
        return data1.length;
    }

    class ProgrammingViewHolder extends RecyclerView.ViewHolder{
        TextView t1;
        TextView t2;
        Guideline helper;
        ConstraintLayout constraintLayout;
        ProgrammingViewHolder(@NonNull View itemView) {
            super(itemView);
            t1 = itemView.findViewById(R.id.textView4);
            t2 = itemView.findViewById(R.id.textView5);
            helper = itemView.findViewById(R.id.guideline2);
        }
    }
}

