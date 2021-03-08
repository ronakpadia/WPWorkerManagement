package com.theinternetcompany.wpworkermanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class    MainActivity extends AppCompatActivity {

    TableLayout projectTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        projectTable = findViewById(R.id.projectTable);
        TableRow row = new TableRow(this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(lp);
        TextView text = new TextView(this);
        text.setText("CHALU HAI");
        row.addView(text);
        row.addView(text);
        row.addView(text);
        row.addView(text);
        row.addView(text);
        projectTable.addView(row);
        projectTable.addView(row);
//        transitionToNewWorkerActivity();
    }

    private void transitionToNewWorkerActivity() {

        Intent intent = new Intent(MainActivity.this, EditWorkerActivity.class);
        startActivity(intent);
    }
}