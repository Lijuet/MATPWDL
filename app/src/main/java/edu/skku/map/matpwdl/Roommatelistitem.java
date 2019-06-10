package edu.skku.map.matpwdl;

import java.util.HashMap;
import java.util.Map;

public class Roommatelistitem{
    private String memberid;
    private String name;
    private String status;
    private String roomID;
    private String ID;
    private String PW;

    public Roommatelistitem(){
        //Constructor
    }

    public Roommatelistitem(String ID, String PW, String memberid, String name, String roomID, String status){
        this.ID = ID;
        this.PW = PW;
        this.memberid = memberid;
        this.name = name;
        this.roomID = roomID;
        this.status = status;
    }

    public String getMemberid() { return memberid; }
    public String getName(){ return name;}
    public String getStatus(){ return status; }
    public String getRoomID(){ return roomID; }
    public String getPW(){return PW;}
    public String getID(){ return ID; }

    public void setMemberid(String _memberid){ this.memberid = _memberid; }
    public void setName(String _name){ this.name = _name; }
    public void setStatus(String _status){ this.status = _status; }
    public void setRoomID(String _roomID){ this.roomID = _roomID; }
    public void setPW(String PW){this.PW = PW;}
    public void setID(String _ID){ this.roomID = _ID; }


    public Roommatelistitem(String _memberid){
        this.memberid = _memberid;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("ID",ID);
        result.put("PW",PW);
        result.put("memberid", memberid);
        result.put("name", name);
        result.put("roomID", roomID);
        result.put("status",status);
        result.put("ID", ID);
        return result;
    }

}
