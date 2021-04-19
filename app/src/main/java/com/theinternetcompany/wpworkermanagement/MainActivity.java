package com.theinternetcompany.wpworkermanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theinternetcompany.wpworkermanagement.Models.Project;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TableLayout projectTable;
    private SearchView searchView;
    private Button btnHideShowColumns, btnExportCSV;
    private FloatingActionButton btnAddNewProject,btnEmployeeList;
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


        int actionBarTitleId = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
        if (actionBarTitleId > 0) {
            TextView title = (TextView) findViewById(actionBarTitleId);
            if (title != null) {
                title.setTextColor(Color.parseColor("#deb103"));
            }
        }


        searchView = findViewById(R.id.searchView);
        btnAddNewProject = findViewById(R.id.btnAddNewProject);
        btnEmployeeList = findViewById(R.id.btnEmployeeList);
        btnHideShowColumns = findViewById(R.id.btnHideShowColumns);
//        btnExportCSV = findViewById(R.id.btnExportProjectList);
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
                openNewProjectDialog();

            }
        });

//        btnExportCSV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                exportCSV();
//            }
//        });

        btnHideShowColumns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // setup the alert builder
                final List<Integer> selectedItems = new ArrayList<Integer>();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Choose Tags");
                // add a checkbox list
                String[] tags = {"Name", "Company", "Location","Period", "Expenses"};
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

