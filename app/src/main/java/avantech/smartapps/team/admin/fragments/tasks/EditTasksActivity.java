package avantech.smartapps.team.admin.fragments.tasks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import avantech.smartapps.team.R;
import avantech.smartapps.team.admin.AdminHomeActivity;
import avantech.smartapps.team.admin.fragments.projects.EditProjectsActivity;
import avantech.smartapps.team.common.EditProfileActivity;
import avantech.smartapps.team.common.SharedPreferences;
import avantech.smartapps.team.model.TasksModel;
import avantech.smartapps.team.staff.StaffHomeActivity;

public class EditTasksActivity extends AppCompatActivity {
    String projectTitle;
    private static final String TAG = "EDIT TASKS ACTIVITY";
    EditText taskID, taskTitle, taskNotes;
    String id,title,priority,startDate,dueDate,status,notes;
    Button start, stop, completed, reEnable;
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
    ImageView delete;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tasks);
        sharedPreferences = new SharedPreferences(EditTasksActivity.this);
        Intent intent = getIntent();
        projectTitle = intent.getStringExtra("enteredProjectTitle");
        id = intent.getStringExtra("enteredTaskId");
        title = intent.getStringExtra("enteredTaskTitle");
        priority = intent.getStringExtra("enteredTaskPriority");
        startDate = intent.getStringExtra("enteredTaskStartDate");
        dueDate = intent.getStringExtra("enteredTaskDueDate");
        status = intent.getStringExtra("enteredTaskStatus");
        notes = intent.getStringExtra("enteredTaskNotes");
        Log.d(TAG, "Entered ID through intent is : "+id);
        start = findViewById(R.id.start);
        stop = findViewById(R.id.stop);
        completed = findViewById(R.id.completed);
        reEnable = findViewById(R.id.reEnable);
        completed.setVisibility(View.GONE);
        reEnable.setVisibility(View.GONE);
        if(startDate == null){
            start.setVisibility(View.VISIBLE);
            stop.setVisibility(View.GONE);
            completed.setVisibility(View.GONE);
            reEnable.setVisibility(View.GONE);
        }else if(dueDate == null){
            start.setVisibility(View.GONE);
            stop.setVisibility(View.VISIBLE);
            completed.setVisibility(View.GONE);
            reEnable.setVisibility(View.GONE);
        }else{
            start.setVisibility(View.GONE);
            stop.setVisibility(View.GONE);
            completed.setVisibility(View.VISIBLE);
            reEnable.setVisibility(View.GONE);
            if(sharedPreferences.getTYPE().equals("admin")){
                start.setVisibility(View.GONE);
                stop.setVisibility(View.GONE);
                completed.setVisibility(View.VISIBLE);
                reEnable.setVisibility(View.VISIBLE);
            }
        }

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date currentTime = Calendar.getInstance().getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy 'at' HH:mm:ss");
                startDate = dateFormat.format(currentTime);
                start.setVisibility(View.GONE);
                stop.setVisibility(View.VISIBLE);
                Toast.makeText(EditTasksActivity.this, "Click on update button now", Toast.LENGTH_SHORT).show();
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date currentTime = Calendar.getInstance().getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy 'at' HH:mm:ss");
                dueDate = dateFormat.format(currentTime);
                start.setVisibility(View.GONE);
                stop.setVisibility(View.GONE);
                completed.setVisibility(View.VISIBLE);
                Toast.makeText(EditTasksActivity.this, "Click on update button now", Toast.LENGTH_SHORT).show();
            }
        });
        reEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reEnable.setVisibility(View.GONE);
                start.setVisibility(View.VISIBLE);
                completed.setVisibility(View.GONE);
                stop.setVisibility(View.GONE);
                startDate = (null);
                dueDate = (null);
                Toast.makeText(EditTasksActivity.this, "Click on update button now", Toast.LENGTH_SHORT).show();
            }
        });
        delete = findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(EditTasksActivity.this);
                alert.setTitle("Log Out");
                alert.setMessage("Are you sure you want to Delete?");
                alert.setCancelable(false);
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressBar.setVisibility(View.VISIBLE);
                        deleteTask();
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
        taskTitle = findViewById(R.id.title);
        taskTitle.setText(title);
        taskPriority = findViewById(R.id.rg);
        setIntentPriority();
        spinnerTaskStatus = findViewById(R.id.status);
        taskNotes = findViewById(R.id.notes);
        taskNotes.setText(notes);
        register = findViewById(R.id.button_register);
        progressBar = findViewById(R.id.progressBar);
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        mTasksCollection = mFirebaseFirestore.collection("Tasks");
        spinnerStatusesArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, statuses);
        spinnerTaskStatus.setAdapter(spinnerStatusesArrayAdapter);
        spinnerTaskStatus.setSelection(spinnerStatusesArrayAdapter.getPosition(status));
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
                progressBar.setVisibility(View.VISIBLE);
                tasksModelForTaskDatabase = new TasksModel(projectTitle,id,title,priority,startDate,dueDate,status,notes);
                updateTasksToFirebaseTasksCollection(tasksModelForTaskDatabase);
            }
        });
    }

    private void setIntentPriority() {
        if(priority.equals("High")){
            taskPriority.check(R.id.high);
        }else if(priority.equals("Low")){
            taskPriority.check(R.id.low);
        }else {
            taskPriority.check(R.id.medium);
        }
    }

    private void updateTasksToFirebaseTasksCollection(TasksModel tasksModelForTaskDatabase) {
        mTasksCollection.document(tasksModelForTaskDatabase.getId())
                .set(tasksModelForTaskDatabase)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressBar.setVisibility(View.GONE);
                        clearAllFields();
                        Toast.makeText(EditTasksActivity.this, "Data updated to tasks collection", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditTasksActivity.this, ViewTasksActivity.class);
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

    private void clearAllFields() {
        taskTitle.setText("");
        taskNotes.setText("");
        tasksModelForTaskDatabase = new TasksModel();
    }

    private void deleteTask() {
        mTasksCollection.document(id)
                .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(EditTasksActivity.this, "Task has been deleted.", Toast.LENGTH_SHORT).show();
                            if(sharedPreferences.getTYPE().equals("admin")){
                                Intent intent = new Intent(EditTasksActivity.this, AdminHomeActivity.class);
                                startActivity(intent);
                                finish();
                            }else {
                                Intent intent = new Intent(EditTasksActivity.this, StaffHomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            Toast.makeText(EditTasksActivity.this, "Fail to delete the task. ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    @Override
    protected void onResume(){
        super.onResume();
    }
}