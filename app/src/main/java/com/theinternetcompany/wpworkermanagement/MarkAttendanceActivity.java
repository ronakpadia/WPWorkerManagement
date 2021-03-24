package com.theinternetcompany.wpworkermanagement;

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
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theinternetcompany.wpworkermanagement.Models.Project;
import com.theinternetcompany.wpworkermanagement.Models.WorkerProfile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MarkAttendanceActivity extends AppCompatActivity {
    private EditText ETdate;
    private Button btnMarkAttendance, btnAddConveyance, btnSave;
    private Project project = new Project();
    private TableLayout workerTable, workerTable2;
    private SearchView searchView;
    private TableRow tableHeaderTags;
    private LinearLayout layout, buttonLayout;
    private ArrayList<WorkerProfile> attendenceList = new ArrayList<>();
    private ArrayList<WorkerProfile> pWorkerList = new ArrayList<>();
    private DatabaseReference mainRef = FirebaseDatabase.getInstance().getReference();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_attendance);

        workerTable = findViewById(R.id.workerTable);
        workerTable2 = findViewById(R.id.workerTable2);
        searchView = findViewById(R.id.searchView);
        layout = findViewById(R.id.linearLayoutT);
        btnMarkAttendance = findViewById(R.id.btnMarkAttendance);
        btnAddConveyance = findViewById(R.id.btnAddConveyance);
        ETdate = findViewById(R.id.ETdate);
        btnSave = findViewById(R.id.btnSave);
        tableHeaderTags = findViewById(R.id.tableHeaderTags);
        Intent i = getIntent();
        project = (Project) i.getSerializableExtra("project");
