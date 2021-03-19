package com.theinternetcompany.wpworkermanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.theinternetcompany.wpworkermanagement.Models.Project;

public class NewProjectActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home_button, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.go_home:
                Intent intent = new Intent(NewProjectActivity.this, MainActivity.class);
                startActivity(intent);
                break;
        }

        return true;
    }

    private EditText name, company, location, period;
    private Button btnCreateProject;
    private DatabaseReference mainRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // FORCE DAY MODE
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_new_project);
        mainRef.keepSynced(true);
        name = findViewById(R.id.newProjectName);
        company = findViewById(R.id.newProjectCompany);
        location = findViewById(R.id.newProjectLocation);
        period = findViewById(R.id.newProjectPeriod);
        btnCreateProject = findViewById(R.id.btnCreateProject);

        btnCreateProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(name.getText().toString())) {
                    Toast.makeText(NewProjectActivity.this,"Enter Worker Name", Toast.LENGTH_SHORT).show();
                }

                else if (TextUtils.isEmpty(company.getText().toString())) {
                    Toast.makeText(NewProjectActivity.this,"Enter Company", Toast.LENGTH_SHORT).show();
                }

                else if (TextUtils.isEmpty(location.getText().toString())) {
                    Toast.makeText(NewProjectActivity.this,"Enter Project Location", Toast.LENGTH_SHORT).show();
                }

                else if (TextUtils.isEmpty(period.getText().toString())) {
                    Toast.makeText(NewProjectActivity.this,"Enter The Period of Project", Toast.LENGTH_SHORT).show();
                }

                else {
                    saveNewProject();
                    transitionToProjectList();
                }
                //transitionToShopPage();
            }
        });
    }

    private void transitionToProjectList() {
        Intent intent = new Intent(NewProjectActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void saveNewProject() {
        final String projectID = mainRef.child("Project_List").push().getKey();
        Project newProject = new Project(projectID, name.getText().toString(), company.getText().toString(), location.getText().toString(), period.getText().toString(), "ZERO");
        mainRef.child("Project_List").child(projectID).setValue(newProject);
    }
}