package com.theinternetcompany.wpworkermanagement;

import androidx.annotation.IntegerRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.icu.text.CaseMap;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.theinternetcompany.wpworkermanagement.Models.CashExpense;
import com.theinternetcompany.wpworkermanagement.Models.ChequeExpense;
import com.theinternetcompany.wpworkermanagement.Models.Project;
import com.theinternetcompany.wpworkermanagement.Models.WorkerProfile;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;
import static android.graphics.Color.green;

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
    private ArrayList<CashExpense> cashExpenseList = new ArrayList<>();
    private ArrayList<ChequeExpense> chequeExpenseList = new ArrayList<>();
    private DatabaseReference mainRef = FirebaseDatabase.getInstance().getReference();
    private TextView projectName, projectId, projectLocation, projectCompany, projectDuration, twConveyance, twWages,twCashExpenses,twChequePayments,twTotalExpenses, tableTitle, tvWorkers, tvCash, tvCheque, tvOperationTitle;
    private Button btnAdd, btnRemove,btnMarkAttendance, btnAddConveyance, btnAddEntry, btnEditEntry, btnDeleteEntry, btnAddChequeEntry, btnEditChequeEntry, btnDeleteChequeEntry;
    private FloatingActionButton btnEditProject,btnDelete, btnDone, btnCashDone, btnChequeDone;
    ScrollView tableScollView;
    LinearLayout layout, dateLL, buttonsLL, buttonsLL2, buttonsLL3;

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
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        projectName = findViewById(R.id.twProjectName);
        projectId = findViewById(R.id.twProjectId);
        workerTable = findViewById(R.id.projectTable);
        workerTable2 = findViewById(R.id.projectTable2);
        tableTitle = findViewById(R.id.tableTitle);
        ETdate = findViewById(R.id.ETdate);
        tvWorkers = findViewById(R.id.tvWorkers);
        tvCash = findViewById(R.id.tvCash);
        tvCheque = findViewById(R.id.tvCheque);
        pWorkerSearchView = findViewById(R.id.pWorkerSearchView);
        workerRemoveSearchView = findViewById(R.id.workerRemoveSearchView);
        workerAddSearchView = findViewById(R.id.workerAddSearchView);
        workerConveyanceSearchView = findViewById(R.id.workerConveyanceSearchView);
        workerAttendanceSearchView = findViewById(R.id.workerAttendanceSearchView);
        layout = findViewById(R.id.linearLayoutT);
        dateLL = findViewById(R.id.dateLL);
        buttonsLL = findViewById(R.id.buttonsLL);
        buttonsLL2 = findViewById(R.id.buttonsLL2);
        buttonsLL3 = findViewById(R.id.buttonsLL3);
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
        btnAddEntry = findViewById(R.id.btnAddEntry);
        btnEditEntry = findViewById(R.id.btnEditEntry);
        btnDeleteEntry = findViewById(R.id.btnDeleteEntry);
        btnAddChequeEntry = findViewById(R.id.btnAddChequeEntry);
        btnEditChequeEntry = findViewById(R.id.btnEditChequeEntry);
        btnDeleteChequeEntry = findViewById(R.id.btnDeleteChequeEntry);
        btnCashDone = findViewById(R.id.btnCashDone);
        btnChequeDone = findViewById(R.id.btnChequeDone);
        tvOperationTitle = findViewById(R.id.operationTitle);
        tableScollView = findViewById(R.id.tableScrollView);

        Intent i = getIntent();
        project = (Project) i.getSerializableExtra("project");
