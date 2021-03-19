  package com.theinternetcompany.wpworkermanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theinternetcompany.wpworkermanagement.Models.Project;
import com.theinternetcompany.wpworkermanagement.Models.WorkerProfile;

import java.util.ArrayList;
import java.util.HashMap;

public class WokerDetailActivity extends AppCompatActivity {

    private WorkerProfile worker = new WorkerProfile();
    TableLayout projectTable;
    private HashMap<String, WorkerProfile> pWorkerDetails = new HashMap<>();
    private WorkerProfile pWorker = new WorkerProfile();
    private ArrayList<Project> projectList = new ArrayList<>();
    private ArrayList<Project> wProjectList = new ArrayList<>();
    private DatabaseReference projectListRef = FirebaseDatabase.getInstance().getReference();
    private TextView workerName, workerCardNo, workType, baseRate, totalProjects, totalEarnings, pendingExpenses;

//    @Override
//    public void onBackPressed() {
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_woker_detail);

        workerName = findViewById(R.id.twWorkerName);
        workerCardNo = findViewById(R.id.twWorkerCardNo);
        workType = findViewById(R.id.tvWorkType);
        baseRate = findViewById(R.id.tvBaseRate);
        totalProjects = findViewById(R.id.tvTotalProjects);
        totalEarnings = findViewById(R.id.twTotalEarnings);
        pendingExpenses = findViewById(R.id.twPendingExpenses);
        projectTable = findViewById(R.id.projectTable);

        Intent i = getIntent();
        worker = (WorkerProfile) i.getSerializableExtra("worker");
        workerName.setText(worker.getName());
        workerCardNo.setText(worker.getCardNo());
        workType.setText(worker.getWorkType());
        baseRate.setText(worker.getBaseRate());
        getProjectData();


    }

    private void getProjectData() {
        projectListRef = FirebaseDatabase.getInstance().getReference().child("Project_List");
        projectListRef.keepSynced(true);

        projectListRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    projectList.add(snap.getValue(Project.class));
                }
                for (Project project: projectList){
                    HashMap<String,WorkerProfile> workerList;
                    workerList = project.getWorkerList();
                    for (String id: workerList.keySet()){
                        if(id.equals(worker.getId())){
                            wProjectList.add(project);
                        }
                    }
                }
                populateTable();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void populateTable() {
        Log.d("lol",String.valueOf(wProjectList.size()));
        cleanTable(projectTable);
        for (Project p : wProjectList){
            TableRow row = new TableRow(WokerDetailActivity.this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
            row.setLayoutParams(lp);
            // Generating TextViews for row
//            TextView pid = new TextView(MainActivity.this);
//            pid.setText(p.getId());
            TextView ID = new TextView(WokerDetailActivity.this);
            ID.setText(p.getId());
            ID.setPadding(20,20,20,20);
            ID.setTextSize(20);
            TextView name = new TextView(WokerDetailActivity.this);
            name.setText(p.getName());
            name.setPadding(20,20,20,20);
            name.setTextSize(20);
            TextView company = new TextView(WokerDetailActivity.this);
            company.setText(p.getCompany());
            company.setPadding(20,20,20,20);
            company.setTextSize(20);



            HashMap<String,WorkerProfile> workerList = new HashMap<>();
            workerList = p.getWorkerList();

            Log.d("lol2",String.valueOf(workerList.size()));
            Log.d("lol2",String.valueOf(workerList.get(worker.getId())));

            TextView rate = new TextView(WokerDetailActivity.this);
            rate.setText(String.valueOf(workerList.get(worker.getId()).getRate()));
            rate.setPadding(20,20,20,20);
            rate.setTextSize(20);
            TextView shifts = new TextView(WokerDetailActivity.this);
            shifts.setText("");
            shifts.setPadding(20,20,20,20);
            shifts.setTextSize(20);
            TextView wage = new TextView(WokerDetailActivity.this);
            wage.setText("");
            wage.setPadding(20,20,20,20);
            wage.setTextSize(20);
            TextView conveyance = new TextView(WokerDetailActivity.this);
            conveyance.setText("");
            conveyance.setPadding(20,20,20,20);
            conveyance.setTextSize(20);
            TextView total = new TextView(WokerDetailActivity.this);
            total.setText("");
            total.setPadding(20,20,20,20);
            total.setTextSize(20);
            // Adding TextViews
//            row.addView(pid);
            row.addView(ID);
            row.addView(name);
            row.addView(company);
            row.addView(rate);
            row.addView(shifts);
            row.addView(wage);
            row.addView(conveyance);
            row.addView(total);
            // Adding the row to tableLayout
            Log.v("BHENCHOD", "MADARCHOD");



            projectTable.addView(row);
            Log.v("KIA ADD", "RANDI");
        }
    }

    private void cleanTable(TableLayout projectTable) {

        int childCount = projectTable.getChildCount();

        // Remove all rows except the first one
        if (childCount > 1) {
            projectTable.removeViews(1, childCount - 1);
        }
    }
}