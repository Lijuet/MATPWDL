package edu.skku.map.matpwdl;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class KnockAdapter extends BaseAdapter {
    LayoutInflater inflater;
    private ArrayList<Knock> knocks;

    public KnockAdapter(Context context, ArrayList<Knock> knocks) {
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.knocks = knocks;
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

        return view;
    }
}
