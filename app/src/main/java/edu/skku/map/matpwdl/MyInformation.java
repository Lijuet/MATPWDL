package edu.skku.map.matpwdl;

public class MyInformation {
    private String name;
    private String roomID;
    private String memberID;//todo ID 말고 memberID( 논의해야할 점)
    private String status;

    public String getName() { return name; }
    public String getRoomID() { return roomID; }
    public String getMemberID() { return memberID; }
    public String getStatus() { return status; }

    public void setName(String _name) { this.name = _name; }
    public void setRoomID(String _roomID) { this.roomID = _roomID; }
    public void setMemberID(String _memberID ) { this.memberID = _memberID; }
    public void setStatus(String _status) { this.status = _status; }
}
