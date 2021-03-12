package com.theinternetcompany.wpworkermanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.theinternetcompany.wpworkermanagement.Models.WorkerProfile;

public class NewWorkerActivity extends AppCompatActivity {

    private EditText name, cardNo, workType, rate;
    private Button btnCreateProfile;
    private long back_pressed;
    private DatabaseReference mainRef = FirebaseDatabase.getInstance().getReference();

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_worker);

        mainRef.keepSynced(true);
        name = findViewById(R.id.newName);
        cardNo = findViewById(R.id.newCardNo);
        workType = findViewById(R.id.newWorkType);
        rate = findViewById(R.id.newRate);
        btnCreateProfile = findViewById(R.id.btnCreateProfile);


        btnCreateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(name.getText().toString())) {
                    Toast.makeText(NewWorkerActivity.this,"Enter Worker Name", Toast.LENGTH_SHORT).show();
                }

                else if (TextUtils.isEmpty(cardNo.getText().toString())) {
                    Toast.makeText(NewWorkerActivity.this,"Enter Card Number", Toast.LENGTH_SHORT).show();
                }

                else if (TextUtils.isEmpty(workType.getText().toString())) {
                    Toast.makeText(NewWorkerActivity.this,"Enter Work Type", Toast.LENGTH_SHORT).show();
                }

                else if (TextUtils.isEmpty(rate.getText().toString())) {
                    Toast.makeText(NewWorkerActivity.this,"Enter Rate", Toast.LENGTH_SHORT).show();
                }

                else {
                    saveWorkerProfileData();
                }
                transitionToWorkerListActivity();
            }

        });

    }

    private void transitionToWorkerListActivity() {
        Intent intent = new Intent(NewWorkerActivity.this, WorkerListActivity.class);
        startActivity(intent);
    }

    private void saveWorkerProfileData()
    {
        final String workerID = mainRef.child("Worker_List").push().getKey();
        WorkerProfile newWorker = new WorkerProfile(workerID, name.getText().toString(), cardNo.getText().toString(), rate.getText().toString(), rate.getText().toString(), workType.getText().toString());
        mainRef.child("Worker_List").child(workerID).setValue(newWorker);
//        final String workerID = FirebaseDatabase.getInstance().getReference().child("Worker_List").push().getKey();
//        WorkerProfile newWorker = new WorkerProfile(workerID, name.getText().toString(), cardNo.getText().toString(), rate.getText().toString(), workType.getText().toString());
//        FirebaseDatabase.getInstance().getReference().child("Worker_List").child(workerID).setValue(newWorker);
//        DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference("scores");
//        scoresRef.keepSynced(true);
    }
}