package com.techbytecare.kk.onlinequiz;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rey.material.widget.CheckBox;
import com.techbytecare.kk.onlinequiz.BroadcastReciever.AlarmReceiver;
import com.techbytecare.kk.onlinequiz.Common.Common;
import com.techbytecare.kk.onlinequiz.Model.User;

import java.util.Calendar;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    MaterialEditText edtNewUser,edtNewPassword,edtNewEmail;
    MaterialEditText edtUser,edtPassword;

    CheckBox ckbRemember;

    Button btnSignUp,btnSignIn;

    FirebaseDatabase database;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerAlarm();

        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");

        edtUser = (MaterialEditText)findViewById(R.id.edtUser);
        edtPassword = (MaterialEditText)findViewById(R.id.edtPassword);

        btnSignUp = (Button) findViewById(R.id.btn_sign_up);
        btnSignIn = (Button)findViewById(R.id.btn_sign_in);

        ckbRemember = (CheckBox) findViewById(R.id.ckbRemember);

        //init paper
        Paper.init(this);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSignUpDialog();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn(edtUser.getText().toString(),edtPassword.getText().toString());
                //save user and password
                if (ckbRemember.isChecked())    {
                    Paper.book().write(Common.USER_KEY,edtUser.getText().toString());
                    Paper.book().write(Common.PWD_KEY,edtPassword.getText().toString());
                }
            }
        });

        //check remember
        String user = Paper.book().read(Common.USER_KEY);
        String pwd = Paper.book().read(Common.PWD_KEY);

        if (edtUser != null && pwd != null)    {
            if (!user.isEmpty() && !pwd.isEmpty())
                login(user,pwd);
        }
    }

    private void login(final String user, final String pwd) {

        final ProgressDialog mDialog = new ProgressDialog(MainActivity.this);
        mDialog.setTitle("USER LOG-IN");
        mDialog.setMessage("Please wait! while we check your credential!!");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();

        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(user).exists())  {

                    if (!user.isEmpty())    {

                        User logIn = dataSnapshot.child(user).getValue(User.class);

                        if (logIn.getPassword().equals(pwd))    {

                            mDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Login Successful!!!", Toast.LENGTH_SHORT).show();
                            Intent homeActivity = new Intent(MainActivity.this,Home.class);
                            Common.currentUser = logIn;
                            startActivity(homeActivity);
                            finish();
                        }
                        else    {
                            mDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Wrong Password!!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else    {
                        mDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Please Enter Your User Name", Toast.LENGTH_SHORT).show();
                    }
                }
                else    {
                    mDialog.dismiss();
                    Toast.makeText(MainActivity.this, "User Doesn't Exist in Database!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void registerAlarm() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY,13);

        Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager)this.getSystemService(this.ALARM_SERVICE);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }


    private void signIn(final String user, final String pwd) {

        final ProgressDialog mDialog = new ProgressDialog(MainActivity.this);
        mDialog.setTitle("USER LOG-IN");
        mDialog.setMessage("Please wait! while we check your credential!!");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();

        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(user).exists())  {

                    if (!user.isEmpty())    {

                        User logIn = dataSnapshot.child(user).getValue(User.class);

                        if (logIn.getPassword().equals(pwd))    {
                            mDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Login Successful!!!", Toast.LENGTH_SHORT).show();
                            Intent homeActivity = new Intent(MainActivity.this,Home.class);
                            Common.currentUser = logIn;
                            startActivity(homeActivity);
                            finish();
                        }
                        else    {
                            mDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Wrong Password!!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else    {
                        mDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Please Enter Your User Name", Toast.LENGTH_SHORT).show();
                    }
                }
                else    {
                    mDialog.dismiss();
                    Toast.makeText(MainActivity.this, "User Doesn't Exist in Database!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showSignUpDialog() {

        final ProgressDialog mDialog = new ProgressDialog(MainActivity.this);
        mDialog.setTitle("USER SIGN-UP");
        mDialog.setMessage("Please wait! while we Register Your Account!!");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Sign Up");
        alertDialog.setMessage("Please Fill full Information");

        LayoutInflater inflater = this.getLayoutInflater();
        View sign_up_layout = inflater.inflate(R.layout.sign_up_layout,null);

        edtNewEmail = (MaterialEditText)sign_up_layout.findViewById(R.id.edtNewEmail);
        edtNewUser = (MaterialEditText)sign_up_layout.findViewById(R.id.edtNewUserName);
        edtNewPassword = (MaterialEditText)sign_up_layout.findViewById(R.id.edtNewPassword);

        alertDialog.setView(sign_up_layout);
        alertDialog.setIcon(R.drawable.ic_account_circle_black_24dp);

        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                mDialog.dismiss();
                dialogInterface.dismiss();
            }
        });

        alertDialog.setPositiveButton("REGISTER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                final User user = new User(edtNewUser.getText().toString(),
                        edtNewPassword.getText().toString(),
                        edtNewEmail.getText().toString());

                users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(user.getUserName()).exists())    {
                            mDialog.dismiss();
                            Toast.makeText(MainActivity.this, "User Already Registered!!", Toast.LENGTH_SHORT).show();
                        }
                        else    {
                            mDialog.dismiss();
                            users.child(user.getUserName()).setValue(user);

                            Toast.makeText(MainActivity.this, "User Registration Successful", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }
}
