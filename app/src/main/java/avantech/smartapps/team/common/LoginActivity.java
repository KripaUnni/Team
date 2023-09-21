package avantech.smartapps.team.common;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import avantech.smartapps.team.R;
import avantech.smartapps.team.admin.AdminHomeActivity;
import avantech.smartapps.team.staff.StaffHomeActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText editText,editText1;
    public FirebaseFirestore db;
    private AlertDialog dialog;
    Context context;
    String documentID,empPassword,empType;
    private Boolean isSwipe = false;
    private ConstraintLayout constraintLayout;
    private static final String TAG = "LOGIN ACTIVITY";
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("");
        db = FirebaseFirestore.getInstance();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initialize();
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    if((motionEvent.getRawX() >= (editText.getRight() - editText.getCompoundDrawables()[2].getBounds().width() - 20))){
                        if(editText.getText().length() <= 0){
                            editText.setError("Enter ID ");
                        }
                        else{
                            documentID = (editText.getText().toString().trim());
                            dialog.show();
                            //check if it is the first user or not
                            db.collection("Employees").document(documentID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG,"document id is"+documentID);
                                        if(Objects.requireNonNull(task.getResult()).exists()) {
                                            dialog.dismiss();
//                                            if(task.getResult().getString("password").equals("")) {
//                                                //new user activity
//                                                Intent intent = new Intent(getBaseContext(),NewUserActivity.class);
//                                                intent.putExtra("empid",documentID);
//                                                finish();
//                                                startActivity(intent);
//                                            }
//                                            else {
                                                //display the password field
                                                isSwipe = true;
                                                final AnimatorSet as = new AnimatorSet();
                                                ObjectAnimator animator1 = ObjectAnimator.ofFloat(findViewById(R.id.et_emp_id), "translationX", -constraintLayout.getWidth());
                                                ObjectAnimator animator2 = ObjectAnimator.ofFloat(findViewById(R.id.et_pwd), "translationX", 0);
                                                animator1.setDuration(500);
                                                animator2.setDuration(500);
                                                as.play(animator1);
                                                as.play(animator2);
                                                as.start();
                                                editText1.requestFocus();
                                            }
//                                        }
                                        else {
                                            dialog.dismiss();
                                            Toast.makeText(context, "User doesn't exist", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                            return true;
                        }
                    }
                }
                return false;
            }
        });
        editText1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    if((motionEvent.getRawX() >= (editText1.getRight() - editText1.getCompoundDrawables()[2].getBounds().width() - 20)) && editText1.getText().length() != 10){
                        if(editText1.getText().length() <= 0){
                            editText1.setError("Enter password ");
                        }
                        else {
                            dialog.show();
                            db.collection("Employees").document(documentID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful()) {
                                        if( task.getResult().getString("password").equals(editText1.getText().toString().trim()))
                                        {
                                            DocumentSnapshot doc = task.getResult();
                                            if (doc.exists()) {
                                                dialog.dismiss();
                                                empType = doc.getString("type");
                                                empPassword = doc.getString("password");
                                                Log.d(TAG, "User type is " + empType + " " + empPassword);
                                                SharedPreferences sharedPref = new SharedPreferences(context, true, documentID,
                                                        doc.getString("name"),
                                                        doc.getString("phone"),
                                                        doc.getString("email"),
                                                        doc.getString("type"),
                                                        doc.getString("password"));
                                                showActivityAccordingToUserType();
                                            }
                                        }
                                        else
                                        {
                                            Toast.makeText(context, "Wrong password", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    dialog.dismiss();
                                }
                            });
                            return true;
                        }
                    }
                }
                return false;
            }
        });
    }

    private void showActivityAccordingToUserType() {
        Log.d(TAG, "User type is " + empType);
        db.collection("Employees").document(documentID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.d(TAG, "User type is " + documentSnapshot.getString("type"));
                        if(Objects.equals(documentSnapshot.getString("type"),empType)){
                            if (empType.equals("admin")) {
                                dialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Welcome " + documentID, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, AdminHomeActivity.class);
                                intent.putExtra("enteredID", documentID);
                                startActivity(intent);
                                finish();
                            } else if(empType.equals("employee")){
                                dialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Welcome " + documentID, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, StaffHomeActivity.class);
                                intent.putExtra("enteredID", documentID);
                                startActivity(intent);
                                finish();
                            }
                        }else {
                            dialog.dismiss();
                            Toast.makeText(LoginActivity.this, "ID is Invalid, unable to send you IN", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Unable to find user in Users collection");
                        Toast.makeText(LoginActivity.this, "New employee. Please contact admin", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if(!isSwipe)
            super.onBackPressed();
        else {
            isSwipe = false;
            final AnimatorSet as = new AnimatorSet();
            ObjectAnimator animator1 = ObjectAnimator.ofFloat(findViewById(R.id.et_pwd), "translationX", constraintLayout.getWidth());
            ObjectAnimator animator2 = ObjectAnimator.ofFloat(findViewById(R.id.et_emp_id), "translationX", 0);
            animator1.setDuration(500);
            animator2.setDuration(500);
            as.play(animator1);
            as.play(animator2);
            as.start();
        }
    }

    private void initialize() {
        context = LoginActivity.this;
        editText = findViewById(R.id.et_emp_id);
        editText1 = findViewById(R.id.et_pwd);
        constraintLayout = findViewById(R.id.constraint_layout);
        ProgressBar progressBar = new ProgressBar(context);
        progressBar.setPadding(10,30,10,30);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        dialog = alertDialog.create();
        dialog.setCancelable(false);
        dialog.setView(progressBar);
    }
}