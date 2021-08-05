package com.revenger.thunder_for_me;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.felipecsl.gifimageview.library.GifImageView;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

import static com.revenger.thunder_for_me.PasswordActivity.PASS_SHARED_PREFS;

public class MainActivity extends AppCompatActivity {
    private GifImageView gifImageView;
    //private ProgressBar progressBar;

    int SPLASH_TIME = 2000;
    private String flagOfPass;

    public static final String FLAG = "keyFlag";
    String gifFile = "snow.gif";
    //snow

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences(PASS_SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        flagOfPass = sharedPreferences.getString(FLAG, "");
        editor.apply();

        final Intent i = new Intent(this, PasswordActivity.class);
        final Intent j = new Intent(this, FirstScene.class);

        gifImageView = findViewById(R.id.gifImageView);
        /*progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(progressBar.VISIBLE);*/

        try {
            InputStream inputStream = getAssets().open(gifFile);
            byte[] bytes = IOUtils.toByteArray(inputStream);
            gifImageView.setBytes(bytes);
            gifImageView.startAnimation();
        } catch (IOException e) {
            // e.printStackTrace();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (flagOfPass.equals("1")) {
                    MainActivity.this.startActivity(i);
                } else {
                    MainActivity.this.startActivity(j);
                }
                //MainActivity.this.startActivity(new Intent(SplashScreen.this, MainActivity.class));
                MainActivity.this.finish();
            }
        }, SPLASH_TIME); // mseconds

        /*if (flagOfPass.equals("1")) {
            startActivity(i);
        } else {
            startActivity(j);
        }*/
    }
}