package com.example.electronic_queue;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.List;

public class tickets extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {//метод создания активности
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tickets);//установка отображающейся разметки
        TabLayout tl=findViewById(R.id.tab_layout);
        tl.selectTab(tl.getTabAt(1));
        tl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {//метод выбора вкладки
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int p=tab.getPosition();
                if(p==0){
                    startActivity(new Intent(tickets.this, main.class));//запуск активности
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}//обязательное переопределение методов
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
        List<String> DateList,TimeList;//инициализация массивов дат
        DateList=new ArrayList<String>();
        TimeList=new ArrayList<String>();
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("data.db", MODE_PRIVATE, null);//создание или открытие базы данных
        db.execSQL("CREATE TABLE IF NOT EXISTS tickets (year INTEGER, month INTEGER, day INTEGER, hour INTEGER, minute INTEGER)");//создание или открытие таблицы дат
        Cursor query = db.rawQuery("SELECT * FROM tickets;", null);//установка курсора на начало таблицы
        while(query.moveToNext()){//проход по записям в таблице дат с записью в массив в строковом представлении
            int year = query.getInt(0);
            int month = query.getInt(1);
            int day = query.getInt(2);
            int hour = query.getInt(3);
            int minute = query.getInt(4);
            DateList.add(String.valueOf(day)+"."+String.valueOf(month)+"."+String.valueOf(year));
            if (minute==0){
                TimeList.add(String.valueOf(hour)+":00");
            }
            else{
                TimeList.add(String.valueOf(hour)+":"+String.valueOf(minute));
            }
        }
        query.close();//закрытие базы данных
        db.close();
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));//установка параметров разметки и адаптера для списка талонов
        recyclerView.setAdapter(new recycler_adapter(DateList,TimeList));
    }
}