//        projectName.setText(project.getName());
//        projectId.setText(project.getId());
//        projectCompany.setText(project.getCompany());
//        projectLocation.setText(project.getLocation());
//        projectDuration.setText(project.getPeriod());
//        twConveyance.setText(String.valueOf(project.calculateConveyance()));
//        twWages.setText(String.valueOf(project.calculateWage()));
//        twCashExpenses.setText(String.valueOf(project.getCash()));
//        twChequePayments.setText(String.valueOf(project.getCheque()));
//        twTotalExpenses.setText(String.valueOf(project.calculateTotal()));
        refreshProjectDetails();
        tvWorkers.setBackgroundResource(R.drawable.roundedbutton);
        tvWorkers.setTextColor(BLACK);
        btnDone.setVisibility(View.GONE);
        tableTitle.setVisibility(View.GONE);
        buttonsLL2.setVisibility(View.GONE);
        buttonsLL3.setVisibility(View.GONE);
        workerRemoveSearchView.setVisibility(View.GONE);
        workerAddSearchView.setVisibility(View.GONE);
        workerConveyanceSearchView.setVisibility(View.GONE);
        workerAttendanceSearchView.setVisibility(View.GONE);
        layout.setVisibility(View.GONE);
        dateLL.setVisibility(View.GONE);
        tvOperationTitle.setVisibility(View.GONE);
        btnCashDone.setVisibility(View.GONE);
        btnChequeDone.setVisibility(View.GONE);
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

        tvWorkers.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                getPWorkerData("main");
                tvCash.setBackgroundResource(R.color.grey);
                tvCash.setTextColor(R.color.dark_grey);
                tvCheque.setBackgroundResource(R.color.grey);
                tvCheque.setTextColor(R.color.dark_grey);
                tvWorkers.setBackgroundResource(R.drawable.roundedbutton);
                tvWorkers.setTextColor(BLACK);
                tvOperationTitle.setVisibility(View.GONE);
                btnCashDone.setVisibility(View.GONE);
                btnChequeDone.setVisibility(View.GONE);
                buttonsLL2.setVisibility(View.GONE);
                buttonsLL3.setVisibility(View.GONE);
                buttonsLL.setVisibility(View.VISIBLE);
                pWorkerSearchView.setVisibility(View.VISIBLE);
                btnEditProject.setVisibility(View.VISIBLE);
                btnDelete.setVisibility(View.VISIBLE);
                btnDone.setVisibility(View.GONE);
                tableTitle.setVisibility(View.GONE);
                workerRemoveSearchView.setVisibility(View.GONE);
                workerAddSearchView.setVisibility(View.GONE);
                workerConveyanceSearchView.setVisibility(View.GONE);
                workerAttendanceSearchView.setVisibility(View.GONE);
                layout.setVisibility(View.GONE);
                dateLL.setVisibility(View.GONE);
                ViewGroup.LayoutParams params = tableScollView.getLayoutParams();
                params.height = dP(150);
                tableScollView.setLayoutParams(params);
                tableScollView.requestLayout();
            }
        });

        tvCash.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                getCashExpenseData("none");
                tvWorkers.setBackgroundResource(R.color.grey);
                tvWorkers.setTextColor(R.color.dark_grey);
                tvCheque.setBackgroundResource(R.color.grey);
                tvCheque.setTextColor(R.color.dark_grey);
                tvCash.setBackgroundResource(R.drawable.roundedbutton);
                tvCash.setTextColor(BLACK);
                btnCashDone.setVisibility(View.GONE);
                btnChequeDone.setVisibility(View.GONE);
                pWorkerSearchView.setVisibility(View.GONE);
                buttonsLL2.setVisibility(View.VISIBLE);
                buttonsLL3.setVisibility(View.GONE);
                btnEditProject.setVisibility(View.VISIBLE);
                btnDelete.setVisibility(View.VISIBLE);
                buttonsLL.setVisibility(View.GONE);
                tvOperationTitle.setVisibility(View.GONE);
                btnDone.setVisibility(View.GONE);
                tableTitle.setVisibility(View.GONE);
                workerRemoveSearchView.setVisibility(View.GONE);
                workerAddSearchView.setVisibility(View.GONE);
                workerConveyanceSearchView.setVisibility(View.GONE);
                workerAttendanceSearchView.setVisibility(View.GONE);
                layout.setVisibility(View.GONE);
                dateLL.setVisibility(View.GONE);
                ViewGroup.LayoutParams params = tableScollView.getLayoutParams();
                params.height = dP(300);
                tableScollView.setLayoutParams(params);
                tableScollView.requestLayout();
            }
        });

        tvCheque.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                getChequeExpenseData("none");
                tvCash.setBackgroundResource(R.color.grey);
                tvCash.setTextColor(R.color.dark_grey);
                tvWorkers.setBackgroundResource(R.color.grey);
                tvWorkers.setTextColor(R.color.dark_grey);
                tvCheque.setBackgroundResource(R.drawable.roundedbutton);
                tvCheque.setTextColor(BLACK);
                buttonsLL2.setVisibility(View.GONE);
                buttonsLL3.setVisibility(View.VISIBLE);
                btnEditProject.setVisibility(View.VISIBLE);
                btnDelete.setVisibility(View.VISIBLE);
                buttonsLL.setVisibility(View.GONE);
                pWorkerSearchView.setVisibility(View.GONE);
                btnDone.setVisibility(View.GONE);
                btnCashDone.setVisibility(View.GONE);
                btnChequeDone.setVisibility(View.GONE);
                tvOperationTitle.setVisibility(View.GONE);
                tableTitle.setVisibility(View.GONE);
                workerRemoveSearchView.setVisibility(View.GONE);
                workerAddSearchView.setVisibility(View.GONE);
                workerConveyanceSearchView.setVisibility(View.GONE);
                workerAttendanceSearchView.setVisibility(View.GONE);
                layout.setVisibility(View.GONE);
                dateLL.setVisibility(View.GONE);
                ViewGroup.LayoutParams params = tableScollView.getLayoutParams();
                params.height = dP(300);
                tableScollView.setLayoutParams(params);
                tableScollView.requestLayout();
            }
        });

        btnAddEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnCashDone.setVisibility(View.GONE);
                btnChequeDone.setVisibility(View.GONE);
                btnDelete.setVisibility(View.VISIBLE);
                btnEditProject.setVisibility(View.VISIBLE);
                tvOperationTitle.setVisibility(View.GONE);
                openAddCashEntryDialogue("add", new CashExpense());
            }
        });

        btnAddChequeEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnCashDone.setVisibility(View.GONE);
                btnChequeDone.setVisibility(View.GONE);
                btnDelete.setVisibility(View.VISIBLE);
                btnEditProject.setVisibility(View.VISIBLE);
                tvOperationTitle.setVisibility(View.GONE);
                openAddChequeEntryDialogue("add", new ChequeExpense());
            }
        });

        btnEditEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnCashDone.setVisibility(View.VISIBLE);
                btnChequeDone.setVisibility(View.GONE);
                btnDelete.setVisibility(View.GONE);
                btnEditProject.setVisibility(View.GONE);
                tvOperationTitle.setVisibility(View.VISIBLE);
                tvOperationTitle.setText("Select entry to edit");
                buttonsLL2.setVisibility(View.GONE);
                populateCashTable(workerTable, "edit", cashExpenseList);

            }
        });

        btnEditChequeEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnCashDone.setVisibility(View.GONE);
                btnChequeDone.setVisibility(View.VISIBLE);
                btnDelete.setVisibility(View.GONE);
                btnEditProject.setVisibility(View.GONE);
                tvOperationTitle.setVisibility(View.VISIBLE);
                tvOperationTitle.setText("Select entry to edit");
                buttonsLL3.setVisibility(View.GONE);
                populateChequeTable(workerTable, "edit", chequeExpenseList);

            }
        });

        btnDeleteEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnCashDone.setVisibility(View.VISIBLE);
                btnChequeDone.setVisibility(View.GONE);
                btnDelete.setVisibility(View.GONE);
                btnEditProject.setVisibility(View.GONE);
                tvOperationTitle.setVisibility(View.VISIBLE);
                tvOperationTitle.setText("Select entry to delete");
                buttonsLL3.setVisibility(View.GONE);
                populateCashTable(workerTable, "delete", cashExpenseList);

            }
        });

        btnDeleteChequeEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnCashDone.setVisibility(View.GONE);
                btnChequeDone.setVisibility(View.VISIBLE);
                btnDelete.setVisibility(View.GONE);
                btnEditProject.setVisibility(View.GONE);
                tvOperationTitle.setVisibility(View.VISIBLE);
                tvOperationTitle.setText("Select entry to delete");
                buttonsLL3.setVisibility(View.GONE);
                populateChequeTable(workerTable, "delete", chequeExpenseList);

            }
        });


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
                buttonsLL2.setVisibility(View.GONE);
                btnEditProject.setVisibility(View.GONE);
                btnDelete.setVisibility(View.GONE);
                dateLL.setVisibility(View.GONE);
                workerAddSearchView.setVisibility(View.GONE);
                workerConveyanceSearchView.setVisibility(View.GONE);
                workerAttendanceSearchView.setVisibility(View.GONE);
                tableTitle.setVisibility(View.VISIBLE);
                tableTitle.setText("Select Worker To Remove: ");
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
                buttonsLL2.setVisibility(View.GONE);
                btnEditProject.setVisibility(View.GONE);
                btnDelete.setVisibility(View.GONE);
                dateLL.setVisibility(View.GONE);
                workerConveyanceSearchView.setVisibility(View.GONE);
                workerAttendanceSearchView.setVisibility(View.GONE);
                workerRemoveSearchView.setVisibility(View.GONE);
                tableTitle.setVisibility(View.VISIBLE);
                tableTitle.setText("Select Worker To Add: ");
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
                buttonsLL2.setVisibility(View.GONE);
                btnEditProject.setVisibility(View.GONE);
                btnDelete.setVisibility(View.GONE);
                dateLL.setVisibility(View.GONE);
                workerConveyanceSearchView.setVisibility(View.VISIBLE);
                workerAttendanceSearchView.setVisibility(View.GONE);
                workerRemoveSearchView.setVisibility(View.GONE);
                workerAddSearchView.setVisibility(View.GONE);
                tableTitle.setVisibility(View.VISIBLE);
                tableTitle.setText("Select Worker To Add Conveyance");
                btnDone.setVisibility(View.VISIBLE);
                layout .setVisibility(View.VISIBLE);
                getPWorkerData("conveyance");
            }
        });

        btnMarkAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonsLL.setVisibility(View.GONE);
                buttonsLL2.setVisibility(View.GONE);
                btnEditProject.setVisibility(View.GONE);
                btnDelete.setVisibility(View.GONE);
                workerConveyanceSearchView.setVisibility(View.GONE);
                workerAttendanceSearchView.setVisibility(View.VISIBLE);
                workerRemoveSearchView.setVisibility(View.GONE);
                workerAddSearchView.setVisibility(View.GONE);
                tableTitle.setVisibility(View.VISIBLE);
                tableTitle.setText("Select Worker To Mark Attendance");
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

        btnCashDone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                buttonsLL2.setVisibility(View.VISIBLE);
                btnEditProject.setVisibility(View.VISIBLE);
                btnDelete.setVisibility(View.VISIBLE);
                btnCashDone.setVisibility(View.GONE);
                btnChequeDone.setVisibility(View.GONE);
                layout .setVisibility(View.GONE);
                tvOperationTitle.setVisibility(View.GONE);
                populateCashTable(workerTable,"none", cashExpenseList);

            }
        });

        btnChequeDone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                buttonsLL3.setVisibility(View.VISIBLE);
                btnEditProject.setVisibility(View.VISIBLE);
                btnDelete.setVisibility(View.VISIBLE);
                btnCashDone.setVisibility(View.GONE);
                btnChequeDone.setVisibility(View.GONE);
                layout .setVisibility(View.GONE);
                tvOperationTitle.setVisibility(View.GONE);
                populateChequeTable(workerTable,"none", chequeExpenseList);

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
                if(s.equals("")){

                }
                else{
                    doMySearch(s, pWorkerList, "add");
                }
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
                DatabaseReference projectRef = mainRef.child("Project_List").child(project.getId());
                projectRef.keepSynced(true);

                projectRef.addListenerForSingleValueEvent(new ValueEventListener(){
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Project project = snapshot.getValue(Project.class);
                        openEditProjectDialog(project);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
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

    private void openAddCashEntryDialogue(String parentMethod, CashExpense cashExpense) {
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
        EditText partyName = new EditText(this);
        EditText particulars = new EditText(this);

        RadioGroup radioGroup = new RadioGroup(this);
        RadioButton radioButtonCredit = new RadioButton(this);
        radioButtonCredit.setText("Credit");
        RadioButton radioButtonDebit = new RadioButton(this);
        radioButtonDebit.setText("Debit");
        radioGroup.addView(radioButtonCredit);
        radioGroup.addView(radioButtonDebit);
        if (parentMethod.equals("edit")){
            if (cashExpense.getCredit() != null){
                radioButtonDebit.setChecked(false);
                radioButtonCredit.setChecked(true);
            }else{
                radioButtonDebit.setChecked(true);
                radioButtonCredit.setChecked(false);
            }
        }else{
            radioButtonDebit.setChecked(true);
            radioButtonCredit.setChecked(false);
        }



//        radioButtonDebit.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                radioButtonDebit.setChecked(true);
//                radioButtonCredit.setChecked(false);
//            }
//        });
//
//        radioButtonCredit.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                radioButtonDebit.setChecked(false);
//                radioButtonCredit.setChecked(true);
//            }
//        });


        EditText amount = new EditText(this);
        amount.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        TextView partyNameTag = new TextView(this);
        TextView particularsTag = new TextView(this);
        TextView amountTag = new TextView(this);

        partyNameTag.setText("Party Name:");
        particularsTag.setText("Particulars:");
        amountTag.setText("Amount:");
        partyNameTag.setLayoutParams(tagParams);
        amountTag.setLayoutParams(tagParams);
        particularsTag.setLayoutParams(tagParams);

        partyName.setLayoutParams(params);
        particulars.setLayoutParams(params);
        amount.setLayoutParams(params);

        if (cashExpense != null){
            partyName.setText(cashExpense.getPartyName());
            if (cashExpense.getParticulars() != null){
                particulars.setText(cashExpense.getParticulars());
            }
            if (cashExpense.getCredit() != null){
                amount.setText(String.valueOf(cashExpense.getCredit()));
            }else if (cashExpense.getDebit() != null){
                amount.setText(String.valueOf(cashExpense.getDebit()));
            }
        }

        String msg = "";

        if (parentMethod.equals("add")){
            msg = "Add Entry";
        }else if(parentMethod.equals("edit")){
            msg = "Edit Entry";
        }

        editProjectLL.addView(partyNameTag);
        editProjectLL.addView(partyName);
        editProjectLL.addView(particularsTag);
        editProjectLL.addView(particulars);
        editProjectLL.addView(radioGroup);
        editProjectLL.addView(amountTag);
        editProjectLL.addView(amount);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Cash Expense")
                .setMessage(msg)
                .setView(editProjectLL)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//
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
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();

                CashExpense cashExpenseEntry = new CashExpense();
                String strPartyName = partyName.getText().toString().trim();
                String strParticulars= particulars.getText().toString().trim();
                String strAmount = amount.getText().toString().trim();


                if (TextUtils.isEmpty(strParticulars)) {
                    cashExpenseEntry.setParticulars("");
                }


                if (TextUtils.isEmpty(strPartyName)) {
                    //this will stop your dialog from closing
                    partyName.setError("This field is required!");
                    return;

                }
                else if (TextUtils.isEmpty(strAmount)) {
                    //this will stop your dialog from closing
                    amount.setError("This field is required!");
                    return;

                }
                else{
                    cashExpenseEntry.setPartyName(strPartyName);
                    cashExpenseEntry.setParticulars(strParticulars);
                    if (radioButtonCredit.isChecked()){
                        cashExpenseEntry.setCredit(Integer.parseInt(strAmount));
                    }
                    else{
                        cashExpenseEntry.setDebit(Integer.parseInt(strAmount));
                    }
                    String id = "";
                    if (parentMethod.equals("add")){
                        id = mainRef.child("Project_List").child(project.getId()).child("cashExpenseList").push().getKey();
                    }else{
                        id = cashExpense.getId();

                    }
                    cashExpenseEntry.setId(id);
                    mainRef.child("Project_List").child(project.getId()).child("cashExpenseList").child(cashExpenseEntry.getId()).setValue(cashExpenseEntry);
                    getCashExpenseData(parentMethod);
                    refreshProjectDetails();

                }

                //you logic here


                dialog.dismiss();

            }
        });
        projectName.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        imm.showSoftInput(projectName, InputMethodManager.SHOW_IMPLICIT);
        showKeyboard();
    }



    private void openAddChequeEntryDialogue(String parentMethod, ChequeExpense chequeExpense) {
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
        LinearLayout addChequeEntryLL = new LinearLayout(this);
        addChequeEntryLL.setOrientation(LinearLayout.VERTICAL);
        EditText partyName = new EditText(this);
        EditText amount = new EditText(this);
        EditText discount = new EditText(this);
        amount.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        TextView partyNameTag = new TextView(this);
        TextView amountTag = new TextView(this);
        TextView discountTag = new TextView(this);

        partyNameTag.setText("Party Name:");
        amountTag.setText("Amount:");
        discountTag.setText("Discount:");
        partyNameTag.setLayoutParams(tagParams);
        amountTag.setLayoutParams(tagParams);
        discountTag.setLayoutParams(tagParams);

        partyName.setLayoutParams(params);
        discount.setLayoutParams(params);
        amount.setLayoutParams(params);

        if (chequeExpense != null){
            partyName.setText(chequeExpense.getName());
            if (chequeExpense.getAmount() != null){
                amount.setText(String.valueOf(chequeExpense.getAmount()));
            }
            if (chequeExpense.getDiscount() != null){
                discount.setText(String.valueOf(chequeExpense.getDiscount()));
            }
        }



        String msg = "";

        if (parentMethod.equals("add")){
            msg = "Add Entry";
        }else if(parentMethod.equals("edit")){
            msg = "Edit Entry";
        }

        addChequeEntryLL.addView(partyNameTag);
        addChequeEntryLL.addView(partyName);
        addChequeEntryLL.addView(amountTag);
        addChequeEntryLL.addView(amount);
        addChequeEntryLL.addView(discountTag);
        addChequeEntryLL.addView(discount);


        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Cheque Expense")
                .setMessage(msg)
                .setView(addChequeEntryLL)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//
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
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();

                ChequeExpense chequeExpenseEntry = new ChequeExpense();
                String strPartyName = partyName.getText().toString().trim();
                String strDiscount= discount.getText().toString().trim();
                String strAmount = amount.getText().toString().trim();


                if (TextUtils.isEmpty(strDiscount)) {
                    chequeExpenseEntry.setDiscount(0);
                }


                if (TextUtils.isEmpty(strPartyName)) {
                    //this will stop your dialog from closing
                    partyName.setError("This field is required!");
                    return;

                }
                else if (TextUtils.isEmpty(strAmount)) {
                    //this will stop your dialog from closing
                    amount.setError("This field is required!");
                    return;

                }
                else{
                    chequeExpenseEntry.setName(strPartyName);
                    if (strDiscount != null && !strDiscount.equals("")){
                        chequeExpenseEntry.setDiscount(Integer.parseInt(strDiscount));
                    }
                    chequeExpenseEntry.setAmount(Integer.parseInt(strAmount));
                    String id = "";
                    if (parentMethod.equals("add")){
                        id = mainRef.child("Project_List").child(project.getId()).child("chequeExpenseList").push().getKey();
                    }else{
                        id = chequeExpense.getId();

                    }
                    chequeExpenseEntry.setId(id);
                    mainRef.child("Project_List").child(project.getId()).child("chequeExpenseList").child(chequeExpenseEntry.getId()).setValue(chequeExpenseEntry);
                    getChequeExpenseData(parentMethod);
                    refreshProjectDetails();

                }

                //you logic here


                dialog.dismiss();

            }
        });
        projectName.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        imm.showSoftInput(projectName, InputMethodManager.SHOW_IMPLICIT);
        showKeyboard();
    }

    private void  doMySearch(String query, ArrayList<WorkerProfile> pWorkerList, String parent) {
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


    private void openEditProjectDialog(Project project) {

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

//                            HashMap<String,WorkerProfile> WorkerList = new HashMap<String,WorkerProfile>();
//                            Log.d("debug", String.valueOf(pWorkerList.size()));
//                            for (WorkerProfile worker: pWorkerList){
//                                WorkerList.put(worker.getId(), worker);
//                            }

                            project.setName(pName);
                            project.setLocation(pLocation);
                            project.setPeriod(pPeriod);
                            project.setCashExpenseList(project.getCashExpenseList());
                            project.setCompany(pCompany);
                            project.setWorkerList(project.getWorkerList());
                            mainRef.child("Project_List").child(project.getId()).setValue(project);
                            refreshProjectDetails();
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
            Integer i;
            WorkerProfile randomWorker = new WorkerProfile();
            HashMap<String,String> nullAttendance = new HashMap<>();
            for (i=0; i<=project.getWorkerList().size(); i++ ){
                randomWorker = (WorkerProfile) project.getWorkerList().values().toArray()[i];
                if (randomWorker.getAttendance() != null){
                    for (String date : randomWorker.getAttendance().keySet()){
                        nullAttendance.put(date, "0");
                        Log.d("addWorker", date);
                    }

                }
                break;
            }
            workerRef.child(worker.getId()).child("Attendance").setValue(nullAttendance);
            getPWorkerData("add");
            Log.d("addWorker", randomWorker.getName());

        }
    }

    private void removePWorker(WorkerProfile worker) {

        DatabaseReference workerRef = mainRef.child("Project_List").child(project.getId()).child("workerList");
        workerRef.keepSynced(true);
        workerRef.child(worker.getId()).removeValue();
        getPWorkerData("remove");
    }

    private void getCashExpenseData(String parentMethod) {




        DatabaseReference cashExpenseRef = mainRef.child("Project_List").child(project.getId()).child("cashExpenseList");;
        cashExpenseRef.keepSynced(true);


        cashExpenseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cashExpenseList.clear();
                for(DataSnapshot snap : snapshot.getChildren()) {
                    cashExpenseList.add(snap.getValue(CashExpense.class));
                }
                populateCashTable(workerTable, parentMethod, cashExpenseList);

//                if (parentMethod.equals("remove") || parentMethod.equals("edit")){
//                    populateCashTable(workerTable2, parentMethod);
//                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void getChequeExpenseData(String parentMethod) {




        DatabaseReference chequeExpenseRef = mainRef.child("Project_List").child(project.getId()).child("chequeExpenseList");
        chequeExpenseRef.keepSynced(true);

        chequeExpenseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chequeExpenseList.clear();
                for(DataSnapshot snap : snapshot.getChildren()) {
                    chequeExpenseList.add(snap.getValue(ChequeExpense.class));
                }
                populateChequeTable(workerTable, parentMethod, chequeExpenseList);



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

    private void populateCashTable(TableLayout table, String parentMethod, ArrayList<CashExpense> cashExpenseList){
        cleanTable(table);



        TableRow headerRow = new TableRow(ProjectDetailActivity.this);
        TableRow.LayoutParams rlp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
        headerRow.setLayoutParams(rlp);

        TextView nameTag = new TextView(ProjectDetailActivity.this);
        nameTag.setText("Name");
        nameTag.setPadding(20,20,20,20);
        nameTag.setTextSize(20);
        nameTag.setTextColor(BLACK);
        nameTag.setTypeface(Typeface.DEFAULT_BOLD);
        headerRow.addView(nameTag);

        TextView particularsTag = new TextView(ProjectDetailActivity.this);
        particularsTag.setText("Particulars");
        particularsTag.setPadding(20,20,20,20);
        particularsTag.setTextSize(20);
        particularsTag.setTextColor(BLACK);
        particularsTag.setTypeface(Typeface.DEFAULT_BOLD);
        headerRow.addView(particularsTag);

        TextView creditTag = new TextView(ProjectDetailActivity.this);
        creditTag.setText("Credit");
        creditTag.setPadding(20,20,20,20);
        creditTag.setTextSize(20);
        creditTag.setTextColor(BLACK);
        creditTag.setTypeface(Typeface.DEFAULT_BOLD);
        headerRow.addView(creditTag);

        TextView debitTag = new TextView(ProjectDetailActivity.this);
        debitTag.setText("Debit");
        debitTag.setPadding(20,20,20,20);
        debitTag.setTextSize(20);
        debitTag.setTextColor(BLACK);
        debitTag.setTypeface(Typeface.DEFAULT_BOLD);
        headerRow.addView(debitTag);

        TextView balanceTag = new TextView(ProjectDetailActivity.this);
        balanceTag.setText("Balance");
        balanceTag.setPadding(20,20,20,20);
        balanceTag.setTextSize(20);
        balanceTag.setTextColor(BLACK);
        balanceTag.setTypeface(Typeface.DEFAULT_BOLD);
        headerRow.addView(nameTag);
        headerRow.addView(particularsTag);
        headerRow.addView(creditTag);
        headerRow.addView(debitTag);
        headerRow.addView(balanceTag);


        table.addView(headerRow);

        if (cashExpenseList != null){
            Integer intBalacnce = 0;

            for (CashExpense cashExpense : cashExpenseList){
                TableRow row = new TableRow(ProjectDetailActivity.this);
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
                row.setLayoutParams(lp);

                TextView partyName = new TextView(ProjectDetailActivity.this);
                partyName.setText(cashExpense.getPartyName());
                partyName.setPadding(20,20,20,20);
                partyName.setTextSize(20);

                TextView particulars = new TextView(ProjectDetailActivity.this);
                particulars.setText(cashExpense.getParticulars());
                particulars.setPadding(20,20,20,20);
                particulars.setTextSize(20);

                TextView credit = new TextView(ProjectDetailActivity.this);
                if (cashExpense.getCredit() != null){
                    intBalacnce += cashExpense.getCredit();
                    credit.setText(String.valueOf(cashExpense.getCredit()));
                }
                credit.setPadding(20,20,20,20);
                credit.setTextSize(20);

                TextView debit = new TextView(ProjectDetailActivity.this);
                if (cashExpense.getDebit() != null){
                    intBalacnce -= cashExpense.getDebit();
                    debit.setText(String.valueOf(cashExpense.getDebit()));
                }
                debit.setPadding(20,20,20,20);
                debit.setTextSize(20);

                TextView balance = new TextView(ProjectDetailActivity.this);
                balance.setText(String.valueOf(intBalacnce));
                balance.setPadding(20,20,20,20);
                balance.setTextSize(20);

                row.addView(partyName);
                row.addView(particulars);
                row.addView(credit);
                row.addView(debit);
                row.addView(balance);

                table.addView(row);

                if (parentMethod.equals("edit") || parentMethod.equals("delete")){
                    row.setClickable(true);
                    row.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if (parentMethod.equals("edit")){
                                openAddCashEntryDialogue("edit", cashExpense);
//                            populateTable(pWorkerList, projectTable, "inner");
                            }
                            else if (parentMethod.equals("delete")){
                                openDeleteEntryDialog("delete", cashExpense);
//                            populateTable(pWorkerList, projectTable, "inner");
                            }
                        }
                    });
                }

                Button pay = new Button(ProjectDetailActivity.this);
//                pay.setPadding(20,20,20,20);
                pay.setTextSize(16);
                if (cashExpense.getPaid()){
                    pay.setText("Paid");
                    pay.setTextColor(Color.parseColor("#00A300"));
                }else{
                    pay.setText("Pay");
                    pay.setTextColor(Color.parseColor("#CC0000"));
                }

//                pay.setBackgroundColor(Color.parseColor("#00A300"));
                pay.setTypeface(Typeface.DEFAULT_BOLD);
                pay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        markCashEntryPaid(cashExpense);
                    }
                });
                row.addView(pay);
            }

        }

    }

    private void populateChequeTable(TableLayout table, String parentMethod, ArrayList<ChequeExpense> chequeExpenseList){
        cleanTable(table);



        TableRow headerRow = new TableRow(ProjectDetailActivity.this);
        TableRow.LayoutParams rlp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
        headerRow.setLayoutParams(rlp);

        TextView nameTag = new TextView(ProjectDetailActivity.this);
        nameTag.setText("Name");
        nameTag.setPadding(20,20,20,20);
        nameTag.setTextSize(20);
        nameTag.setTextColor(BLACK);
        nameTag.setTypeface(Typeface.DEFAULT_BOLD);
        headerRow.addView(nameTag);

        TextView amountTag = new TextView(ProjectDetailActivity.this);
        amountTag.setText("Amount");
        amountTag.setPadding(20,20,20,20);
        amountTag.setTextSize(20);
        amountTag.setTextColor(BLACK);
        amountTag.setTypeface(Typeface.DEFAULT_BOLD);
        headerRow.addView(amountTag);

        TextView discountTag = new TextView(ProjectDetailActivity.this);
        discountTag.setText("Dicount");
        discountTag.setPadding(20,20,20,20);
        discountTag.setTextSize(20);
        discountTag.setTextColor(BLACK);
        discountTag.setTypeface(Typeface.DEFAULT_BOLD);
        headerRow.addView(discountTag);

        TextView finalAmountTag = new TextView(ProjectDetailActivity.this);
        finalAmountTag.setText("Final Amount");
        finalAmountTag.setPadding(20,20,20,20);
        finalAmountTag.setTextSize(20);
        finalAmountTag.setTextColor(BLACK);
        finalAmountTag.setTypeface(Typeface.DEFAULT_BOLD);
        headerRow.addView(nameTag);
        headerRow.addView(amountTag);
        headerRow.addView(discountTag);
        headerRow.addView(finalAmountTag);


        table.addView(headerRow);

        if (chequeExpenseList != null){
            Integer intBalacnce = 0;

            for (ChequeExpense chequeExpense : chequeExpenseList){
                TableRow row = new TableRow(ProjectDetailActivity.this);
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
                row.setLayoutParams(lp);

                TextView partyName = new TextView(ProjectDetailActivity.this);
                partyName.setText(chequeExpense.getName());
                partyName.setPadding(20,20,20,20);
                partyName.setTextSize(20);

                TextView amount = new TextView(ProjectDetailActivity.this);
                amount.setText(String.valueOf(chequeExpense.getAmount()));
                amount.setPadding(20,20,20,20);
                amount.setTextSize(20);


                TextView discount = new TextView(ProjectDetailActivity.this);
                discount.setText(String.valueOf(chequeExpense.getDiscount()));
                discount.setPadding(20,20,20,20);
                discount.setTextSize(20);

                TextView finalAmount = new TextView(ProjectDetailActivity.this);
                finalAmount.setText(String.valueOf(chequeExpense.calculateFinalAmount()));
                finalAmount.setPadding(20,20,20,20);
                finalAmount.setTextSize(20);

                row.addView(partyName);
                row.addView(amount);
                row.addView(discount);
                row.addView(finalAmount);

                table.addView(row);

                if (parentMethod.equals("edit") || parentMethod.equals("delete")){
                    row.setClickable(true);
                    row.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if (parentMethod.equals("edit")){
                                openAddChequeEntryDialogue("edit", chequeExpense);
//                            populateTable(pWorkerList, projectTable, "inner");
                            }
                            else if (parentMethod.equals("delete")){
                                openDeleteChequeEntryDialog("delete", chequeExpense);
//                            populateTable(pWorkerList, projectTable, "inner");
                            }
                        }
                    });
                }

                Button pay = new Button(ProjectDetailActivity.this);
