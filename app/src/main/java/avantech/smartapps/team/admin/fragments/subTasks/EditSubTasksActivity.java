package avantech.smartapps.team.admin.fragments.subTasks;

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
import avantech.smartapps.team.admin.fragments.tasks.EditTasksActivity;
import avantech.smartapps.team.admin.fragments.tasks.ViewTasksActivity;
import avantech.smartapps.team.common.SharedPreferences;
import avantech.smartapps.team.model.SubTasksModel;
import avantech.smartapps.team.model.TasksModel;
import avantech.smartapps.team.staff.StaffHomeActivity;

public class EditSubTasksActivity extends AppCompatActivity {
    private static final String TAG = "EDIT SUBTASKS ACTIVITY";
    EditText  subTaskTitle, taskNotes;
    String projectTitle,taskTitle,id,title,priority,startDate,dueDate,status,notes;
    Button register;
    Button start, stop, completed, reEnable;
    ProgressBar progressBar;
    private RadioGroup taskPriority;
    private RadioButton rb;
    Spinner spinnerTaskStatus;
    String statuses[] = {"Planning","Analysis","Design","Implementation","Testing&Integration","Maintenance"};
    ArrayAdapter<String> spinnerStatusesArrayAdapter;
    FirebaseFirestore mFirebaseFirestore;
    CollectionReference mSubTasksCollection;
    SubTasksModel subTasksModelForTaskDatabase;
    ImageView delete;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sub_tasks);
        sharedPreferences = new SharedPreferences(EditSubTasksActivity.this);
        Intent intent = getIntent();
        projectTitle = intent.getStringExtra("enteredProjectTitle");
        taskTitle = intent.getStringExtra("enteredTaskTitle");
        id = intent.getStringExtra("enteredSubTaskId");
        title = intent.getStringExtra("enteredSubTaskTitle");
        priority = intent.getStringExtra("enteredSubTaskPriority");
        startDate = intent.getStringExtra("enteredSubTaskStartDate");
        dueDate = intent.getStringExtra("enteredSubTaskDueDate");
        status = intent.getStringExtra("enteredSubTaskStatus");
        notes = intent.getStringExtra("enteredSubTaskNotes");
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
                Toast.makeText(EditSubTasksActivity.this, "Click on update button now", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(EditSubTasksActivity.this, "Click on update button now", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(EditSubTasksActivity.this, "Click on update button now", Toast.LENGTH_SHORT).show();
            }
        });
        delete = findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(EditSubTasksActivity.this);
                alert.setTitle("Log Out");
                alert.setMessage("Are you sure you want to Delete?");
                alert.setCancelable(false);
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressBar.setVisibility(View.VISIBLE);
                        deleteSubTask();
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
        subTaskTitle = findViewById(R.id.title);
        subTaskTitle.setText(title);
        taskPriority = findViewById(R.id.rg);
        setIntentPriority();
        spinnerTaskStatus = findViewById(R.id.status);
        taskNotes = findViewById(R.id.notes);
        taskNotes.setText(notes);
        register = findViewById(R.id.button_register);
        progressBar = findViewById(R.id.progressBar);
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        mSubTasksCollection = mFirebaseFirestore.collection("SubTasks");
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
                title = subTaskTitle.getText().toString();
                int selectedId = taskPriority.getCheckedRadioButtonId();
                rb = (RadioButton) findViewById(selectedId);
                priority = rb.getText().toString();
                notes = taskNotes.getText().toString();
                progressBar.setVisibility(View.VISIBLE);
                subTasksModelForTaskDatabase = new SubTasksModel(projectTitle,taskTitle,id,title,priority,startDate,dueDate,status,notes);
                updateSubTasksToFirebaseSubTasksCollection(subTasksModelForTaskDatabase);
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

    private void updateSubTasksToFirebaseSubTasksCollection(SubTasksModel subTasksModelForTaskDatabase) {
        mSubTasksCollection.document(subTasksModelForTaskDatabase.getId())
                .set(subTasksModelForTaskDatabase)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressBar.setVisibility(View.GONE);
                        clearAllFields();
                        Toast.makeText(EditSubTasksActivity.this, "Data updated to subtasks collection", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditSubTasksActivity.this, ViewSubTasksActivity.class);
                        intent.putExtra("enteredProjectTitle", projectTitle);
                        intent.putExtra("enteredTaskTitle", taskTitle);
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
        subTaskTitle.setText("");
        taskNotes.setText("");
        subTasksModelForTaskDatabase = new SubTasksModel();
    }

    private void deleteSubTask() {
        mSubTasksCollection.document(id)
                .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(EditSubTasksActivity.this, "SubTask has been deleted.", Toast.LENGTH_SHORT).show();
                            if(sharedPreferences.getTYPE().equals("admin")){
                                Intent intent = new Intent(EditSubTasksActivity.this, AdminHomeActivity.class);
                                startActivity(intent);
                                finish();
                            }else {
                                Intent intent = new Intent(EditSubTasksActivity.this, StaffHomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            Toast.makeText(EditSubTasksActivity.this, "Fail to delete the subtask. ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}