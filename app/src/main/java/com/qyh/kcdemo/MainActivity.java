package com.qyh.kcdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.qyh.litemvp.ui.FragmentStack;
import com.qyh.rongclound.listener.RongAppContext;
import com.qyh.rongclound.ui.ViewPagerFragment;

import io.rong.imkit.RongIM;

public class MainActivity extends AppCompatActivity {
    private final String id1 = "qiuyongheng";
    private final String name1 = "邱永恒";
    private final String imgUrl1 = "https://avatars3.githubusercontent.com/u/248093?s=460&v=4";
    private final String token1 = "9/gUKjPtl1+L5KHXXliNy7xBYkLxNwzbinUL1J9QpMKyuUDrRsLQ5pMXNNFTwTwiVlrk/XQzjORzC4s0ViQnL4NZxSaxlzDJ";

    private final String id2 = "gaga";
    private final String name2 = "嘎嘎";
    private final String imgUrl2 = "https://avatars2.githubusercontent.com/u/8560582?s=460&v=4";
    private final String token2 = "/VURzMW+6kCRWFnUBKi+QTLS6nBGiP25U6fekLmZdUBsQxT3s//kS7ym44ROPMgKbi9biAXUAhcEbuu2DDNGbQ==";
    private FragmentStack fragmentStack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentStack = new FragmentStack(this, getSupportFragmentManager(), R.id.container_fragment);
        fragmentStack.replace(new ViewPagerFragment());

    }
}
