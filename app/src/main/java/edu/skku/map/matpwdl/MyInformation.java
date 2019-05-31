package edu.skku.map.matpwdl;

import java.io.Serializable;
import java.util.ArrayList;

public class MyInformation implements Serializable {
    private String name;
    private String roomID;
    private String myID;//todo ID 말고 memberID( 논의해야할 점)
    private String status;
    private ArrayList<String> membersID;//test case

    public String getName() { return name; }
    public String getRoomID() { return roomID; }
    public String getMyID() { return myID; }
    public String getStatus() { return status; }
    public ArrayList<String> getMembersID() { return membersID; }

    public void setName(String _name) { this.name = _name; }
    public void setRoomID(String _roomID) { this.roomID = _roomID; }
    public void setMyID(String _myID ) { this.myID = _myID; }
    public void setStatus(String _status) { this.status = _status; }
    public void setMembersID(ArrayList<String> _membersID) { this.membersID = _membersID;}
}
