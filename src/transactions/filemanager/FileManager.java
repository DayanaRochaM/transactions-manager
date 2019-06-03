/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transactions.filemanager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import transactions.Tr_Manager;
/**
 *
 * @author pucks
 */
public class FileManager {
    
    PrintStream p = System.out; // So pra simplificar o uso do print
    Tr_Manager trManager = new Tr_Manager();
    public List<String> readFile(String filename) throws FileNotFoundException, IOException{
        
        List<String> lines = new ArrayList<>();
        String line = "";

        String path = new File("").getAbsolutePath();
        FileReader file = new FileReader(path + "/src/transactions/" + filename);
        BufferedReader bufferedArchive = new BufferedReader(file);
     
        while(line != null){
            line = bufferedArchive.readLine();
            if(line != null){
                lines.add(line);
            }
        }
        
        return lines;
    }
    
    public void executeLine(String line){
        // As possiveis operacoes sao:
        // • BT(A): inicia uma transacao com identiﬁcador de valor A (ACTIVE)
        // • R1(x): transacao 1 deseja ler o item x 
        // • W1(x): transacao 1 deseja escrever o item x 
        // • CM(A): transacao A confirma suas operacoes (COMMITED)
        
        // Pegando cada operacao
        List<String> commands = new ArrayList<>();
        commands =  Arrays.asList(line.split(","));
        
        for(String command: commands){
            // FALTA QUESTAO DO TIMESTAMP
            
            // BEGIN TRANSACTION
            if (command.startsWith("BT")){
                // getting idTransaction
                String idTransaction = command.split("[\\(\\)]")[1];
                trManager.BT(idTransaction);
            }
            
            // READ
            else if (command.startsWith("R")){
                String [] parts = command.split("[\\(\\)]");
                // getting idTransaction
                String idTransaction = parts[0].replace("R","");
                // getting dado
                String dado = parts[1];
                
                this.trManager.R(idTransaction, dado);
            }
            
            // WRITE
            else if (command.startsWith("W")){
                String [] parts = command.split("[\\(\\)]");
                // getting idTransaction
                String idTransaction = parts[0].replace("W","");
                // getting dado
                String dado = parts[1];
                
                this.trManager.W(idTransaction, dado);
            }
            
            // COMMIT
            else if (command.startsWith("CM")){
                String [] parts = command.split("[\\(\\)]");
                // getting idTransaction
                String idTransaction = parts[1];
                
                this.trManager.CM(idTransaction);
            }
            
            else{
                p.println("OPERACAO IRRECONHECIDA!");
            }
        }
    }
    
    public void executeLines(List<String> lines){
        // Caso geral que executa a funcao acima para toda linha
        for(String line: lines){
            this.executeLine(line);
            this.trManager = new Tr_Manager();
        }
    }
}
