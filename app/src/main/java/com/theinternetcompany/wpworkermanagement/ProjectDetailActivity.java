package com.theinternetcompany.wpworkermanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theinternetcompany.wpworkermanagement.Models.Project;
import com.theinternetcompany.wpworkermanagement.Models.WorkerProfile;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class ProjectDetailActivity extends AppCompatActivity {

    private Project project = new Project();
    TableLayout projectTable, projectTable2;
    private SearchView searchView;
    private ArrayList<WorkerProfile> workerList = new ArrayList<>();
    private ArrayList<WorkerProfile> pWorkerList = new ArrayList<>();
    private ArrayList<WorkerProfile> addWorkerList = new ArrayList<>();
    private ArrayList<WorkerProfile> removeWorkerList = new ArrayList<>();
    private HashMap<String, WorkerProfile> WorkerList = new HashMap<>();
    private DatabaseReference mainRef = FirebaseDatabase.getInstance().getReference();
    private TextView projectName, projectId, projectLocation, projectCompany, projectDuration;
    private Button btnAdd, btnRemove, btnSave,btnMarkAttendance;
    private FloatingActionButton btnEditProject,btnDelete;
    LinearLayout layout;

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
                Intent intent = new Intent(ProjectDetailActivity.this, MainActivity.class);
                startActivity(intent);
                break;
        }

        return true;
    }

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
        btnEditProject = findViewById(R.id.btnEditProject);
        btnDelete = findViewById(R.id.btnDelete);
        btnMarkAttendance = findViewById(R.id.btnMarkAttendance);

        Intent i = getIntent();
        project = (Project) i.getSerializableExtra("project");
        projectName.setText(project.getName());
        projectId.setText(project.getId());
        projectCompany.setText(project.getCompany());
        projectLocation.setText(project.getLocation());
        projectDuration.setText(project.getPeriod());
//        btnSave.setVisibility(View.GONE);
        layout.setVisibility(View.GONE);
        getPWorkerData("main");
//        getWorkerData();
        populateTable(pWorkerList, projectTable, "main");

        btnRemove.setOnClickListener(new View.OnClickListener() {
            String parent = "Remove";

            @Override
            public void onClick(View v) {
                btnRemove.setVisibility(View.GONE);
                btnSave.setVisibility(View.VISIBLE);
                btnAdd.setVisibility(View.GONE);
                layout .setVisibility(View.VISIBLE);
                getPWorkerData("remove");



            }
        });

