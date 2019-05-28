package edu.skku.map.matpwdl;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class KnockAdapter extends BaseAdapter {
    LayoutInflater inflater;
    private ArrayList<Knock> knocks;
    private int mode;

    public KnockAdapter(Context context, ArrayList<Knock> knocks, int mode) {
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.knocks = knocks;
        this.mode = mode;
    }

    @Override
    public int getCount() {
        return knocks.size();
    }

    @Override
    public Knock getItem(int i) {
        return knocks.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if ( view == null ) {
            view = inflater.inflate(R.layout.knock_item_layout, viewGroup, false);
        }

        Knock item = knocks.get(i);

        TextView receiever_tv = (TextView)view.findViewById(R.id.textView_KnockItemReciever);
        TextView contents_tv = (TextView)view.findViewById(R.id.textView_KnockItemContent);
        TextView date_tv = (TextView)view.findViewById(R.id.textView_KnockItemDate);

        receiever_tv.setText(item.getReceiver());
        contents_tv.setText(item.getContent());
        date_tv.setText(item.getDate());


        if(mode == 1) {//우리 방의 똑똑똑
            //받는 사람별 랜덤 색깔 배정
            int weight = Integer.valueOf(item.getReceiver());
            view.setBackgroundColor(Color.argb(50, (weight * 111) % 255, (weight * 11) % 255, weight % 255));
        }
        else{//나의 똑똑똑
            if(item.getReceiver().equals("1234")/*todo 나의 정보*/){//내가 받은거는 LTGray
                view.setBackgroundColor(Color.LTGRAY);
            }
            else{//내가 보낸거는 Gray
                view.setBackgroundColor(Color.GRAY);
            }

        }
        return view;
    }
}
