package edu.skku.map.matpwdl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class MyInformation implements Serializable {
    private String name;
    private String roomID;
    private String memberid;
    private String myID;
    private String status;
    private Map<Integer,String> roommatessID;//test case

    public String getMemberid( ){ return memberid;}
    public String getName() { return name; }
    public String getRoomID() { return roomID; }
    public String getMyID() { return myID; }
    public String getStatus() { return status; }
    public Map<Integer,String> getRoommatessID() { return roommatessID; }

    public void setMemberid(String _memberid){this.memberid = _memberid;}
    public void setName(String _name) { this.name = _name; }
    public void setRoomID(String _roomID) { this.roomID = _roomID; }
    public void setMyID(String _myID ) { this.myID = _myID; }
    public void setStatus(String _status) { this.status = _status; }
    public void setRoommatessID(Map<Integer,String> _membersID) { this.roommatessID = _membersID;}
}
