package com.example.testavito.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Items {
    private ArrayList<Item> items;
    private Queue<Item> deleteItems;
    private int index = 0;

    // Инициализация ArrayList с элементами, Queue удаленных элементов и  первых 15-ти элементов
    public Items() {
        items = new ArrayList<>();
        deleteItems = new LinkedList<>();

        int count = 15;
        for(int i = 0;i < count;i++){
            items.add(new Item(++index));
        }
    }

    public ArrayList<Item> getItems() { return items; }

    public Queue<Item> getDeleteItems() { return deleteItems; }

    // Добавление нового элемента, на случайную позицию с увеличенным значением на +1
    public void newItem(int position){
        items.add(position, new Item(++index));
    }
}

