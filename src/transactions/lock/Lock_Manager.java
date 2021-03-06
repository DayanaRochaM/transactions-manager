/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transactions.lock;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.w3c.dom.ls.LSException;

import transactions.Tr_Manager;

/**
 *
 * @author pucks
 */
public class Lock_Manager {
    
    // columns: 0 - id transaction, 1 - dado, 2 - lock type
    Map<String, List<String>> Lock_Table = new HashMap<>();
    // columns: 0 - dado, 1 - queue of who's waiting for access the dado
    Map<String, Wait_Q> dado_queue = new HashMap<String, Wait_Q>();
    
    Tr_Manager trManager;
    PrintStream p = System.out; // So pra simplificar o uso do print
    String shared, exclusive;
    
    public Lock_Manager(Tr_Manager trManager) {
        shared = "S";
        exclusive = "X";
        this.trManager = trManager;
    }
    
    String isShared(){
        return shared;
    }
    
    String isExclusive(){
        return exclusive;
    }

    
    public void LS(String idTransaction2, String dado){
       
        // Criar logo a fila do dado
        if(!this.dado_queue.containsKey(dado)){
            this.dado_queue.put(dado, new Wait_Q());
        }
        // Percorrendo tabela pra saber se existe algum tipo de lock sobre o 
        // dado desejado
        String idTransaction1;
        boolean processed = false; // Indica se a operacao foi já abortada ou pra fila
        Iterator<String> ids = this.Lock_Table.keySet().iterator();
        while(ids.hasNext() && !processed){           
            idTransaction1 = ids.next();
            List<String> dadoAndLock = this.Lock_Table.get(idTransaction1);
            if(dadoAndLock.get(0).equals(dado)){
                // Caso em que há outra transação com lock sobre o dado e se não são duas shareds
                if(!idTransaction1.equals(idTransaction2) && !dadoAndLock.get(1).equals(this.isShared())){
                    this.WaitDie(idTransaction1, idTransaction2, dado, this.isShared());
                }
                // Caso em que a transação já tem lock sobre o dado
                else{
                    List<String> newdadoAndLock = new ArrayList<>();
                    newdadoAndLock.add(dado);
                    newdadoAndLock.add(this.isShared());
                    this.Lock_Table.put(idTransaction2, newdadoAndLock);
                }
                this.getElementsFromQueue(dadoAndLock.get(0));
                this.getElementsFromQueue(dado);
                processed = true;
            }
        }
        // Caso em que não há transacao acessando o dado
        if(!processed){
            List<String> dadoAndLock = new ArrayList<>();
            dadoAndLock.add(dado);
            dadoAndLock.add(this.isShared());
            this.Lock_Table.put(idTransaction2, dadoAndLock);
        }  
        p.println("\n\nLS: ");
        this.printLockTable();
        
        this.getFromAllQueues();
    }
    
    public void LX(String idTransaction2, String dado){ 
        
        if(!this.dado_queue.containsKey(dado)){
            this.dado_queue.put(dado, new Wait_Q());
        }
        // Percorrendo tabela pra saber se existe algum tipo de lock sobre o 
        // dado desejado
        String idTransaction1;
        boolean processed = false; // Indica se a operacao foi já abortada ou pra fila
        Iterator<String> ids = this.Lock_Table.keySet().iterator();
        while(ids.hasNext() && !processed){           
            idTransaction1 = ids.next();
            List<String> dadoAndLock = this.Lock_Table.get(idTransaction1);
            if(dadoAndLock.get(0).equals(dado)){
                // Caso em que há outra transação com lock sobre o dado
                if(!idTransaction1.equals(idTransaction2)){
                   this.WaitDie(idTransaction1, idTransaction2, dado, this.isExclusive());
                }
                // Caso em que a transação já tem lock sobre o dado
                else{
                    List<String> newdadoAndLock = new ArrayList<>();
                    newdadoAndLock.add(dado);
                    newdadoAndLock.add(this.isExclusive());
                    this.Lock_Table.put(idTransaction2, newdadoAndLock);
                }
                processed = true;
            }
        }
        
        // Caso em que não há transacao acessando o dado
        if(!processed){
            List<String> dadoAndLock = new ArrayList<>();
            dadoAndLock.add(dado);
            dadoAndLock.add(this.isExclusive());
            this.Lock_Table.put(idTransaction2, dadoAndLock);
        }
        p.println("\n\nLX: ");
        this.printLockTable();
        
        this.getFromAllQueues();
    }

