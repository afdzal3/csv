/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gitd.iws.csv;

import com.google.common.io.Files;
import com.opencsv.CSVWriterBuilder;
import com.opencsv.ICSVWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 *
 * @author afdzal
 */
public class CsvData {

    @Autowired
    JdbcTemplate jdbcTemplate;

    // private final ArrayList<String[]> Rs = new ArrayList<String[]>();
    private String[] OneRow;

    File sourceFile = new File("");
    ArrayList<String> colArr = new ArrayList<String>();

    public ArrayList<String[]> ReadCSVfile(File DataFile) {
        ArrayList<String[]> Rs = new ArrayList<String[]>();
        try {
            BufferedReader brd = new BufferedReader(new FileReader(DataFile));
            while (brd.ready()) {
                String st = brd.readLine();
                OneRow = st.split("\\|");
                Rs.add(OneRow);
                //   System.out.println(Arrays.toString(OneRow));
            } // end of while
        } // end of try
        catch (Exception e) {
            String errmsg = e.getMessage();
            System.out.println("File not found:" + errmsg);
        } // end of Catch
        return Rs;
    }

    public void popDB(ArrayList<String[]> Rs) {
        System.out.println("Reach popDB");
        this.jdbcTemplate.execute("drop table employees if exists ");

        this.jdbcTemplate.execute("create table employees (id int primary key "
                + "auto_increment, name varchar(30), email varchar(30))");

        this.jdbcTemplate.update("delete from csvdata");
        this.jdbcTemplate.update("delete from csvdata2");

        this.jdbcTemplate.update("TRUNCATE TABLE csvdata");
        this.jdbcTemplate.update("TRUNCATE TABLE csvdata2");

        //  NatQuery nt = new NatQuery();
        //nt.truncateCsvData();
        System.out.println("truncating");

        int colCount = 0;

        Iterator<String[]> iterator = Rs.iterator();
        while (iterator.hasNext()) {
            ArrayList<String> colArrTemp = new ArrayList<String>();
            int col = 0;
            String[] arr = iterator.next();
            String colSql = " ";
            String valSql = " ";

            for (String i : arr) {
                col = col + 1;
                String with2digits = String.format("%02d", col);
                String colStr = "c" + with2digits;
                colArrTemp.add(colStr);

                if (col == 1) {
                    colSql = colStr;
                } else {
                    colSql = colSql + " ," + colStr;
                }

                //  System.out.println(colStr + " " + i);
                if (col == 1) {
                    valSql = "'" + i + "'";
                } else {
                    valSql = valSql + " ," + "'" + i + "'";
                }

            }

            if (col > colCount) {
                colCount = col;
                this.colArr = colArrTemp;

            }

            String insSql = "insert into csvdata ("
                    + colSql
                    + ") values "
                    + "( "
                    + valSql
                    + ") ;";



            System.out.println(colSql);
            System.out.println(valSql);
            System.out.println(insSql);

            this.jdbcTemplate.execute(insSql);
            

        }
        String insSql2 = "insert into csvdata2 select * from csvdata;";
        this.jdbcTemplate.execute(insSql2);

    } 

    public void removeOutlets(String outletColumn, String outlet) {

        String sql1 = "update csvdata2 set flag1 = 'DELETE' where " + outletColumn
                + " = "
                + "'" + outlet + "' ;";
        System.out.println(sql1);
        this.jdbcTemplate.execute(sql1);

    }
    
    public void updNullOutlets(String outletColumn, String resellerColumn) {

        String sql1 = "update csvdata2 set flag1 = 'NULL_R' where " + outletColumn + " is null";
        System.out.println(sql1);
        this.jdbcTemplate.execute(sql1);
        
        String sql2 = "update csvdata2 q1 set q1." + outletColumn + " = (select max(q2." + outletColumn 
                + " from csvdata q2 "
                + " where q2."+outletColumn + "is not null "
                + "   and q2."+resellerColumn + " =  q1."+resellerColumn  ;
        
        

    }


    public void createFiles() {

        String srcFile = this.sourceFile.getAbsolutePath();
        String baseFile = Files.getNameWithoutExtension(srcFile);
        Path path1 = Paths.get(srcFile);

        String errFile = path1.getParent() + "/" + baseFile + "_err.csv";
        String updFile = path1.getParent() + "/" + baseFile + "_upd.csv";

        System.out.println(srcFile);
        System.out.println(errFile);
        System.out.println(updFile);

        String sqlUpd = "select * from csvdata2 where flag1 != 'DELETE' ";
        String sqlErr = "select * from csvdata2 where flag1 in ('DELETE', 'NULL_R') ";
        
        List<String[]> dataUpd =  getResultFromDB(sqlUpd);
        List<String[]> dataErr =  getResultFromDB(sqlErr);

        /*
        try {
            Writer writer = new FileWriter(updFile);

            CSVWriter csvWriter = new CSVWriter(writer,
                    '|',
                    CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END);
            csvWriter.w
           

        } catch (IOException ex) {
            Logger.getLogger(CsvData.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
         */
        File updObj = new File(updFile);
        updObj.delete();
        try (
                 ICSVWriter writer1 = new CSVWriterBuilder(
                        new FileWriter(updFile)).withSeparator('|').build()) {
            writer1.writeAll(dataUpd, false);
        } catch (IOException ex) {
            ex.printStackTrace();

            Logger.getLogger(CsvData.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        File errObj = new File(errFile);
        errObj.delete();
        try (
                 ICSVWriter writer2 = new CSVWriterBuilder(
                        new FileWriter(errFile)).withSeparator('|').build()) {
            writer2.writeAll(dataErr, false);
        } catch (IOException ex) {
            ex.printStackTrace();

            Logger.getLogger(CsvData.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public List<String[]> getResultFromDB(String sql) {

        List<Map<String, Object>> rows = this.jdbcTemplate.queryForList(sql);
        int rowsize = rows.size();
        List<String[]> dataFromDB = new ArrayList<String[]>();

        System.out.println("this.colArr.size()");
        System.out.println(this.colArr.size());

        for (Map<String, Object> row : rows) {
            String[] str = new String[this.colArr.size()];

            for (int i2 = 0; i2 < this.colArr.size(); i2++) {
                str[i2] = (String) row.get(this.colArr.get(i2));
                System.out.println("i2" + i2);
            }
            System.out.println(str);
            dataFromDB.add(str);
        }
        System.out.println("dataFromDB");
        System.out.println(dataFromDB);
        
        return dataFromDB;
    }

    public void setJdbcTemplate(JdbcTemplate jd) {
        this.jdbcTemplate = jd;
    }

}
