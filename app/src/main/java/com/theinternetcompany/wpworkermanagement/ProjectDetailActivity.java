package com.theinternetcompany.wpworkermanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.theinternetcompany.wpworkermanagement.Models.WorkerProfile;

import java.util.ArrayList;

import static android.os.SystemClock.sleep;

public class ProjectDetailActivity extends AppCompatActivity {

    private Project project = new Project();
    TableLayout projectTable, projectTable2;
    private SearchView searchView;
    private ArrayList<WorkerProfile> workerList = new ArrayList<>();
    private ArrayList<WorkerProfile> pWorkerList = new ArrayList<>();
    private DatabaseReference mainRef = FirebaseDatabase.getInstance().getReference();
    private TextView projectName, projectId, projectLocation, projectCompany, projectDuration;
    private Button btnAdd, btnRemove, btnSave;
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);
        projectName = findViewById(R.id.twProjectName);
        projectId = findViewById(R.id.twProjectId);
        projectTable = findViewById(R.id.projectTable);
        projectTable2 = findViewById(R.id.projectTable2);
        searchView = findViewById(R.id.searchView);
        layout = findViewById(R.id.linearLayoutT);
        projectCompany = findViewById(R.id.twProjectCompany);
        projectDuration = findViewById(R.id.twProjectDuration);
        projectLocation = findViewById(R.id.twProjectLocation);
        btnAdd = findViewById(R.id.btnAdd);
        btnRemove = findViewById(R.id.btnRemove);
        btnSave = findViewById(R.id.btnSave);

        Intent i = getIntent();
        project = (Project) i.getSerializableExtra("project");
        projectName.setText(project.getName());
        projectId.setText(project.getId());
        projectCompany.setText(project.getCompany());
        projectLocation.setText(project.getLocation());
        projectDuration.setText(project.getPeriod());
        btnSave.setVisibility(View.GONE);
        layout.setVisibility(View.GONE);
        getWorkerData();
        getPWorkerData();
        populateTable(pWorkerList, projectTable, "main");

        btnRemove.setOnClickListener(new View.OnClickListener() {
            String parent = "Remove";

            @Override
            public void onClick(View v) {
                btnRemove.setVisibility(View.GONE);
                btnSave.setVisibility(View.VISIBLE);
                btnAdd.setVisibility(View.GONE);
                layout .setVisibility(View.VISIBLE);
                getPWorkerData();
                populateTable(pWorkerList, projectTable2, "remove");


            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                btnRemove.setVisibility(View.VISIBLE);
                btnAdd.setVisibility(View.VISIBLE);
                btnSave.setVisibility(View.GONE);
                layout.setVisibility(View.GONE);

            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {

            // Reminder : duplicate in pWorkerList shud not overwrite previous entry

            @Override
            public void onClick(View v) {
                btnAdd.setVisibility(View.GONE);
                btnSave.setVisibility(View.VISIBLE);
                btnRemove.setVisibility(View.GONE);
                layout .setVisibility(View.VISIBLE);
                populateTable(workerList, projectTable2, "add");

            }
        });

    }

    private void addPWorker(WorkerProfile worker) {

        DatabaseReference workerRef = mainRef.child("Project_List").child(project.getId()).child("worker_list");
        workerRef.keepSynced(true);
        WorkerProfile newWorker = new WorkerProfile(worker.getId(), worker.getName(),worker.getCardNo(), worker.getBaseRate() , worker.getRate(), worker.getWorkType() );
        workerRef.child(worker.getId()).setValue(newWorker);
    }

    private void removePWorker(WorkerProfile worker) {

        DatabaseReference workerRef = mainRef.child("Project_List").child(project.getId()).child("worker_list");
        workerRef.keepSynced(true);
        workerRef.child(worker.getId()).removeValue();
    }

    private void getWorkerData() {
        DatabaseReference workerRef = mainRef.child("Worker_List");
        workerRef.keepSynced(true);


        workerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                workerList.clear();
                for(DataSnapshot snap : snapshot.getChildren()) {
                    workerList.add(snap.getValue(WorkerProfile.class));

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getPWorkerData() {
        mainRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference workerRef = mainRef.child("Project_List").child(project.getId()).child("worker_list");
        workerRef.keepSynced(true);


        workerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pWorkerList.clear();
                for(DataSnapshot snap : snapshot.getChildren()){
                    pWorkerList.add(snap.getValue(WorkerProfile.class));

                }

                populateTable(pWorkerList, projectTable, "inner");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void populateTable(ArrayList<WorkerProfile> workerList, TableLayout table, String parentMethod) {
        cleanTable(table);
        for (WorkerProfile p : workerList){
            TableRow row = new TableRow(ProjectDetailActivity.this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
            row.setLayoutParams(lp);
            // Generating TextViews for row
//            TextView pid = new TextView(MainActivity.this);
//            pid.setText(p.getId());
            TextView wName = new TextView(ProjectDetailActivity.this);
            wName.setText(p.getName());
            wName.setPadding(20,20,20,20);
            wName.setTextSize(20);
            TextView card = new TextView(ProjectDetailActivity.this);
            card.setText(p.getCardNo());
            card.setPadding(20,20,20,20);
            card.setTextSize(20);
            TextView rate = new TextView(ProjectDetailActivity.this);
            rate.setText(p.getRate());
            rate.setPadding(20,20,20,20);
            rate.setTextSize(20);
            TextView workType = new TextView(ProjectDetailActivity.this);
            workType.setText(p.getWorkType());
            workType.setPadding(20,20,20,20);
            workType.setTextSize(20);
            // Adding TextViews
//            row.addView(pid);
            row.addView(wName);
            row.addView(card);
            row.addView(rate);
            row.addView(workType);
            // Adding the row to tableLayout
            Log.v("BHENCHOD", "MADARCHOD");

            if (table == projectTable2){
                row.setClickable(true);
                row.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (parentMethod.equals("add")){
                            addPWorker(p);
                            sleep(1000);
                            getPWorkerData();
//                            populateTable(pWorkerList, projectTable, "inner");
                        }
                        else if (parentMethod.equals("remove")){
                            removePWorker(p);
                            sleep(1000);
                            getPWorkerData();
//                            populateTable(pWorkerList, projectTable, "inner");
                        }


                    }
                });
            }


            table.addView(row);
            Log.v("KIA ADD", "RANDI");
        }
    }

    private void cleanTable(TableLayout table) {

        int childCount = table.getChildCount();

        // Remove all rows except the first one
        if (childCount > 1) {
            table.removeViews(1, childCount - 1);
        }
    }

    private void refreshDatabase(){
        mainRef = FirebaseDatabase.getInstance().getReference();
    }


}