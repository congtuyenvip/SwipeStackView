package com.ctvsoft.view.swipelistview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        TextView mHello = (TextView) findViewById(R.id.text_hello);
//        mHello.setOnTouchListener(new SwipeDetector(mHello));
        SwipeStackView swipeListView = (SwipeStackView) findViewById(R.id.view_swipelist);
        final ArrayList al = new ArrayList<>();
        al.add("1");
        al.add("2");
        al.add("3");
        al.add("4");
        al.add("5");
        al.add("6");
        al.add("7");
        al.add("8");

        final ArrayAdapter arrayAdapter = new ArrayAdapter<>(this, R.layout.item, R.id.text_item, al );
        swipeListView.setOnSwipeActionListener(new OnSwipeActionListener() {
            @Override
            public void onSwipeFinish(int pSwipeDirection) {
                if(arrayAdapter.getCount()>0) {
                    String item = (String) al.get(0);
                    al.remove(0);
                    al.add(al.size() -1, item);
                    arrayAdapter.notifyDataSetChanged();
                }
            }
        });
        swipeListView.setAdapter(arrayAdapter);
    }
}
