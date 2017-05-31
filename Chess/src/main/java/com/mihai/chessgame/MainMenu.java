package com.mihai.chessgame;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainMenu extends AppCompatActivity {


    private Context current;
    public boolean isMultiplayer;
    Toolbar myToolbar;
    TextToSpeech toSpeech;
    public String message;
    Animation animTranslate, animRotate, animScale, animAlpha, bounceAnim;
    TextView mTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Intent intent = getIntent();
        //overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        bounceAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        mTitle = (TextView) myToolbar.findViewById(R.id.toolbar_title);
        myToolbar.inflateMenu(R.menu.buttons);
        myToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()){

                    case R.id.rules_of_chess:

                        Intent intent = new Intent(MainMenu.this, RulesOfChess.class);
                        intent.setAction(Intent.ACTION_SEND);
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        }
                        break;

                    case R.id.rate_app:
                        Intent intentM = new Intent(MainMenu.this, RateApp.class);
                        intentM.setAction(Intent.ACTION_SEND);
                        if (intentM.resolveActivity(getPackageManager()) != null) {
                            startActivity(intentM);
                        }

                        break;
                    case R.id.about:


                        Intent intentA = new Intent(MainMenu.this, AboutActivity.class);
                        intentA.setAction(Intent.ACTION_SEND);
                        if (intentA.resolveActivity(getPackageManager()) != null) {
                            startActivity(intentA);
                        }
                        break;
                }

                return true;
            }
        });

    }


    public void singlePlayer(View v) {


      /*  final ProgressBar pBar=(ProgressBar) findViewById(R.id.progressBar);

                pBar.setVisibility(View.VISIBLE);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent myIntent = new Intent(MainMenu.this, MainActivity.class);
                        startActivityForResult(myIntent, 0);
                    }
                }, 5000);
            }

*/
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        bounceAnim.setInterpolator(interpolator);
        v.startAnimation(bounceAnim);
        Intent intent = new Intent(MainMenu.this, MainActivity.class);
        intent.putExtra("isMultiplayer", false);
        intent.setAction(Intent.ACTION_SEND);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }



    public void twoPlayer(View v){

        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        bounceAnim.setInterpolator(interpolator);
        v.startAnimation(bounceAnim);
        Intent intent = new Intent(MainMenu.this, Multiplayer.class);
        intent.setAction(Intent.ACTION_SEND);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void startMultiplayer(View v){
        Intent intent = new Intent(MainMenu.this, MainActivity.class);
        intent.putExtra("isMultiplayer", true);
        intent.setAction(Intent.ACTION_SEND);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }



    class MyBounceInterpolator implements android.view.animation.Interpolator {
        double mAmplitude = 1;
        double mFrequency = 10;

        MyBounceInterpolator(double amplitude, double frequency) {
            mAmplitude = amplitude;
            mFrequency = frequency;
        }

        public float getInterpolation(float time) {
            return (float) (-1 * Math.pow(Math.E, -time/ mAmplitude) *
                    Math.cos(mFrequency * time) + 1);
        }
    }




    //private String url="http://www.google.co.in";



   /* @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        new AsyncCaller().execute();

    }

    private class AsyncCaller extends AsyncTask<Void, Void, Void>
    {
        ProgressDialog pdLoading = new ProgressDialog(MainMenu.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.show();
        }
        @Override
        protected Void doInBackground(Void... params) {

            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            //this method will be running on UI thread

            pdLoading.dismiss();
        }

    }*/


}
