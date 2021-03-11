package com.theinternetcompany.wpworkermanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TableLayout projectTable;
    private Button btnEmployeeList, btnAddNewProject, btnHideShowColumns;
    private TextView idTag, nameTag, locationTag, companyTag, expensesTag;
    private DatabaseReference mainRef = FirebaseDatabase.getInstance().getReference();
    private ArrayList<Project> projectList = new ArrayList<>();

    // Don't do anything when back is pressed
    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // FORCE DAY MODE
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);
        btnAddNewProject = findViewById(R.id.btnAddNewProject);
        btnEmployeeList = findViewById(R.id.btnEmployeeList);
        btnHideShowColumns = findViewById(R.id.btnHideShowColumns);
        idTag = findViewById(R.id.idTag);
        nameTag = findViewById(R.id.nameTag);
        locationTag = findViewById(R.id.locationTag);
        companyTag = findViewById(R.id.companyTag);
        expensesTag = findViewById(R.id.expensesTag);
        mainRef.keepSynced(true);
        getProjectData(); // Populate the table with latest project
        btnAddNewProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transitionToNewProjectActivity();
            }
        });
        btnHideShowColumns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // setup the alert builder
                final List<Integer> selectedItems = new ArrayList<Integer>();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Choose Tags");
                // add a checkbox list
                String[] tags = {"id", "name", "company", "location", "expenses"};
                boolean[] checkedItems = {false, false, false, false, false};
                builder.setMultiChoiceItems(tags, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        // user checked or unchecked a box
                        if (isChecked) {
                            // If the user checked the item, add it to the selected items
                            selectedItems.add(which);
                        } else if (selectedItems.contains(which)) {
                            // Else, if the item is already in the array, remove it
                            selectedItems.remove(Integer.valueOf(which));
                        }
                    }
                });
                // add OK and Cancel buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // user clicked OK
                        int i;
                        for (i=0; i<=(tags.length-1); i++){
                            if (selectedItems.contains(i)){
                                showColumn(i);
                            }
                            else{
                                collapseColumn(i);
                            }
                        }

                    }
                });
                builder.setNegativeButton("Cancel", null);
                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();



//                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//                ViewGroup viewGroup = findViewById(android.R.id.content);
//                View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_hide_show_columns, viewGroup, false);
//                builder.setView(dialogView);
//                AlertDialog alertDialog = builder.create();
//                alertDialog.show();
//                Button btnIdHideShow;
//                btnIdHideShow = findViewById(R.id.btnViewColID);
//                btnIdHideShow.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        collapseId();
//                    }
//                });
            }
        });
        idTag.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                collapseColumn(0);
            }
        });
//        transitionToNewWorkerActivity();
    }

    private void collapseColumn(Integer tag) {
        projectTable = findViewById(R.id.projectTable);
        projectTable.setColumnCollapsed(tag, true);

    }

    private void showColumn(Integer tag) {
        projectTable = findViewById(R.id.projectTable);
        projectTable.setColumnCollapsed(tag, false);

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