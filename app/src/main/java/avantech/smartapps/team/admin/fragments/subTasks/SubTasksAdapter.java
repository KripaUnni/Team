package avantech.smartapps.team.admin.fragments.subTasks;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import avantech.smartapps.team.R;
import avantech.smartapps.team.admin.fragments.tasks.EditTasksActivity;
import avantech.smartapps.team.admin.fragments.tasks.TasksAdapter;
import avantech.smartapps.team.common.SharedPreferences;
import avantech.smartapps.team.model.SubTasksModel;
import avantech.smartapps.team.model.TasksModel;

public class SubTasksAdapter extends RecyclerView.Adapter <SubTasksAdapter.ViewHolder>{
    ArrayList<SubTasksModel> subTasksData;
    Context applicationContext;
    SharedPreferences sharedPreferences;
    private static final String TAG = "SUB TASKS";
    public SubTasksAdapter(ArrayList<SubTasksModel> subTasksData, Context applicationContext) {
        this.subTasksData = subTasksData;
        this.applicationContext = applicationContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_task_list_item,parent,false);
        sharedPreferences = new SharedPreferences(applicationContext);
        return (new ViewHolder(view));
    }

    @Override
    public void onBindViewHolder(@NonNull SubTasksAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.title.setText(subTasksData.get(position).getTitle());
        holder.priority.setText(subTasksData.get(position).getPriority());
        holder.start.setText(subTasksData.get(position).getStartDate());
        holder.due.setText(subTasksData.get(position).getDueDate());
        holder.notes.setText(subTasksData.get(position).getNotes());
        holder.status.setText(subTasksData.get(position).getStatus());
        if(subTasksData.get(position).getStartDate() == null){
            holder.date.setVisibility(View.GONE);
        }else{
            holder.date.setVisibility(View.VISIBLE);
        }
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(applicationContext, EditSubTasksActivity.class);
                intent.putExtra("enteredProjectTitle", subTasksData.get(position).getProjectTitle());
                intent.putExtra("enteredTaskTitle", subTasksData.get(position).getTaskTitle());
                intent.putExtra("enteredSubTaskId", subTasksData.get(position).getId());
                intent.putExtra("enteredSubTaskTitle", subTasksData.get(position).getTitle());
                intent.putExtra("enteredSubTaskPriority", subTasksData.get(position).getPriority());
                intent.putExtra("enteredSubTaskStartDate", subTasksData.get(position).getStartDate());
                intent.putExtra("enteredSubTaskDueDate", subTasksData.get(position).getDueDate());
                intent.putExtra("enteredSubTaskStatus", subTasksData.get(position).getStatus());
                intent.putExtra("enteredSubTaskNotes", subTasksData.get(position).getNotes());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                applicationContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return subTasksData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CardView sub_task_item;
        ImageButton edit;
        TextView title,priority,start,due,notes,status;
        LinearLayout date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sub_task_item = itemView.findViewById(R.id.sub_task_item);
            title = itemView.findViewById(R.id.title);
            priority = itemView.findViewById(R.id.priority);
            start = itemView.findViewById(R.id.start);
            due = itemView.findViewById(R.id.due);
            notes = itemView.findViewById(R.id.notes);
            status = itemView.findViewById(R.id.status);
            edit = itemView.findViewById(R.id.edit);
            date = itemView.findViewById(R.id.date);
        }
    }
}
