package avantech.smartapps.team.admin.fragments.projects;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import avantech.smartapps.team.R;
import avantech.smartapps.team.admin.fragments.employees.AddEmployeesActivity;
import avantech.smartapps.team.admin.fragments.employees.EmployeesAdapter;
import avantech.smartapps.team.model.EmployeesModel;
import avantech.smartapps.team.model.ProjectsModel;

public class ProjectsFragment extends Fragment {
    View v;
    RecyclerView viewProjectsRecycleView;
    FloatingActionButton fab;
    ArrayList<ProjectsModel> projectsData;
    FirebaseFirestore mFirebaseFirestore;
    ProjectsAdapter projectsAdapter;
    private static final String TAG = "PROJECT FRAGMENT";
    ProgressBar progressBar;

    private ProjectsViewModel mViewModel;

    public static ProjectsFragment newInstance() {
        return new ProjectsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_projects, container, false);
        fab = v.findViewById(R.id.fab);
        viewProjectsRecycleView = v.findViewById(R.id.viewProjectsRecycleView);
        progressBar = v.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        viewProjectsRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        projectsData = new ArrayList<>();
        projectsAdapter = new ProjectsAdapter(projectsData, getContext());
        viewProjectsRecycleView.setAdapter(projectsAdapter);
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        mFirebaseFirestore.collection("Projects")
                .orderBy("startDate", Query.Direction.ASCENDING)
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
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "No data", Toast.LENGTH_SHORT).show();
                    }
                });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddProjectsActivity.class);
                startActivity(intent);
            }
        });

        return v;
    }

}