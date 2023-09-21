package avantech.smartapps.team.admin.fragments.subTasks;

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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import avantech.smartapps.team.R;
import avantech.smartapps.team.model.SubTasksModel;

public class ViewSubTasksActivity extends AppCompatActivity {
    String projectTitle,taskTitle;
    private static final String TAG = "VIEW SUBTASKS ACTIVITY";
    RecyclerView viewSubTasksRecycleView;
    ArrayList<SubTasksModel> subTasksData;
    FloatingActionButton fab;
    FirebaseFirestore mFirebaseFirestore;
    CollectionReference mSubTasksCollection;
    ProgressBar progressBar;
    SubTasksAdapter subTasksAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_sub_tasks);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        projectTitle = intent.getStringExtra("enteredProjectTitle");
        Log.d(TAG, "Entered project through intent is : "+projectTitle);
        taskTitle = intent.getStringExtra("enteredTaskTitle");
        Log.d(TAG, "Entered task through intent is : "+taskTitle);
        setTitle("Subtasks for "+taskTitle);
        fab = findViewById(R.id.fab);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        mSubTasksCollection = mFirebaseFirestore.collection("SubTasks");
        viewSubTasksRecycleView = findViewById(R.id.viewSubTasksRecycleView);
        viewSubTasksRecycleView.setLayoutManager(new LinearLayoutManager(ViewSubTasksActivity.this));
        subTasksData = new ArrayList<>();
        subTasksAdapter = new SubTasksAdapter(subTasksData, ViewSubTasksActivity.this);
        viewSubTasksRecycleView.setAdapter(subTasksAdapter);
        mSubTasksCollection.whereEqualTo("projectTitle",projectTitle)
                .whereEqualTo("taskTitle",taskTitle)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        progressBar.setVisibility(View.GONE);
                        List<DocumentSnapshot> list = documentSnapshots.getDocuments();
                        for(DocumentSnapshot d : list){
                            SubTasksModel subTasksModel = d.toObject(SubTasksModel.class);
                            subTasksData.add(subTasksModel);
                        }
                        subTasksAdapter.notifyDataSetChanged();
                    }
                });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewSubTasksActivity.this, AddSubTasksActivity.class);
                intent.putExtra("enteredProjectTitle", projectTitle);
                intent.putExtra("enteredTaskTitle", taskTitle);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
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