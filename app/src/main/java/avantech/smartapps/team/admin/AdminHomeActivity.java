package avantech.smartapps.team.admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import avantech.smartapps.team.R;
import avantech.smartapps.team.admin.fragments.employees.EmployeesFragment;
import avantech.smartapps.team.admin.fragments.projects.ProjectsFragment;
import avantech.smartapps.team.common.EditProfileActivity;
import avantech.smartapps.team.common.ExitActivity;
import avantech.smartapps.team.common.LoginActivity;
import avantech.smartapps.team.common.SharedPreferences;
import avantech.smartapps.team.staff.StaffHomeActivity;

public class AdminHomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private Bundle workerSortBundle, taskSortBundle;
    private Bundle leaveSortBundle;
    private MenuItem menuItem;
    private LinearLayout linearLayout;
    NavigationView navigationView;
    SharedPreferences sharedPreferences;
    private TextView nav_name, nav_email;
    private ImageView imageView;
    private FirebaseFirestore db;
    private static final String TAG = "ADMIN HOME ACTIVITY";
    String empID;
    ConstraintLayout profile_card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        sharedPreferences = new SharedPreferences(AdminHomeActivity.this);
        Intent intent = getIntent();
        empID = intent.getStringExtra("enteredID");
        Log.d(TAG, "Entered ID through intent is : "+empID);

        db = FirebaseFirestore.getInstance();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        linearLayout = findViewById(R.id.ll_logout);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_employees,
                R.id.nav_projects)
                .setOpenableLayout(drawer)
                .build();
        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_admin_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        menuItem = navigationView.getCheckedItem();
        View header = navigationView.getHeaderView(0);

        profile_card = header.findViewById(R.id.profile_card);
        nav_name = header.findViewById(R.id.nav_name);
        nav_email = header.findViewById(R.id.nav_email);
        imageView = header.findViewById(R.id.profile_image);
        nav_name.setText(sharedPreferences.getNAME());
        nav_email.setText(sharedPreferences.getEMAIL());

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(AdminHomeActivity.this);
                alert.setTitle("Log Out");
                alert.setMessage("Are you sure you want to Logout?");
                alert.setCancelable(false);
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(AdminHomeActivity.this, LoginActivity.class));
                        android.content.SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
                        android.content.SharedPreferences.Editor editor = sp.edit();
                        editor.clear();
                        editor.commit();
                        finish();
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
        profile_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = new SharedPreferences(AdminHomeActivity.this);
                startActivity(new Intent(AdminHomeActivity.this, EditProfileActivity.class));
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin_home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_admin_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}