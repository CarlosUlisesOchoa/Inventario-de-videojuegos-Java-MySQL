/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a5_t1_ochoaulises;

import java.awt.HeadlessException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import shaneDialogs.Utils;

public class FileChooser {
 
    //Declare Variable
    JFileChooser fileChooser = new JFileChooser();
    FileNameExtensionFilter filter;
    
    StringBuilder sb = new StringBuilder();
    
    File defaultDir = null;
    
    public FileChooser()
    {
        //this.filter = new FileNameExtensionFilter();
           
    }
    

    public File getDirToSave(String default_File_Name, String[] filters) {
        File dir;
        this.filter = new FileNameExtensionFilter(String.format("Archivos (%s)", Utils.stringArrayToString(filters)), filters);
        fileChooser.setSelectedFile(new File(default_File_Name)); //set a default filename (this is where you default extension first comes in)
        fileChooser.setFileFilter(filter); //Set an extension filter
        int retrival = fileChooser.showSaveDialog(null);
        if (retrival == JFileChooser.APPROVE_OPTION) {
            dir = fileChooser.getSelectedFile();
            String[] dirS = dir.getName().split("\\.");

            if(dirS.length > 0){
                for(String i : filters){
                    if(dirS[dirS.length-1].equalsIgnoreCase(i)) return dir;
                }
            }
        }
        return null;
    }
    private boolean checkFilter(File archivo)
    {
        String[] filtros = this.filter.getExtensions();
        
        for (String filtro : filtros) 
        {
            if(!archivo.getName().endsWith(filtro)) return false;
        }
        return true;
    }

    File openFile(String filterDescription, String[] filters)
    {
        this.filter = new FileNameExtensionFilter(filterDescription, filters);
        
        fileChooser.setFileFilter(filter);
        
        
        File fileSelected = null;
        if(defaultDir != null) fileChooser.setCurrentDirectory(defaultDir);
        try
        {
            if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
            {

                fileSelected = fileChooser.getSelectedFile();

                if(checkFilter(fileSelected))
                {

                     Scanner input = new Scanner(fileSelected);

                     while(input.hasNext())
                     {
                         sb.append(input.nextLine());
                         sb.append("\n");
                     }

                     input.close();
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "Este archivo no es permitido", "Error", JOptionPane.ERROR_MESSAGE);
                    fileSelected = null;
                }
            }
        } catch (HeadlessException | FileNotFoundException e) {}
       return fileSelected;
    }
}
