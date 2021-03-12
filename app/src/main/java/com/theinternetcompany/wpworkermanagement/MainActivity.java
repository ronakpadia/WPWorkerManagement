package com.theinternetcompany.wpworkermanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
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
    private SearchView searchView;
    private Button btnEmployeeList, btnAddNewProject, btnHideShowColumns;
    private TextView idTag, nameTag, locationTag, companyTag,periodTag, expensesTag;
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


        searchView = findViewById(R.id.searchView);
        btnAddNewProject = findViewById(R.id.btnAddNewProject);
        btnEmployeeList = findViewById(R.id.btnEmployeeList);
        btnHideShowColumns = findViewById(R.id.btnHideShowColumns);
        nameTag = findViewById(R.id.nameTag);
        locationTag = findViewById(R.id.locationTag);
        periodTag = findViewById(R.id.periodTag);
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
                String[] tags = {"name", "company", "location","period", "expenses"};
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

        nameTag.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                collapseColumn(0);
            }
        });

        companyTag.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                collapseColumn(1);
            }
        });

        locationTag.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                collapseColumn(2);
            }
        });

        periodTag.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                collapseColumn(3);
            }
        });

        expensesTag.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                collapseColumn(4);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                doMySearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(s.equals("")){
                    this.onQueryTextSubmit("");
                }
                return false;
            }
        });

        btnEmployeeList.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                transitionToWorkerListActivity();
            }
        });
//
    }

    private void doMySearch(String query) {
        ArrayList<Project> filteredList = new ArrayList<>();
        if(query == null || query.length() == 0)
        {
            filteredList.addAll(projectList);
            Log.v("tag",String.valueOf(projectList.size()));
            projectTable = findViewById(R.id.projectTable);
            populateTable(filteredList);
        }
        else
        {
            String filterPattern = query.toLowerCase().trim();
            Log.v("tag1",filterPattern);
            for  (Project p : projectList)
            {

                if(p.getName().toLowerCase().contains(filterPattern))
                {
                    Log.v("tag2",p.getName());
                    filteredList.add(p);
                    Log.v("tag3",String.valueOf(filteredList.size()));
                }
                else if(p.getCompany().toLowerCase().contains(filterPattern))
                {
                    filteredList.add(p);
                }else if(p.getLocation().toLowerCase().contains(filterPattern))
                {
                    filteredList.add(p);
                }else if(p.getPeriod().toLowerCase().contains(filterPattern))
                {
                    filteredList.add(p);
                }

            }
            projectTable = findViewById(R.id.projectTable);
            populateTable(filteredList);
        }
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
        cleanTable(projectTable);
        for (Project p : projectList){
            TableRow row = new TableRow(MainActivity.this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
            row.setLayoutParams(lp);
            // Generating TextViews for row
//            TextView pid = new TextView(MainActivity.this);
//            pid.setText(p.getId());
            TextView pname = new TextView(MainActivity.this);
            pname.setText(p.getName());
            pname.setPadding(20,20,20,20);
            pname.setTextSize(20);
            TextView pcompany = new TextView(MainActivity.this);
            pcompany.setText(p.getCompany());
            pcompany.setPadding(20,20,20,20);
            pcompany.setTextSize(20);
            TextView plocation = new TextView(MainActivity.this);
            plocation.setText(p.getLocation());
            plocation.setPadding(20,20,20,20);
            plocation.setTextSize(20);
            TextView pperiod = new TextView(MainActivity.this);
            pperiod.setText(p.getPeriod());
            pperiod.setPadding(20,20,20,20);
            pperiod.setTextSize(20);
            TextView pexpenses = new TextView(MainActivity.this);
            pexpenses.setText(p.getExpenses());
            pexpenses.setPadding(20,20,20,20);
            pexpenses.setTextSize(20);
            // Adding TextViews
//            row.addView(pid);
            row.addView(pname);
            row.addView(pcompany);
            row.addView(plocation);
            row.addView(pperiod);
            row.addView(pexpenses);
            row.setClickable(true);
            row.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.v("Click", p.getName());
                    Intent intent = new Intent(MainActivity.this, ProjectDetailActivity.class);
                    intent.putExtra("project", p);
                    startActivity(intent);

                }
            });
            // Adding the row to tableLayout
            Log.v("BHENCHOD", "MADARCHOD");
            projectTable.addView(row);
            Log.v("KIA ADD", "RANDI");
        }
    }

    private void transitionToWorkerListActivity() {

        Intent intent = new Intent(MainActivity.this, WorkerListActivity.class);
        startActivity(intent);
    }

    private void transitionToNewProjectActivity(){
        Intent intent = new Intent(MainActivity.this, NewProjectActivity.class);
        startActivity(intent);
    }

    private void cleanTable(TableLayout table) {

        int childCount = table.getChildCount();

        // Remove all rows except the first one
        if (childCount > 1) {
            table.removeViews(1, childCount - 1);
        }
    }

}