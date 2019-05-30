/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transactions.lock;

import java.io.PrintStream;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author pucks
 */
public class Wait_Q {
    
    // columns : 0 - id transaction, 1 - type lock 
    Map<String, String> queue = new LinkedHashMap<>(0,20,false);
    PrintStream p = System.out;
    
    void addToQueue(String idTransaction, String typeLock){
        queue.put(idTransaction, typeLock);
        p.println("\n\nAdd to Queue: ");
        this.printQueue();
    }
    
    Map<String, String> getQueue(){
        return this.queue;
    }
    
    void removeFromQueue(String idTransaction){
        queue.remove(idTransaction);
        p.println("\n\nRemove from Queue: ");
        this.printQueue();
    }
    
    String getTypeLock(String idTransaction){
        return queue.get(idTransaction);
    }
    
    void printQueue(){
        p.println("||  ID TRANSACTION | TYPE LOCK  ||");
        for(String key: this.queue.keySet()){
            p.println("||  " + key + "  |  " + this.queue.get(key) + "  ||");
        }
    }
}
