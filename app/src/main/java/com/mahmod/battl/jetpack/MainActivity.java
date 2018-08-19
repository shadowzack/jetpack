package com.mahmod.battl.jetpack;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.LeaderboardsClient;

import java.io.InputStream;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    protected static MyMusicRunnable musicPlayer;
    protected static MySFxRunnable effects;
    protected static boolean playSFX = true;
    private static final int RC_SIGN_IN = 9001;
    private static final int RC_UNUSED = 5001;
    private static final int RC_LEADERBOARD_UI = 9004;
    private static final String TAG = "SignIn";
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions mGoogleSignInOptions;

    private TextView mStatusTextView;

    private LeaderboardsClient mLeaderboardsClient;
    private InterstitialAd mInterstitialAd;
    private AdView mAdView;


    @Override
    public void onResume(){
        super.onResume();
     /*   if (mInterstitialAd.isLoaded()) {
        mInterstitialAd.show();
    }*/}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        MobileAds.initialize(this, "ca-app-pub-7240450863585767~6256633776");


        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        setContentView(R.layout.activity_main);




///////////////////////////////////////////////////////////////////////////////////////////////



        //##################new########################//

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
               // .requestScopes(Games.SCOPE_GAMES)
                .requestEmail()
                .build();





       /* GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();*/

     /*  GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
                .requestEmail()
                .build();*/
//        GoogleSignInOptions gso = new GoogleSignInOptions
//                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                //.requestScopes(Games.SCOPE_GAMES_LITE)
//                .requestEmail()
//                .build();

        mStatusTextView = findViewById(R.id.status);
        findViewById(R.id.back_btn).setOnClickListener(this);
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.button_sign_out).setOnClickListener(this);
        findViewById(R.id.disconnect_button).setOnClickListener(this);
        findViewById(R.id.login_view_btn).setOnClickListener(this);
        findViewById(R.id.show_leaderboard).setOnClickListener(this);

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //##################new########################//
        //mGoogleSignInOptions =GoogleSignIn.get



        //Games.Players.getCurrentPlayerId(GoogleSignIn.getClient(this,gso));

        // Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.
           GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
         updateUI(account);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        // Set the dimensions of the sign-in button.
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setColorScheme(SignInButton.COLOR_LIGHT);


        SharedPreferencesManager prefManager = new SharedPreferencesManager(this);
        int best = prefManager.get(SharedPreferencesManager.PREF_BEST_SCORE);
       // Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this)).submitScore(getString(R.string.leaderboard_jetpackleaderboards), 1111);
        //showLeaderboard();
        ////////////////////////////////////////////////////////////////////////////




        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        SharedPreferences settings = getSharedPreferences(PreferenceManager.getDefaultSharedPreferencesName(this), MODE_PRIVATE);
        if (effects == null) effects = new MySFxRunnable(this);
        if (musicPlayer == null)
            musicPlayer = new MyMusicRunnable(this, settings.getInt(getString(R.string.volume_key), 100));
        playSFX = settings.getBoolean(getString(R.string.pref_sfx_buttons), true);
        effects.run();
        musicPlayer.run();







