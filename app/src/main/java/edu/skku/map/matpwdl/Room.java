package edu.skku.map.matpwdl;

import java.util.HashMap;
import java.util.Map;

public class Room {
    private String room_id;
    private String room_name;

    public Room(){

    }
    public Room(String room_id, String room_name){
        this.room_id = room_id;
        this.room_name =room_name;
    }

    public String getRoom_id(){return room_id;}
    public String getRoom_name(){return room_name;}

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("room_id",room_id);
        result.put("room_name",room_name);
        return result;
    }

}
