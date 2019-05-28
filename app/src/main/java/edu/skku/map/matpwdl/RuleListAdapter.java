package edu.skku.map.matpwdl;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class RuleListAdapter extends BaseAdapter{
    LayoutInflater inflater;
    Context context;
    private ArrayList<ListViewRuleItem> ruleList = new ArrayList<>();
    private DatabaseReference rPostReference = FirebaseDatabase.getInstance().getReference();

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

        final ListViewRuleItem item = ruleList.get(i);

        TextView title = view.findViewById(R.id.textView_ruleTitle);
        TextView content = view.findViewById(R.id.textView_ruleContent);
        TextView week = view.findViewById(R.id.textView_ruleWeek);
        TextView time = view.findViewById(R.id.textView_ruleTime);
        TextView member = view.findViewById(R.id.textView_ruleMember);

        title.setText(item.getTitle());
        content.setText((item.getContent()+"\n"));
        week.setText(("*요일: "+item.getWeek()));
        time.setText(("*시간: "+item.getTime()));
        member.setText(("*멤버: "+item.getMember()));

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
                String rule_id = item.rule_id;
                Intent intent = new Intent(context,AddEditRuleActivity.class);
                intent.putExtra("rule_id",rule_id);
                intent.putExtra("content",item.content);
                intent.putExtra("day",item.day);
                intent.putExtra("member",item.member);
                intent.putExtra("time",item.time);
                intent.putExtra("title",item.title);
                intent.putExtra("repeat",item.repeat);
                context.startActivity(intent);
            }
        });
        deleteBtn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(context);

                alert_confirm.setMessage("삭제하시겠습니까?").setCancelable(false).setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 'YES'
                                Map<String,Object> childUpdates = new HashMap<>();
                                childUpdates.put("ROOM/room1/rule/rule"+item.rule_id , null);
                                rPostReference.updateChildren(childUpdates);
                            }
                        }).setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 'No'
                                return;
                            }
                        });
                AlertDialog alert = alert_confirm.create();
                alert.show();
            }
        });

        return view;
    }
}