//        btnSave.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                btnRemove.setVisibility(View.VISIBLE);
//                btnAdd.setVisibility(View.VISIBLE);
//                btnSave.setVisibility(View.GONE);
//                layout.setVisibility(View.GONE);
//
//            }
//        });

        btnAdd.setOnClickListener(new View.OnClickListener() {

            // Reminder : duplicate in pWorkerList shud not overwrite previous entry

            @Override
            public void onClick(View v) {
                btnAdd.setVisibility(View.GONE);
                btnSave.setVisibility(View.VISIBLE);
                btnRemove.setVisibility(View.GONE);
                layout .setVisibility(View.VISIBLE);
                getPWorkerData("add");
//                populateTable(workerList, projectTable2, "add");

            }
        });

        btnEditProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEditProjectDialog(pWorkerList);
            }
        });
        

        btnDelete.setOnClickListener(new View.OnClickListener() {
            String parent = "Remove";

            @Override
            public void onClick(View v) {
                deleteProjectDialog();
            }
        });

        btnMarkAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transitionToMarkAttendanceActivity();
            }
        });
    }

    private void transitionToMarkAttendanceActivity() {
        Intent intent = new Intent(ProjectDetailActivity.this, MarkAttendanceActivity.class);
        intent.putExtra("project", project);
        startActivity(intent);
    }


    private void openEditProjectDialog(ArrayList<WorkerProfile> pWorkerList) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        LinearLayout.LayoutParams tagParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        params.setMargins(40,15,40,15);
        tagParams.setMargins(30,15,30,15);
        LinearLayout editProjectLL = new LinearLayout(this);
        editProjectLL.setOrientation(LinearLayout.VERTICAL);
        EditText projectName = new EditText(this);
        EditText projectCompany = new EditText(this);
        EditText projectLocation = new EditText(this);
        EditText projectPeriod = new EditText(this);
        TextView nameTag = new TextView(this);
        TextView companyTag = new TextView(this);
        TextView locationTag = new TextView(this);
        TextView periodTag = new TextView(this);
        nameTag.setText("Name:");
        companyTag.setText("Company:");
        locationTag.setText("Location:");
        periodTag.setText("Period:");
        nameTag.setLayoutParams(tagParams);
        companyTag.setLayoutParams(tagParams);
        locationTag.setLayoutParams(tagParams);
        periodTag.setLayoutParams(tagParams);
        projectName.setLayoutParams(params);
        projectCompany.setLayoutParams(params);
        projectLocation.setLayoutParams(params);
        projectPeriod.setLayoutParams(params);
        projectName.setText(project.getName());
        projectCompany.setText(project.getCompany());
        projectLocation.setText(project.getLocation());
        projectPeriod.setText(project.getPeriod());
        editProjectLL.addView(nameTag);
        editProjectLL.addView(projectName);
        editProjectLL.addView(companyTag);
        editProjectLL.addView(projectCompany);
        editProjectLL.addView(locationTag);
        editProjectLL.addView(projectLocation);
        editProjectLL.addView(periodTag);
        editProjectLL.addView(projectPeriod);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(project.getName())
                .setMessage("Edit Project")
                .setView(editProjectLL)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        closeKeyboard();
                        String pName = projectName.getText().toString();
                        String pLocation= projectLocation.getText().toString();
                        String pPeriod = projectPeriod.getText().toString();
                        String pCompany = projectCompany.getText().toString();
                        if (TextUtils.isEmpty(pName)) {
                            Toast.makeText(ProjectDetailActivity.this,"Enter Project Name", Toast.LENGTH_SHORT).show();
                        }

                        else if (TextUtils.isEmpty(pLocation)) {
                            Toast.makeText(ProjectDetailActivity.this,"Enter Location", Toast.LENGTH_SHORT).show();
                        }

                        else if (TextUtils.isEmpty(pPeriod)) {
                            Toast.makeText(ProjectDetailActivity.this,"Enter Period", Toast.LENGTH_SHORT).show();
                        }

                        else if (TextUtils.isEmpty(pCompany)) {
                            Toast.makeText(ProjectDetailActivity.this,"Enter Company", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            HashMap<String,WorkerProfile> WorkerList = new HashMap<String,WorkerProfile>();
                            Log.d("debug", String.valueOf(pWorkerList.size()));
                            for (WorkerProfile worker: pWorkerList){
                                WorkerList.put(worker.getId(), worker);
                            }

                            project.setName(pName);
                            project.setLocation(pLocation);
                            project.setPeriod(pPeriod);
                            project.setCompany(pCompany);
                            project.setWorkerList(WorkerList);
                            mainRef.child("Project_List").child(project.getId()).setValue(project);
                            refreshActivity();
                        }


//                        String editTextInput = rateInput.getText().toString();
//                        w.setRate(editTextInput);
//                        addPWorker(w);
//                        Log.d("onclick","editext value is: "+ editTextInput);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        closeKeyboard();
                    }
                })
                .create();
        dialog.show();
        projectName.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        imm.showSoftInput(projectName, InputMethodManager.SHOW_IMPLICIT);
        showKeyboard();
    }

    private void refreshActivity(){
//        Intent intent = getIntent();
//        finish();
//        startActivity(intent);
        Intent intent = new Intent(ProjectDetailActivity.this, ProjectDetailActivity.class);
        intent.putExtra("project", project);
        startActivity(intent);
    }

    private void transitionToMainActivity() {
        Intent intent = new Intent(ProjectDetailActivity.this, MainActivity.class);
        startActivity(intent);
    }


    private void addPWorker(WorkerProfile worker) {

        DatabaseReference workerRef = mainRef.child("Project_List").child(project.getId()).child("workerList");
        workerRef.keepSynced(true);
        WorkerProfile newWorker = new WorkerProfile(worker.getId(), worker.getName(),worker.getCardNo(), worker.getRate(), worker.getBaseRate(), worker.getWorkType() );
        if (project.getWorkerList().size() != 0){
            WorkerProfile randomWorker = (WorkerProfile) project.getWorkerList().values().toArray()[0];
            Log.d("addWorker", randomWorker.getName());
            HashMap<String,String> nullAttendance = new HashMap<>();
            for (String date : randomWorker.getAttendance().keySet()){
                nullAttendance.put(date, "0");
                Log.d("addWorker", date);
            }

            newWorker.setAttendance(nullAttendance);
        }

        workerRef.child(worker.getId()).setValue(newWorker);
        getPWorkerData("add");

    }

    private void removePWorker(WorkerProfile worker) {

        DatabaseReference workerRef = mainRef.child("Project_List").child(project.getId()).child("workerList");
        workerRef.keepSynced(true);
        workerRef.child(worker.getId()).removeValue();
        getPWorkerData("remove");
    }

    private void getWorkerData() {

        mainRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference pWorkerRef = mainRef.child("Project_List").child(project.getId()).child("workerList");
        pWorkerRef.keepSynced(true);


        pWorkerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pWorkerList.clear();
                for(DataSnapshot snap : snapshot.getChildren()){
                    pWorkerList.add(snap.getValue(WorkerProfile.class));

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


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

    private void getPWorkerData(String parent) {
        mainRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference workerRef = mainRef.child("Project_List").child(project.getId()).child("workerList");
        Log.d("pWorker",String.valueOf(pWorkerList.size()) );
        workerRef.keepSynced(true);


        workerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pWorkerList.clear();
                for(DataSnapshot snap : snapshot.getChildren()){
                    pWorkerList.add(snap.getValue(WorkerProfile.class));

                }

                populateTable(pWorkerList, projectTable, "inner");
                if (parent.equals("add")){
                    DatabaseReference workerRef = mainRef.child("Worker_List");
                    workerRef.keepSynced(true);
                    workerList.clear();
                    workerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot snap : snapshot.getChildren()){
                                WorkerProfile add = new WorkerProfile();
                                add = snap.getValue(WorkerProfile.class);
                                workerList.add(add);

                            }
                            addWorkerList.clear();

                            //Make AddWOrkerLItst
                            Log.v("WORKERLISTSIZE", String.valueOf(workerList.size()));
                            Log.v("PWORKERLIST", String.valueOf(pWorkerList.size()));
                            for(WorkerProfile w : workerList){
                                boolean isPresent = false;
                                for(WorkerProfile pw : pWorkerList){
                                    if(pw.getId().equals(w.getId())){
                                        isPresent = true;
                                    }
                                }
                                if(!isPresent){
                                    addWorkerList.add(w);
                                }

                            }
                            Log.v("MAKICHUT", String.valueOf(addWorkerList.size()));
                            populateTable(addWorkerList, projectTable2, parent);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
//                    populateTable(workerList, projectTable2, "add");
//                            populateTable(pWorkerList, projectTable, "inner");
                }
                else {
                    populateTable(pWorkerList, projectTable2, parent);
//                            populateTable(pWorkerList, projectTable, "inner");
                }

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

                            openRateDialog(p);
//                            populateTable(pWorkerList, projectTable, "inner");
                        }
                        else if (parentMethod.equals("remove")){
                            removePWorker(p);
//                            populateTable(pWorkerList, projectTable, "inner");
                        }


                    }
                });
            }


            table.addView(row);
            Log.v("KIA ADD", "RANDI");
        }
    }

    private void openRateDialog(WorkerProfile w){
        EditText rateInput = new EditText(this);
        rateInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        Integer rate = Integer.valueOf(w.getRate());
        Log.v("RATE",String.valueOf(rate));
        rateInput.setText(String.valueOf(rate));
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Rate")
                .setMessage("Enter Rate:")
                .setView(rateInput)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        closeKeyboard();
                        String editTextInput = rateInput.getText().toString();
                        w.setRate(editTextInput);
                        addPWorker(w);
                        Log.d("onclick","editext value is: "+ editTextInput);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        closeKeyboard();
                    }
                })
                .create();
        dialog.show();
        rateInput.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        imm.showSoftInput(rateInput, InputMethodManager.SHOW_IMPLICIT);
        showKeyboard();
    }

    public void showKeyboard(){
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public void closeKeyboard(){
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    private void deleteProjectDialog() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(15,15,15,15);
        LinearLayout editProjectLL = new LinearLayout(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Delete Project")
                .setMessage("Are you sure you want to delete this project? This action is irreversible, you may lose data permanently.")
                .setView(editProjectLL)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseReference projectRef = mainRef.child("Project_List").child(project.getId());
                        projectRef.keepSynced(true);
                        projectRef.removeValue();
                        transitionToMainActivity();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .create();
        dialog.show();

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