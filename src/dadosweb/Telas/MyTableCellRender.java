/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dadosweb.Telas;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author andre
 */
class ColorirLinha extends DefaultTableCellRenderer {
 
    int linha;
    Color fundo,texto,fundodef,textdef;
    
        
public ColorirLinha() {
setOpaque(true);
}
 
@Override
public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    
    {
        final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        
        if(row ==linha){
            
                c.setBackground(fundo);
                c.setForeground(texto);
            setText(value.toString());
            System.out.println(value+" "+isSelected+" "+hasFocus+" "+row+" "+column);
        }
        
        if(row>linha){
                    c.setBackground(fundodef);
                    c.setForeground(textdef);
                }
        return c;
    }
    
}
} 
 