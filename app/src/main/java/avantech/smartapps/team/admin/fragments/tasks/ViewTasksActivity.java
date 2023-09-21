package avantech.smartapps.team.admin.fragments.tasks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import avantech.smartapps.team.R;
import avantech.smartapps.team.model.TasksModel;

public class ViewTasksActivity extends AppCompatActivity {
    String projectTitle;
    private static final String TAG = "VIEW TASKS ACTIVITY";
    RecyclerView viewTasksRecycleView;
    ArrayList<TasksModel> tasksData;
    FloatingActionButton fab;
    FirebaseFirestore mFirebaseFirestore;
    CollectionReference mTasksCollection;
    ProgressBar progressBar;
    TasksAdapter tasksAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tasks);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        projectTitle = intent.getStringExtra("enteredProjectTitle");
        Log.d(TAG, "Enterd project through intent is : "+projectTitle);
        setTitle("Tasks for "+projectTitle);
        fab = findViewById(R.id.fab);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        mTasksCollection = mFirebaseFirestore.collection("Tasks");
        viewTasksRecycleView = findViewById(R.id.viewTasksRecycleView);
        viewTasksRecycleView.setLayoutManager(new LinearLayoutManager(ViewTasksActivity.this));
        tasksData = new ArrayList<>();
        tasksAdapter = new TasksAdapter(tasksData, ViewTasksActivity.this);
        viewTasksRecycleView.setAdapter(tasksAdapter);
        mTasksCollection
                .whereEqualTo("projectTitle",projectTitle)
                .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot documentSnapshots) {
                                progressBar.setVisibility(View.GONE);
                                List<DocumentSnapshot> list = documentSnapshots.getDocuments();
                                for(DocumentSnapshot d : list){
                                    TasksModel tasksModel = d.toObject(TasksModel.class);
                                    tasksData.add(tasksModel);
                                }
                                tasksAdapter.notifyDataSetChanged();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ViewTasksActivity.this, "No data", Toast.LENGTH_SHORT).show();
                    }
                });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewTasksActivity.this, AddTasksActivity.class);
                intent.putExtra("enteredProjectTitle", projectTitle);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}