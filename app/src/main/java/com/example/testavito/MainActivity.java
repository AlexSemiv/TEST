package com.example.testavito;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.testavito.fragment.FragmentRecycler;

public class MainActivity extends AppCompatActivity implements FragmentRecycler.RecycleCallbacks{
    public final String TAG_RECYCLER_FRAGMENT = "TAG_RECYCLER_FRAGMENT";
    public FragmentRecycler fragmentRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Создаем фрагмент в RecyclerView, если впервые запускаем приложение.
        // Иначе находим его по тэгу

        fragmentRecycler = (FragmentRecycler) getSupportFragmentManager()
                .findFragmentByTag(TAG_RECYCLER_FRAGMENT);

        if (fragmentRecycler == null) {
            fragmentRecycler = new FragmentRecycler();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.mainLayout, fragmentRecycler, TAG_RECYCLER_FRAGMENT)
                    .commit();
            }
    }

    @Override
    public void onProgressUpdate(int position) {
        // Реализуем методы из интерфейса из фрагмента,
        // если нам необходимо получать информацию из фрагмента в Activity
    }

    @Override
    public void onCancelled() {

    }
}