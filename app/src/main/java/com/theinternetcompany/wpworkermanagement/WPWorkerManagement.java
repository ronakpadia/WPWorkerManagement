package com.theinternetcompany.wpworkermanagement;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class WPWorkerManagement extends Application {

    @Override
    public void onCreate(){
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

}
