package org.cyrilleguipie.rolldice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;


public class SplashActivity extends Activity {

    private View mImageView;

    private Thread mSplashThread;
    
    @Override
    public void onStart() {
      super.onStart();

    }

    @Override
    public void onStop() {
      super.onStop();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_splash);

        View splashLayout = findViewById(R.id.theSplashLayout);
        View root = splashLayout.getRootView();
        mImageView = root.findViewById(R.id.splashImg);
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_alpha);
        mImageView.startAnimation(hyperspaceJumpAnimation);

        mSplashThread = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        this.wait(3000);
                    }
                } catch (InterruptedException ex) {
                    // Ne rien faire
                }

                finish();
                startActivity(new Intent(SplashActivity.this, RollActivity.class));

            }
        };

        mSplashThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent evt) {
        if (evt.getAction() == MotionEvent.ACTION_DOWN) {
            synchronized (mSplashThread) {
                mSplashThread.notifyAll();
            }
        }
        return true;
    }


}
