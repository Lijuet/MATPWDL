package edu.skku.map.matpwdl;

import java.util.HashMap;
import java.util.Map;

public class Roommatelistitem{
    private String memberid;
    private String name;
    private String status;
    private String roomID;

    public Roommatelistitem(){
        //Constructor
    }

    public String getMemberid() { return memberid; }
    public String getName(){ return name;}
    public String getStatus(){ return status; }
    public String getRoomID(){ return roomID; }

    public void setMemberid(String _memberid){ this.memberid = _memberid; }
    public void setName(String _name){ this.name = _name; }
    public void setStatus(String _status){ this.status = _status; }
    public void setRoomID(String _roomID){ this.roomID = _roomID; }


    public Roommatelistitem(String _memberid){
        this.memberid = _memberid;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("memberid", memberid);
        result.put("name", name);
        result.put("roomID", roomID);
        result.put("status",status);
        return result;
    }

}
