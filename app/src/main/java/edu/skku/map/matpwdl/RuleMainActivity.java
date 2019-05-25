package edu.skku.map.matpwdl;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class RuleMainActivity extends AppCompatActivity {

    FloatingActionButton fab_addRule;
    ListView listView;
    ArrayList<ListViewRuleItem> rules = new ArrayList<>();
    RuleListAdapter ruleListAdapter;
    ListViewRuleItem testRule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule_main);

        listView = findViewById(R.id.ListView_rules);

        fab_addRule = findViewById(R.id.floatingActionButton_addrule);
        fab_addRule.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                //String rule_id;
                Intent intent = new Intent(RuleMainActivity.this,AddEditRuleActivity.class);
                //intent.putExtra("rule_id",rule_id);
                startActivity(intent);
            }
        });

        testRule = new ListViewRuleItem();
        testRule.setInformation("title","hello","mon","10","me","3");
        rules.add(testRule);

        ruleListAdapter = new RuleListAdapter(this, rules);
        listView.setAdapter(ruleListAdapter);
        //
    }
}
