/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gitd.iws.csv;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author afdzal
 */
public class TModel extends AbstractTableModel {
    
    
        private String[] columnNames = { "c01","c02","c03"};
        private ArrayList<String[]> Data = new ArrayList<String[]>();

        public void AddCSVData(ArrayList<String[]> DataIn) {
            this.Data = DataIn;
            this.fireTableDataChanged();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;// length;
        }

        @Override
        public int getRowCount() {
            return Data.size();
        }

        @Override
        public String getColumnName(int col) {
            return columnNames[col];
        }

        @Override
        public Object getValueAt(int row, int col) {
            return Data.get(row)[col];
        }
        
        public void setColumNames(ArrayList<String> colNames){
            System.out.println("colNames");
            System.out.println(colNames);
         columnNames = colNames.toArray(new String[0]);
        }
    }
