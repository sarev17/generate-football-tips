
package dadosweb.Telas;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

public class JTableUtilities
{
    public static void setCellsAlignment(JTable table, int alignment,int column)
    {
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(alignment);
            table.getColumnModel().getColumn(column).setCellRenderer(rightRenderer);
        
    }
}