////////////////banner ads
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-7240450863585767/4413494091");
          // TODO: Add adView to your view hierarchy.
        mAdView = findViewById(R.id.adView);
       // AdRequest adRequest = new AdRequest.Builder().build();
        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                //.addTestDevice("8669BC68E4A6AA301F6DDD7658B4038C")
                .build();
        mAdView.loadAd(adRequest);


        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
            }
        });



        /*mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-7240450863585767/4708173416");
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                //.addTestDevice("8669BC68E4A6AA301F6DDD7658B4038C")
                .build();
        mInterstitialAd.loadAd(adRequest);

        // mInterstitialAd.show();
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                mInterstitialAd.show();
                Log.i("Ads", "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Log.i("Ads", "onAdFailedToLoad");
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
                Log.i("Ads", "onAdOpened");
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                Log.i("Ads", "onAdLeftApplication");
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the interstitial ad is closed.
                // Load the next interstitial.
                AdRequest adRequest = new AdRequest.Builder()
                        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                        //.addTestDevice("8669BC68E4A6AA301F6DDD7658B4038C")
                        .build();
                mInterstitialAd.loadAd(adRequest);
                //mInterstitialAd.loadAd(new AdRequest.Builder().build());
                Log.i("Ads", "onAdClosed");
            }
        });

*/




    }

    public void onAboutClicked(View v) {
        if (playSFX) {
            effects.play(R.raw.effect);
        }
        startActivity(new Intent(MainActivity.this, AboutActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public void onSettingsClicked(View v) {
        if (playSFX) effects.play(R.raw.effect);
        startActivity(new Intent(MainActivity.this, SettingsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public void onStartClicked(View v) {
        if (playSFX) effects.play(R.raw.effect);
        startActivity(new Intent(MainActivity.this, GameActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }



    private boolean isSignedIn() {
        return GoogleSignIn.getLastSignedInAccount(this) != null;
    }

    /////////login
    @Override
    public void onStart() {
        super.onStart();

        // [START on_start_sign_in]
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
        // [END on_start_sign_in]
    }

    @Override
    public boolean onSupportNavigateUp() {


        return true;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
              //  signIn();
                startSignInIntent();
                break;
            case R.id.button_sign_out:
                signOut();
                break;
            case R.id.disconnect_button:
                revokeAccess();
                break;
            case R.id.login_view_btn:
                /*if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }*/
                ShowLogin();
                break;
            case R.id.back_btn:
                HideLogin();
                break;
            case R.id.show_leaderboard:
                showLeaderboard();
               // startActivityForResult(Games.Leaderboards.getLeaderboardIntent(
                   //     getApiClient(), getString(R.string.number_guesses_leaderboard)),
                    //    2);
                break;
                // ...
        }
    }



    ////////////////////////

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    private void startSignInIntent() {
        GoogleSignInClient signInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
        Intent intent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(intent,RC_SIGN_IN);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // The signed in account is stored in the result.
                GoogleSignInAccount signedInAccount = result.getSignInAccount();
                //GoogleSignInAccount account = completedTask.getResult(ApiException.class);

                // Signed in successfully, show authenticated UI.
                updateUI(signedInAccount);
            } else {
                String message = result.getStatus().getStatusMessage();
                if (message == null || message.isEmpty()) {
                    message = getString(R.string.signin_other_error);
                    message+= "    " +result.getStatus();
                }
                new AlertDialog.Builder(this).setMessage(message)
                        .setNeutralButton(android.R.string.ok, null).show();
            }
        }
    }

    private void signOut() {
        GoogleSignInClient signInClient = GoogleSignIn.getClient(this,
                GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
        signInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // at this point, the user is signed out.
                        updateUI(null);
                    }
                });
    }

    ////////////////
    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            new AlertDialog.Builder(this).setMessage(result.getStatus().toString()).setNeutralButton(android.R.string.ok, null).show();

        }else {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            String message = result.getStatus().getStatusMessage();
            if (message == null || message.isEmpty()) {
                message = getString(R.string.signin_other_error);
                message+= "    " +result.getStatus();
            }
            new AlertDialog.Builder(this).setMessage(message)
                    .setNeutralButton(android.R.string.ok, null).show();
        }
    }*/
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }
  /*  private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        updateUI(null);

                    }
                });
    }*/
    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        updateUI(null);
                        // [END_EXCLUDE]
                    }
                });
    }
    private void updateUI(@Nullable GoogleSignInAccount account) {
        if (account != null) {
            mStatusTextView.setText( account.getDisplayName());

            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.disconnect_button).setVisibility(View.VISIBLE);
            findViewById(R.id.button_sign_out).setVisibility(View.VISIBLE);
            Button loginViewButton = (Button)findViewById(R.id.login_view_btn);
            loginViewButton.setText(account.getDisplayName());

            if(account.getPhotoUrl()!= null) {
                new DownloadImageTask((ImageView) findViewById(R.id.account_image))
                        .execute(account.getPhotoUrl().toString());
                // (Button)findViewById(R.id.login_view_btn).setTextAlignment();


                //ImageView tmp=(ImageView)findViewById(R.id.account_image_upper);
                new DownloadImageTask((ImageView) findViewById(R.id.account_image_upper))
                        .execute(account.getPhotoUrl().toString());
            }

        } else {
            mStatusTextView.setText("Guest user");

            ImageView tmp=(ImageView)findViewById(R.id.account_image);
            ImageView tmp2=(ImageView)findViewById(R.id.account_image_upper);
            tmp.setImageResource(R.drawable.account);
            tmp2.setImageResource(R.drawable.account2);
            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.button_sign_out).setVisibility(View.GONE);
            findViewById(R.id.disconnect_button).setVisibility(View.GONE);
        }
    }
    private void ShowLogin(){
        findViewById(R.id.login_view).setVisibility(View.VISIBLE);
    }

    private void HideLogin(){
        findViewById(R.id.login_view).setVisibility(View.GONE);
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }



   /* private void showLeaderboard() {
        Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .getLeaderboardIntent(getString(R.string.leaderboard_id))
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        startActivityForResult(intent, RC_LEADERBOARD_UI);
                    }
                });
    }*/
   // Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this).submitScore(getString(R.string.leaderboard_id), 1337);





    private void showLeaderboard() {
        System.out.println(GoogleSignIn.getLastSignedInAccount(this).getId() +"\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");

        Games.getLeaderboardsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .getLeaderboardIntent(getString(R.string.leaderboard_jetpackleaderboards))
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        startActivityForResult(intent, RC_LEADERBOARD_UI);
                    }
                });
    }


    /*@Override
    public void onShowLeaderboardsRequested() {
        mLeaderboardsClient.getAllLeaderboardsIntent()
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        startActivityForResult(intent, RC_UNUSED);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                       //handleException(e, getString(R.string.leaderboards_exception));
                    }
                });
    }
    @Override
    public void onShowLeaderboardsRequested() {
        mLeaderboardsClient.getAllLeaderboardsIntent()
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        startActivityForResult(intent, RC_UNUSED);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                        //handleException(e, getString(R.string.leaderboards_exception));
                    }
                });
    }*/
    /*private void handleException(Exception e, String details) {
        int status = 0;

        if (e instanceof ApiException) {
            ApiException apiException = (ApiException) e;
            status = apiException.getStatusCode();
        }

        String message = getString(R.string.status_exception_error, details, status, e);

        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setNeutralButton(android.R.string.ok, null)
                .show();
    }*/
}
