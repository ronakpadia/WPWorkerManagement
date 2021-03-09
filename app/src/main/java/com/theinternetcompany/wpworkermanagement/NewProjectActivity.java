package com.theinternetcompany.wpworkermanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.theinternetcompany.wpworkermanagement.Models.Project;

public class NewProjectActivity extends AppCompatActivity {

    private EditText name, company, location, period;
    private Button btnCreateProject;
    private DatabaseReference mainRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                }
                //transitionToShopPage();
            }
        });
    }

    private void saveNewProject() {
        final String projectID = mainRef.child("Project_List").push().getKey();
        Project newProject = new Project(projectID, name.getText().toString(), company.getText().toString(), location.getText().toString(), period.getText().toString(), "ZERO");
        mainRef.child("Project_List").child(projectID).setValue(newProject);
    }
}