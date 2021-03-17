package com.theinternetcompany.wpworkermanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.theinternetcompany.wpworkermanagement.Models.WorkerProfile;

import java.util.ArrayList;

public class WokerDetailActivity extends AppCompatActivity {

    private WorkerProfile worker = new WorkerProfile();
    TableLayout projectTable;
    private ArrayList<WorkerProfile> projectList = new ArrayList<>();
    private DatabaseReference mainRef = FirebaseDatabase.getInstance().getReference();
    private TextView workerName, workerCardNo, workType, baseRate, totalProjects, totalEarnings, pendingExpenses;

    @Override
    public void onBackPressed() {
    }

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

        
    }
}