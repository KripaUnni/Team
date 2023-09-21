package avantech.smartapps.team.admin.fragments.employees;

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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import avantech.smartapps.team.R;
import avantech.smartapps.team.common.SharedPreferences;
import avantech.smartapps.team.model.EmployeesModel;

public class EmployeesFragment extends Fragment {
    View v;
    RecyclerView viewEmployeesRecycleView;
    FloatingActionButton fab;
    ArrayList<EmployeesModel> employeesData;
    FirebaseFirestore mFirebaseFirestore;
    EmployeesAdapter employeesAdapter;
    private static final String TAG = "EMPLOYEES FRAGMENT";
    ProgressBar progressBar;
    SharedPreferences sharedPreferences;

    private EmployeesViewModel mViewModel;

    public static EmployeesFragment newInstance() {
        return new EmployeesFragment();
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_employees, container, false);

        FloatingActionButton fab = v.findViewById(R.id.fab);
        viewEmployeesRecycleView = v.findViewById(R.id.viewEmployeesRecycleView);
        progressBar = v.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        viewEmployeesRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        employeesData = new ArrayList<>();
        employeesAdapter = new EmployeesAdapter(employeesData, getContext());
        viewEmployeesRecycleView.setAdapter(employeesAdapter);
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        mFirebaseFirestore.collection("Employees")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        progressBar.setVisibility(View.GONE);
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d : list){
                            EmployeesModel employeesModel = d.toObject(EmployeesModel.class);
                            employeesData.add(employeesModel);
                        }
                        employeesAdapter.notifyDataSetChanged();
                    }
                });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent generateEmployeeId = new Intent(getContext(), AddEmployeesActivity.class);
                startActivity(generateEmployeeId);
            }
        });

        return v;
    }
}