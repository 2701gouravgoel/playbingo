package com.example.playbingo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.github.nkzawa.socketio.client.IO;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class RegisterActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button Submit;
    private Socket mSocket;
    private String name;
    private String pass;
    private TextView login;
    private static Toast currentToast;
    private boolean backpressed=false;



    {
        try {
            mSocket = IO.socket("https://obscure-reaches-99859.herokuapp.com/");
        } catch (URISyntaxException e) {}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Initializedfields();


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });

        mSocket.on("userSuccess",onSuccess);
        mSocket.on("failed",onDuplicate);

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name  = username.getText().toString();
                pass = password.getText().toString();

                if(TextUtils.isEmpty(name))
                {
                    String message = "Enter NickName";

                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.custum_toast, null);
                    TextView text = (TextView) layout.findViewById(R.id.meage);

                    text.setText(message);
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout);
                    toast.show();
                }
                else if(TextUtils.isEmpty(pass))
                {

                    String message = "Enter Password";

                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.custum_toast, null);
                    TextView text = (TextView) layout.findViewById(R.id.meage);

                    text.setText(message);
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout);
                    toast.show();
                }
                else {
                    JSONObject info = new JSONObject();
                    try {
                        mSocket.connect();
                        info.put("username", name);
                        info.put("password", pass);
                        mSocket.emit("regisinfo", info);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });


    }


    private Emitter.Listener onSuccess = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            UserDatabase db = new UserDatabase(RegisterActivity.this);
            db.addUser(name,pass);

            Intent i = new Intent(RegisterActivity.this,MainActivity.class);
            startActivity(i);

        }
    };
    private Emitter.Listener onDuplicate = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int length = args.length;
                    if(length == 0){
                        return;
                    }
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.custum_toast, null);
                    TextView text = (TextView) layout.findViewById(R.id.meage);

                    text.setText(args[0].toString());
                    Toast toast = new Toast(RegisterActivity.this);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout);
                    toast.show();
                }
            });
            int length = args.length;
            if(length == 0)
            {

                return;
            }
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();


        moveTaskToBack(true);

        if(backpressed)
        {
            Intent i = new Intent(Intent.ACTION_MAIN);
            startActivity(i);
        }
        Toast.makeText(RegisterActivity.this,"press again",Toast.LENGTH_SHORT).show();
        backpressed=true;
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                backpressed = false;
            }
        }, 3000);

    }

    private void Initializedfields()
    {
        login=(TextView)findViewById(R.id.already_have);
        username =(EditText) findViewById(R.id.regis_username);
        password =(EditText) findViewById(R.id.regis_pass);
        Submit =(Button) findViewById(R.id.regis_submit);
    }


}
