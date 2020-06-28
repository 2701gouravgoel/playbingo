package com.example.playbingo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UserDatabase db = new UserDatabase(MainActivity.this);
        username = db.getcurrentuser();

        if(TextUtils.isEmpty(username))
        {
            Intent i = new Intent(MainActivity.this,RegisterActivity.class);
            startActivity(i);
        }
        else
        {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.custum_toast, null);
            TextView text = (TextView) layout.findViewById(R.id.meage);

            text.setText("Welcome "+username);
            Toast toast = new Toast(MainActivity.this);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
        }

    }
}