    public void U(String idTransaction){
        // Apaga o bloqueio da transacao sobre o item dado na Lock Table.
        String dado;
        if (this.Lock_Table.containsKey(idTransaction)){
            dado = this.Lock_Table.get(idTransaction).get(0);
            this.Lock_Table.remove(idTransaction);
            this.getElementsFromQueue(dado);
        }
        // Aqui deve-se pegar a lista do dado e pegar a proxima transacao
        //p.println("\n\nU: ");
        //this.printLockTable();
    }
    
    void getFromAllQueues(){
        for(String dado: this.dado_queue.keySet()){
            this.getElementsFromQueue(dado);
        }
    }
    
    public void getElementsFromQueue(String dado){
        
        Wait_Q waitQ = this.dado_queue.get(dado);
        if (waitQ != null) {
            Map<String, String> queue = waitQ.getQueue();
            boolean mustContinue = false;
            Map.Entry<String, String> transaction;
            String idTransaction;
            Iterator<Map.Entry<String, String>> transactions = queue.entrySet().iterator();
            while(transactions.hasNext() && !mustContinue){  
                transaction = transactions.next();
                idTransaction = transaction.getKey();
                //for(String idTransaction: transactions.keySet()){
                if(this.Lock_Table.containsKey(idTransaction)){
                    this.Lock_Table.get(idTransaction).set(1, transaction.getValue());
                }
                else if(this.checkCanRemoveFromQueue(dado, transaction.getValue())){
                    waitQ.removeFromQueue(idTransaction);
                    if (transaction.getValue().equals(this.isShared())){
                        this.LS(idTransaction, dado);
                    }else{
                        this.LX(idTransaction, dado);
                    }
                    //transactions = this.dado_queue.get(dado).getQueue().entrySet().iterator();
                }else{
                    mustContinue = false;
                }
            }
        }
    }
    
    boolean checkCanRemoveFromQueue(String dado, String typeLock){
        
        List<String> lockers = this.getAllLockers(dado);
        if(lockers.contains(this.isExclusive())||(lockers.contains(this.isShared()) && typeLock.equals(this.isExclusive()))){
            return false;
        }
        else{
            return true;
        }
    }
    
    List<String> getAllLockers(String dado){
        List<String> lockers = new ArrayList<>();
        for(List<String> locker: this.Lock_Table.values()){
            if(locker.get(0).equals(dado)){
                lockers.add(locker.get(1));
            }
        }
        return lockers;
    }
    
    void WaitDie(String idTransaction1, String idTransaction2, String dado, String lockType){
        // idTransaction1 é o que chegou antes
        
        // Chame o metodo getTimestamp() do Tr_Manager para
        // pegar o timestamp das duas transacoes e decidir o que fazer
        // Seja Ti a transacao já com locked, e Tj a que acabou de chegar,
        // a decisao vai de acordo com as seguintes regras:
        // 1 - Se o timestamp Ti < timestamp Tj, Tj é abortada (ABORTED)
        // 2 - Caso contrario, Tj vai pra Wait_Q do dado
        if(this.trManager.getTimestamp(idTransaction1) < this.trManager.getTimestamp(idTransaction2)){
            this.trManager.toAbort(idTransaction2);
            p.print("\n\nTransação " + idTransaction2 + " abortada!");
        }else{
            this.dado_queue.get(dado).addToQueue(idTransaction2, lockType);
        }
        this.U(idTransaction2);
    }

    void printLockTable(){
        p.println("||  ID TRANSACTION  |   DADO  | LOCK TYPE  ||");
        for(String key: this.Lock_Table.keySet()){
            p.println("||  " + key + "  |  " + this.Lock_Table.get(key).get(0) + "  |  " + this.Lock_Table.get(key).get(1) + "  ||");
        }
    }
    
}
