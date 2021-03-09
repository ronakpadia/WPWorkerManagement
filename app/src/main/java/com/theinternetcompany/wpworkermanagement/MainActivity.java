package com.theinternetcompany.wpworkermanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theinternetcompany.wpworkermanagement.Models.Project;

import java.util.ArrayList;

public class    MainActivity extends AppCompatActivity {

    TableLayout projectTable;
    private Button btnEmployeeList, btnAddNewProject;
    private DatabaseReference mainRef = FirebaseDatabase.getInstance().getReference();
    private ArrayList<Project> projectList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnAddNewProject = findViewById(R.id.btnAddNewProject);
        btnEmployeeList = findViewById(R.id.btnEmployeeList);
        mainRef.keepSynced(true);
        getProjectData(); // Populate the table with latest project
        btnAddNewProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transitionToNewProjectActivity();
            }
        });
//        transitionToNewWorkerActivity();
    }

    private void getProjectData() {
        projectTable = findViewById(R.id.projectTable);
        DatabaseReference projectRef = mainRef.child("Project_List");
        projectRef.keepSynced(true);


        projectRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap : snapshot.getChildren()){
                    projectList.add(snap.getValue(Project.class));

                }

                populateTable(projectList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void populateTable(ArrayList<Project> projectList) {
        for (Project p : projectList){
            TableRow row = new TableRow(MainActivity.this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
            row.setLayoutParams(lp);
            // Generating TextViews for row
            TextView pid = new TextView(MainActivity.this);
            pid.setText(p.getId());
            TextView pname = new TextView(MainActivity.this);
            pname.setText(p.getName());
            TextView pcompany = new TextView(MainActivity.this);
            pcompany.setText(p.getCompany());
            TextView plocation = new TextView(MainActivity.this);
            plocation.setText(p.getLocation());
            TextView pexpenses = new TextView(MainActivity.this);
            pexpenses.setText(p.getExpenses());
            // Adding TextViews
            row.addView(pid);
            row.addView(pname);
            row.addView(pcompany);
            row.addView(plocation);
            row.addView(pexpenses);
            // Adding the row to tableLayout
            Log.v("BHENCHOD", "MADARCHOD");
            projectTable.addView(row);
            Log.v("KIA ADD", "RANDI");
        }
    }

    private void transitionToNewWorkerActivity() {

        Intent intent = new Intent(MainActivity.this, EditWorkerActivity.class);
        startActivity(intent);
    }

    private void transitionToNewProjectActivity(){
        Intent intent = new Intent(MainActivity.this, NewProjectActivity.class);
        startActivity(intent);
    }
}