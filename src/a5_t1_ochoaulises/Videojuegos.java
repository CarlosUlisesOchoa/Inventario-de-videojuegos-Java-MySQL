/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a5_t1_ochoaulises;

/**
 *
 * @author Uli Gibson
 */

import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Videojuegos {
    
    private static Registrar vReg = new Registrar();
    private static Buscar vBus = new Buscar();
    private static Registros vRegs = new Registros();
    
    
    public int ID;
    public String nombre;
    public int cantidadExistencia;
    public int consola;
    public int categoria;
    public String precio;

    public Videojuegos(int ID, String nombre, int cantidadExistencia, int consola, int categoria, String precio) {
        this.ID = ID;
        this.nombre = nombre;
        this.cantidadExistencia = cantidadExistencia;
        this.consola = consola;
        this.categoria = categoria;
        this.precio = precio;
    }
    
    public static void mostrarVentana(int ID)
    {
        switch(ID)
        {
            case 1:
            {
                if(!vReg.isShowing())   vReg.setVisible(true);
                else                    vReg.toFront();
                vReg.setLocationRelativeTo(null);
                break;
            }
            
            case 2:
            {
                if(Integer.valueOf(Principal.lblContador.getText()) == 0)
                {
                    JOptionPane.showMessageDialog(null, "No se tiene ningún registro en la base de datos", "Videojuegos - Error", JOptionPane.ERROR_MESSAGE);
                    break;
                }
                
                if(!vBus.isShowing())   vBus.setVisible(true);
                else                    vBus.toFront();
                vBus.setLocationRelativeTo(null);
                break;
            }
            
            case 3:
            {
                if(Integer.valueOf(Principal.lblContador.getText()) == 0)
                {
                    JOptionPane.showMessageDialog(null, "No se tiene ningún registro en la base de datos", "Videojuegos - Error", JOptionPane.ERROR_MESSAGE);
                    break;
                }
                
                if(!vRegs.isShowing())   vRegs.setVisible(true);
                else                    vRegs.toFront();
                vRegs.setLocationRelativeTo(null);
                break;
            }
        }
    }
    
    private static boolean checkCampos()
    {
        if(vReg.txtID.getText().length() == 0
        || vReg.txtNombre.getText(). length() == 0
        || vReg.txtExistencias.getText(). length() == 0
        || vReg.txtPrecio.getText(). length() == 0) return false;
        return true;
    }
    
    private static void cleanCampos(int ID)
    {
        switch(ID)
        {
            case 1: // Registro
            {
                vReg.txtID.setText("");
                vReg.txtNombre.setText("");
                vReg.txtExistencias.setText("");
                vReg.cboxConsola.setSelectedIndex(0);
                vReg.cboxCategoria.setSelectedIndex(0);
                vReg.txtPrecio.setText("");
            }
            case 2: // Busqueda
            {
                vBus.txtIDBuscar.setText("");
                vBus.txtID.setText("");
                vBus.txtNombre.setText("");
                vBus.txtExistencias.setText("");
                vBus.cboxConsola.setSelectedIndex(0);
                vBus.cboxCategoria.setSelectedIndex(0);
                vBus.txtPrecio.setText("");
                vBus.txtIDBuscar.requestFocus();
            }
        }
    }
    
    public static void setInformacionJuego()
    {
        if(!checkCampos())
        {
            JOptionPane.showMessageDialog(null, "Uno o más campos se encuentran vacios, verifique por favor", "Videojuegos - Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        try 
        {
            Videojuegos vjNuevo = new Videojuegos(
                    Integer.parseInt(vReg.txtID.getText()), 
                    vReg.txtNombre.getText(), 
                    Integer.parseInt(vReg.txtExistencias.getText()), 
                    vReg.cboxConsola.getSelectedIndex(), 
                    vReg.cboxCategoria.getSelectedIndex(), 
                    vReg.txtPrecio.getText()
            );

            if(Principal.myDB.add(vjNuevo)) {
            
                JLabel msg = new JLabel((String.format("<html>El videojuego <b>%s</b>, ID: <b>%d</b> se registró exitosamente en la base de datos<br><br><center>¿Desea agregar otro juego?</center><br></html>", vReg.txtNombre.getText(), Integer.parseInt(vReg.txtID.getText()))));
                msg.setFont(new Font("Arial", Font.PLAIN, 18));

                int reply = JOptionPane.showConfirmDialog(null, msg, "Videojuegos - Aviso", JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.NO_OPTION) {
                   vReg.dispose();
                }
                cleanCampos(1);
                vReg.txtID.requestFocus();
                vBus.actualizarTabla();
            }
        
        }
        catch (Exception e) 
        {
            JOptionPane.showMessageDialog(null, "Ocurrió un problema al intentar registrar el videojuego, por favor revisa los valores ingresados [Cod. Error: "+e+"]", "Videojuegos - Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public static Videojuegos getInformacionJuego(int ID)
    {
        return Principal.myDB.searchByID(ID);
    }
    
    public static void eliminarReg(int ID)
    {
        if(Principal.myDB.deleteByID(ID)) JOptionPane.showMessageDialog(null, "¡Eliminación exitosa!", "Videojuegos - Aviso", JOptionPane.INFORMATION_MESSAGE);
        cleanCampos(2);
    }
    
    public static void actualizarReg(int ID, Videojuegos modReg)
    {
        if(Principal.myDB.updateByID(ID, modReg)) JOptionPane.showMessageDialog(null, "¡Cambios guardados!", "Videojuegos - Aviso", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static boolean accBuscar()
    {
        int IDBuscado = Integer.parseInt(vBus.txtIDBuscar.getText());
        
        Videojuegos vjResultado = getInformacionJuego(IDBuscado);
        
        if(vjResultado != null)
        {
            vBus.txtID.setText(String.valueOf(vjResultado.ID));
            vBus.txtNombre.setText(vjResultado.nombre);
            vBus.txtExistencias.setText(String.valueOf(vjResultado.cantidadExistencia));
            vBus.cboxConsola.setSelectedIndex(vjResultado.consola);
            vBus.cboxCategoria.setSelectedIndex(vjResultado.categoria);
            vBus.txtPrecio.setText(String.valueOf(vjResultado.precio));
        }
        else
        {
            JOptionPane.showMessageDialog(null, (String.format("El ID: %d no se encontró en la base de datos", IDBuscado)), "Videojuegos - Aviso", JOptionPane.INFORMATION_MESSAGE);
            cleanCampos(2);
            return false;
        }
        return true;
    }
 
    public static String getConsoleName(int ID)
    {
        String rtext;
        switch(ID)
        {
            case 0:  rtext = "PC"; break; 
            case 1:  rtext = "PS4"; break;
            case 2:  rtext = "XBOX ONE"; break;
            case 3:  rtext = "PS VITA"; break;
            default: rtext = "Desconocido";
        }
        return rtext;
    }
    
    public static String getCategoryName(int ID)
    {
        String rtext;
        switch(ID)
        {
            case 0: rtext = "ACCIÓN"; break;
            case 1: rtext = "DISPAROS"; break;
            case 2: rtext = "ESTRATEGIA"; break;
            case 3: rtext = "TERROR"; break;
            case 4: rtext = "DEPORTE"; break;
            case 5: rtext = "AVENTURA"; break;
            default: rtext = "Desconocido";
        }
        return rtext;
    }
    
}