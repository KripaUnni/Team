package avantech.smartapps.team.admin.fragments.employees;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import avantech.smartapps.team.R;
import avantech.smartapps.team.admin.AdminHomeActivity;
import avantech.smartapps.team.model.EmployeesModel;

public class AddEmployeesActivity extends AppCompatActivity {
    EditText userName,userDesignation,userPhone,userEmail,userID,userPassword;
    Button register;
    ProgressBar progressBar;
    String name,gender,designation,phone,email,type,id,password;
    boolean valid = true;

    private RadioGroup rg;
    private RadioButton rb;
    Spinner spinnerType;
    String types[] = {"admin","employee"};
    ArrayAdapter<String> spinnerArrayAdapter;

    FirebaseFirestore mFirebaseFirestore;
    CollectionReference mEmployeesCollection;
    EmployeesModel employeesModelForEmployeeDatabase;
    private static final String TAG = "AddUserActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employees);
        setTitle("Add Employees");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        userName = findViewById(R.id.name);
        rg = findViewById(R.id.rg);
        userDesignation = findViewById(R.id.designation);
        userPhone = findViewById(R.id.phone);
        userEmail = findViewById(R.id.email);
        spinnerType = findViewById(R.id.type);
        userID = findViewById(R.id.id);
        userPassword = findViewById(R.id.password);
        register = findViewById(R.id.button_register);
        progressBar = findViewById(R.id.progressBar);
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        mEmployeesCollection = mFirebaseFirestore.collection("Employees");
        spinnerArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, types);
        spinnerType.setAdapter(spinnerArrayAdapter);
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                type = spinnerArrayAdapter.getItem(i);
                Log.d(TAG, "Selected blood group through spinner is : "+type);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = userName.getText().toString();
                designation = userDesignation.getText().toString();
                int selectedId = rg.getCheckedRadioButtonId();
                rb = (RadioButton) findViewById(selectedId);
                gender = rb.getText().toString();
                phone = userPhone.getText().toString();
                email = userEmail.getText().toString();
                id = userID.getText().toString();
                password = userPassword.getText().toString();
                if (name.isEmpty() || name.length() < 3 || !name.matches("^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$")) {
                    userName.setError("User name can't be empty !");
                    valid = false;
                } else if (designation.isEmpty()){
                    userDesignation.setError("Designation can't be empty !");
                    valid = false;
                } else if (id.isEmpty()){
                    userID.setError("Employee ID can't be empty !");
                    valid = false;
                } else if (password.isEmpty()){
                    userPassword.setError("Employee Password can't be empty !");
                    valid = false;
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    employeesModelForEmployeeDatabase = new EmployeesModel(name, designation, gender, phone,email, type, id, password);
                    addEmployeesToFirebaseEmployeesCollection(employeesModelForEmployeeDatabase);
                    clearAllFields();
                }
            }
        });

    }

    private void clearAllFields() {
        userName.setText("");
        userDesignation.setText("");
        userPhone.setText("");
        userEmail.setText("");
        userID.setText("");
        userPassword.setText("");
        employeesModelForEmployeeDatabase = new EmployeesModel();
    }

    private void addEmployeesToFirebaseEmployeesCollection(EmployeesModel employeesModelForEmployeeDatabase) {
        mEmployeesCollection.document(employeesModelForEmployeeDatabase.getId())
                .set(employeesModelForEmployeeDatabase)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(AddEmployeesActivity.this, "Data added to employees collection", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddEmployeesActivity.this, AdminHomeActivity.class);
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