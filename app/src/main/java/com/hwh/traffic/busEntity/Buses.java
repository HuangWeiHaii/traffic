
package com.hwh.traffic.busEntity;


public class Buses {

    private long busId;
    private String busNo;
    private double lng;
    private double lat;
    private int passedSeqNo;
    private int nextSeqNo;
    private String nextStation;
    private int nextDistance;
    private boolean isArrive;
    private boolean lastBus;
    private int targetDistance;
    private int targetSeconds;
    private int targetStopCount;
    private boolean sign;
    private int type;
    public void setBusId(long busId) {
         this.busId = busId;
     }
     public long getBusId() {
         return busId;
     }

    public int getNextDistance() {
        return nextDistance;
    }

    public void setNextDistance(int nextDistance) {
        this.nextDistance = nextDistance;
    }

    public boolean isArrive() {
        return isArrive;
    }

    public void setArrive(boolean arrive) {
        isArrive = arrive;
    }

    public boolean isLastBus() {
        return lastBus;
    }

    public boolean isSign() {
        return sign;
    }

    public void setBusNo(String busNo) {
         this.busNo = busNo;
     }
     public String getBusNo() {
         return busNo;
     }

    public void setLng(double lng) {
         this.lng = lng;
     }
     public double getLng() {
         return lng;
     }

    public void setLat(double lat) {
         this.lat = lat;
     }
     public double getLat() {
         return lat;
     }

    public void setPassedSeqNo(int passedSeqNo) {
         this.passedSeqNo = passedSeqNo;
     }
     public int getPassedSeqNo() {
         return passedSeqNo;
     }

    public void setNextSeqNo(int nextSeqNo) {
         this.nextSeqNo = nextSeqNo;
     }
     public int getNextSeqNo() {
         return nextSeqNo;
     }

    public void setNextStation(String nextStation) {
         this.nextStation = nextStation;
     }
     public String getNextStation() {
         return nextStation;
     }

    public void setIsArrive(boolean isArrive) {
         this.isArrive = isArrive;
     }
     public boolean getIsArrive() {
         return isArrive;
     }

    public void setLastBus(boolean lastBus) {
         this.lastBus = lastBus;
     }
     public boolean getLastBus() {
         return lastBus;
     }

    public void setTargetDistance(int targetDistance) {
         this.targetDistance = targetDistance;
     }
     public int getTargetDistance() {
         return targetDistance;
     }

    public void setTargetSeconds(int targetSeconds) {
         this.targetSeconds = targetSeconds;
     }
     public int getTargetSeconds() {
         return targetSeconds;
     }

    public void setTargetStopCount(int targetStopCount) {
         this.targetStopCount = targetStopCount;
     }
     public int getTargetStopCount() {
         return targetStopCount;
     }

    public void setSign(boolean sign) {
         this.sign = sign;
     }
     public boolean getSign() {
         return sign;
     }

    public void setType(int type) {
         this.type = type;
     }
     public int getType() {
         return type;
     }


}