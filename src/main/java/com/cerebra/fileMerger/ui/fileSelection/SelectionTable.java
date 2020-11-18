package com.cerebra.fileMerger.ui.fileSelection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.event.MouseEvent;

public class SelectionTable {
    public JTable table;
    public DefaultTableModel defaultTableModel;

    public SelectionTable() {
        String[] headers = {" Select ", "  File Name  ", "  No of Lines  ", "  Path  "};
        defaultTableModel = new DefaultTableModel(headers, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? Boolean.class : String.class;
            }
        };
        table = new JTable(defaultTableModel) {
            @Override
            public String getToolTipText(MouseEvent e) {
                String tip = null;
                java.awt.Point p = e.getPoint();
                int rowIndex = rowAtPoint(p);
                int colIndex = columnAtPoint(p);
                TableModel model = getModel();
                if (rowIndex > 0 && (colIndex > 0 && colIndex < 4))
                    tip = (String) model.getValueAt(rowIndex, colIndex);
                return tip;
            }
        };
        table.setAutoCreateRowSorter(false);
        TableColumn column;
        for (int i = 0; i < 4; i++) {
            column = table.getColumnModel().getColumn(i);
            if (i == 0) column.setPreferredWidth(100);
            if (i == 1) column.setPreferredWidth(250);
            if (i == 2) column.setPreferredWidth(100);
            if (i == 3) column.setPreferredWidth(400);
        }
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
}
