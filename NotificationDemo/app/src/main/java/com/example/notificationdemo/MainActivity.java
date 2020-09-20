package com.example.notificationdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    Button button,button2;
    EditText editText1, editText2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button =  findViewById(R.id.button);
        button2 = findViewById(R.id.button2);
        editText1 = findViewById(R.id.editTextNumberDecimal);
        editText2 = findViewById(R.id.editText);
        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Data data = new Data.Builder().putString("content",editText2.getText().toString()).build();
                final PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(UploadWorker.class,Long.parseLong(editText1.getText().toString()) ,TimeUnit.MINUTES)
                        .setConstraints(constraints)
                        .setInputData(data)
                        .build();
                WorkManager.getInstance(MainActivity.this).getWorkInfoByIdLiveData(request.getId()).observe(MainActivity.this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        String name = workInfo.getState().name();
                        Toast.makeText(MainActivity.this,name,Toast.LENGTH_LONG).show();
                    }
                });
                WorkManager.getInstance(MainActivity.this).enqueue(request);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Data data = new Data.Builder().putString("content",editText2.getText().toString()).build();
                final OneTimeWorkRequest request2 = new OneTimeWorkRequest.Builder(UploadWorker.class)
                        .setInitialDelay(Duration.ofMinutes(Long.parseLong(editText1.getText().toString())))
                        .setConstraints(constraints)
                        .setInputData(data)
                        .build();
                WorkManager.getInstance(MainActivity.this).getWorkInfoByIdLiveData(request2.getId()).observe(MainActivity.this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        String name = workInfo.getState().name();
                        Toast.makeText(MainActivity.this,name,Toast.LENGTH_LONG).show();
                    }
                });
                WorkManager.getInstance(MainActivity.this).enqueue(request2);
            }
        });
    }


}