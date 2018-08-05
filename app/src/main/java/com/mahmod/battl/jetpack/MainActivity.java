package com.mahmod.battl.jetpack;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    protected static MyMusicRunnable musicPlayer;
    protected static MySFxRunnable effects;
    protected static boolean playSFX = true;
    //protected ImageView img;

//    @Override
//    public void onResume(){
//        super.onResume();
//        effects.run();
//        musicPlayer.run();
//            effects.play(R.raw.menu);
//
//
//    }
//    @Override
//    public void onStart(){
//        super.onStart();
//        effects.run();
//        musicPlayer.run();
//        effects.play(R.raw.menu);
//
//
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        View decor_View = getWindow().getDecorView();
        int ui_Options = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        decor_View.setSystemUiVisibility(ui_Options);
      //  requestWindowFeature(Window.FEATURE_NO_TITLE |Window.FEATURE_ACTION_BAR);

        setContentView(R.layout.activity_main);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        SharedPreferences settings = getSharedPreferences(PreferenceManager.getDefaultSharedPreferencesName(this), MODE_PRIVATE);
        if (effects == null ) effects = new MySFxRunnable(this);
        if (musicPlayer == null) musicPlayer = new MyMusicRunnable(this,settings.getInt(getString(R.string.volume_key),100));
        playSFX = settings.getBoolean(getString(R.string.pref_sfx_buttons),true);
        effects.run();
        musicPlayer.run();
    }

    public void onAboutClicked(View v) {
        if (playSFX) {
            effects.play(R.raw.effect);
        }
        startActivity(new Intent(MainActivity.this,AboutActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }
    public void onSettingsClicked(View v) {
        if (playSFX) effects.play(R.raw.effect);
        startActivity(new Intent(MainActivity.this,SettingsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }
    public void onStartClicked(View v) {
        if (playSFX) effects.play(R.raw.effect);
        startActivity(new Intent(MainActivity.this,GameActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }



}
