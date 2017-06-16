package com.mihai.chessgame;

import android.content.Intent;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.io.IOException;


public class MainMenu extends AppCompatActivity implements SurfaceHolder.Callback {

    RearrangeableLayout root;
    Animation bounceAnim;
    private MediaPlayer mp = null;
    SurfaceView mSurfaceView=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Intent intent = getIntent();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        bounceAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);

        mp = new MediaPlayer();
        mSurfaceView = (SurfaceView) findViewById(R.id.surface);
        mSurfaceView.getHolder().addCallback(this);

        root = (RearrangeableLayout) findViewById(R.id.activity_main_menu);
        root.setChildPositionListener(new RearrangeableLayout.ChildPositionListener() {
            @Override
            public void onChildMoved(View childView, Rect oldPosition, Rect newPosition) {

            }
        });

        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

            }
        });
        root.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                //Log.d(TAG, "onPreDraw");
                //Log.d(TAG, root.toString());
                return true;
            }
        });

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {


        Uri video = Uri.parse("android.resource://" + getPackageName() + "/"
                + R.raw.videoplayback);


        try {
            mp.setDataSource(this, video);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Get the dimensions of the video
        int videoWidth = mp.getVideoWidth();
        int videoHeight = mp.getVideoHeight();

        //Get the width of the screen
        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        int height = getWindowManager().getDefaultDisplay().getHeight();

        //Get the SurfaceView layout parameters
        android.view.ViewGroup.LayoutParams lp = mSurfaceView.getLayoutParams();

        //Set the width of the SurfaceView to the width of the screen
        if(screenWidth < 1000) {
            lp.width = 1200;
        }
        else {
            lp.width = 1700;
        }

        //Set the height of the SurfaceView to match the aspect ratio of the video
        //be sure to cast these as floats otherwise the calculation will likely be 0
        //lp.height = (int) (((float)videoHeight / (float)videoWidth) * (float)screenWidth);
        if(screenWidth <1000){
            lp.height = 1233;
        }
        else{
            lp.height = 1800;
        }



        //Commit the layout parameters
        mSurfaceView.setLayoutParams(lp);

        //Start video
        mp.setDisplay(holder);
        mp.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mp.stop();
    }


    public void singlePlayer(View v) {

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
    public void rulesOfChess(View v){

        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        bounceAnim.setInterpolator(interpolator);
        v.startAnimation(bounceAnim);
        Intent intent = new Intent(MainMenu.this, RulesOfChess.class);
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

        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        bounceAnim.setInterpolator(interpolator);
        v.startAnimation(bounceAnim);
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

}
