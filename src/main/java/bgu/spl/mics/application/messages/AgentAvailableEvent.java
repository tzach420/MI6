package bgu.spl.mics.application.messages;
import java.util.List;
import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.passiveObjects.Report;

public class AgentAvailableEvent implements Event<Object []> {
    private List<String> serials;
    private int timeToSend;
    private Future<Boolean> agentsFinishedMissionFuture;

    public AgentAvailableEvent(List<String> serials, int time,Future<Boolean> future){
        this.serials=serials;
        timeToSend=time;
        agentsFinishedMissionFuture=future;
    }
    public List<String> getSerials(){ return serials;}
    //public int getTimeTickToTerminate(){return  timeTickToTerminate;}
    public int getTimeToSend(){return timeToSend;}
    public String toString(){return "AgentAvailableEvent";}
    public Future<Boolean> isAgentsFinishedMission(){return agentsFinishedMissionFuture;}
}
