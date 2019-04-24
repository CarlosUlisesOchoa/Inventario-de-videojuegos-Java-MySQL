/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a5_t1_ochoaulises;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JTable;
import shaneDialogs.Dialogs;

/**
 *
 * @author Uli Gibson
 */
public class PDF {

    public boolean exportJTableToPDF(JTable table, File dir, boolean openAfterSave) {
        try {
            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream(dir));
            doc.open();
            PdfPTable pdfTable = new PdfPTable(table.getColumnCount());
            //adding table headers
            for (int i = 0; i < table.getColumnCount(); i++) {
                pdfTable.addCell(table.getColumnName(i));
            }
            //extracting data from the JTable and inserting it to PdfPTable
            for (int rows = 0; rows < table.getRowCount() - 1; rows++) {
                for (int cols = 0; cols < table.getColumnCount(); cols++) {
                    pdfTable.addCell(table.getModel().getValueAt(rows, cols).toString());

                }
            }
            doc.add(pdfTable);
            doc.close();
            if(openAfterSave) {
                try {
                    Desktop.getDesktop().open(dir);
                } catch (IOException e) {
                    Dialogs.ErrorMsg("No se pudo abrir el archivo generado [Cod. Error: "+e+"]");
                }
                
            }
        } catch (FileNotFoundException | DocumentException ex) {
            Dialogs.ExceptionMsg(ex);
            return false;
        }
        return true;
    }

}
