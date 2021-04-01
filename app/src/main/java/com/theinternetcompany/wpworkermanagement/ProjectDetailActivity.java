package com.theinternetcompany.wpworkermanagement;

import androidx.annotation.IntegerRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class ProjectDetailActivity extends AppCompatActivity {

    private Project project = new Project();
    TableLayout workerTable, workerTable2;
    private EditText  ETdate;
    private SearchView workerRemoveSearchView, workerAddSearchView,workerConveyanceSearchView, workerAttendanceSearchView, pWorkerSearchView;
    private ArrayList<WorkerProfile> workerList = new ArrayList<>();
    private ArrayList<WorkerProfile> pWorkerList = new ArrayList<>();
    private ArrayList<WorkerProfile> addWorkerList = new ArrayList<>();
    private ArrayList<WorkerProfile> removeWorkerList = new ArrayList<>();
    private HashMap<String, WorkerProfile> WorkerList = new HashMap<>();
    private DatabaseReference mainRef = FirebaseDatabase.getInstance().getReference();
    private TextView projectName, projectId, projectLocation, projectCompany, projectDuration, twConveyance, twWages,twCashExpenses,twChequePayments,twTotalExpenses, tableTitle;
    private Button btnAdd, btnRemove,btnMarkAttendance, btnAddConveyance;
    private FloatingActionButton btnEditProject,btnDelete, btnDone;
    LinearLayout layout, dateLL, buttonsLL;

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
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        projectName = findViewById(R.id.twProjectName);
        projectId = findViewById(R.id.twProjectId);
        workerTable = findViewById(R.id.projectTable);
        workerTable2 = findViewById(R.id.projectTable2);
        tableTitle = findViewById(R.id.tableTitle);
        ETdate = findViewById(R.id.ETdate);
        pWorkerSearchView = findViewById(R.id.pWorkerSearchView);
        workerRemoveSearchView = findViewById(R.id.workerRemoveSearchView);
        workerAddSearchView = findViewById(R.id.workerAddSearchView);
        workerConveyanceSearchView = findViewById(R.id.workerConveyanceSearchView);
        workerAttendanceSearchView = findViewById(R.id.workerAttendanceSearchView);
        layout = findViewById(R.id.linearLayoutT);
        dateLL = findViewById(R.id.dateLL);
        buttonsLL = findViewById(R.id.buttonsLL);
        projectCompany = findViewById(R.id.twProjectCompany);
        projectDuration = findViewById(R.id.twProjectDuration);
        projectLocation = findViewById(R.id.twProjectLocation);
        twCashExpenses = findViewById(R.id.twCashExpenses);
        twChequePayments = findViewById(R.id.twChequePayments);
        twConveyance = findViewById(R.id.twConveyance);
        twWages = findViewById(R.id.twWages);
        twTotalExpenses = findViewById(R.id.twTotalExpenses);
        btnAdd = findViewById(R.id.btnAdd);
        btnRemove = findViewById(R.id.btnRemove);
        btnAddConveyance = findViewById(R.id.btnConveyance);
        btnMarkAttendance = findViewById(R.id.btnMarkAttendance);
        btnDone = findViewById(R.id.btnDone);
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
        twConveyance.setText(String.valueOf(project.calculateConveyance()));
        twWages.setText(String.valueOf(project.calculateWage()));
        twCashExpenses.setText(String.valueOf(project.getCash()));
        twChequePayments.setText(String.valueOf(project.getCheque()));
        twTotalExpenses.setText(String.valueOf(project.calculateTotal()));
        btnDone.setVisibility(View.GONE);
        tableTitle.setVisibility(View.GONE);
        workerRemoveSearchView.setVisibility(View.GONE);
        workerAddSearchView.setVisibility(View.GONE);
        workerConveyanceSearchView.setVisibility(View.GONE);
        workerAttendanceSearchView.setVisibility(View.GONE);
        layout.setVisibility(View.GONE);
        dateLL.setVisibility(View.GONE);
        getPWorkerData("main");
//        getWorkerData();
//        populateTable(pWorkerList, workerTable, "main");

        final Calendar myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {


            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(myCalendar);
            }

        };

        pWorkerSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                doMySearch(s, pWorkerList, "null");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(s.equals("")){
                    this.onQueryTextSubmit("");
                }
                else{
                    doMySearch(s, pWorkerList, "null");
                }
                return false;
            }
        });

        ETdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(ProjectDetailActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            String parent = "Remove";

            @Override
            public void onClick(View v) {
                buttonsLL.setVisibility(View.GONE);
                btnEditProject.setVisibility(View.GONE);
                btnDelete.setVisibility(View.GONE);
                dateLL.setVisibility(View.GONE);
                workerAddSearchView.setVisibility(View.GONE);
                workerConveyanceSearchView.setVisibility(View.GONE);
                workerAttendanceSearchView.setVisibility(View.GONE);
                tableTitle.setVisibility(View.VISIBLE);
                tableTitle.setText("Remove Worker: ");
                workerRemoveSearchView.setVisibility(View.VISIBLE);
                btnDone.setVisibility(View.VISIBLE);
                getPWorkerData("remove");
                layout .setVisibility(View.VISIBLE);




            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {

            // Reminder : duplicate in pWorkerList shud not overwrite previous entry

            @Override
            public void onClick(View v) {
                buttonsLL.setVisibility(View.GONE);
                btnEditProject.setVisibility(View.GONE);
                btnDelete.setVisibility(View.GONE);
                dateLL.setVisibility(View.GONE);
                workerConveyanceSearchView.setVisibility(View.GONE);
                workerAttendanceSearchView.setVisibility(View.GONE);
                workerRemoveSearchView.setVisibility(View.GONE);
                tableTitle.setVisibility(View.VISIBLE);
                tableTitle.setText("Add Worker: ");
                workerAddSearchView.setVisibility(View.VISIBLE);
                btnDone.setVisibility(View.VISIBLE);
                getPWorkerData("add");
                layout .setVisibility(View.VISIBLE);

//                populateTable(workerList, projectTable2, "add");

            }
        });

        btnAddConveyance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonsLL.setVisibility(View.GONE);
                btnEditProject.setVisibility(View.GONE);
                btnDelete.setVisibility(View.GONE);
                dateLL.setVisibility(View.GONE);
                workerConveyanceSearchView.setVisibility(View.VISIBLE);
                workerAttendanceSearchView.setVisibility(View.GONE);
                workerRemoveSearchView.setVisibility(View.GONE);
                workerAddSearchView.setVisibility(View.GONE);
                tableTitle.setVisibility(View.VISIBLE);
                tableTitle.setText("Add Conveyance");
                btnDone.setVisibility(View.VISIBLE);
                layout .setVisibility(View.VISIBLE);
                getPWorkerData("conveyance");
            }
        });

        btnMarkAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonsLL.setVisibility(View.GONE);
                btnEditProject.setVisibility(View.GONE);
                btnDelete.setVisibility(View.GONE);
                workerConveyanceSearchView.setVisibility(View.GONE);
                workerAttendanceSearchView.setVisibility(View.VISIBLE);
                workerRemoveSearchView.setVisibility(View.GONE);
                workerAddSearchView.setVisibility(View.GONE);
                tableTitle.setVisibility(View.VISIBLE);
                tableTitle.setText("Mark Attendance");
                dateLL.setVisibility(View.VISIBLE);
                btnDone.setVisibility(View.VISIBLE);
                layout .setVisibility(View.VISIBLE);
                getPWorkerData("attendance");
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                buttonsLL.setVisibility(View.VISIBLE);
                btnEditProject.setVisibility(View.VISIBLE);
                btnDelete.setVisibility(View.VISIBLE);
                dateLL.setVisibility(View.GONE);
                tableTitle.setVisibility(View.GONE);
                workerConveyanceSearchView.setVisibility(View.GONE);
                workerAttendanceSearchView.setVisibility(View.GONE);
                workerRemoveSearchView.setVisibility(View.GONE);
                workerAddSearchView.setVisibility(View.GONE);
                btnDone.setVisibility(View.GONE);
                layout .setVisibility(View.GONE);

            }
        });

        workerRemoveSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                doMySearch(s, pWorkerList, "remove");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(s.equals("")){
                    this.onQueryTextSubmit("");
                }
                else{
                    doMySearch(s, pWorkerList, "remove");
                }
                return false;
            }
        });

        workerAddSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                doMySearch(s, workerList, "add");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(s.equals("")){
                    this.onQueryTextSubmit("");
                }
                else{
                    doMySearch(s, pWorkerList, "add");
                }
                return false;
            }
        });

        workerConveyanceSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                doMySearch(s, pWorkerList, "conveyance");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(s.equals("")){
                    this.onQueryTextSubmit("");
                }
                else{
                    doMySearch(s, pWorkerList, "conveyance");
                }
                return false;
            }
        });

        workerAttendanceSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                doMySearch(s, pWorkerList, "attendance");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(s.equals("")){
                    this.onQueryTextSubmit("");
                }
                else{
                    doMySearch(s, pWorkerList, "attendance");
                }
                return false;
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


    }

    private void doMySearch(String query, ArrayList<WorkerProfile> pWorkerList, String parent) {
        ArrayList<WorkerProfile> filteredList = new ArrayList<>();
        if(query == null || query.length() == 0)
        {
            filteredList.addAll(pWorkerList);
            populateTable(filteredList, workerTable , parent);
        }
        else
        {
            String filterPattern = query.toLowerCase().trim();
            Log.v("tag1",filterPattern);
            for  (WorkerProfile worker : pWorkerList)
            {

                if(worker.getName().toLowerCase().contains(filterPattern))
                {
                    filteredList.add(worker);
                }
                else if(worker.getWorkType().toLowerCase().contains(filterPattern))
                {
                    filteredList.add(worker);
                }else if(worker.getCardNo().toLowerCase().contains(filterPattern))
                {
                    filteredList.add(worker);
                }

            }

            if (parent.equals("null")){
                populateTable(filteredList, workerTable , parent);
            }
            else{
                populateTable(filteredList, workerTable2 , parent);
            }

        }
    }

    private void updateLabel(Calendar myCalendar) {
        String myFormat = "dd-MM-yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        ETdate.setText(sdf.format(myCalendar.getTime()));
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
        workerRef.child(worker.getId()).setValue(newWorker);
        if (project.getWorkerList().size() != 0){
            WorkerProfile randomWorker = (WorkerProfile) project.getWorkerList().values().toArray()[0];
            Log.d("addWorker", randomWorker.getName());
            HashMap<String,String> nullAttendance = new HashMap<>();
            for (String date : randomWorker.getAttendance().keySet()){
                nullAttendance.put(date, "0");
                Log.d("addWorker", date);
            }

            workerRef.child(worker.getId()).child("Attendance").setValue(nullAttendance);
        }
        getPWorkerData("add");

    }

    private void removePWorker(WorkerProfile worker) {

        DatabaseReference workerRef = mainRef.child("Project_List").child(project.getId()).child("workerList");
        workerRef.keepSynced(true);
        workerRef.child(worker.getId()).removeValue();
        getPWorkerData("remove");
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

                populateTable(pWorkerList, workerTable, "inner");
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
                            populateTable(addWorkerList, workerTable2, parent);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
//                    populateTable(workerList, projectTable2, "add");
//                            populateTable(pWorkerList, projectTable, "inner");
                }
                else {
                    populateTable(pWorkerList, workerTable2, parent);
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

        TableRow headerRow = new TableRow(ProjectDetailActivity.this);
        TableRow.LayoutParams rlp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
        headerRow.setLayoutParams(rlp);

        TextView nameTag = new TextView(ProjectDetailActivity.this);
        nameTag.setText("Name");
        nameTag.setPadding(20,20,20,20);
        nameTag.setTextSize(20);
        nameTag.setTextColor(Color.BLACK);
        nameTag.setTypeface(Typeface.DEFAULT_BOLD);
        headerRow.addView(nameTag);

        TextView cardNoTag = new TextView(ProjectDetailActivity.this);
        cardNoTag.setText("Card No.");
        cardNoTag.setPadding(20,20,20,20);
        cardNoTag.setTextSize(20);
        cardNoTag.setTextColor(Color.BLACK);
        cardNoTag.setTypeface(Typeface.DEFAULT_BOLD);
        headerRow.addView(cardNoTag);

        TextView rateTag = new TextView(ProjectDetailActivity.this);
        rateTag.setText("Rate");
        rateTag.setPadding(20,20,20,20);
        rateTag.setTextSize(20);
        rateTag.setTextColor(Color.BLACK);
        rateTag.setTypeface(Typeface.DEFAULT_BOLD);
        headerRow.addView(rateTag);

        TextView workTypeTag = new TextView(ProjectDetailActivity.this);
        workTypeTag.setText("Work Type");
        workTypeTag.setPadding(20,20,20,20);
        workTypeTag.setTextSize(20);
        workTypeTag.setTextColor(Color.BLACK);
        workTypeTag.setTypeface(Typeface.DEFAULT_BOLD);
        headerRow.addView(workTypeTag);

        if (table == workerTable){
            if (pWorkerList.size() != 0){
                if (pWorkerList.get(0).getAttendance() != null){
                    for (String date : pWorkerList.get(0).getAttendance().keySet()){
                        TextView dateTag = new TextView(ProjectDetailActivity.this);
                        dateTag.setText(String.valueOf(date));
                        dateTag.setPadding(20,20,20,20);
                        dateTag.setTextSize(20);
                        dateTag.setTextColor(Color.BLACK);
                        dateTag.setTypeface(Typeface.DEFAULT_BOLD);
                        headerRow.addView(dateTag);
                    }
                }
            }


            TextView totalShiftsTag = new TextView(ProjectDetailActivity.this);
            totalShiftsTag.setText("Total Shifts");
            totalShiftsTag.setPadding(20,20,20,20);
            totalShiftsTag.setTextSize(20);
            totalShiftsTag.setTextColor(Color.BLACK);
            totalShiftsTag.setTypeface(Typeface.DEFAULT_BOLD);
            headerRow.addView(totalShiftsTag);

            TextView wageTag = new TextView(ProjectDetailActivity.this);
            wageTag.setText("Wage");
            wageTag.setPadding(20,20,20,20);
            wageTag.setTextSize(20);
            wageTag.setTextColor(Color.BLACK);
            wageTag.setTypeface(Typeface.DEFAULT_BOLD);
            headerRow.addView(wageTag);

            TextView conveyanceTag = new TextView(ProjectDetailActivity.this);
            conveyanceTag.setText("Conveyance");
            conveyanceTag.setPadding(20,20,20,20);
            conveyanceTag.setTextSize(20);
            conveyanceTag.setTextColor(Color.BLACK);
            conveyanceTag.setTypeface(Typeface.DEFAULT_BOLD);
            headerRow.addView(conveyanceTag);

            TextView totalTag = new TextView(ProjectDetailActivity.this);
            totalTag.setText("Total");
            totalTag.setPadding(20,20,20,20);
            totalTag.setTextSize(20);
            totalTag.setTextColor(Color.BLACK);
            totalTag.setTypeface(Typeface.DEFAULT_BOLD);
            headerRow.addView(totalTag);
        }

        table.addView(headerRow);
        Log.d("header", parentMethod);


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

            if (table == workerTable2){
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
                        else if (parentMethod.equals("conveyance")){
                            openConveyanceDialog(p);
                        }
                        else if (parentMethod.equals("attendance")){
                            openShiftDialog(p);
                        }


                    }
                });
            }

            else{
                if(p.getAttendance() != null){
                    for (String shift : p.getAttendance().values()){
                        TextView shifts = new TextView(ProjectDetailActivity.this);
                        shifts.setText(String.valueOf(shift));
                        shifts.setPadding(20,20,20,20);
                        shifts.setTextSize(20);
                        row.addView(shifts);
                    }
                }

                TextView totalShifts = new TextView(ProjectDetailActivity.this);
                if (p.calculateTotalShifts() != null){
                    totalShifts.setText(String.valueOf(p.calculateTotalShifts()));
                }
                else{
                    totalShifts.setText("0");
                }

                totalShifts.setPadding(20,20,20,20);
                totalShifts.setTextSize(20);
                row.addView(totalShifts);

                TextView wage = new TextView(ProjectDetailActivity.this);
                if (p.calculateTotalWage() != null){
                    wage.setText(String.valueOf(p.calculateTotalWage()));
                }
                else{
                    wage.setText("0");
                }

                wage.setPadding(20,20,20,20);
                wage.setTextSize(20);
                row.addView(wage);

                TextView conveyance = new TextView(ProjectDetailActivity.this);
                conveyance.setText(String.valueOf(p.getConveyance()));
                conveyance.setPadding(20,20,20,20);
                conveyance.setTextSize(20);
                row.addView(conveyance);

                TextView total = new TextView(ProjectDetailActivity.this);
                total.setText(String.valueOf(p.calculateTotal()));
                total.setPadding(20,20,20,20);
                total.setTextSize(20);
                row.addView(total);
            }


            table.addView(row);
            Log.v("KIA ADD", "RANDI");
        }
    }

    private void openShiftDialog(WorkerProfile p) {
        EditText shiftsInput = new EditText(this);
        shiftsInput.setInputType(InputType.TYPE_CLASS_NUMBER);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(p.getName())
                .setMessage("Enter Shifts:")
                .setView(shiftsInput)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String editTextInput = shiftsInput.getText().toString();

                        if (TextUtils.isEmpty(ETdate.getText().toString())) {
                            Toast.makeText(ProjectDetailActivity.this,"Enter Date", Toast.LENGTH_SHORT).show();
                        }

                        else if (TextUtils.isEmpty(editTextInput)) {
                            Toast.makeText(ProjectDetailActivity.this,"Enter Shift", Toast.LENGTH_SHORT).show();
                            Log.d("debug" , editTextInput);

                        }
                        else{
                            addWorkerAttendance(p,editTextInput, ETdate.getText().toString());
                        }
                        closeKeyboard();
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
        shiftsInput.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        imm.showSoftInput(shiftsInput, InputMethodManager.SHOW_IMPLICIT);
        showKeyboard();
    }

    private void openConveyanceDialog(WorkerProfile p) {
        EditText conveyanceInput = new EditText(this);

        conveyanceInput.setInputType(InputType.TYPE_CLASS_NUMBER);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(p.getName())
                .setMessage("Enter Conveyance:")
                .setView(conveyanceInput)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String editTextInput = conveyanceInput.getText().toString();
                        if (TextUtils.isEmpty(editTextInput)) {

                            Toast.makeText(ProjectDetailActivity.this,"Enter Conveyance", Toast.LENGTH_SHORT).show();
                            Log.d("debug" , editTextInput);

                        }
                        else{
                            addWorkerConveyance(p,editTextInput);
                        }
                        closeKeyboard();
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
        conveyanceInput.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        imm.showSoftInput(conveyanceInput, InputMethodManager.SHOW_IMPLICIT);
        showKeyboard();
    }

    private void addWorkerConveyance(WorkerProfile worker, String editTextInput) {
        DatabaseReference projecRef = mainRef.child("Project_List").child(project.getId());
        projecRef.child("workerList").child(worker.getId()).child("conveyance").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Integer conveyance = snapshot.getValue(Integer.class);
                Integer projectConveyance = conveyance + Integer.parseInt(editTextInput);
                projecRef.child("workerList").child(worker.getId()).child("conveyance").setValue(projectConveyance);

                refreshProjectDetails();
                getPWorkerData("conveyance");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void addWorkerAttendance(WorkerProfile targetWorker, String editTextInput,String ETdate) {
        ArrayList<WorkerProfile> workerList = new ArrayList<>();
        DatabaseReference projectRef = mainRef.child("Project_List").child(project.getId());
        projectRef.keepSynced(true);

        projectRef.child("workerList").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap : snapshot.getChildren()){
                    workerList.add(snap.getValue(WorkerProfile.class));

                }
                for (WorkerProfile worker: workerList){
                    Log.d("addAttendance", worker.getId() + " " + targetWorker.getId());
                    if (worker.getId().equals(targetWorker.getId())){
                        if(worker.getAttendance() != null){
                            if (worker.getAttendance().containsKey(ETdate)){
                                if (worker.getAttendance().get(ETdate).equals("0")){
                                    projectRef.child("workerList").child(worker.getId()).child("Attendance").child(ETdate).setValue(editTextInput);
                                    refreshProjectDetails();
                                    projectRef.keepSynced(true);
                                }else{
                                    duplicateAttendanceWarningDialog(worker, editTextInput);
                                }

                            }
                            else{
                                projectRef.child("workerList").child(worker.getId()).child("Attendance").child(ETdate).setValue(editTextInput);
                                refreshProjectDetails();
                                projectRef.keepSynced(true);

                            }
                        }else{
                            projectRef.child("workerList").child(worker.getId()).child("Attendance").child(ETdate).setValue(editTextInput);
                            refreshProjectDetails();
                            projectRef.keepSynced(true);

                        }


                    }
                    else{
                        DatabaseReference workerRef = mainRef.child("Project_List").child(project.getId()).child("workerList").child(worker.getId()).child("Attendance");
                        workerRef.keepSynced(true);
                        if (worker.getAttendance() != null){
                            if (!worker.getAttendance().containsKey(ETdate)){
                                workerRef.child(ETdate).setValue("0");
                            }

                        }
                        else{
                            workerRef.child(ETdate).setValue("0");
                        }

                    }
                }
                getPWorkerData("attendance");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



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


    private void refreshProjectDetails(){
        DatabaseReference projectRef = mainRef.child("Project_List").child(project.getId());
        projectRef.keepSynced(true);


        projectRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Project updatedProject = snapshot.getValue(Project.class);
                twConveyance.setText(String.valueOf(updatedProject.calculateConveyance()));
                twWages.setText(String.valueOf(updatedProject.calculateWage()));
                twCashExpenses.setText(String.valueOf(updatedProject.getCash()));
                twChequePayments.setText(String.valueOf(updatedProject.getCheque()));
                twTotalExpenses.setText(String.valueOf(updatedProject.calculateTotal()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    private void duplicateAttendanceWarningDialog(WorkerProfile targetWorker, String editTextInput) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(15,15,15,15);
        LinearLayout warningLL = new LinearLayout(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Duplicate Entry")
                .setMessage("Worker" + " " + targetWorker.getName() + " " + "already have an entry on " + ETdate.getText().toString() + ". " + "Do you want to override previous entry? Press Yes to continue." )
                .setView(warningLL)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseReference projectRef = mainRef.child("Project_List").child(project.getId());
                        projectRef.child("workerList").child(targetWorker.getId()).child("Attendance").child(String.valueOf(ETdate.getText())).setValue(editTextInput);
                        refreshProjectDetails();
                        projectRef.keepSynced(true);
                        getPWorkerData("attendance");
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


    public void showKeyboard(){
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        ScrollView scroll = (ScrollView) this.findViewById(R.id.scroll);
//        scroll.scrollTo(5, scroll.getBottom());
        scroll.scrollTo(0, scroll.getTop());
        scroll.post(new Runnable() {
            @Override
            public void run() {
                scroll.scrollTo(0, scroll.getBottom());
//                scroll.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
//        mScrollView.post(new Runnable() {
//            public void run() {
//                mScrollView.scrollTo(0, mScrollView.getBottom());
//            }
//        });

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
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
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
        if (childCount > 0) {
            table.removeViews(0, childCount );
        }
    }

    private void refreshDatabase(){
        mainRef = FirebaseDatabase.getInstance().getReference();
    }


}