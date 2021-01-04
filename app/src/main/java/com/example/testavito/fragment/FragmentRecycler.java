package com.example.testavito.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testavito.model.Items;
import com.example.testavito.R;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class FragmentRecycler extends Fragment{
    public interface RecycleCallbacks{
        void onProgressUpdate(int position);
        void onCancelled();
    }
    private RecycleCallbacks mCallbacks;
    private AddItemTask mAsync;
    private Items mItems;
    private Random mRandom;
    private MyAdapter mAdapter;
    private GridLayoutManager gridLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Создаем "внешний вид" фрагмента, где находится RecycleView,
        // используя заранее созданный для этого layout
        View view = inflater.inflate(R.layout.fragment_recycler,container,false);

        // Определяем RecyclerView из макета.
        // Устанавливаем количество колонок в RecyclerView, т.е. GridLayoutManager,
        // исходя из текущей ориентации телефона
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(gridLayoutManager);

        // Создаем Adapter для RecyclerView,
        // который отвечает за все операции, связанные с элементами в RecyclerView.
        // В качестве аргумента передаем в Adapter объект Items
        mAdapter = new MyAdapter(mItems);
        recyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Одной из задач, поставленных в тз является продолжение работы async после переворота экрана.
        // Т.к. в случае переворота экрана Activity уничтожается, для продолжения работы async мы используем его в фрагменте.
        // В этом случае при использовании setRetainInstance(true) методы onCreate() и onDestroy() не будут вызваны.
        // Т.е. при перевороте экрана, фрагмент не пересоздатся и мы сможем сохранить, текущий объект фрагмента и, следовательно,
        // сохранить выполнение операции в async
        setRetainInstance(true);

        mRandom = new Random();
        mItems = new Items();

        // Создание и запуск асинхронной задачи по добавлению элементов в RecyclerView
        mAsync = new AddItemTask();
        mAsync.execute();
    }

    // Вызывается в начале жизненного цикла Fragment
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mCallbacks = (RecycleCallbacks) getActivity();

        // Исходя из текущей ориентации экрана, инициализация gridLayoutManager с 2-умя или 4-мя колонками
        int orientation =  getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_PORTRAIT){
            gridLayoutManager = new GridLayoutManager(getContext(),2);
        }else{
            gridLayoutManager = new GridLayoutManager(getContext(),4);
        }
    }

    // Вызывается в конце жизненного цикла Fragment
    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    private class AddItemTask extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... items) {

            // Запуск асинхронной задачи, пока количество элементов не достигнет установленного максимума или задача не будет остановлена
            while(mItems.getItems().size() <= 40 && !isCancelled()){
                try {

                    // Остановка в 5 секунд в фоновом потоке и вызов метода publishProgress() для проведения изменений в UI в UI-потоке
                    TimeUnit.SECONDS.sleep(5);
                    publishProgress();
                } catch (InterruptedException e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

            // Выбираем случайную позицию для нового элемента, используя текущее количество элементов.
            // Если элементов на экране нет, то устанавливаем элемент на 0-ую позицию
            int randomPosition;
            if(!mItems.getItems().isEmpty())
                randomPosition = mRandom.nextInt(mItems.getItems().size());
            else
                randomPosition = 0;
            // Передаем randomPosition в Adapter для создания нового элемента в RecyclerView
            mAdapter.addItem(randomPosition);

            if(mCallbacks!=null){
                mCallbacks.onProgressUpdate(randomPosition);
            }
        }

        // В случае остановки задачи можем передать информацию об остановке в MainActivity
        // или любое другое Activity, реализующее наш интерфейс
        @Override
        protected void onCancelled() {
            super.onCancelled();
            if(mCallbacks!=null){
                mCallbacks.onCancelled();
            }
        }
    }
}
