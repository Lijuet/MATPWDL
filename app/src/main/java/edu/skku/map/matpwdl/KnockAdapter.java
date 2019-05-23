package edu.skku.map.matpwdl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class KnockAdapter extends BaseAdapter {
    LayoutInflater inflater;
    private ArrayList<Knock> items;

    public KnockAdapter (Context context, ArrayList<Knock> memos) {
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.items = memos;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Knock getItem(int i) {
        return items.get(i);
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


        return view;
    }
}