//                pay.setPadding(20,20,20,20);
                pay.setTextSize(16);
                if (chequeExpense.getPaid()){
                    pay.setText("Paid");
                    pay.setTextColor(Color.parseColor("#00A300"));
                }else{
                    pay.setText("Pay");
                    pay.setTextColor(Color.parseColor("#CC0000"));
                }

//                pay.setBackgroundColor(Color.parseColor("#00A300"));
                pay.setTypeface(Typeface.DEFAULT_BOLD);
                pay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        markChequeEntryPaid(chequeExpense);
                    }
                });
                row.addView(pay);
            }

        }

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
        nameTag.setTextColor(BLACK);
        nameTag.setTypeface(Typeface.DEFAULT_BOLD);
        headerRow.addView(nameTag);

        TextView cardNoTag = new TextView(ProjectDetailActivity.this);
        cardNoTag.setText("Card No.");
        cardNoTag.setPadding(20,20,20,20);
        cardNoTag.setTextSize(20);
        cardNoTag.setTextColor(BLACK);
        cardNoTag.setTypeface(Typeface.DEFAULT_BOLD);
        headerRow.addView(cardNoTag);

        TextView rateTag = new TextView(ProjectDetailActivity.this);
        rateTag.setText("Rate");
        rateTag.setPadding(20,20,20,20);
        rateTag.setTextSize(20);
        rateTag.setTextColor(BLACK);
        rateTag.setTypeface(Typeface.DEFAULT_BOLD);
        headerRow.addView(rateTag);

        TextView workTypeTag = new TextView(ProjectDetailActivity.this);
        workTypeTag.setText("Work Type");
        workTypeTag.setPadding(20,20,20,20);
        workTypeTag.setTextSize(20);
        workTypeTag.setTextColor(BLACK);
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
                        dateTag.setTextColor(BLACK);
                        dateTag.setTypeface(Typeface.DEFAULT_BOLD);
                        headerRow.addView(dateTag);
                    }
                }
            }


            TextView totalShiftsTag = new TextView(ProjectDetailActivity.this);
            totalShiftsTag.setText("Total Shifts");
            totalShiftsTag.setPadding(20,20,20,20);
            totalShiftsTag.setTextSize(20);
            totalShiftsTag.setTextColor(BLACK);
            totalShiftsTag.setTypeface(Typeface.DEFAULT_BOLD);
            headerRow.addView(totalShiftsTag);

            TextView wageTag = new TextView(ProjectDetailActivity.this);
            wageTag.setText("Wage");
            wageTag.setPadding(20,20,20,20);
            wageTag.setTextSize(20);
            wageTag.setTextColor(BLACK);
            wageTag.setTypeface(Typeface.DEFAULT_BOLD);
            headerRow.addView(wageTag);

            TextView conveyanceTag = new TextView(ProjectDetailActivity.this);
            conveyanceTag.setText("Conveyance");
            conveyanceTag.setPadding(20,20,20,20);
            conveyanceTag.setTextSize(20);
            conveyanceTag.setTextColor(BLACK);
            conveyanceTag.setTypeface(Typeface.DEFAULT_BOLD);
            headerRow.addView(conveyanceTag);

            TextView totalTag = new TextView(ProjectDetailActivity.this);
            totalTag.setText("Total");
            totalTag.setPadding(20,20,20,20);
            totalTag.setTextSize(20);
            totalTag.setTextColor(BLACK);
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
                            deleteWorkerDialog(p);
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

                Button pay = new Button(ProjectDetailActivity.this);
