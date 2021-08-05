package com.revenger.thunder_for_me;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.revenger.thunder_for_me.MainActivity.FLAG;
import static com.revenger.thunder_for_me.PasswordActivity.PASS_KEY;
import static com.revenger.thunder_for_me.PasswordActivity.PASS_SHARED_PREFS;

public class FirstScene extends AppCompatActivity {
    private Toolbar toolbar;
    private ActionBar actionBar;
    private AutoCompleteTextView etTitle;
    private EditText etText;
    private ImageButton ibtnSearch;
    private ImageButton ibtnEdit;
    private Button btnSave;
    private Button btnDelete;
    private Button btnSecret;

    private String oldPassText;
    private String newPassText;

    private CheckBox checkBox;
    private CheckBox checkBox2;
    private EditText oldPass;
    private EditText newPass;

    final String symbol = "\n";
    final int needSecretClick = 13;
    private int secretClickCounter = 0;
    //String strTitles;
    public static boolean titleExist = false;

    List<String> titleList;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String KEY_TITLE = "keyTitle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_scene);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        etTitle = findViewById(R.id.title);
        etText = findViewById(R.id.text);

        ibtnSearch = findViewById(R.id.ibtnSearch);
        ibtnEdit = findViewById(R.id.ibtnEdit);
        btnSave = findViewById(R.id.btnSaveRecord);
        btnDelete = findViewById(R.id.btnDelRecord);
        btnSecret = findViewById(R.id.secretButton);

        titleList = new ArrayList<>();

        // автодополнение текста
        autoCompletion();

