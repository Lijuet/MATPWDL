package edu.skku.map.matpwdl;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Map;

public class KnockDetailActivity extends AppCompatActivity {
    MyInformation myInfo;
    String mode;
    ArrayList<Knock> knocks;
    Button btnSearchKnock;
    AutoCompleteTextView etSearchKnockContent;
    ListView lvDetailKnock;
    private ArrayList<Knock> searchList;
    KnockAdapter adapter;
    ArrayList<String> roommates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knock_detail);

        Intent intent = getIntent();
        myInfo = (MyInformation) intent.getSerializableExtra("myInfo");
        mode = intent.getStringExtra("mode");
        knocks = intent.getParcelableArrayListExtra("knocks");

        btnSearchKnock = findViewById(R.id.button_SearchKnock);
        etSearchKnockContent = findViewById(R.id.editText_SearchKnock);
        lvDetailKnock = findViewById(R.id.listView_KnockDetailList);

        settingList();
        etSearchKnockContent.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line , roommates));
        adapter = new KnockAdapter(this, searchList, myInfo,3)/*detailMode*/;
        lvDetailKnock.setAdapter(adapter);

        btnSearchKnock.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchKey = etSearchKnockContent.getText().toString();
                search(searchKey);
            }
        });
    }

    public void search(String charText) {
        // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
        searchList.clear();

        // 문자 입력이 없을때는 모든 데이터를 보여준다.
        if (charText.length() == 0) {
            searchList.addAll(knocks);
        }
        // 문자 입력을 할때..
        else
        {
            // 리스트의 모든 데이터를 검색한다.
            for(int i = 0; i < knocks.size(); i++)
            {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (myInfo.getRoommatessID().get(Integer.valueOf(knocks.get(i).getReceiver())).contains(charText.trim()))
                {
                    // 검색된 데이터를 리스트에 추가한다.
                    searchList.add(knocks.get(i));
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
    private void settingList(){
        roommates = new ArrayList<String>();
        searchList = new ArrayList<Knock>();
        searchList.addAll(knocks);
        for(Integer key : myInfo.getRoommatessID().keySet()){
            roommates.add(myInfo.getRoommatessID().get(key));
        }
    }
}
