package avantech.smartapps.team.admin.fragments.subTasks;

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
import avantech.smartapps.team.admin.fragments.tasks.AddTasksActivity;
import avantech.smartapps.team.admin.fragments.tasks.ViewTasksActivity;
import avantech.smartapps.team.model.SubTasksModel;

public class AddSubTasksActivity extends AppCompatActivity {
    private static final String TAG = "ADD SUBTASKS ACTIVITY";
    EditText projectTitleHead, taskTitleHead, subTaskTitle, taskNotes;
    String projectTitle,taskTitle,id,title,priority,startDate,dueDate,status,notes;
    Button register;
    ProgressBar progressBar;
    boolean valid = true;
    private RadioGroup taskPriority;
    private RadioButton rb;
    Spinner spinnerTaskStatus;
    String statuses[] = {"Planning","Analysis","Design","Implementation","Testing&Integration","Maintenance"};
    ArrayAdapter<String> spinnerStatusesArrayAdapter;
    FirebaseFirestore mFirebaseFirestore;
    CollectionReference mSubTasksCollection;
    SubTasksModel subTasksModelForTaskDatabase;
    final Calendar myCalendar= Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sub_tasks);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        projectTitle = intent.getStringExtra("enteredProjectTitle");
        Log.d(TAG, "Entered project through intent is : "+projectTitle);

        taskTitle = intent.getStringExtra("enteredTaskTitle");
        Log.d(TAG, "Entered task through intent is : "+taskTitle);
        setTitle("Add sub tasks for "+projectTitle);

        projectTitleHead = findViewById(R.id.projectTitle);
        projectTitleHead.setText(projectTitle);
        projectTitleHead.setEnabled(false);
        taskTitleHead = findViewById(R.id.taskTitle);
        taskTitleHead.setText(taskTitle);
        taskTitleHead.setEnabled(false);
        generateSubTaskID();
        subTaskTitle = findViewById(R.id.title);

        taskPriority = findViewById(R.id.rg);
        spinnerTaskStatus = findViewById(R.id.status);

        taskNotes = findViewById(R.id.notes);
        register = findViewById(R.id.button_register);
        progressBar = findViewById(R.id.progressBar);

        mFirebaseFirestore = FirebaseFirestore.getInstance();
        mSubTasksCollection = mFirebaseFirestore.collection("SubTasks");
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
                projectTitle = projectTitleHead.getText().toString();
                taskTitle = taskTitleHead.getText().toString();
                title = subTaskTitle.getText().toString();
                int selectedId = taskPriority.getCheckedRadioButtonId();
                rb = (RadioButton) findViewById(selectedId);
                priority = rb.getText().toString();
                notes = taskNotes.getText().toString();
                if (title.isEmpty()){
                    subTaskTitle.setError("This field can't be empty !");
                    valid = false;
                } else if (notes.isEmpty()){
                    taskNotes.setError("This field can't be empty !");
                    valid = false;
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    subTasksModelForTaskDatabase = new SubTasksModel(projectTitle, taskTitle, id, title, priority, startDate, dueDate, status, notes);
                    addSubTasksToFirebaseSubTasksCollection(subTasksModelForTaskDatabase);
                }
            }
        });
    }

    private void generateSubTaskID() {
        Random r = new Random();
        id = "SUBTASK" + r.nextInt(999-1);
    }

    private void addSubTasksToFirebaseSubTasksCollection(SubTasksModel subTasksModelForTaskDatabase) {
        mSubTasksCollection.document(subTasksModelForTaskDatabase.getId())
                .set(subTasksModelForTaskDatabase)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressBar.setVisibility(View.GONE);
                        Log.d(TAG, "Subtask details added");
                        Toast.makeText(AddSubTasksActivity.this, "Subtask details added successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddSubTasksActivity.this, ViewSubTasksActivity.class);
                        intent.putExtra("enteredProjectTitle", projectTitle);
                        intent.putExtra("enteredTaskTitle", taskTitle);
                        startActivity(intent);
                        finish();
                        clearAllTextFields();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Error Message : "+e.getMessage());
                        Toast.makeText(AddSubTasksActivity.this, "Failed to add data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void clearAllTextFields() {
        projectTitleHead.setText("");
        taskTitleHead.setText("");
        subTaskTitle.setText("");
        taskNotes.setText("");
        subTasksModelForTaskDatabase = new SubTasksModel();
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