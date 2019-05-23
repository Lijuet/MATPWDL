package edu.skku.map.matpwdl;

public class Knock {
    private String title;
    private String content;
    private String sender;
    private String receiver;
    private int count;
    private int read;

    public Knock () { }

    public Knock(String title, String content, String sender, String receiver) {
        this.title = title;
        this.content = content;
        this.sender = sender;
        this.receiver = receiver;
    }

    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getSender() { return sender; }
    public String getReceiver() { return receiver; }
    public int getCount() { return  count; }
    public int getRead() { return read; }

    public void setTitle(String _title){ this.title = _title; }
    public void setContent(String _content){ this.content = _content; }
    public void setSender(String _sender){ this.sender = _sender; }
    public void setReceiver(String _receiver){ this.receiver = _receiver; }
    public void setCount(int _count){ this.count = _count; }
    public void setRead(int _read){ this.read = _read; }
}
