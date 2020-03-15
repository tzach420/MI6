package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Report;

public class GadgetAvailableEvent implements Event<Integer> {
    private String gadget;
    public GadgetAvailableEvent(String gadget){
        this.gadget=gadget;
    }
    public String getGadget(){
        return gadget;
    }
    public String toString(){return "GadgetAvailableEvent";}
}