        // переход в секретный режим
        btnSecret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                secretClickCounter++;
                if (secretClickCounter == needSecretClick) {
                    Intent intent = new Intent(".SecretScene");
                    startActivity(intent);
                }
            }
        });

        // сохранение заметки
        btnSave.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                saveRecord();
            }
        });

        // редактирование темы заметки
        ibtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editThemeName();
            }
        });

        // поиск заметки
        ibtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchRecord();
            }
        });

        // удаление заметки
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delRecord();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void saveRecord() {
        final String tmpTitle = etTitle.getText().toString();
        final String tmpText = etText.getText().toString();

        // сохранение заметки, если текст не пустой
        if (!tmpText.equals("")) {
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            // ПЕРЕЗАПИСЬ ТЕМЫ - РЕАЛИЗОВАТЬ В ОТДЕЛЬНОМ ОКНЕ
            //editor.putString(tmpTitle, tmpText);

            // выгрузка строки с темами
            String strTitles = sharedPreferences.getString(KEY_TITLE, "");

            // проверка, есть ли вообще темы
            if (!strTitles.equals("")) {
                //editor.putString(KEY_TITLE, strTitles);
                editor.apply();

                // проверка на существование данной темы
                String [] tmpStrArray = strTitles.split(symbol);
                for (String element: tmpStrArray) {
                    if (element.equals(tmpTitle)) {
                        titleExist = true;
                        break;
                    }
                }
                // проверка существования данной темы
                if (!titleExist) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(FirstScene.this);
                    builder.setTitle("Сохранение")
                            .setMessage("Сохранить заметку?")
                            .setCancelable(false);
                    builder.setNegativeButton("Отмена",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    builder.setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    String strTitles = sharedPreferences.getString(KEY_TITLE, "");
                                    editor.putString(tmpTitle, tmpText);
                                    strTitles += symbol + tmpTitle;
                                    Toast.makeText(FirstScene.this, "Сохранено", Toast.LENGTH_SHORT).show();

                                    // сортировка тем
                                    String[] arrStr = strTitles.split(symbol);
                                    Arrays.sort(arrStr);
                                    strTitles = String.join(symbol, arrStr);
                                    editor.putString(KEY_TITLE, strTitles);
                                    editor.apply();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(FirstScene.this);
                    builder2.setTitle("Перезапись темы");
                    builder2.setMessage("Вы хотите перезаписать существующую тему?");
                    builder2.setCancelable(true);
                    builder2.setNegativeButton("Отмена",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    builder2.setPositiveButton("Ок",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(etTitle.getText().toString(), etText.getText().toString());
                                    editor.apply();
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert2 = builder2.create();
                    alert2.show();

                    //Toast.makeText(FirstScene.this, "Сохранение невозможно - заметка с такой темой уже есть", Toast.LENGTH_SHORT).show();
                }

                titleExist = false;
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(FirstScene.this);
                builder.setTitle("Сохранение")
                        .setMessage("Сохранить заметку?")
                        .setCancelable(false);
                builder.setNegativeButton("Отмена",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                builder.setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        String strTitles = "";
                        editor.putString(tmpTitle, tmpText);
                        strTitles += tmpTitle;

                        editor.putString(KEY_TITLE, strTitles);
                        editor.apply();
                        Toast.makeText(FirstScene.this, "Сохранено", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }

        } else {
            Toast.makeText(FirstScene.this, "Нельзя сохранить пустую заметку", Toast.LENGTH_SHORT).show();
        }
    }

    public void editThemeName() {
        LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.lauout_dialog_theme_name, null);
        final EditText themeText = view.findViewById(R.id.new_theme);
        // вызов окна
        AlertDialog.Builder builder = new AlertDialog.Builder(FirstScene.this);
        builder.setView(view)
            .setCancelable(true);
        builder.setNegativeButton("Отмена",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.setPositiveButton("Ок",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        // ----------------------------------------удаление темы из списка тем
                        String tmpTitle = etTitle.getText().toString();

                        // выгрузка строки тем
                        String strTitles3 = sharedPreferences.getString(KEY_TITLE, "");
                        String[] itemsTitles = strTitles3.split(symbol);
                        // добавление в список эл-в из строки
                        List<String> tmpList = new ArrayList<String>(Arrays.asList(itemsTitles));

                        // если эл-т существует, удалить
                        if (tmpList.indexOf(tmpTitle) > -1) {
                            tmpList.remove(tmpTitle);
                        }

                        // создание строки
                        StringBuilder stringBuilder = new StringBuilder();

                        // проверка на пустоту списка
                        if (!tmpList.isEmpty()) {
                            stringBuilder.append(tmpList.get(0));
                            tmpList.remove(0);
                            for (String s: tmpList) {
                                stringBuilder.append(symbol);
                                stringBuilder.append(s);
                            }
                        } else {
                            stringBuilder.append("");
                        }

                        String tempStringBuilder = stringBuilder.toString();

                        editor.putString(KEY_TITLE, tempStringBuilder);

                        // удаление ключа
                        editor.remove(etTitle.getText().toString());
                        // ------------------------------------------ УДАЛЕНА СТАРАЯ ТЕМА

                        // зафиксировать данные
                        editor.apply();
                        // запись в список тем
                        String tmpTitle2 = themeText.getText().toString();
                        String tmpText = etText.getText().toString();

                        // сохранение заметки, если текст не пустой
                        if (!tmpText.equals("")) {
                            // ПЕРЕЗАПИСЬ ТЕМЫ - РЕАЛИЗОВАТЬ В ОТДЕЛЬНОМ ОКНЕ
                            // editor.putString(tmpTitle, tmpText);

                            // выгрузка строки с темами
                            String strTitles = sharedPreferences.getString(KEY_TITLE, "");

                            // ПРОВЕРКА НЕ НУЖНА, Т.К. МИНИМУМ 1 ТЕМА ЕСТЬ
                            // проверка, есть ли вообще темы
                            if (!strTitles.equals("")) {
                                // проверка на существование данной темы
                                String [] tmpStrArray = strTitles.split(symbol);
                                for (String element: tmpStrArray) {
                                    if (element.equals(tmpTitle2)) {
                                        titleExist = true;
                                        break;
                                    }
                                }
                                // проверка существования данной темы
                                if (!titleExist) {
                                    editor.putString(tmpTitle2, tmpText);
                                    strTitles += symbol + tmpTitle2;
                                    Toast.makeText(FirstScene.this, "Сохранено", Toast.LENGTH_SHORT).show();

                                    // сортировка тем
                                    String[] arrStr = strTitles.split(symbol);
                                    Arrays.sort(arrStr);
                                    strTitles = String.join(symbol, arrStr);
                                    etTitle.setText(tmpTitle2);
                                } else {
                                    Toast.makeText(FirstScene.this, "Сохранение невозможно - заметка с такой темой уже есть", Toast.LENGTH_SHORT).show();
                                }

                                titleExist = false;
                            } else {
                                editor.putString(tmpTitle2, tmpText);
                                strTitles += tmpTitle2;
                            }

                            editor.putString(KEY_TITLE, strTitles);

                            // editor.putString(KEY_TITLE, "");
                        } else {
                            Toast.makeText(FirstScene.this, "Нельзя сохранить пустую заметку", Toast.LENGTH_SHORT).show();
                        }

                        editor.apply();
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void searchRecord() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String text = sharedPreferences.getString(etTitle.getText().toString(), "");
        etText.setText(text);
        if (text.equals("")) {
            Toast.makeText(this, "Не найдено заметок с такой темой", Toast.LENGTH_SHORT).show();
        }
    }

    //тот же saveData(), только
    public void delRecord() {
        // вызов окна
        AlertDialog.Builder builder = new AlertDialog.Builder(FirstScene.this);
        builder.setTitle("Удаление заметки")
                .setMessage("Удалить заметку?")
                .setCancelable(false);
        builder.setNegativeButton("Отмена",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.setPositiveButton("Ок",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        // ----------------------------------------удаление темы из списка тем
                        String tmpTitle = etTitle.getText().toString();

                        // выгрузка строки тем
                        String strTitles3 = sharedPreferences.getString(KEY_TITLE, "");
                        String[] itemsTitles = strTitles3.split(symbol);
                        // добавление в список эл-в из строки
                        List<String> tmpList = new ArrayList<String>(Arrays.asList(itemsTitles));

                        // если эл-т существует, удалить
                        if (tmpList.indexOf(tmpTitle) > -1) {
                            tmpList.remove(tmpTitle);
                        }

                        // создание строки
                        StringBuilder stringBuilder = new StringBuilder();

                        // проверка на пустоту списка
                        if (!tmpList.isEmpty()) {
                            stringBuilder.append(tmpList.get(0));
                            tmpList.remove(0);
                            for (String s: tmpList) {
                                stringBuilder.append(symbol);
                                stringBuilder.append(s);
                            }
                        } else {
                            stringBuilder.append("");
                        }

                        String tempStringBuilder = stringBuilder.toString();

                        editor.putString(KEY_TITLE, tempStringBuilder);
                        //Toast.makeText(this, tempStringBuilder, Toast.LENGTH_SHORT).show();

                        // ----------------------------------- Удаление темы и текста
                        // удаление ключа
                        editor.remove(etTitle.getText().toString());
                        etTitle.setText("");
                        etText.setText("");

                        editor.apply();
                        Toast.makeText(FirstScene.this, "Заметка удалена", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    // работа с меню и окнами
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // получим идентификатор выбранного пункта меню
        int id = item.getItemId();
        LayoutInflater inflater = getLayoutInflater();
        LayoutInflater inflater_pass = getLayoutInflater();
        View view = inflater.inflate(R.layout.lauout_dialog, null);
        View view_pass = inflater_pass.inflate(R.layout.lauout_dialog_ch_pass, null);

        switch (id) {
            case R.id.action_del_all_notes:
                checkBox = view.findViewById(R.id.checkBox);
                // вызов окна удаления
                AlertDialog.Builder builder2 = new AlertDialog.Builder(FirstScene.this);
                builder2.setView(view)
                        .setCancelable(true);
                //builder2.setTitle("Удаление");
                //builder2.setMessage("Вы действительно хотите уадалить все заметки?");
                //.setIcon(R.drawable.ic_android_cat)
                builder2.setNegativeButton("Отмена",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                builder2.setPositiveButton("Ок",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (checkBox.isChecked()) {
                                    etTitle.setText("");
                                    etText.setText("");
                                    SharedPreferences sharedPreferences1 = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                                    SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                                    // удаление всего содержимого SharedPreferences
                                    editor1.clear();
                                    editor1.apply();
                                    dialog.cancel();
                                    Toast.makeText(FirstScene.this, "Все заметки удалены", Toast.LENGTH_SHORT).show();
                                    checkBox.setChecked(false);
                                } else {
                                    Toast.makeText(FirstScene.this, "WARNING", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                //builder2.setCancelable(false);
                AlertDialog alert2 = builder2.create();
                alert2.show();
                return true;

            case R.id.action_titles:
                // -------------------------------------- ОКНО С ТЕМАМИ
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                // выгрузка строки с темами
                String strTitles2 = sharedPreferences.getString(KEY_TITLE, "");
                editor.apply();

                // вызов окна
                AlertDialog.Builder builder = new AlertDialog.Builder(FirstScene.this);

                if (strTitles2.equals("")) {
                    strTitles2 = "Тем пока нет :)";
                    builder.setTitle("Темы")
                            .setMessage(strTitles2)
                            .setCancelable(true)
                            .setNegativeButton("Ок",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                } else {
                    final String[] arrStr = strTitles2.split(symbol);

                    builder.setItems(arrStr, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            etTitle.setText(arrStr[item]);
                            searchRecord();
                        }
                    });
                    builder.setTitle("Темы")
                            .setCancelable(true);
                }

                AlertDialog alert = builder.create();
                alert.show();
                return true;

            case R.id.action_change_pass:
                oldPass = view_pass.findViewById(R.id.old_pass);
                newPass = view_pass.findViewById(R.id.new_pass);
                checkBox2 = view_pass.findViewById(R.id.checkBox2);
                // вызов окна
                AlertDialog.Builder builder3 = new AlertDialog.Builder(FirstScene.this);
                        builder3.setView(view_pass);
                        builder3.setCancelable(true);
                        builder3.setNegativeButton("Отмена",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                        builder3.setPositiveButton("Ок",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        SharedPreferences sharedPreferences3 = getSharedPreferences(PASS_SHARED_PREFS, MODE_PRIVATE);
                                        SharedPreferences.Editor editor3 = sharedPreferences3.edit();

                                        String passString;
                                        // выгрузка строки с паролем
                                        passString = sharedPreferences3.getString(PASS_KEY, "");

                                        oldPassText = oldPass.getText().toString();
                                        newPassText = newPass.getText().toString();

                                        if (oldPassText.equals(passString)) {
                                            if (checkBox2.isChecked()) {
                                                editor3.putString(FLAG, "0");
                                                editor3.putString(PASS_KEY, "");
                                                Toast.makeText(FirstScene.this, "Пароль сброшен", Toast.LENGTH_SHORT).show();
                                            } else if (!newPassText.equals("")) {
                                                editor3.putString(PASS_KEY, newPassText);
                                                editor3.putString(FLAG, "1");
                                                Toast.makeText(FirstScene.this, "Пароль успешно изменен", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(FirstScene.this, "Не задан новый пароль", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(FirstScene.this, "Неверный старый пароль", Toast.LENGTH_SHORT).show();
                                            //Toast.makeText(FirstScene.this, oldPassText+"|"+passString, Toast.LENGTH_SHORT).show();
                                        }

                                        editor3.apply();
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert3 = builder3.create();
                alert3.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void autoCompletion () {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // выгрузка строки тем
        String strTitles4 = sharedPreferences.getString(KEY_TITLE, "");

        String[] itemsTitles = strTitles4.split(symbol);
        editor.apply();

        etTitle.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, itemsTitles));
    }
}
