/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transactions;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import transactions.filemanager.ArchiveReader;

/**
 *
 * @author pucks
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        
        // Reading file
        ArchiveReader reader = new ArchiveReader();
        // Returning List<String> lines
        List<String> lines = reader.readFile("fileteste");
        reader.executeLines(lines);
    }
    
}
