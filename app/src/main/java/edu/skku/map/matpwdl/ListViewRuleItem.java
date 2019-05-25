package edu.skku.map.matpwdl;

public class ListViewRuleItem {
    private String title;
    private String content;
    private String week;
    private String time;
    private String member;
    private String rule_id;

    public void setInformation(String title, String content, String week, String time, String member, String rule_id) {
        this.title = title;
        this.content = content;
        this.week = week;
        this.time = time;
        this.member = member;
        this.rule_id = rule_id;
    }

    public String getTitle(){
        return title;
    }
    public String getContent(){
        return content;
    }
    public String getWeek(){
        return week;
    }
    public String getTime(){
        return time;
    }
    public String getMember(){
        return member;
    }
    public String getRuleId() { return rule_id; }
}
