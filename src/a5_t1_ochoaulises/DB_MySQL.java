/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a5_t1_ochoaulises;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;



/**
 *
 * @author Uli Gibson
 */
public class DB_MySQL {
    
    public Connection conexiondb;
    public Statement senteciasSQL;
    public ResultSet datosConsultados;
    
    String consulta = "";
    String nombreBase = "games";
    String nombreTabla = "data";
    String username = "root";
    String password = "";
    String url;
    
    
    boolean conect()
    {
        url = "jdbc:mysql://localhost/"+nombreBase;

        System.out.println("Intentando conectar a la base...");

        try 
        {
            conexiondb = DriverManager.getConnection(url, username, password);
            senteciasSQL = conexiondb.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            System.out.println("Conexión exitosa a la base: "+nombreBase);
        } catch (SQLException e) {
            String exception = String.valueOf(e);
            if(exception.contains("Unknown database")) {
                try {
                    System.out.println("La base de datos "+nombreBase+" no existe se intentará crear...");
                    create();
                    System.out.println("Éxito con la creación");
                    return conect();
                } catch (Exception ex) {
                    errorMessage("Ocurrio un error inesperado [Cod. Error: "+e+"]");
                }
            }
            else if(exception.contains("Communications link failure")) {
                errorMessage("No se pudo conectar al servidor");
            }
            return false;
        }
        return true;
    }
    
    private void create() throws SQLException
    {
        url = "jdbc:mysql://localhost/mysql?zeroDateTimeBehavior=convertToNull";
        conexiondb = DriverManager.getConnection(url, username, password);
        senteciasSQL = conexiondb.createStatement();
        
        consulta = "CREATE DATABASE "+nombreBase;
        
        senteciasSQL.executeUpdate(consulta);
        
        consulta = "CREATE TABLE `"+nombreBase+"`.`"+nombreTabla+"` ( `IDE` INT(10) NOT NULL , "
                + "`NAME` VARCHAR(30) NOT NULL , `QTY` INT(5) NOT NULL , "
                + "`CONSOLE` INT(1) NOT NULL , `CATEGORY` INT(1) NOT NULL , "
                + "`PRICE` VARCHAR(8) NOT NULL , PRIMARY KEY (`IDE`)) "
                + "ENGINE = InnoDB;";
        
        senteciasSQL.executeUpdate(consulta);
    }
    
    public boolean add(Videojuegos v1)
    {
        
        try 
        { 
            consulta = String.format("INSERT INTO DATA VALUES (%d, '%s', %d, %d, %d, '%s')", 
                    v1.ID, 
                    v1.nombre, 
                    v1.cantidadExistencia, 
                    v1.consola, 
                    v1.categoria, 
                    v1.precio
            );

           senteciasSQL.executeUpdate(consulta);
           
        } catch (Exception e) {
            
            errorMessage("Ocurrió un problema al intentar guardar el registro [Cpd. Error: "+e+"]");
            return false;
        }
        return true;
    }
    
    public boolean updateByID(int IDE, Videojuegos modReg)
    {
        int res = 0;
        
        consulta = String.format("UPDATE DATA SET NAME='%s', QTY=%d, CONSOLE=%d, CATEGORY=%d, PRICE='%s' WHERE IDE=%d", 
                modReg.nombre,
                modReg.cantidadExistencia,
                modReg.consola,
                modReg.categoria,
                modReg.precio,
                IDE
        );
        
        try {
            res = senteciasSQL.executeUpdate(consulta);
        } catch (Exception e) {
            errorMessage("Ocurrió un error inesperado al intentar modificar el registro [Cod. Error: "+e+"]");
        }
        return res == 1;
    }
    
    public boolean deleteByID(int IDE)
    {
        
        int res = 0;
        
        consulta = "DELETE FROM DATA WHERE IDE="+IDE;
        
        try {
            res = senteciasSQL.executeUpdate(consulta);
        } catch (Exception e) {
            errorMessage("Error al intentar eliminar el registro [Cod. Error: "+e+"]");
            return false;
        }
        return res == 1; // Si res == 1 retorna true, sino retorna false
    }
    
    public DefaultTableModel getTable()
    {
        DefaultTableModel modeloTabla = null;
        consulta = "SELECT * FROM "+nombreTabla;
        try {
            datosConsultados=senteciasSQL.executeQuery(consulta);
            
            ResultSetMetaData metaData = datosConsultados.getMetaData();
            int count = metaData.getColumnCount(); //number of column
            String columnName[] = new String[count];
            Object filas[]=new Object[count];

            for (int i = 1; i <= count; i++)
            {
               columnName[i-1] = metaData.getColumnLabel(i);
            }

            modeloTabla = new DefaultTableModel(null,columnName)
            {
                @Override
                public boolean isCellEditable(int row, int column) {
                   //all cells false
                   return false;
                }
            };
            
            while(datosConsultados.next())
            {
                filas[0] = datosConsultados.getInt("IDE");
                filas[1] = datosConsultados.getString("NAME");
                filas[2] = datosConsultados.getInt("QTY");
                filas[3] = Videojuegos.getConsoleName(datosConsultados.getInt("CONSOLE"));
                filas[4] = Videojuegos.getCategoryName(datosConsultados.getInt("CATEGORY"));
                filas[5] = datosConsultados.getString("PRICE");
                
                modeloTabla.addRow(filas);
            }    
        } catch (Exception e) {
            errorMessage("Error al intentar refrescar la tabla [Cod. Error: "+e+"]");
        }
        return modeloTabla;
    }
    
    public Videojuegos searchByID(int IDE)
    {
        consulta = "SELECT * FROM DATA WHERE IDE="+IDE;
        
        Videojuegos resultado = null;
        
        try{
            
            datosConsultados = senteciasSQL.executeQuery(consulta);
            
            while(datosConsultados.next()){
                resultado = new Videojuegos( 
                        datosConsultados.getInt("IDE"),
                        datosConsultados.getString("NAME"),
                        datosConsultados.getInt("QTY"),
                        datosConsultados.getInt("CONSOLE"),
                        datosConsultados.getInt("CATEGORY"),
                        datosConsultados.getString("PRICE")
                );
            }
        } catch (Exception e){
            errorMessage("Error al intentar realizar la búsqueda [Cod. Error: "+e+"]");
        }
        return resultado;
    }
    
    public int count()
    {
        int cuenta = 0;
        
        consulta = "SELECT * FROM DATA";
        
        try 
        {
            datosConsultados = senteciasSQL.executeQuery(consulta);
            
            while(datosConsultados.next()) cuenta++;
            
        } catch(Exception e) {
            errorMessage("Ocurrio un error al intentar obtener la cuenta de registros ["+e+"]");
        }
        return cuenta;
    }
    
    private void errorMessage(String texto)
    {
        JOptionPane.showMessageDialog(null, texto, "Base de datos - Error", JOptionPane.ERROR_MESSAGE);
    }
    
}
