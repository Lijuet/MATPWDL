package edu.skku.map.matpwdl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//Knock Adpater에 넣을 리스트뷰 아이템입니다.

public class Knock {
    private String title;
    private String content;
    private String sender;
    private String receiver;
    private String date;
    private int count;
    private int read;

    public Knock () { }

    public Knock(String title, String content, String sender, String receiver, String date) {
        this.title = title;
        this.content = content;
        this.sender = sender;
        this.receiver = receiver;
        this.date = date;
    }

    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getSender() { return sender; }
    public String getReceiver() { return receiver; }
    public String getDate() {return date; }
    public int getCount() { return  count; }
    public int getRead() { return read; }

    public void setTitle(String _title){ this.title = _title; }
    public void setContent(String _content){ this.content = _content; }
    public void setSender(String _sender){ this.sender = _sender; }
    public void setReceiver(String _receiver){ this.receiver = _receiver; }
    public void setDate(String _date){ this.date = _date; }
    public void setCount(int _count){ this.count = _count; }
    public void setRead(int _read){ this.read = _read; }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", title);
        result.put("content", content);
        result.put("sender", sender);
        result.put("receiver", receiver);
        result.put("date", date);
        result.put("count", count);
        result.put("read", read);
        return result;
    }
}
