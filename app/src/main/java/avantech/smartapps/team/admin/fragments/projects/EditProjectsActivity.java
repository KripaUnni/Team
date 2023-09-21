package avantech.smartapps.team.admin.fragments.projects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.Transliterator;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import avantech.smartapps.team.R;
import avantech.smartapps.team.admin.AdminHomeActivity;
import avantech.smartapps.team.common.LoginActivity;
import avantech.smartapps.team.model.ProjectsModel;

public class EditProjectsActivity extends AppCompatActivity {
    private static final String TAG = "EditProjectActivity";
    String id,title,client,type,priority,assignedTo,assistedBy,startDate,deadLine,dueDate,status,notes;
    EditText projectTitle, projectClient, projectType, projectStartDate, projectDeadline, projectDueDate, projectNotes;
    private RadioGroup projectPriority;
    private RadioButton rb;
    Spinner spinnerAssignedTo, spinnerAssistedBy, spinnerProjectStatus;
    String statuses[] = {"Planning","Analysis","Design","Implementation","Testing&Integration","Maintenance"};
    ArrayAdapter<String> spinnerStatusesArrayAdapter;
    Button register;
    ProgressBar progressBar;
    FirebaseFirestore mFirebaseFirestore;
    CollectionReference mProjectsCollection;
    CollectionReference mEmployeesCollection;
    CollectionReference mTasksCollection;
    ProjectsModel projectsModelForProjectDatabase;
    final Calendar myCalendar= Calendar.getInstance();
    ImageView delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_projects);
        Intent intent = getIntent();
        id = intent.getStringExtra("enteredProjectId");
        title = intent.getStringExtra("enteredProjectTitle");
        client = intent.getStringExtra("enteredProjectClient");
        type = intent.getStringExtra("enteredProjectType");
        priority = intent.getStringExtra("enteredProjectPriority");
        assignedTo = intent.getStringExtra("enteredProjectAssignedTo");
        assistedBy = intent.getStringExtra("enteredProjectAssistedBy");
        startDate = intent.getStringExtra("enteredProjectStartDate");
        deadLine = intent.getStringExtra("enteredProjectDeadline");
        dueDate = intent.getStringExtra("enteredProjectDueDate");
        status = intent.getStringExtra("enteredProjectStatus");
        notes = intent.getStringExtra("enteredProjectNotes");
        Log.d(TAG, "Enterd ID through intent is : "+id);

        delete = findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(EditProjectsActivity.this);
                alert.setTitle("Log Out");
                alert.setMessage("Are you sure you want to Delete?");
                alert.setCancelable(false);
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressBar.setVisibility(View.VISIBLE);
                        deleteProject();
                    }
                });
                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.show();
            }
        });
        projectTitle = findViewById(R.id.title);
        projectTitle.setText(title);
        projectClient = findViewById(R.id.client);
        projectClient.setText(client);
        projectType = findViewById(R.id.type);
        projectType.setText(type);
        projectPriority = findViewById(R.id.rg);
        setIntentPriority();
        spinnerAssignedTo = findViewById(R.id.employees);
        spinnerAssistedBy = findViewById(R.id.asst_by);
        projectStartDate = findViewById(R.id.startDate);
        projectStartDate.setText(startDate);
        projectDeadline = findViewById(R.id.deadline);
        projectDeadline.setText(deadLine);
        projectDueDate = findViewById(R.id.dueDate);
        projectDueDate.setText(dueDate);
        spinnerProjectStatus = findViewById(R.id.status);
        projectNotes = findViewById(R.id.notes);
        projectNotes.setText(notes);
        register = findViewById(R.id.button_register);
        progressBar = findViewById(R.id.progressBar);
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        mProjectsCollection = mFirebaseFirestore.collection("Projects");
        mEmployeesCollection = mFirebaseFirestore.collection("Employees");
        mTasksCollection = mFirebaseFirestore.collection("Taks");
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
                                int spinnerPosition = adapter1.getPosition(assignedTo);
                                spinnerAssignedTo.setSelection(spinnerPosition);
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
                                        int spinnerPosition = finalAdapter.getPosition(assistedBy);
                                        spinnerAssistedBy.setSelection(spinnerPosition);
                                    }
                                    finalAdapter.notifyDataSetChanged();
                                }
                            }
                        });
                spinnerAssistedBy.setSelection(finalAdapter.getPosition(assistedBy));
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
        spinnerProjectStatus.setSelection(spinnerStatusesArrayAdapter.getPosition(status));
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
                new DatePickerDialog(EditProjectsActivity.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
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
                new DatePickerDialog(EditProjectsActivity.this,date2,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
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
                new DatePickerDialog(EditProjectsActivity.this,date3,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
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
                progressBar.setVisibility(View.VISIBLE);
                projectsModelForProjectDatabase = new ProjectsModel(id,title,client,type,priority,assignedTo,assistedBy,startDate,deadLine,dueDate,status,notes);
                addProjectsToFirebaseProjectsCollection(projectsModelForProjectDatabase);
            }
        });

    }

    private void setIntentPriority() {
        if(priority.equals("High")){
            projectPriority.check(R.id.high);
        }else if(priority.equals("Low")){
            projectPriority.check(R.id.low);
        }else {
            projectPriority.check(R.id.medium);
        }
    }

    private void deleteProject() {
        mProjectsCollection.document(id)
                .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(EditProjectsActivity.this, "Project has been deleted.", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(EditProjectsActivity.this,AdminHomeActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(EditProjectsActivity.this, "Fail to delete the task. ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
                        Toast.makeText(EditProjectsActivity.this, "Data updated to projects collection", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditProjectsActivity.this, AdminHomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
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
}