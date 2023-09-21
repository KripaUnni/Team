package avantech.smartapps.team.admin.fragments.projects;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import avantech.smartapps.team.R;
import avantech.smartapps.team.admin.AdminHomeActivity;
import avantech.smartapps.team.admin.fragments.tasks.ViewTasksActivity;
import avantech.smartapps.team.common.LoginActivity;
import avantech.smartapps.team.common.SharedPreferences;
import avantech.smartapps.team.model.ProjectsModel;

public class ProjectsAdapter extends RecyclerView.Adapter<ProjectsAdapter.ViewHolder> {
    ArrayList<ProjectsModel> projectsData;
    Context applicationContext;
    SharedPreferences sharedPreferences;
    private static final String TAG = "PROJECTS FRAGMENT";
    public ProjectsAdapter(ArrayList<ProjectsModel> projectsData, Context applicationContext) {
        this.projectsData = projectsData;
        this.applicationContext = applicationContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_list_item,parent,false);
        sharedPreferences = new SharedPreferences(applicationContext);
        return (new ViewHolder(view));
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectsAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.title.setText(projectsData.get(position).getTitle());
        holder.client.setText(projectsData.get(position).getClient());
        holder.type.setText(projectsData.get(position).getType());
        holder.status.setText(projectsData.get(position).getStatus());
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(applicationContext, EditProjectsActivity.class);
                intent.putExtra("enteredProjectId",projectsData.get(position).getId());
                intent.putExtra("enteredProjectTitle", projectsData.get(position).getTitle());
                intent.putExtra("enteredProjectClient", projectsData.get(position).getClient());
                intent.putExtra("enteredProjectType", projectsData.get(position).getType());
                intent.putExtra("enteredProjectPriority", projectsData.get(position).getPriority());
                intent.putExtra("enteredProjectAssignedTo", projectsData.get(position).getAssignedTo());
                intent.putExtra("enteredProjectAssistedBy", projectsData.get(position).getAssistedBy());
                intent.putExtra("enteredProjectStartDate", projectsData.get(position).getStartDate());
                intent.putExtra("enteredProjectDeadline", projectsData.get(position).getDeadLine());
                intent.putExtra("enteredProjectDueDate", projectsData.get(position).getDueDate());
                intent.putExtra("enteredProjectStatus", projectsData.get(position).getStatus());
                intent.putExtra("enteredProjectNotes", projectsData.get(position).getNotes());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                applicationContext.startActivity(intent);
            }
        });

        holder.linear.setBackgroundColor(position % 2 == 0 ? Color.WHITE : Color.parseColor("#DEE7FA"));
        holder.project_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(applicationContext, ViewTasksActivity.class);
                intent.putExtra("enteredProjectTitle", projectsData.get(position).getTitle());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                applicationContext.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return projectsData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout linear;
        CardView project_item;
        TextView title,client,type,status;
        ImageView edit;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            project_item = itemView.findViewById(R.id.project_item);
            linear = itemView.findViewById(R.id.linear);
            title = itemView.findViewById(R.id.title);
            client = itemView.findViewById(R.id.client);
            type = itemView.findViewById(R.id.type);
            status = itemView.findViewById(R.id.status);
            edit = itemView.findViewById(R.id.edit);
            if(sharedPreferences.getTYPE().equals("employee")){
                edit.setVisibility(View.GONE);
            }
        }
    }
}
