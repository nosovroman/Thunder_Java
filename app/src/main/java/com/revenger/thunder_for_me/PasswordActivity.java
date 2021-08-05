package com.revenger.thunder_for_me;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class PasswordActivity extends AppCompatActivity {

    private Button OKbtn;
    private Button one;
    private Button two;
    private Button three;
    private Button four;
    private Button five;
    private Button six;
    private Button seven;
    private Button eight;
    private Button nine;

    public static final String PASS_SHARED_PREFS = "PASS_sharedPrefs";
    public static final String PASS_KEY = "PASS_key";
    private String passString;
    private String tempString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        OKbtn = findViewById(R.id.btnOK);
        one = findViewById(R.id.btnOne);
        two = findViewById(R.id.btnTwo);
        three = findViewById(R.id.btnThree);
        four = findViewById(R.id.btnFour);
        five = findViewById(R.id.btnFive);
        six = findViewById(R.id.btnSix);
        seven = findViewById(R.id.btnSeven);
        eight = findViewById(R.id.btnEight);
        nine = findViewById(R.id.btnNine);

        SharedPreferences sharedPreferences = getSharedPreferences(PASS_SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // выгрузка строки с паролем
        passString = sharedPreferences.getString(PASS_KEY, "");

        getButtonClick();

        editor.apply();
    }

    private void getButtonClick() {
        // кнопка проверки пароля
        OKbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tempString.equals(passString)) {
                    tempString = "";
                    Intent intent = new Intent(".FirstScene");
                    startActivity(intent);
                } else {
                    //Toast.makeText(PasswordActivity.this, "Неверный пароль", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(MainActivity.this, tempString+"|"+passString, Toast.LENGTH_SHORT).show();
                    tempString = "";
                }
            }
        });

        // номерные кнопки
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tempString += "1";
            }
        });
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tempString += "2";
            }
        });
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tempString += "3";
            }
        });
        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tempString += "4";
            }
        });
        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tempString += "5";
            }
        });
        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tempString += "6";
            }
        });
        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tempString += "7";
            }
        });
        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tempString += "8";
            }
        });
        nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tempString += "9";
            }
        });
    }
}