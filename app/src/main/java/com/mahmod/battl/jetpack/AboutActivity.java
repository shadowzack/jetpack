package com.mahmod.battl.jetpack;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;



public class AboutActivity extends AppCompatActivity {
    protected TextView About;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decor_View = getWindow().getDecorView();
        int ui_Options = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        decor_View.setSystemUiVisibility(ui_Options);


        setContentView(R.layout.activity_about);
        About = findViewById(R.id.About);
        About.setText(" Jet Pack :Space Marine\n" +
                "Version:1.00\n" +
                "developer mahmod hasan\n" +
                "Developed in the Introduction to Mobile Computing Course, 2018. Instructors: Dr. Amnon Dekel and Amir Uval.\n" +
                "© 2018 - The Dept. of Software Engineering, Shenkar: Engineering. Design. Art." +
                "\n" +
                "the app is simple press and KEEP holding to go up\n" +
                "Release to go down \n" +
                "score a better score than last time\n" +
                "HAVE FUN !!!\n");


    }
    public void goBack(View v) {


        startActivity(new Intent(AboutActivity.this,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK ));
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();
        finish();
        return true;
    }
}
