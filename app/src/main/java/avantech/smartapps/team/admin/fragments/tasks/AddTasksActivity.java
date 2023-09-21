package avantech.smartapps.team.admin.fragments.tasks;

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

import avantech.smartapps.team.R;
import avantech.smartapps.team.admin.fragments.projects.AddProjectsActivity;
import avantech.smartapps.team.model.TasksModel;

public class AddTasksActivity extends AppCompatActivity {
    String projectTitle;
    private static final String TAG = "ADD TASKS ACTIVITY";
    EditText projectTitleHead, taskTitle, taskNotes;
    String id,title,priority,startDate,dueDate,status,notes;
    Button register;
    ProgressBar progressBar;
    boolean valid = true;
    private RadioGroup taskPriority;
    private RadioButton rb;
    Spinner spinnerTaskStatus;
    String statuses[] = {"Planning","Analysis","Design","Implementation","Testing&Integration","Maintenance"};
    ArrayAdapter<String> spinnerStatusesArrayAdapter;
    FirebaseFirestore mFirebaseFirestore;
    CollectionReference mTasksCollection;
    TasksModel tasksModelForTaskDatabase;
    final Calendar myCalendar= Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tasks);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        projectTitle = intent.getStringExtra("enteredProjectTitle");
        Log.d(TAG, "Enterd project through intent is : "+projectTitle);
        setTitle("Add tasks for "+projectTitle);
        projectTitleHead = findViewById(R.id.projectTitle);
        projectTitleHead.setText(projectTitle);
        projectTitleHead.setEnabled(false);
        generateTaskID();
        taskTitle = findViewById(R.id.title);
        taskPriority = findViewById(R.id.rg);
        spinnerTaskStatus = findViewById(R.id.status);

        taskNotes = findViewById(R.id.notes);
        register = findViewById(R.id.button_register);
        progressBar = findViewById(R.id.progressBar);

        mFirebaseFirestore = FirebaseFirestore.getInstance();
        mTasksCollection = mFirebaseFirestore.collection("Tasks");

        spinnerStatusesArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, statuses);
        spinnerTaskStatus.setAdapter(spinnerStatusesArrayAdapter);
        spinnerTaskStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                status = spinnerStatusesArrayAdapter.getItem(i);
                Log.d(TAG, "Selected status through spinner is : "+status);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = taskTitle.getText().toString();
                int selectedId = taskPriority.getCheckedRadioButtonId();
                rb = (RadioButton) findViewById(selectedId);
                priority = rb.getText().toString();
                notes = taskNotes.getText().toString();
                if (title.isEmpty()){
                    taskTitle.setError("This field can't be empty !");
                    valid = false;
                }  else if (notes.isEmpty()){
                    taskNotes.setError("This field can't be empty !");
                    valid = false;
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    tasksModelForTaskDatabase = new TasksModel(projectTitle, id, title, priority, startDate, dueDate, status, notes);
                    addTasksToFirebaseTasksCollection(tasksModelForTaskDatabase);
                }
            }
        });


    }

    private void generateTaskID() {
        Random r = new Random();
        id = "TASK" + r.nextInt(999-1);
    }

    private void clearAllFields() {
        taskTitle.setText("");
        taskNotes.setText("");
        tasksModelForTaskDatabase = new TasksModel();
    }

    private void addTasksToFirebaseTasksCollection(TasksModel tasksModelForTaskDatabase) {
        mTasksCollection.document(tasksModelForTaskDatabase.getId())
                .set(tasksModelForTaskDatabase)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressBar.setVisibility(View.GONE);
                        clearAllFields();
                        Toast.makeText(AddTasksActivity.this, "Data added to tasks collection", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddTasksActivity.this, ViewTasksActivity.class);
                        intent.putExtra("enteredProjectTitle", projectTitle);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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