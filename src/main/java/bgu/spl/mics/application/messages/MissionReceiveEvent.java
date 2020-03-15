package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import  bgu.spl.mics.application.passiveObjects.*;

public class MissionReceiveEvent implements Event<Report> {
    private MissionInfo missionInfo;
    private boolean terminateHappened=false;
    public MissionReceiveEvent(MissionInfo info){
        missionInfo=info;
    }

    public MissionInfo getMissionInfo() {
        return missionInfo;
    }
    public String toString(){return "MissionRecievedEvent";}
    public void terminateHappened(){terminateHappened=true;}
    public boolean getTerminateHappened(){return terminateHappened;}
}
