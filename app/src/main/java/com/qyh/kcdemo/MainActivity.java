package com.qyh.kcdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.qyh.litemvp.ui.FragmentStack;
import com.qyh.rongclound.ui.main.RongCloudFragment;

public class MainActivity extends AppCompatActivity {
    private FragmentStack fragmentStack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentStack = new FragmentStack(this, getSupportFragmentManager(), R.id.container_fragment);
        fragmentStack.replace(new RongCloudFragment());
    }
}
