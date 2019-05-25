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
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author pucks
 */
public class ArchiveReader {
    
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
    
    void executeLine(String line){
        // Quebrar linha em operacoes a serem executadas.
        // É preciso identificar o que cada operacao vai realizar (de preferencia
        // usar use case e "contains" pra string, remover essa parte da string e pegar
        // o resto pra ser o id da transacao)
        //
        // As possiveis operacoes sao:
        // • BT(A): inicia uma transacao com identiﬁcador de valor A (ACTIVE)
        // • R1(x): transacao 1 deseja ler o item x 
        // • W1(x): transacao 1 deseja escrever o item x 
        // • CM(A): transacao A confirma suas operacoes (COMMITED)
        //
        // DICA: procurar funcao parecida com o "split" do python pra dividir
        // cada linha em comando. 
    }
    
    void executeLines(List<String> lines){
        // Caso geral que executa a funcao acima para toda linha
    }
}
