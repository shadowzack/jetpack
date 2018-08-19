package com.mahmod.battl.jetpack;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;

import java.net.Inet4Address;
import java.util.regex.Pattern;

import io.fabric.sdk.android.Fabric;


public class AboutActivity extends AppCompatActivity {
    protected TextView About;
    private TextView noteView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_about);
        About = findViewById(R.id.About);
        About.setText(" Jet Pack :Space Marine\n" +
                "Version:1.00\n" +
                "developer: mahmod hasan\n" +
                "Developed in the Introduction to Mobile Computing Course, 2018.\n Instructors: Dr. Amnon Dekel and Amir Uval.\n" +
                "© 2018 - The Dept. of Software Engineering, Shenkar: Engineering. Design. Art." +
                "\n" +
                "the app is simple press and KEEP holding to go up\n" +
                "Release to go down \n" +
                "score a better score than last time\n" +
                "after passing your score rockets will get faster the more you continue\n" +
                "HAVE FUN !!!\n");
        TextView noteView = (TextView) findViewById(R.id.privacy_policy);
        Pattern wikiWordMatcher = Pattern.compile("\\b[A-Z]+[a-z0-9]+[A-Z][A-Za-z0-9]+\\b");
        String wikiViewURL = "content:https://game-arts-privacy-policy.herokuapp.com/";
        Linkify.addLinks(noteView, wikiWordMatcher, wikiViewURL);

    }

    public void goBack(View v) {

        finish();
        //startActivity(new Intent(AboutActivity.this,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK ));
        //finish();
    }

    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();
        finish();
        return true;
    }
    public void openPrivacyPolicy(View v) {

        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://game-arts-privacy-policy.herokuapp.com/"));
        startActivity(i);
    }
    public void openjetpackWebsite(View v) {

        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://jet-pack-space-marine.herokuapp.com/"));
        startActivity(i);
    }


}


