package com.qyh.kcdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * @author 邱永恒
 * @time 2017/10/30  15:27
 * @desc ${TODD}
 */

public class SocketActivity extends AppCompatActivity implements View.OnClickListener {

    private Button open;
    private Button close;
    private EditText et;
    private TextView tv;
    private Button send;
    private Request request;
    private OkHttpClient okHttpClient;
    private WebSocket webSocket;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);

        String s = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOi8vbHVtZW4uY29tL2FwaS9hdXRob3JpemF0aW9ucy9jdXJyZW50IiwiaWF0IjoxNTA5MzUwNTkyLCJleHAiOjE1MTA1NjAxOTIsIm5iZiI6MTUwOTM1MDU5MiwianRpIjoiRnU0UWVyVEw4b1lLYXhycSIsInN1YiI6MTR9.6PZ5jBXUUl1rIutgm6iVz6psQ6_FRwXypjfG4SCKmiM";
        initView();

        initSocket();
    }

    private void initSocket() {
        okHttpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();

                Headers headers = request.headers();
                for (int i = 0, count = headers.size(); i < count; i++) {
                    // 打印头
                    Log.d("==", "\t" + headers.name(i) + ": " + headers.value(i));
                }
                return chain.proceed(chain.request());
            }
        }).build();
        request = new Request.Builder()
                .url("ws://" + "192.168.254.252" + ":" + 9501)
                .addHeader("Authorization", "bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOi8vbHVtZW4uY29tL2FwaS9hdXRob3JpemF0aW9ucy9jdXJyZW50IiwiaWF0IjoxNTA5MzUwNTkyLCJleHAiOjE1MTA1NjAxOTIsIm5iZiI6MTUwOTM1MDU5MiwianRpIjoiRnU0UWVyVEw4b1lLYXhycSIsInN1YiI6MTR9.6PZ5jBXUUl1rIutgm6iVz6psQ6_FRwXypjfG4SCKmiM")
                .build();
    }

    private void initView() {
        open = (Button) findViewById(R.id.btn_open);
        close = (Button) findViewById(R.id.btn_close);
        et = (EditText) findViewById(R.id.et);
        tv = (TextView) findViewById(R.id.tv);
        send = (Button) findViewById(R.id.btn_send);

        open.setOnClickListener(this);
        close.setOnClickListener(this);
        send.setOnClickListener(this);
    }

    public static void startActivity(Context context){
        Intent intent = new Intent(context, SocketActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_open:
                webSocket = okHttpClient.newWebSocket(request, new WebSocketListener() {
                    @Override
                    public void onOpen(WebSocket webSocket, Response response) {
                        super.onOpen(webSocket, response);
                        Log.d("==", "onOpen");
                    }

                    @Override
                    public void onMessage(WebSocket webSocket, final String text) {
                        super.onMessage(webSocket, text);
                        Log.d("==", "onOpen");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv.setText(text);
                            }
                        });
                    }

                    @Override
                    public void onMessage(WebSocket webSocket, final ByteString bytes) {
                        super.onMessage(webSocket, bytes);
                        Log.d("==", "onMessage");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv.setText(String.valueOf(bytes));
                            }
                        });
                    }

                    @Override
                    public void onClosing(WebSocket webSocket, int code, String reason) {
                        super.onClosing(webSocket, code, reason);
                        Log.d("==", "onClosing");
                    }

                    @Override
                    public void onClosed(WebSocket webSocket, int code, String reason) {
                        super.onClosed(webSocket, code, reason);
                        Log.d("==", "onClosed");
                    }

                    @Override
                    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                        super.onFailure(webSocket, t, response);
                        Log.d("==", "onFailure: " + t.toString());
                    }
                });
                break;
            case R.id.btn_close:
                if (webSocket == null) {
                    Toast.makeText(this, "请链接webSocket", Toast.LENGTH_SHORT).show();
                    return;
                }
                webSocket.cancel();
                break;
            case R.id.btn_send:
                String trim = et.getText().toString().trim();
                if (TextUtils.isEmpty(trim)) {
                    Toast.makeText(this, "请输入", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (webSocket != null) {
                    webSocket.send(trim);
                }
                break;
        }
    }
}
