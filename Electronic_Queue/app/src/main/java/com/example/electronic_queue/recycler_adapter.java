package com.example.electronic_queue;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class recycler_adapter extends RecyclerView.Adapter<recycler_adapter.ViewHolder>{//адаптер списка талонов
    private final List<String> DateData,TimeData;//хранилище времени полученных талонов
    public static class ViewHolder extends RecyclerView.ViewHolder {//инициализация хранилища талонов
        private final TextView tv1,tv2;
        public ViewHolder(View view) {
            super(view);
            tv1 = (TextView) view.findViewById(R.id.text_view1);
            tv2 = (TextView) view.findViewById(R.id.text_view2);
        }
        public TextView getTextView1() {
            return tv1;
        }
        public TextView getTextView2() {
            return tv2;
        }
    }
    public recycler_adapter(List<String> DateArray, List<String> TimeArray) {//конструктор адаптера, хранящий список талонов
        DateData=DateArray;
        TimeData=TimeArray;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {//функия создания хранилища талонов
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_item, viewGroup, false);//создание разметки
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {//инициализация объекта разметки для талона
        viewHolder.getTextView1().setText(TimeData.get(position));
        viewHolder.getTextView2().setText(DateData.get(position));
    }
    @Override
    public int getItemCount() {//функция получения количества талонов в списке
        return DateData.size();
    }
}
