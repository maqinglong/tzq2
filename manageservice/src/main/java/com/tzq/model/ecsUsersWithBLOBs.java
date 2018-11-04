package com.tzq.model;

public class ecsUsersWithBLOBs extends ecsUsers {
    private String aiteId;

    private String aiteIdWb;

    private String aiteIdAl;

    private String aiteIdRr;

    private String mainInstruction;

    private String otherInstructions;

    private String bestTime;

    public String getAiteId() {
        return aiteId;
    }

    public void setAiteId(String aiteId) {
        this.aiteId = aiteId == null ? null : aiteId.trim();
    }

    public String getAiteIdWb() {
        return aiteIdWb;
    }

    public void setAiteIdWb(String aiteIdWb) {
        this.aiteIdWb = aiteIdWb == null ? null : aiteIdWb.trim();
    }

    public String getAiteIdAl() {
        return aiteIdAl;
    }

    public void setAiteIdAl(String aiteIdAl) {
        this.aiteIdAl = aiteIdAl == null ? null : aiteIdAl.trim();
    }

    public String getAiteIdRr() {
        return aiteIdRr;
    }

    public void setAiteIdRr(String aiteIdRr) {
        this.aiteIdRr = aiteIdRr == null ? null : aiteIdRr.trim();
    }

    public String getMainInstruction() {
        return mainInstruction;
    }

    public void setMainInstruction(String mainInstruction) {
        this.mainInstruction = mainInstruction == null ? null : mainInstruction.trim();
    }

    public String getOtherInstructions() {
        return otherInstructions;
    }

    public void setOtherInstructions(String otherInstructions) {
        this.otherInstructions = otherInstructions == null ? null : otherInstructions.trim();
    }

    public String getBestTime() {
        return bestTime;
    }

    public void setBestTime(String bestTime) {
        this.bestTime = bestTime == null ? null : bestTime.trim();
    }
}