package avantech.smartapps.team.common;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import avantech.smartapps.team.R;
import avantech.smartapps.team.admin.AdminHomeActivity;
import avantech.smartapps.team.admin.fragments.employees.AddEmployeesActivity;
import avantech.smartapps.team.model.EmployeesModel;
import avantech.smartapps.team.staff.StaffHomeActivity;

public class EditProfileActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    Context context;
    TextView empId;
    private EditText empName, empPhone, empEmail, empPassword;
    Button register;
    ProgressBar progressBar;
    String id,name,phone,email,password;
    boolean valid = true;
    FirebaseFirestore mFirebaseFirestore;
    CollectionReference mEmployeesCollection;
    EmployeesModel employeesModelForEmployeeDatabase;
    private static final String TAG = "EditProfileActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        sharedPreferences = new SharedPreferences(EditProfileActivity.this);
        setTitle("Edit your profile");
        empId= findViewById(R.id.id);
        empId.setText(sharedPreferences.getEMP_ID());
        empName = findViewById(R.id.name);
        empName.setText(sharedPreferences.getNAME());
        empPhone = findViewById(R.id.phone);
        empPhone.setText(sharedPreferences.getPHONE());
        empEmail = findViewById(R.id.email);
        empEmail.setText(sharedPreferences.getEMAIL());
        empPassword = findViewById(R.id.password);
        empPassword.setText(sharedPreferences.getPASSWORD());
        register = findViewById(R.id.button_register);
        progressBar = findViewById(R.id.progressBar);
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        mEmployeesCollection = mFirebaseFirestore.collection("Employees");
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = empName.getText().toString();
                phone = empPhone.getText().toString();
                email = empEmail.getText().toString();
                password = empPassword.getText().toString();
                if (name.isEmpty() || name.length() < 3 || !name.matches("^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$")) {
                    empName.setError("Employee name can't be empty !");
                    valid = false;
                } else if (phone.isEmpty()){
                    empPhone.setError("Employee phone number can't be empty !");
                    valid = false;
                } else if (email.isEmpty()){
                    empEmail.setError("Employee email can't be empty !");
                    valid = false;
                } else if (password.isEmpty()){
                    empPassword.setError("Employee Password can't be empty !");
                    valid = false;
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    addEmployeesToFirebaseEmployeesCollection(employeesModelForEmployeeDatabase);
                }
            }
        });


    }

    private void clearAllFields() {
        empName.setText("");
        empPhone.setText("");
        empEmail.setText("");
        empPassword.setText("");
        employeesModelForEmployeeDatabase = new EmployeesModel();
    }

    private void addEmployeesToFirebaseEmployeesCollection(EmployeesModel employeesModelForEmployeeDatabase) {
        //update profile
        Map<String,String> data = new HashMap<>();
        data.put("name",name);
        data.put("email",email);
        data.put("phone",phone);
        data.put("password",password);
        mEmployeesCollection.document(sharedPreferences.getEMP_ID())
                .set(data, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressBar.setVisibility(View.GONE);
                        sharedPreferences.setName(name);
                        sharedPreferences.setEmail(email);
                        sharedPreferences.setPhone(phone);
                        sharedPreferences.setPassword(password);
                        Toast.makeText(EditProfileActivity.this, "Data updated", Toast.LENGTH_SHORT).show();
                        clearAllFields();
                        if(sharedPreferences.getTYPE().equals("admin")){
                            Intent intent = new Intent(EditProfileActivity.this, AdminHomeActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            Intent intent = new Intent(EditProfileActivity.this, StaffHomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfileActivity.this, "Data cannot be updated", Toast.LENGTH_SHORT).show();

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