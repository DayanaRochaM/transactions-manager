/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transactions.lock;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author pucks
 */
public class Wait_Q {
    
    // columns : 0 - id transaction, 1 - type lock 
    Map<String, String> cache = new LinkedHashMap<>(0,0,false);
    
    void addToQueue(){
        // Essa funcao adiciona uma linha ao Map
    }
    
    void removeFromQueue(){
    }
}
