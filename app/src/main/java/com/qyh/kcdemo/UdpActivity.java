package com.qyh.kcdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.qyh.rongclound.udp.UdpService;


/**
 * @author 邱永恒
 * @time 2017/10/31  9:17
 * @desc ${TODD}
 */

public class UdpActivity extends AppCompatActivity {

    private Button btnStartService;
    private EditText etUid;
    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_udp);

        initView();
    }

    private void initView() {
        btnStartService = (Button) findViewById(R.id.btn_start_service);
        etUid = (EditText) findViewById(R.id.et_uid);


        btnStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = etUid.getText().toString();
                Integer uid = Integer.valueOf(s);

                intent = new Intent(UdpActivity.this, UdpService.class);
                Bundle bundle = new Bundle();
                bundle.putInt(UdpService.UID, uid);
                intent.putExtras(bundle);
                startService(intent);
            }
        });
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, UdpActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (intent != null) {
//            stopService(intent);
//        }
    }
}
