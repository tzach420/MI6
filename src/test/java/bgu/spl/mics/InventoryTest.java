package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class InventoryTest {
    private Inventory inventory;
    private String[] gadgets={"a","b","c","d"};
    @BeforeEach
    public void setUp(){
        inventory=Inventory.getInstance();
        inventory.load(gadgets);
    }


    @Test
    public void getInstanceTest(){
        Inventory j=Inventory.getInstance();
        assertEquals(inventory,j,"Failed to create only 1 instance of inventory");
    }
    @Test
    public void loadTest(){
        for(String x:gadgets)
            assertEquals(true,inventory.getItem(x),"gadget "+x+" should be in the list");

    }
    @Test
    public void getItemTest(){
        assertEquals(true,inventory.getItem("a"),"a should be in the list.");
        assertEquals(false,inventory.getItem("a"),"a should not be in the list");
        assertEquals(true,inventory.getItem("b"),"b should be in the list.");
        assertEquals(false,inventory.getItem("b"),"b should not be in the list.");
        assertEquals(true,inventory.getItem("c"),"c should be in the list.");
        assertEquals(false,inventory.getItem("c"),"c should  not in the list.");
    }


}
