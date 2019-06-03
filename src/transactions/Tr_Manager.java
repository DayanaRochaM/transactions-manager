/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transactions;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import transactions.lock.Lock_Manager;

/**
 *
 * @author pucks
 */
public class Tr_Manager {
    
    PrintStream p = System.out; // So pra simplificar o uso do print
    // Columns : 0 - id, 1 - timestamp, 2 - status
    Map<String, List<Object>> transactions = new HashMap<>();
    Lock_Manager lockManager = new Lock_Manager(this);
    String active, committed, aborted;
    int time;
    
    public Tr_Manager() {
        active = "ACTIVE";
        committed = "COMMITTED";
        aborted = "ABORTED";
        time = 1;
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
    
    public void toAbort(String idTransaction){
        if(this.transactions.containsKey(idTransaction)){
            this.transactions.get(idTransaction).set(1, this.isAborted());
            this.lockManager.U(idTransaction);
        }else{
            p.println("TRANSACAO NAO REGISTRADA!");
        }
    }
    
    public int getTimestamp(String idTransaction){
        // Retorna o timestamp da transacao
        return (int) this.transactions.get(idTransaction).get(0);
    }
    
    public void BT(String idTransaction){ // Begin Transaction
        
        List<Object> timeAndStatus= new ArrayList<>();
        timeAndStatus.add(time);
        timeAndStatus.add(this.isActive());
        this.transactions.put(idTransaction, timeAndStatus);
        time++;
        p.println("\n\nBegin Translate: ");
        this.printTransactions();
    }
    
    public void R(String idTransaction, String dado){ // Read dado
        if(this.transactions.containsKey(idTransaction)){ // Checking if idTransaction is already registred
            // Checking if need to transform to active
            this.transactions.get(idTransaction).set(1, this.isActive());
            lockManager.LS(idTransaction, dado);
        }else{
            p.println("Operacao invalida! Transacao nao registrada!");
        }
//        p.println("\n\nRead: ");
//        this.printTransactions();
    }
    
    public void W(String idTransaction, String dado){ // Write dado
        if(this.transactions.containsKey(idTransaction)){ // Checking if idTransaction is already registred
            this.transactions.get(idTransaction).set(1, this.isActive());
            lockManager.LX(idTransaction, dado);
        }else{
            p.println("Operacao invalida! Transacao nao registrada!");
        }
//        p.println("\n\nWrite: ");
//        this.printTransactions();
    }
    
    public void CM(String idTransaction){
        if(this.transactions.containsKey(idTransaction)){ // Checking if idTransaction is already registred
            this.transactions.get(idTransaction).set(1, this.isCommitted());
            this.lockManager.U(idTransaction);
        }else{
            p.println("Operacao invalida! Transacao nao registrada!");
        }
        p.println("\n\nCommit: ");
        this.printTransactions();
    }
    
    void printTransactions(){
        p.println("||  ID   |   TIMESTAMP  | STATUS  ||");
        for(String key: this.transactions.keySet()){
            p.println("||  " + key + "  |  " + this.transactions.get(key).get(0) + "  |  " + this.transactions.get(key).get(1) + "  ||");
        }
    }

}