//                pay.setPadding(20,20,20,20);
                pay.setTextSize(16);
                if (p.getPaid()){
                    pay.setText("Paid");
                    pay.setTextColor(Color.parseColor("#00A300"));
                }else{
                    pay.setText("Pay");
                    pay.setTextColor(Color.parseColor("#CC0000"));
                }

//                pay.setBackgroundColor(Color.parseColor("#00A300"));
                pay.setTypeface(Typeface.DEFAULT_BOLD);
                pay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        markWorkerPaid(p);
                    }
                });
                row.addView(pay);

            }


            table.addView(row);
            Log.v("KIA ADD", "RANDI");
        }
    }

    private void markWorkerPaid(WorkerProfile p) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        String msg = "";
        if (p.getPaid()){
            msg = "Do you want to unmark " + p.getName() + "'s entry as paid?";
        }else{
            msg = "Do you want to mark " + p.getName() + "'s entry as paid?";
        }
        params.setMargins(15,15,15,15);
        LinearLayout editProjectLL = new LinearLayout(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Pay Worker")
                .setMessage(msg)
                .setView(editProjectLL)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (p.getPaid()){
                            p.setPaid(false);
                        }else{
                            p.setPaid(true);
                        }

                        DatabaseReference workerRef = mainRef.child("Project_List").child(project.getId()).child("workerList").child(p.getId());
                        workerRef.setValue(p);
                        getPWorkerData("none");
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

    private void markChequeEntryPaid(ChequeExpense p) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        String msg = "";
        if (p.getPaid()){
            msg = "Do you want to unmark " + p.getName() + "'s entry as paid?";
        }else{
            msg = "Do you want to mark " + p.getName() + "'s entry as paid?";
        }
        params.setMargins(15,15,15,15);
        LinearLayout editProjectLL = new LinearLayout(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Pay Cheque Entry")
                .setMessage(msg)
                .setView(editProjectLL)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (p.getPaid()){
                            p.setPaid(false);
                        }else{
                            p.setPaid(true);
                        }

                        DatabaseReference workerRef = mainRef.child("Project_List").child(project.getId()).child("chequeExpenseList").child(p.getId());
                        workerRef.setValue(p);
                        getChequeExpenseData("none");
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

    private void markCashEntryPaid(CashExpense p) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        String msg = "";
        if (p.getPaid()){
            msg = "Do you want to unmark " + p.getPartyName() + "'s entry as paid?";
        }else{
            msg = "Do you want to mark " + p.getPartyName() + "'s entry as paid?";
        }
        params.setMargins(15,15,15,15);
        LinearLayout editProjectLL = new LinearLayout(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Pay Cash Entry")
                .setMessage(msg)
                .setView(editProjectLL)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (p.getPaid()){
                            p.setPaid(false);
                        }else{
                            p.setPaid(true);
                        }

                        DatabaseReference workerRef = mainRef.child("Project_List").child(project.getId()).child("cashExpenseList").child(p.getId());
                        workerRef.setValue(p);
                        getChequeExpenseData("none");
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
                                    projectRef.child("workerList").child(worker.getId()).child("attendance").child(ETdate).setValue(editTextInput);
                                    refreshProjectDetails();
                                    projectRef.keepSynced(true);
                                }else{
                                    duplicateAttendanceWarningDialog(worker, editTextInput);
                                }

                            }
                            else{
                                projectRef.child("workerList").child(worker.getId()).child("attendance").child(ETdate).setValue(editTextInput);
                                refreshProjectDetails();
                                projectRef.keepSynced(true);

                            }
                        }else{
                            projectRef.child("workerList").child(worker.getId()).child("attendance").child(ETdate).setValue(editTextInput);
                            refreshProjectDetails();
                            projectRef.keepSynced(true);

                        }


                    }
                    else{
                        DatabaseReference workerRef = mainRef.child("Project_List").child(project.getId()).child("workerList").child(worker.getId()).child("attendance");
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
                projectName.setText(updatedProject.getName());
                projectId.setText(updatedProject.getId());
                projectCompany.setText(updatedProject.getCompany());
                projectLocation.setText(updatedProject.getLocation());
                projectDuration.setText(updatedProject.getPeriod());
                twConveyance.setText(String.valueOf(updatedProject.calculateConveyance()));
                twWages.setText(String.valueOf(updatedProject.calculateWage()));
                twCashExpenses.setText(String.valueOf(updatedProject.calculateCashExpense()));
                twChequePayments.setText(String.valueOf(updatedProject.calculateChequeExpense()));
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

    private void deleteWorkerDialog(WorkerProfile worker) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(15,15,15,15);
        LinearLayout editProjectLL = new LinearLayout(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Remove Worker")
                .setMessage("Are you sure you want to delete " + worker.getName() + " from " + project.getName()+  "? This action is irreversible, you may lose data permanently.")
                .setView(editProjectLL)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseReference workerRef = mainRef.child("Project_List").child(project.getId()).child("workerList").child(worker.getId());
                        workerRef.keepSynced(true);
                        workerRef.removeValue();
                        getPWorkerData("remove");
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

    private void openDeleteEntryDialog(String parentMethod, CashExpense cashExpense) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(15,15,15,15);
        LinearLayout editProjectLL = new LinearLayout(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Delete Entry")
                .setMessage("Are you sure you want to delete this entry? This action is irreversible, you may lose data permanently.")
                .setView(editProjectLL)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseReference cashExpenseRef = mainRef.child("Project_List").child(project.getId()).child("cashExpenseList").child(cashExpense.getId());
                        cashExpenseRef.keepSynced(true);
                        cashExpenseRef.removeValue();
                        getCashExpenseData("delete");
                        refreshProjectDetails();
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

    private void openDeleteChequeEntryDialog(String parentMethod, ChequeExpense chequeExpense) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(15,15,15,15);
        LinearLayout editProjectLL = new LinearLayout(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Delete Cheque Entry")
                .setMessage("Are you sure you want to delete this entry? This action is irreversible, you may lose data permanently.")
                .setView(editProjectLL)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseReference chequeExpenseRef = mainRef.child("Project_List").child(project.getId()).child("chequeExpenseList").child(chequeExpense.getId());
                        chequeExpenseRef.keepSynced(true);
                        chequeExpenseRef.removeValue();
                        getChequeExpenseData(parentMethod);
                        refreshProjectDetails();
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

    private int dP(int P){
        // Converting From int(pixels) To DP
        int dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, P, getResources().getDisplayMetrics());
        return dimensionInDp;
    }


}