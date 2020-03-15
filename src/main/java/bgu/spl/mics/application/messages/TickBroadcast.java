package bgu.spl.mics.application.messages;
import bgu.spl.mics.Broadcast;

public class TickBroadcast implements Broadcast {
    private int timeTick;

    public TickBroadcast(int tick){
        this.timeTick=tick;
    }

    public int getTimeTick(){
        return timeTick;
    }
    public String toString(){return "TickBroadcast";}
}
