package org.example;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.*;

public class CvsToJsonConverter {

    private static String filePath;

    public static void main(String[] args) {
        filePath = System.getProperty("user.dir") + "\\Resources\\students.csv";
        File input = new File(filePath);

        try {
            CsvSchema csv = CsvSchema.emptySchema().withHeader();
            CsvMapper csvMapper = new CsvMapper();
            MappingIterator<LinkedHashMap<?, ?>> mappingIterator = csvMapper.reader().forType(Map.class).with(csv).readValues(input);
            List<LinkedHashMap<?, ?>> jsonList = mappingIterator.readAll(); //Got list of maps with  key ,value pairs of each row from CSV

            LinkedHashMap<String, Object> classElements = new LinkedHashMap<String, Object>();

            String currentValue=null;
            float sum ;
            LinkedHashMap<String, Object> marksElements =null;
            LinkedHashMap<String, Object> restElements =null;
            ArrayList<HashMap<String, Object>> studentList =null;

            //Begin List Looping
             for (LinkedHashMap<?, ?> map : jsonList) {
                 marksElements = new LinkedHashMap<String, Object>();
                 restElements = new LinkedHashMap<String, Object>();
                studentList = new ArrayList<HashMap<String, Object>>();
                 sum = 0f;
                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    String key = (String) entry.getKey();
                    Object value = entry.getValue();

                    try {
                        if (key.equals("Class")) {
                            currentValue = (String)value;
                        } else {
                            if (key.equals("Physics") || key.equals("Mathematics") || key.equals("Chemistry")) {
                                marksElements.put(key, value);
                                sum = sum + Float.parseFloat((String) value);
                            } else {
                                restElements.put(key, value);
                            }//else
                        }//outer Else
                    } catch(Exception e){
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                }//Every row mAp
                 float percentage = sum / 3;
                 marksElements.put("Percentage", percentage);
                 restElements.put("Marks", marksElements);
                 if(currentValue!=null)
                 {
                     if(classElements.containsKey(currentValue))
                     {
                         ArrayList student = (ArrayList) classElements.get(currentValue);
                           student.add(restElements);
                     }
                     else {
                         studentList.add(0,restElements);
                         classElements.put( currentValue, studentList);}
                 }

            }//mainlist

//Code to display JSON in Required Format
                 JSONArray jsonResultArray = new JSONArray();
                for (Map.Entry<?, ?> entry : classElements.entrySet()) {
                    JSONObject json_obj = new JSONObject();
                    String key = (String) entry.getKey();
                    Object value = entry.getValue();
                 if(key !=null) {
                     json_obj.put("Class",key);
                     json_obj.put("Students", value);
                 }
                    jsonResultArray.put(json_obj);

                }//Every row mAp
              System.out.println(jsonResultArray.toString(5));

        } catch (Exception e) {
            e.printStackTrace();


        }
    }

}
