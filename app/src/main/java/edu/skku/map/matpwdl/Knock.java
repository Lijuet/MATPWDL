package edu.skku.map.matpwdl;

import java.util.HashMap;
import java.util.Map;

//Knock Adpater에 넣을 리스트뷰 아이템입니다.

public class Knock {
    private String content;
    private String sender;
    private String receiver;
    private String date;
    private String knockID;
    private int count;
    private int read;

    public Knock(){}
    public Knock(String _content, String _sender, String _receiver, String _date, String _knockID) {
        this.content = _content;
        this.sender = _sender;
        this.receiver = _receiver;
        this.date = _date;
        this.knockID = _knockID;
        this.count= 0;
    }

    public String getContent() { return content; }
    public String getSender() { return sender; }
    public String getReceiver() { return receiver; }
    public String getDate() {return date; }
    public String getKnockID(){ return knockID; }
    public int getCount() { return  count; }
    public int getRead() { return read; }

    public void setContent(String _content){ this.content = _content; }
    public void setSender(String _sender){ this.sender = _sender; }
    public void setReceiver(String _receiver){ this.receiver = _receiver; }
    public void setDate(String _date){ this.date = _date; }
    public void setKnockID(String _knockID){ this.knockID = _knockID; }
    public void setCount(int _count){ this.count = _count; }
    public void setRead(int _read){ this.read = _read; }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("content", content);
        result.put("sender", sender);
        result.put("receiver", receiver);
        result.put("date", date);
        result.put("count", count);
        result.put("read", read);
        result.put("knockID", knockID);

        return result;
    }
}
