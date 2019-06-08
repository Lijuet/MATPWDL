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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class KnockAdapter extends BaseAdapter {
    LayoutInflater inflater;
    private ArrayList<Knock> knocks;
    private int mode;
    MyInformation myInfo;


    public KnockAdapter(Context context, ArrayList<Knock> knocks, MyInformation _myInfo, int mode) {
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.knocks = knocks;
        this.mode = mode;
        this.myInfo = _myInfo;
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
        TextView readOrNot_tv = view.findViewById(R.id.testView_ReadOrNot);

        receiever_tv.setText("To : "+ myInfo.getRoommatessID().get(Integer.parseInt(item.getReceiver())));
        contents_tv.setText(item.getContent());
        date_tv.setText(item.getDate());
        int weight;
        switch (mode){
            //받는 사람별 랜덤 색깔 배정
            case 1:
                weight = Integer.valueOf(item.getReceiver());
                view.setBackgroundColor(Color.argb(50, (weight * 111) % 255, (weight * 111) % 255, (weight * 222) % 255));
                break;
            case 2:
                weight = Integer.valueOf(item.getReceiver());
                if(item.getReceiver().equals(myInfo.getMemberid())){//내가 받은거는 LTGray
                    view.setBackgroundColor(Color.argb(50, (weight * 111) % 255, (weight * 111) % 255, (weight * 222) % 255));
                }
                else{//내가 보낸거는 Gray
                    view.setBackgroundColor(Color.WHITE);
                }
                break;
            case 3:
                    readOrNot_tv.setVisibility(View.VISIBLE);
                    Log.d("readOnNot",String.valueOf(item.getRead()));
                    if(item.getRead() == 1) readOrNot_tv.setText("읽음");
                    else readOrNot_tv.setText("안읽음");
                break;
            default:
                break;
        }
        return view;
    }

    @Override
    public void notifyDataSetChanged() {
        Log.d("onDataChange", "Sort data");
        super.notifyDataSetChanged();
    }

    public void SortKnocks(){
        Comparator<Knock> sortKnockByDate = new Comparator<Knock>() {
            @Override
            public int compare(Knock item1, Knock item2) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm");
                try {
                    Date oDate = sdf.parse(item1.getDate());
                    Date tDate = sdf.parse(item2.getDate());

                    long oDateValue = oDate.getTime(), tDateValue = tDate.getTime();

                    Log.d("sortKnock", item1.getContent());
                    Log.d("sortKnock", item2.getContent());
                    if(oDateValue > tDateValue) return -1;
                    else if(oDateValue < tDateValue) return +1;
                    else return 0;

                } catch (ParseException e) {
                    Log.d("sortKnock", e.getMessage());
                    return 0;
                }
            }
        } ;
        Collections.sort(knocks, sortKnockByDate);
    }
}
