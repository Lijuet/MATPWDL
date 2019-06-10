package edu.skku.map.matpwdl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyInformation implements Serializable {
    private String name;
    private String roomID;
    private String memberid;
    private String ID;
    private String PW;
    private String status;
    private Map<Integer,String> roommatessID;//test case

    public String getMemberid( ){ return memberid;}
    public String getName() { return name; }
    public String getRoomID() { return roomID; }
    public String getStatus() { return status; }
    public Map<Integer,String> getRoommatessID() { return roommatessID; }
    public String getID(){ return ID;}
    public String getPW(){ return PW; }

    public void setMemberid(String _memberid){this.memberid = _memberid;}
    public void setName(String _name) { this.name = _name; }
    public void setRoomID(String _roomID) { this.roomID = _roomID; }
    public void setStatus(String _status) { this.status = _status; }
    public void setRoommatessID(Map<Integer,String> _membersID) { this.roommatessID = _membersID;}
    public void addRoommatessID(Integer memberID, String name) { this.roommatessID.put(memberID, name); }
    public void setID(String _ID){ this.ID = _ID; }
    public void setPW(String _PW){ this.PW = _PW; }


    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("roomID", roomID);
        result.put("memberid", memberid);
        result.put("status", status);
        return result;
    }
}
