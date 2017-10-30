package com.qyh.rongclound.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qyh.rongclound.R;
import com.qyh.rongclound.listener.OnRecyclerItemClickListener;
import com.qyh.rongclound.ui.widget.DragPointView;

import java.util.HashMap;

/**
 * @author 邱永恒
 * @time 2017/10/30  8:45
 * @desc ${TODD}
 */

public class OtherMsgAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private final Context context;
    private final String[] titles;
    private final int[] imgs;
    private final LayoutInflater inflater;
    private HashMap<Integer, Integer> counts;
    private OnRecyclerItemClickListener listener;

    public OtherMsgAdapter(Context context, String[] titles, int[] imgs, HashMap<Integer, Integer> counts) {
        this.context = context;
        this.titles = titles;
        this.imgs = imgs;
        this.counts = counts;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NormalHolder(inflater.inflate(R.layout.rong_item_other_msg, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        String title = titles[position];
        int imgId = imgs[position];
        Integer count = counts.get(position);
        ((NormalHolder) holder).itemImg.setImageResource(imgId);
        ((NormalHolder) holder).itemTv.setText(title);

        if (count == 0) {
            ((NormalHolder) holder).rongNum.setVisibility(View.GONE);
        } else if (count > 0 && count < 100) {
            ((NormalHolder) holder).rongNum.setVisibility(View.VISIBLE);
            ((NormalHolder) holder).rongNum.setText(String.valueOf(count));
        } else {
            ((NormalHolder) holder).rongNum.setVisibility(View.VISIBLE);
            ((NormalHolder) holder).rongNum.setText("99+");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onItemClick(holder.itemView, holder.getAdapterPosition());
                }
            }
        });
    }

    class NormalHolder extends RecyclerView.ViewHolder {

        private final ImageView itemImg;
        private final DragPointView rongNum;
        private final TextView itemTv;

        public NormalHolder(View itemView) {
            super(itemView);
            itemImg = (ImageView) itemView.findViewById(R.id.item_img);
            rongNum = (DragPointView) itemView.findViewById(R.id.rong_num);
            itemTv = (TextView) itemView.findViewById(R.id.item_tv);
        }
    }

    class DiviHolder extends RecyclerView.ViewHolder {

        public DiviHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }

    public void setOnClickListener(OnRecyclerItemClickListener listener) {
        this.listener = listener;
    }

    public void setCounts(HashMap<Integer, Integer> counts) {
        this.counts = counts;
    }
}
