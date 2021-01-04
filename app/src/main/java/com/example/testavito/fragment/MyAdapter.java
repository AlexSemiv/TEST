package com.example.testavito.fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testavito.R;
import com.example.testavito.databinding.ItemRecyclerBinding;
import com.example.testavito.model.Item;
import com.example.testavito.model.Items;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
    private Items mItems;

    public MyAdapter(Items items) {
        mItems = items;
    }

    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Создание "оболочки" для элемента с использованием привязки данных из объекта (binding)
        ItemRecyclerBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_recycler,
                parent,
                false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Установка объекта, из которого binding будет брать данные для привязки.
        // Установка события клика на кнопку их макета элемента,
        // и вызов метода для удаление этого элемента
        holder.itemRecyclerBinding.setItem(mItems.getItems().get(position));
        holder.itemView.findViewById(R.id.btnDel).setOnClickListener(v ->
                removeItem(position)
        );
    }

    // Количество элементов в RecyclerView
    @Override
    public int getItemCount() {
        return mItems.getItems().size();
    }

    // Добавление элемента на случайную позицию из Fragment
    public void addItem(int position){

        // Если очередь, в которой хранятся уже удаленные элементы пуста, то мы создаем новый объект,
        // иначе берем элемент из начала очереди и удаляем его из этой очереди
        if(mItems.getDeleteItems().isEmpty()){
            mItems.newItem(position);
        }else{
            mItems.getItems().add(position,mItems.getDeleteItems().poll());
        }

        // Обновление списка элементов в RecyclerView, которое означает что элемент на позиции position изменился,
        // и что все позиции элементов от position и до конца изменились
        notifyItemInserted(position);
        notifyItemRangeChanged(position,mItems.getItems().size());
    }

    // Метод, вызываемый после клика на кнопку "удалить" в элементе с позицией position.
    public void removeItem(int position){

        // Добавление удаленного элемента в очередь с удаленными элементами.
        // Удаление удаленного объекта из листа со всеми элементами в RecyclerView
        mItems.getDeleteItems().offer(mItems.getItems().get(position));
        mItems.getItems().remove(position);

        // Обновление списка элементов в RecyclerView, которое означает что элемент на позиции position был удален,
        // и что все позиции элементов от position и до конца изменились
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,mItems.getItems().size());
    }

    // Класс, предназначенный для отображения элементов в RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {

        public ItemRecyclerBinding itemRecyclerBinding;

        public ViewHolder(ItemRecyclerBinding itemRecyclerBinding) {
            super(itemRecyclerBinding.getRoot());
            this.itemRecyclerBinding = itemRecyclerBinding;
        }
    }
}