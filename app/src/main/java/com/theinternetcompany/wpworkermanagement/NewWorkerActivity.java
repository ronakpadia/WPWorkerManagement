package com.theinternetcompany.wpworkermanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.theinternetcompany.wpworkermanagement.Models.WorkerProfile;

public class NewWorkerActivity extends AppCompatActivity {

    private EditText name, cardNo, workType, rate;
    private Button btnCreateProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_worker);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

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
                //transitionToShopPage();
            }
        });

    }

    private void saveWorkerProfileData()
    {
        final String workerID = FirebaseDatabase.getInstance().getReference().child("Worker_List").push().getKey();
        WorkerProfile newWorker = new WorkerProfile(workerID, name.getText().toString(), cardNo.getText().toString(), rate.getText().toString(), workType.getText().toString());
        FirebaseDatabase.getInstance().getReference().child("Worker_List").child(workerID).setValue(newWorker);
    }
}