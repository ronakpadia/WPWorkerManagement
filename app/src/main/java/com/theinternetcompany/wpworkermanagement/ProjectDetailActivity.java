package com.theinternetcompany.wpworkermanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theinternetcompany.wpworkermanagement.Models.Project;
import com.theinternetcompany.wpworkermanagement.Models.WorkerProfile;

import java.util.ArrayList;

public class ProjectDetailActivity extends AppCompatActivity {

    private Project project = new Project();
    private ArrayList<WorkerProfile> workerList = new ArrayList<>();
    private DatabaseReference mainRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);

        Intent i = getIntent();
        project = (Project) i.getSerializableExtra("project");
        getWorkerData();

    }

    private void getWorkerData() {
        DatabaseReference workerRef = mainRef.child("Worker_List");
        workerRef.keepSynced(true);


        workerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snap : snapshot.getChildren()){
                    workerList.add(snap.getValue(WorkerProfile.class));

                }

                setPedictions();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setPedictions() {
        ArrayAdapter<WorkerProfile> adapter = new ArrayAdapter<WorkerProfile>(this,
                android.R.layout.select_dialog_item,
                workerList);
        //Getting the instance of AutoCompleteTextView
        AutoCompleteTextView actv =  (AutoCompleteTextView)findViewById(R.id.addWorkerAutoComplete);
        actv.setThreshold(1);//will start working from first character
        actv.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        actv.setTextColor(Color.RED);
    }
}