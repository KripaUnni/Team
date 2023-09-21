package avantech.smartapps.team.admin.fragments.tasks;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import avantech.smartapps.team.R;
import avantech.smartapps.team.admin.fragments.subTasks.ViewSubTasksActivity;
import avantech.smartapps.team.common.SharedPreferences;
import avantech.smartapps.team.model.TasksModel;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder> {
    ArrayList<TasksModel> tasksData;
    Context applicationContext;
    SharedPreferences sharedPreferences;
    private static final String TAG = "TASKS ADAPTER";
    public TasksAdapter(ArrayList<TasksModel> tasksData, Context applicationContext) {
        this.tasksData = tasksData;
        this.applicationContext = applicationContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_list_item,parent,false);
        sharedPreferences = new SharedPreferences(applicationContext);
        return (new ViewHolder(view));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.title.setText(tasksData.get(position).getTitle());
        holder.priority.setText(tasksData.get(position).getPriority());
        holder.priority.setTextColor(tasksData.get(position).getPriority().equals("High") ? Color.RED : Color.parseColor("#FF018786"));
        holder.start.setText(tasksData.get(position).getStartDate());
        holder.due.setText(tasksData.get(position).getDueDate());
        holder.notes.setText(tasksData.get(position).getNotes());
        holder.status.setText(tasksData.get(position).getStatus());
        if(tasksData.get(position).getStartDate() == null){
            holder.date.setVisibility(View.GONE);
        }else{
            holder.date.setVisibility(View.VISIBLE);
        }
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(applicationContext, EditTasksActivity.class);
                intent.putExtra("enteredProjectTitle", tasksData.get(position).getProjectTitle());
                intent.putExtra("enteredTaskId", tasksData.get(position).getId());
                intent.putExtra("enteredTaskTitle", tasksData.get(position).getTitle());
                intent.putExtra("enteredTaskPriority", tasksData.get(position).getPriority());
                intent.putExtra("enteredTaskStartDate", tasksData.get(position).getStartDate());
                intent.putExtra("enteredTaskDueDate", tasksData.get(position).getDueDate());
                intent.putExtra("enteredTaskStatus", tasksData.get(position).getStatus());
                intent.putExtra("enteredTaskNotes", tasksData.get(position).getNotes());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                applicationContext.startActivity(intent);
            }
        });
        holder.task_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(applicationContext, ViewSubTasksActivity.class);
                intent.putExtra("enteredProjectTitle", tasksData.get(position).getProjectTitle());
                intent.putExtra("enteredTaskTitle", tasksData.get(position).getTitle());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                applicationContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasksData.size();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder{
        LinearLayout task_item, date;
        ImageView edit;
        TextView title,priority,start,due,notes,status;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            task_item = itemView.findViewById(R.id.task_item);
            title = itemView.findViewById(R.id.title);
            priority = itemView.findViewById(R.id.priority);
            date = itemView.findViewById(R.id.date);
            start = itemView.findViewById(R.id.start);
            due = itemView.findViewById(R.id.due);
            notes = itemView.findViewById(R.id.notes);
            status = itemView.findViewById(R.id.status);
            edit = itemView.findViewById(R.id.edit);
        }
    }
}
