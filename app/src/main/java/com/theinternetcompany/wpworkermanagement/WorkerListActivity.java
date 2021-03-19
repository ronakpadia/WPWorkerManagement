package com.theinternetcompany.wpworkermanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theinternetcompany.wpworkermanagement.Models.Project;
import com.theinternetcompany.wpworkermanagement.Models.WorkerProfile;

import java.util.ArrayList;
import java.util.List;

public class WorkerListActivity extends AppCompatActivity {

    private Button btnHome, btnHideShowColumns;
    private FloatingActionButton btnNewEmployee;
    TableLayout projectTable;
    private SearchView searchView;
    private ArrayList<WorkerProfile> workerList = new ArrayList<>();
    private DatabaseReference mainRef = FirebaseDatabase.getInstance().getReference();

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
                Intent intent = new Intent(WorkerListActivity.this, MainActivity.class);
                startActivity(intent);
                break;
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_list);
        
        btnHome = findViewById(R.id.home);
        btnNewEmployee = findViewById(R.id.btnAddNewWorker);
        searchView = findViewById(R.id.searchView);
        btnHideShowColumns = findViewById(R.id.btnHideShowColumns);
        mainRef.keepSynced(true);
        getWorkerData();

        btnHideShowColumns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // setup the alert builder
                final List<Integer> selectedItems = new ArrayList<Integer>();
                AlertDialog.Builder builder = new AlertDialog.Builder(WorkerListActivity.this);
                builder.setTitle("Choose Tags");
                // add a checkbox list
                String[] tags = {"Name", "Card No", "Base Rate","Work Type"};
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

        btnNewEmployee.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                transitionToNewWorkerActivity();
            }
        });
    }

    private void getWorkerData() {
        projectTable = findViewById(R.id.projectTable);
        DatabaseReference projectRef = mainRef.child("Worker_List");
        projectRef.keepSynced(true);


        projectRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap : snapshot.getChildren()){
                    workerList.add(snap.getValue(WorkerProfile.class));

                }

                populateTable(workerList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void doMySearch(String query) {
        ArrayList<WorkerProfile> filteredList = new ArrayList<>();
        if(query == null || query.length() == 0)
        {
            filteredList.addAll(workerList);
            Log.v("tag",String.valueOf(workerList.size()));
            projectTable = findViewById(R.id.projectTable);
            populateTable(filteredList);
        }
        else
        {
            String filterPattern = query.toLowerCase().trim();
            Log.v("tag1",filterPattern);
            for  (WorkerProfile p : workerList)
            {

                if(p.getName().toLowerCase().contains(filterPattern))
                {
                    Log.v("tag2",p.getName());
                    filteredList.add(p);
                    Log.v("tag3",String.valueOf(filteredList.size()));
                }

            }
            projectTable = findViewById(R.id.projectTable);
            populateTable(filteredList);
        }
    }

    private void populateTable(ArrayList<WorkerProfile> workerList) {
        cleanTable(projectTable);
        for (WorkerProfile p : workerList){
            TableRow row = new TableRow(WorkerListActivity.this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
            row.setLayoutParams(lp);
            // Generating TextViews for row
//            TextView pid = new TextView(MainActivity.this);
//            pid.setText(p.getId());
            TextView wName = new TextView(WorkerListActivity.this);
            wName.setText(p.getName());
            wName.setPadding(20,20,20,20);
            wName.setTextSize(20);
            TextView card = new TextView(WorkerListActivity.this);
            card.setText(p.getCardNo());
            card.setPadding(20,20,20,20);
            card.setTextSize(20);
            TextView rate = new TextView(WorkerListActivity.this);
            rate.setText(p.getRate());
            rate.setPadding(20,20,20,20);
            rate.setTextSize(20);
            TextView workType = new TextView(WorkerListActivity.this);
            workType.setText(p.getWorkType());
            workType.setPadding(20,20,20,20);
            workType.setTextSize(20);
            // Adding TextViews
//            row.addView(pid);
            row.addView(wName);
            row.addView(card);
            row.addView(rate);
            row.addView(workType);
            row.setClickable(true);
            row.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    transitionToWorkerDetailActivity(p);

                }
            });
            // Adding the row to tableLayout
            Log.v("BHENCHOD", "MADARCHOD");
            projectTable.addView(row);
            Log.v("KIA ADD", "RANDI");
        }
    }

    private void transitionToWorkerDetailActivity(WorkerProfile worker) {
        Intent intent = new Intent(WorkerListActivity.this, WokerDetailActivity.class);
        intent.putExtra("worker", worker);
        startActivity(intent);
    }

    private void cleanTable(TableLayout table) {

        int childCount = table.getChildCount();

        // Remove all rows except the first one
        if (childCount > 1) {
            table.removeViews(1, childCount - 1);
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

    private void transitionToNewWorkerActivity() {
        Intent intent = new Intent(WorkerListActivity.this, NewWorkerActivity.class);
        startActivity(intent);
    }

    private void transitionToMainActivity() {
        Intent intent = new Intent(WorkerListActivity.this, MainActivity.class);
        startActivity(intent);
    }
}