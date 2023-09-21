package avantech.smartapps.team.admin.fragments.projects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import avantech.smartapps.team.R;
import avantech.smartapps.team.admin.AdminHomeActivity;
import avantech.smartapps.team.model.ProjectsModel;

public class AddProjectsActivity extends AppCompatActivity {
    EditText projectTitle, projectClient, projectType, projectStartDate, projectDeadline, projectDueDate, projectNotes;
    private RadioGroup projectPriority;
    private RadioButton rb;
    Spinner spinnerAssignedTo, spinnerAssistedBy, spinnerProjectStatus;
    String statuses[] = {"Planning","Analysis","Design","Implementation","Testing&Integration","Maintenance"};
    ArrayAdapter<String> spinnerStatusesArrayAdapter;
    Button register;
    ProgressBar progressBar;
    boolean valid = true;
    String id,title,client,type,priority,assignedTo,assistedBy,startDate,deadLine,dueDate,status,notes;
    FirebaseFirestore mFirebaseFirestore;
    CollectionReference mProjectsCollection;
    CollectionReference mEmployeesCollection;
    ProjectsModel projectsModelForProjectDatabase;

    final Calendar myCalendar= Calendar.getInstance();
    private static final String TAG = "AddProjectsActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_projects);
        setTitle("Add projects");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        generateProjectID();
        projectTitle = findViewById(R.id.title);
        projectClient = findViewById(R.id.client);
        projectType = findViewById(R.id.type);
        projectPriority = findViewById(R.id.rg);
        spinnerAssignedTo = findViewById(R.id.employees);
        spinnerAssistedBy = findViewById(R.id.asst_by);
        projectStartDate = findViewById(R.id.startDate);
        projectDeadline = findViewById(R.id.deadline);
        projectDueDate = findViewById(R.id.dueDate);
        spinnerProjectStatus = findViewById(R.id.status);
        projectNotes = findViewById(R.id.notes);
        register = findViewById(R.id.button_register);
        progressBar = findViewById(R.id.progressBar);
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        mProjectsCollection = mFirebaseFirestore.collection("Projects");
        mEmployeesCollection = mFirebaseFirestore.collection("Employees");
        List<String> employees = new ArrayList<>();
        final ArrayAdapter<String>[] adapter = new ArrayAdapter[]
                {
                        new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, employees)
                };
        adapter[0].setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAssignedTo.setAdapter(adapter[0]);
        ArrayAdapter<String> adapter1 = adapter[0];
        mEmployeesCollection
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String employee = document.getString("name");
                                employees.add(employee);
                            }
                            adapter1.notifyDataSetChanged();
                        }
                    }
                });
        spinnerAssignedTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                assignedTo = employees.get(i);
                Log.d(TAG, "Selected employee for project through spinner is : "+assignedTo);
                List<String> asstEmployees = new ArrayList<>();
                final ArrayAdapter<String>[] adapter = new ArrayAdapter[]{new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, asstEmployees)};
                adapter[0].setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerAssistedBy.setAdapter(adapter[0]);
                ArrayAdapter<String> finalAdapter = adapter[0];
                mEmployeesCollection
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String asstEmployee = document.getString("name");
                                        asstEmployees.add(asstEmployee);
                                    }
                                    finalAdapter.notifyDataSetChanged();
                                }
                            }
                        });
                spinnerAssistedBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        assistedBy = asstEmployees.get(i);
                        Log.d(TAG, "Selected assisting employee through spinner is : "+assistedBy);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerStatusesArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, statuses);
        spinnerProjectStatus.setAdapter(spinnerStatusesArrayAdapter);
        spinnerProjectStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                status = spinnerStatusesArrayAdapter.getItem(i);
                Log.d(TAG, "Selected status through spinner is : "+status);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };
        projectStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddProjectsActivity.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        DatePickerDialog.OnDateSetListener date2 =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel2();
            }
        };
        projectDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddProjectsActivity.this,date2,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        DatePickerDialog.OnDateSetListener date3 =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel3();
            }
        };
        projectDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddProjectsActivity.this,date3,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = projectTitle.getText().toString();
                client = projectClient.getText().toString();
                type = projectType.getText().toString();
                int selectedId = projectPriority.getCheckedRadioButtonId();
                rb = (RadioButton) findViewById(selectedId);
                priority = rb.getText().toString();
                startDate = projectStartDate.getText().toString();
                deadLine = projectDeadline.getText().toString();
                dueDate = projectDueDate.getText().toString();
                notes = projectNotes.getText().toString();
                if (title.isEmpty()){
                    projectTitle.setError("This field can't be empty !");
                    valid = false;
                } else if (client.isEmpty()){
                    projectClient.setError("This field can't be empty !");
                    valid = false;
                } else if (startDate.isEmpty()){
                    projectStartDate.setError("This field can't be empty !");
                    valid = false;
                } else if (deadLine.isEmpty()){
                    projectDeadline.setError("This field can't be empty !");
                    valid = false;
                } else if (dueDate.isEmpty()){
                    projectDueDate.setError("This field can't be empty !");
                    valid = false;
                } else if (notes.isEmpty()){
                    projectNotes.setError("This field can't be empty !");
                    valid = false;
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    projectsModelForProjectDatabase = new ProjectsModel(id, title, client, type, priority, assignedTo, assistedBy, startDate, deadLine, dueDate, status, notes);
                    addProjectsToFirebaseProjectsCollection(projectsModelForProjectDatabase);
                }
            }
        });
    }

    private void generateProjectID() {
        Random r = new Random();
        id = "PROJECT" + r.nextInt(99-1);
    }


    private void updateLabel3() {
        String myFormat="dd/MM/yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        projectDueDate.setText(dateFormat.format(myCalendar.getTime()));
    }

    private void updateLabel2() {
        String myFormat="dd/MM/yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        projectDeadline.setText(dateFormat.format(myCalendar.getTime()));
    }

    private void updateLabel() {
        String myFormat="dd/MM/yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        projectStartDate.setText(dateFormat.format(myCalendar.getTime()));
    }

    private void clearAllFields() {
        projectTitle.setText("");
        projectClient.setText("");
        projectType.setText("");
        projectStartDate.setText("");
        projectDeadline.setText("");
        projectDueDate.setText("");
        projectNotes.setText("");
        projectsModelForProjectDatabase = new ProjectsModel();
    }

    private void addProjectsToFirebaseProjectsCollection(ProjectsModel projectsModelForProjectDatabase) {
        mProjectsCollection.document(projectsModelForProjectDatabase.getId())
                .set(projectsModelForProjectDatabase)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressBar.setVisibility(View.GONE);
                        clearAllFields();
                        Toast.makeText(AddProjectsActivity.this, "Data added to projects collection", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddProjectsActivity.this, AdminHomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

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
}