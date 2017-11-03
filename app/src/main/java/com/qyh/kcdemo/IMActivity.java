package com.qyh.kcdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.qyh.rongclound.constant.Constant;
import com.qyh.rongclound.listener.RongAppContext;
import com.qyh.rongclound.mvp.messagelist.MessageListActivity;
import com.qyh.rongclound.notifycation.NotifyManager;
import com.qyh.rongclound.udp.UdpService;

import java.util.ArrayList;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

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
    private Button btnSocket;
    private Button btnUdp;
    private Button btnNotify;
    private int count = 0;
    private Button btnOpenGroup;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_im);

        ids.add(id1);
        ids.add(id2);

        btnOpen = (Button) findViewById(R.id.btn_open);
        btnCreator = (Button) findViewById(R.id.btn_creator);
        btnSocket = (Button) findViewById(R.id.btn_socket);
        btnUdp = (Button) findViewById(R.id.btn_udp);
        btnNotify = (Button) findViewById(R.id.btn_notify);
        btnOpenGroup = (Button) findViewById(R.id.btn_open_group);

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

        btnSocket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SocketActivity.startActivity(IMActivity.this);
            }
        });

        btnUdp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UdpActivity.startActivity(IMActivity.this);
            }
        });

        btnNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt(MessageListActivity.TYPE, Constant.TYPE_SOS);
                NotifyManager.getInstance().sendNotify(3, R.mipmap.dynamic,  R.mipmap.dynamic, "gaga", String.valueOf(count++), MessageListActivity.class, bundle);
            }
        });

        btnOpenGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                RongIM.getInstance().startGroupChat(IMActivity.this, "8888", "东亚重工");
                RongIM.getInstance().startConversation(IMActivity.this, Conversation.ConversationType.GROUP, "9999", "东亚重工");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(this, UdpService.class);
        stopService(intent);
    }
}