//        nameTag.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                collapseColumn(0);
//            }
//        });
//
//        companyTag.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                collapseColumn(1);
//            }
//        });
//
//        locationTag.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                collapseColumn(2);
//            }
//        });
//
//        periodTag.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                collapseColumn(3);
//            }
//        });
//
//        expensesTag.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                collapseColumn(4);
//            }
//        });

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
                else{
                    doMySearch(s);
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

    private void exportCSV() {
        StringBuilder data = new StringBuilder();
        data.append("ID, Name, Location, Expenses, Company, Duration");
        DatabaseReference projectRef = mainRef.child("Project_List");
        projectRef.keepSynced(true);
        projectList.clear();

        projectRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap : snapshot.getChildren()){
                    projectList.add(snap.getValue(Project.class));
                }
                for(Project p:projectList){
                    String pName = p.getName();
                    pName = addQuotes(pName);
                    String pId = p.getId();
                    pId = addQuotes(pId);
                    String pLocation = p.getLocation();
                    pLocation = addQuotes(pLocation);
                    String pExpenses = p.getExpenses();
                    pExpenses = addQuotes(pExpenses);
                    String pDuration = p.getPeriod();
                    pDuration = addQuotes(pDuration);
                    String pCompany = p.getCompany();
                    pCompany = addQuotes(pCompany);
                    String c = ", ";
                    data.append("\n"+pId+c+pName+c+pLocation+c+pExpenses+c+pCompany+c+pDuration);

                }
                try {
                    // Saving the file into the phone
                    FileOutputStream out = openFileOutput("Report.csv", Context.MODE_PRIVATE);
                    out.write((data.toString()).getBytes());
                    out.close();

                    // exporting
                    Context context = getApplicationContext();
                    File filelocation = new File(getFilesDir(), "Report.csv");
                    Uri path = FileProvider.getUriForFile(context, "com.theinternetcompany.wpworkermanagement.fileprovider", filelocation);
                    Intent fileIntent = new Intent(Intent.ACTION_SEND);
                    fileIntent.setType("text/plain");
                    fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Data");
                    fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    fileIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//            fileIntent.setData(path);
                    fileIntent.putExtra(Intent.EXTRA_STREAM, path);
                    startActivity(Intent.createChooser(fileIntent, "Send mail"));
                } catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private String addQuotes(String data) {
        return '"'+data+'"';
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
            pname.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            TextView pcompany = new TextView(MainActivity.this);
            pcompany.setText(p.getCompany());
            pcompany.setPadding(20,20,20,20);
            pcompany.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            TextView plocation = new TextView(MainActivity.this);
            plocation.setText(p.getLocation());
            plocation.setPadding(20,20,20,20);
            plocation.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            TextView pperiod = new TextView(MainActivity.this);
            pperiod.setText(p.getPeriod());
            pperiod.setPadding(20,20,20,20);
            pperiod.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

            TextView pexpenses = new TextView(MainActivity.this);
            pexpenses.setText(String.valueOf(p.calculateTotal()));
            pexpenses.setPadding(20,20,20,20);
            pexpenses.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

            int index = projectTable.getChildCount();
            if(index % 2 != 0){
                pname.setBackgroundResource(R.drawable.table_divider);
                pcompany.setBackgroundResource(R.drawable.table_divider);
                plocation.setBackgroundResource(R.drawable.table_divider);
                pperiod.setBackgroundResource(R.drawable.table_divider);
                pexpenses.setBackgroundResource(R.drawable.table_divider);

            }else{
                pname.setBackgroundResource(R.drawable.grey_table_divider);
                pcompany.setBackgroundResource(R.drawable.grey_table_divider);
                plocation.setBackgroundResource(R.drawable.grey_table_divider);
                pperiod.setBackgroundResource(R.drawable.grey_table_divider);
                pexpenses.setBackgroundResource(R.drawable.grey_table_divider);

            }

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

    private void openNewProjectDialog() {

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
        editProjectLL.addView(nameTag);
        editProjectLL.addView(projectName);
        editProjectLL.addView(companyTag);
        editProjectLL.addView(projectCompany);
        editProjectLL.addView(locationTag);
        editProjectLL.addView(projectLocation);
        editProjectLL.addView(periodTag);
        editProjectLL.addView(projectPeriod);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("WP Management")
                .setMessage("New Project")
                .setView(editProjectLL)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {



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

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard();
                String pName = projectName.getText().toString();
                String pLocation= projectLocation.getText().toString();
                String pPeriod = projectPeriod.getText().toString();
                String pCompany = projectCompany.getText().toString();
                if (TextUtils.isEmpty(pName)) {
                    projectName.setError("This field is required!");
                    if (TextUtils.isEmpty(pLocation)) {
                        projectLocation.setError("This field is required!");
                        if (TextUtils.isEmpty(pPeriod)) {
                            projectPeriod.setError("This field is required!");
                        }
                        if (TextUtils.isEmpty(pCompany)) {
                            projectCompany.setError("This field is required!");
                        }
                    }
                }

                else if (TextUtils.isEmpty(pLocation)) {
                    projectLocation.setError("This field is required!");
                    if (TextUtils.isEmpty(pPeriod)) {
                        projectPeriod.setError("This field is required!");
                    }
                    if (TextUtils.isEmpty(pCompany)) {
                        projectCompany.setError("This field is required!");
                    }
                }

                else if (TextUtils.isEmpty(pPeriod)) {
                    projectPeriod.setError("This field is required!");
                    if (TextUtils.isEmpty(pCompany)) {
                        projectCompany.setError("This field is required!");
                    }
                }

                else if (TextUtils.isEmpty(pCompany)) {
                    projectCompany.setError("This field is required!");
                }
                else{

//                            HashMap<String,WorkerProfile> WorkerList = new HashMap<String,WorkerProfile>();
//                            Log.d("debug", String.valueOf(pWorkerList.size()));
//                            for (WorkerProfile worker: pWorkerList){
//                                WorkerList.put(worker.getId(), worker);
//                            }

                    String projectID = mainRef.child("Project_List").push().getKey();
                    Project newProject = new Project(projectID, pName, pCompany, pLocation, pPeriod, "0");
                    mainRef.child("Project_List").child(projectID).setValue(newProject);
                    dialog.dismiss();
                }

            }
        });
        projectName.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        imm.showSoftInput(projectName, InputMethodManager.SHOW_IMPLICIT);
        showKeyboard();
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

    public void closeKeyboard(){
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    public void showKeyboard() {
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
    }


}