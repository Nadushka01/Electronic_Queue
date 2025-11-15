package com.example.electronic_queue;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CalendarView;
import androidx.annotation.NonNull;
import com.google.android.material.tabs.TabLayout;
import java.util.Calendar;

public class main extends Activity {
    private int picked_year,picked_month,picked_day=0;//хранилище выбранной на виджете календаря даты
    @Override
    public void onCreate(Bundle savedInstanceState) {//метод создания активности
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//установка отображающейся разметки
        TabLayout tl=findViewById(R.id.tab_layout);
        AutoCompleteTextView hour_text=findViewById(R.id.hour_value);
        AutoCompleteTextView minute_text=findViewById(R.id.minute_value);
        hour_text.setInputType(InputType.TYPE_NULL);//отключение ввода для всплывающего списка
        minute_text.setInputType(InputType.TYPE_NULL);
        ArrayAdapter hours_adapter= new ArrayAdapter(this,R.layout.dropdown_item,getResources().getStringArray(R.array.hours));
        hour_text.setAdapter(hours_adapter);//создание и установка адпатера всплывающего списка
        ArrayAdapter minutes_adapter= new ArrayAdapter(this,R.layout.dropdown_item,getResources().getStringArray(R.array.minutes));
        minute_text.setAdapter(minutes_adapter);
        Button get_ticket_button=findViewById(R.id.get_ticket_button);
        CalendarView calendar_view = findViewById(R.id.calendar_view);
        calendar_view.setMinDate(calendar_view.getDate());//установка минимальной выбранной даты равной текущей дате
        hour_text.setOnItemClickListener(new AdapterView.OnItemClickListener() {//метод при выборе часа
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isTicketAvailable(hour_text,minute_text,get_ticket_button);//проверка корректности выбранной даты
            }
        });
        minute_text.setOnItemClickListener(new AdapterView.OnItemClickListener() {//метод при выборе минуты
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isTicketAvailable(hour_text,minute_text,get_ticket_button);//проверка корректности выбранной даты
            }
        });
        calendar_view.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {//метод при выборе даты
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                picked_year=year;//сохранение выбранной даты
                picked_month=month;
                picked_day=dayOfMonth;
                isTicketAvailable(hour_text,minute_text,get_ticket_button);//проверка корректности выбранной даты
            }
        });
        tl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {//метод выбора вкладки
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int p=tab.getPosition();
                if(p==1){
                    startActivity(new Intent(main.this, tickets.class));//запуск активности
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}//обязательное переопределение методов
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
        get_ticket_button.setOnClickListener(new View.OnClickListener() {//метод получекния талона
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = getBaseContext().openOrCreateDatabase("data.db", MODE_PRIVATE, null);//создание или открытие базы данных
                db.execSQL("CREATE TABLE IF NOT EXISTS tickets (year INTEGER, month INTEGER, day INTEGER, hour INTEGER, minute INTEGER)");//создание или открытие таблицы дат
                db.execSQL(String.format("INSERT OR IGNORE INTO tickets VALUES (%d, %d, %d, %d, %d);",picked_year,picked_month+1,picked_day,
                        Integer.parseInt(String.valueOf(hour_text.getText())),Integer.parseInt(String.valueOf(minute_text.getText()))));//добавление записи выбранной даты
                db.close();//закрытие базы данных
            }
        });
    }
    void isTicketAvailable(AutoCompleteTextView hours, AutoCompleteTextView minutes, Button button) {//метод проверки корректности выбранной даты
        if (hours.getText().length()>0 && minutes.getText().length()>0 && picked_day>0) {//если выбранная дата позже текущего времени, кнопка "Получить талон" разблокируется, иначе станет заблокированной
            Calendar picked_date=Calendar.getInstance();
            picked_date.set(Calendar.YEAR, picked_year);
            picked_date.set(Calendar.MONTH, picked_month);
            picked_date.set(Calendar.DAY_OF_MONTH, picked_day);
            picked_date.set(Calendar.HOUR_OF_DAY, Integer.parseInt(String.valueOf(hours.getText())));
            picked_date.set(Calendar.MINUTE, Integer.parseInt(String.valueOf(minutes.getText())));
            picked_date.set(Calendar.SECOND, 0);
            picked_date.set(Calendar.MILLISECOND, 0);
            Calendar current_date=Calendar.getInstance();
            if (current_date.before(picked_date)) {
                button.setEnabled(true);
            }
            else {
                button.setEnabled(false);
            }
        }
        else {
            button.setEnabled(false);
        }
    }
}