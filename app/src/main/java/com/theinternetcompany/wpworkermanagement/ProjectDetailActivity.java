package com.theinternetcompany.wpworkermanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.theinternetcompany.wpworkermanagement.Models.Project;

public class ProjectDetailActivity extends AppCompatActivity {

    private Project project = new Project();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);

        Intent i = getIntent();
        project = (Project) i.getSerializableExtra("project");
    }
}