/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gitd.iws.csv;

/**
 *
 * @author afdzal
 */
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;
 
public class PropCache
{
   private final Properties configProp = new Properties();
    
   private PropCache()
   {
      //Private constructor to restrict new instances
      InputStream in = this.getClass().getClassLoader().getResourceAsStream("application.properties");
      System.out.println("Reading all properties from the file");
      try {
          configProp.load(in);
      } catch (IOException e) {
          e.printStackTrace();
      }
   }
 
   //Bill Pugh Solution for singleton pattern
   private static class LazyHolder
   {
      private static final PropCache INSTANCE = new PropCache();
   }
 
   public static PropCache getInstance()
   {
      return LazyHolder.INSTANCE;
   }
    
   public String getProperty(String key){
      return configProp.getProperty(key);
   }
    
   public Set<String> getAllPropertyNames(){
      return configProp.stringPropertyNames();
   }
    
   public boolean containsKey(String key){
      return configProp.containsKey(key);
   }
}
