/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transactions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author pucks
 */
public class Tr_Manager {
    
    // Columns : 0 - id, 1 - timestamp, 2 - status
    Map<String, List<String>> matrix = new HashMap<>();
    List<String> timeAndStatus= new ArrayList<>();
    String active, committed, aborted;
    
    Tr_Manager() {
        active = "ACTIVE";
        committed = "COMMITTED";
        aborted = "ABORTED";
        
        List<String> timeAndStatus= new ArrayList<>();
        timeAndStatus.add("1");
        timeAndStatus.add(active);
        matrix.put("1", timeAndStatus);
    }
    
    String isActive(){
        return active;
    }
    
    String isCommitted(){
        return committed;
    }
    
    String isAborted(){
        return aborted;
    }
    
    String getTimestamp(String idTransaction){
        // Retorna o timestamp da transacao
        return matrix.get(idTransaction).get(0);
    }
    
//     void addTr(String idTransaction, String status){
        
//         timeAndStatus.add(Integer.toString(getTimestamp(idTransaction)));
//         timeAndStatus.add(status);
//         matrix.put(idTransaction,timeAndStatus);
//     }
    
}
