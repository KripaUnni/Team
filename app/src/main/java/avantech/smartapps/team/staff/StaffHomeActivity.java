package avantech.smartapps.team.staff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import avantech.smartapps.team.R;
import avantech.smartapps.team.admin.AdminHomeActivity;
import avantech.smartapps.team.admin.fragments.projects.ProjectsAdapter;
import avantech.smartapps.team.common.EditProfileActivity;
import avantech.smartapps.team.common.LoginActivity;
import avantech.smartapps.team.common.SharedPreferences;
import avantech.smartapps.team.model.ProjectsModel;

public class StaffHomeActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    String empID;
    Context context;
    RecyclerView viewProjectsRecycleView;
    ArrayList<ProjectsModel> projectsData;
    FirebaseFirestore mFirebaseFirestore;
    ProjectsAdapter projectsAdapter;
    private static final String TAG = "STAFF HOME ACTIVITY";
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_home);
        sharedPreferences = new SharedPreferences(StaffHomeActivity.this);
        setTitle("Projects assigned to you");
        Intent intent = getIntent();
        empID = intent.getStringExtra("enteredID");
        Log.d(TAG, "Enterd ID through intent is : "+empID);
        viewProjectsRecycleView = findViewById(R.id.viewProjectsRecycleView);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        viewProjectsRecycleView.setLayoutManager(new LinearLayoutManager(StaffHomeActivity.this));
        projectsData = new ArrayList<>();
        projectsAdapter = new ProjectsAdapter(projectsData, StaffHomeActivity.this);
        viewProjectsRecycleView.setAdapter(projectsAdapter);
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        mFirebaseFirestore.collection("Projects")
                .whereEqualTo("assignedTo",sharedPreferences.getNAME())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        progressBar.setVisibility(View.GONE);
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d : list){
                            ProjectsModel projectsModel = d.toObject(ProjectsModel.class);
                            projectsData.add(projectsModel);
                        }
                        projectsAdapter.notifyDataSetChanged();
                    }
                });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.staff_home, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch (id) {
            case R.id.profile:
                SharedPreferences sharedPref = new SharedPreferences(StaffHomeActivity.this);
                startActivity(new Intent(StaffHomeActivity.this, EditProfileActivity.class));
                return true;

            case R.id.logout:
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Log Out");
                alert.setMessage("Are you sure you want to Logout?");
                alert.setCancelable(false);
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(StaffHomeActivity.this, LoginActivity.class));
                        android.content.SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
                        android.content.SharedPreferences.Editor editor = sp.edit();
                        editor.clear();
                        editor.commit();
                        finish();
                    }
                });
                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.show();
                return true;

            default:
        }
        return super.onOptionsItemSelected(item);
    }
}