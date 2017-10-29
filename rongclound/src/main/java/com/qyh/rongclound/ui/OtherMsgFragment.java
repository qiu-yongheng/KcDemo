package com.qyh.rongclound.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qyh.rongclound.R;

/**
 * @author 邱永恒
 * @time 2017/10/27  16:22
 * @desc ${TODD}
 */

public class OtherMsgFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_other_msg, container, false);
    }
}
