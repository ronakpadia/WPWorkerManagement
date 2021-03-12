package com.theinternetcompany.wpworkermanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.theinternetcompany.wpworkermanagement.Models.WorkerProfile;

public class EditWorkerActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
    }

    private EditText name, cardNo, workType, rate;
    private Button btnSave;
    private DatabaseReference mainRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_worker);

        name = findViewById(R.id.edtName);
        cardNo = findViewById(R.id.edtCardNo);
        workType = findViewById(R.id.edtWorkType);
        rate = findViewById(R.id.edtRate);
        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(name.getText().toString())) {
                    Toast.makeText(EditWorkerActivity.this,"Enter Worker Name", Toast.LENGTH_SHORT).show();
                }

                else if (TextUtils.isEmpty(cardNo.getText().toString())) {
                    Toast.makeText(EditWorkerActivity.this,"Enter Card Number", Toast.LENGTH_SHORT).show();
                }

                else if (TextUtils.isEmpty(workType.getText().toString())) {
                    Toast.makeText(EditWorkerActivity.this,"Enter Work Type", Toast.LENGTH_SHORT).show();
                }

                else if (TextUtils.isEmpty(rate.getText().toString())) {
                    Toast.makeText(EditWorkerActivity.this,"Enter Rate", Toast.LENGTH_SHORT).show();
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

        WorkerProfile newWorker = new WorkerProfile("-MVGdE48egxQm3MXHiFB", name.getText().toString(), cardNo.getText().toString(), workType.getText().toString(), rate.getText().toString() , rate.getText().toString() );
        mainRef.child("Worker_List").child("-MVGdE48egxQm3MXHiFB").setValue(newWorker);
//        final String workerID = FirebaseDatabase.getInstance().getReference().child("Worker_List").push().getKey();
//        WorkerProfile newWorker = new WorkerProfile(workerID, name.getText().toString(), cardNo.getText().toString(), rate.getText().toString(), workType.getText().toString());
//        FirebaseDatabase.getInstance().getReference().child("Worker_List").child(workerID).setValue(newWorker);
//        DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference("scores");
//        scoresRef.keepSynced(true);
    }
}