//        getAttendance();
        getWorkerList();
        btnSave.setVisibility(View.GONE);
        layout.setVisibility(View.GONE);


        //Date Picker
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

        ETdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(MarkAttendanceActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnMarkAttendance.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                btnSave.setVisibility(View.VISIBLE);
                layout.setVisibility(View.VISIBLE);
                btnMarkAttendance.setVisibility(View.GONE);
                btnAddConveyance.setVisibility(View.GONE);
                populateTable(workerTable2, "attendance");

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                btnSave.setVisibility(View.GONE);
                layout.setVisibility(View.GONE);
                btnMarkAttendance.setVisibility(View.VISIBLE);
                btnAddConveyance.setVisibility(View.VISIBLE);

            }
        });

        btnAddConveyance.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                btnSave.setVisibility(View.VISIBLE);
                layout.setVisibility(View.VISIBLE);
                btnMarkAttendance.setVisibility(View.GONE);
                btnAddConveyance.setVisibility(View.GONE);
                populateTable(workerTable2, "conveyance");
            }
        });
    }

    private void getAttendance() {
        mainRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference attendanceRef = mainRef.child("Project_List").child(project.getId()).child("workerList").child("Attendance");
        attendanceRef.keepSynced(true);

        attendanceRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                attendenceList.clear();
                for(DataSnapshot snap : snapshot.getChildren()){
                    attendenceList.add(snap.getValue(WorkerProfile.class));

                }

                populateTable(workerTable,"none");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateLabel(Calendar myCalendar) {
        String myFormat = "dd-MM-yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        ETdate.setText(sdf.format(myCalendar.getTime()));
    }

    private void getWorkerList() {
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
                
                populateTable(workerTable, "none");


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void populateTable(TableLayout table, String parent) {
        cleanTable(table);
        if (table == workerTable){
            for (String date : pWorkerList.get(0).getAttendance().keySet()){
                TextView dateTag = new TextView(MarkAttendanceActivity.this);
                dateTag.setText(String.valueOf(date));
                dateTag.setPadding(20,20,20,20);
                dateTag.setTextSize(20);
                dateTag.setTextColor(Color.BLACK);
                dateTag.setTypeface(Typeface.DEFAULT_BOLD);
                tableHeaderTags.addView(dateTag);
            }
        }

        for (WorkerProfile p : pWorkerList){
            TableRow row = new TableRow(MarkAttendanceActivity.this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
            row.setLayoutParams(lp);
            // Generating TextViews for row
//            TextView pid = new TextView(MainActivity.this);
//            pid.setText(p.getId());
            TextView wName = new TextView(MarkAttendanceActivity.this);
            wName.setText(p.getName());
            wName.setPadding(20,20,20,20);
            wName.setTextSize(20);
            TextView card = new TextView(MarkAttendanceActivity.this);
            card.setText(p.getCardNo());
            card.setPadding(20,20,20,20);
            card.setTextSize(20);
            TextView rate = new TextView(MarkAttendanceActivity.this);
            rate.setText(p.getRate());
            rate.setPadding(20,20,20,20);
            rate.setTextSize(20);
            TextView workType = new TextView(MarkAttendanceActivity.this);
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
                        openShiftDialog(p, parent);


                    }
                });
            }
            else{
                for (String shift : p.getAttendance().values()){
                    TextView shifts = new TextView(MarkAttendanceActivity.this);
                    shifts.setText(String.valueOf(shift));
                    shifts.setPadding(20,20,20,20);
                    shifts.setTextSize(20);
                    row.addView(shifts);
                }
            }


            table.addView(row);
            Log.v("KIA ADD", "RANDI");
        }
    }

    private void openShiftDialog(WorkerProfile p, String parent) {
        EditText shiftsInput = new EditText(this);
        String msg = "";
        if (parent.equals("attendance")){
            msg = "Enter Shifts:";
        }else if (parent.equals("conveyance")){
            msg = "Enter Conveyance:";
        }
        shiftsInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(p.getName())
                .setMessage(msg)
                .setView(shiftsInput)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String editTextInput = shiftsInput.getText().toString();
                        if (TextUtils.isEmpty(ETdate.getText().toString())) {
                            Toast.makeText(MarkAttendanceActivity.this,"Enter Date", Toast.LENGTH_SHORT).show();
                        }
                        else if (TextUtils.isEmpty(editTextInput)) {
                            Log.d("debug" , editTextInput);
                            Toast.makeText(MarkAttendanceActivity.this,"Enter Shift", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            if (parent.equals("attendance")){
                                addWorkerAttendance(p,editTextInput);
                            }else if (parent.equals("conveyance")){
                                addWorkerConveyance(p,editTextInput);
                            }
                            
//                            getAttWorkerList();
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

    private void addWorkerConveyance(WorkerProfile worker, String editTextInput) {
        DatabaseReference workerRef = mainRef.child("Project_List").child(project.getId()).child("workerList").child(worker.getId()).child("Conveyance");
        workerRef.child(String.valueOf(ETdate.getText())).setValue(editTextInput);
    }

    private void addWorkerAttendance(WorkerProfile targetWorker, String editTextInput) {
        for (WorkerProfile worker: pWorkerList){
            if (worker.getId().equals(targetWorker.getId())){
                DatabaseReference workerRef = mainRef.child("Project_List").child(project.getId()).child("workerList").child(worker.getId()).child("Attendance");
                workerRef.child(String.valueOf(ETdate.getText())).setValue(editTextInput);
                workerRef.keepSynced(true);
            }
            else{
                DatabaseReference workerRef = mainRef.child("Project_List").child(project.getId()).child("workerList").child(worker.getId()).child("Attendance");
                workerRef.child(String.valueOf(ETdate.getText())).setValue("0");
                workerRef.keepSynced(true);
            }
        }


    }

    public void showKeyboard(){
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }
    

    public void closeKeyboard(){
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    private void cleanTable(TableLayout table) {

        int childCount = table.getChildCount();

        // Remove all rows except the first one
        if (childCount > 1) {
            table.removeViews(1, childCount - 1);
        }
    }


}