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

    public Roommatelistitem(String id){
        this.id = id;
    }
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);

        return result;
    }
}
