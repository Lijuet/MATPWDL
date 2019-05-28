package edu.skku.map.matpwdl;

import java.util.HashMap;
import java.util.Map;

public class ListViewRuleItem {
    public String content;
    public String day;
    public String member;
    public int repeat;
    public String rule_id;
    public String time;
    public String title;

    public ListViewRuleItem() { }

    public ListViewRuleItem(String content, String day, String member, int repeat, String rule_id, String time, String title) {
        this.title = title;
        this.content = content;
        this.day = day;
        this.time = time;
        this.member = member;
        this.rule_id = rule_id;
        this.repeat = repeat;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("title",title);
        result.put("content",content);
        result.put("day",day);
        result.put("time",time);
        result.put("member",member);
        result.put("rule_id",rule_id);
        result.put("repeat",repeat);
        return result;
    }

    public String getTitle(){
        return title;
    }
    public String getContent(){
        return content;
    }
    public String getWeek(){
        return day;
    }
    public String getTime(){
        return time;
    }
    public String getMember(){
        return member;
    }
    public String getRuleId() { return rule_id; }
    public int getRepeat() { return repeat; }

}
