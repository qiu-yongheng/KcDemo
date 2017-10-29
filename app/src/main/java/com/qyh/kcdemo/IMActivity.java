package com.qyh.kcdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.qyh.rongclound.listener.RongAppContext;

import java.util.ArrayList;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * @author 邱永恒
 * @version $Rev$
 * @time 2017/10/28  13:04
 * @desc ${TODD}
 * @updateAutor $Author$
 * @updateDate $Date$
 * @updatedes ${TODO}
 */


public class IMActivity extends AppCompatActivity{
    private final String id1 = "qiuyongheng";
    private final String name1 = "邱永恒";
    private final String imgUrl1 = "https://avatars3.githubusercontent.com/u/248093?s=460&v=4";
    private final String token1 = "9/gUKjPtl1+L5KHXXliNy7xBYkLxNwzbinUL1J9QpMKyuUDrRsLQ5pMXNNFTwTwiVlrk/XQzjORzC4s0ViQnL4NZxSaxlzDJ";

    private final String id2 = "gaga";
    private final String name2 = "嘎嘎";
    private final String imgUrl2 = "https://avatars2.githubusercontent.com/u/8560582?s=460&v=4";
    private final String token2 = "/VURzMW+6kCRWFnUBKi+QTLS6nBGiP25U6fekLmZdUBsQxT3s//kS7ym44ROPMgKbi9biAXUAhcEbuu2DDNGbQ==";
    private ArrayList<String> ids = new ArrayList<>();
    private Button btnOpen;
    private Button btnCreator;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_im);

        ids.add(id1);
        ids.add(id2);

        btnOpen = (Button) findViewById(R.id.btn_open);
        btnCreator = (Button) findViewById(R.id.btn_creator);

        RongIM.connect(token1, RongAppContext.getInstance().getConnectCallback());

        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(IMActivity.this, MainActivity.class));
            }
        });

        btnCreator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RongIM.getInstance().createDiscussion(name1 + ", " + name2, ids, new RongIMClient.CreateDiscussionCallback() {
                    @Override
                    public void onSuccess(String s) {
                        Log.d("==", "创建成功");
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        Log.d("==", "创建失败: " + errorCode);
                    }
                });
            }
        });
    }
}
