package edu.skku.map.matpwdl;

import java.util.HashMap;
import java.util.Map;

public class Roommatelistitem{
    public String id;
    public String roommatename;
    public String status;
    public String roomid;

    public Roommatelistitem(){
        //Constructor
    }
    public Roommatelistitem(String id, String roommatename, String status, String roomid){
        this.id = id;
        this.roommatename = roommatename;
        this.status = roommatename;
        this.roomid = roomid;
    }
    public Roommatelistitem(String id, String status){
        this.id = id;
        this.status = status;
    }
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("status", status);

        return result;
    }
}
