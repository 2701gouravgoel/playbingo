package com.example.playbingo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class LoginActivity extends AppCompatActivity {


    private EditText username;
    private EditText password;
    private Button submit;
    private Socket mSocket;
    private String name;
    private String pass;
    private TextView regis;
    private boolean backpressed=false;
    {
        try {
            mSocket = IO.socket("https://obscure-reaches-99859.herokuapp.com/");
        } catch (URISyntaxException e) {}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        InitializedFields();


        regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(i);
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                name  = username.getText().toString();
                pass = password.getText().toString();


                mSocket.on("loginsuccess",onLoginsuccss);
                mSocket.on("failed",onfailed);

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
                        mSocket.emit("logininfo", info);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }

    private Emitter.Listener onLoginsuccss = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            UserDatabase db = new UserDatabase(LoginActivity.this);
            db.addUser(name,pass);

        }
    };
    private Emitter.Listener onfailed = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.custum_toast, null);
                    TextView text = (TextView) layout.findViewById(R.id.meage);

                    text.setText("incorrect nickname or password");
                    Toast toast = new Toast(LoginActivity.this);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout);
                    toast.show();
                }
            });
        }
    };

    private void InitializedFields()
    {
        regis =(TextView)findViewById(R.id.new_one);
        username =(EditText)findViewById(R.id.login_username);
        password =(EditText)findViewById(R.id.login_pass);
        submit =(Button)findViewById(R.id.login_submit);
    }
}
