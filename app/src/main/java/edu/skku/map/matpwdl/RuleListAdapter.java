package edu.skku.map.matpwdl;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.CheckedOutputStream;

public class RuleListAdapter extends BaseAdapter{
    LayoutInflater inflater;
    Context context;
    private ArrayList<ListViewRuleItem> ruleList = new ArrayList<>();

    public RuleListAdapter(Context context, ArrayList<ListViewRuleItem> ruleList){
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.ruleList = ruleList;
        this.context = context;
    }

    @Override
    public int getCount(){ return ruleList.size(); }

    @Override
    public ListViewRuleItem getItem(int i) { return ruleList.get(i); }

    @Override
    public long getItemId(int i){ return i; }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup){
        if(view==null){
            view = inflater.inflate(R.layout.listview_ruleitem, viewGroup, false);
        }

        ListViewRuleItem item = ruleList.get(i);

        TextView title = view.findViewById(R.id.textView_ruleTitle);
        TextView content = view.findViewById(R.id.textView_ruleContent);
        TextView week = view.findViewById(R.id.textView_ruleWeek);
        TextView time = view.findViewById(R.id.textView_ruleTime);
        TextView member = view.findViewById(R.id.textView_ruleMember);

        title.setText(item.getTitle());
        content.setText(item.getContent());
        week.setText(item.getWeek());
        time.setText(item.getTime());
        member.setText(item.getMember());

        Button shareBtn = view.findViewById(R.id.button_shareRule);
        Button editBtn = view.findViewById(R.id.button_editRule);
        Button deleteBtn = view.findViewById(R.id.button_deleteRule);

        shareBtn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });
        editBtn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                //String rule_id;
                Intent intent = new Intent(context,AddEditRuleActivity.class);
                //intent.putExtra("rule_id",rule_id);
                context.startActivity(intent);
            }
        });
        deleteBtn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }
}
