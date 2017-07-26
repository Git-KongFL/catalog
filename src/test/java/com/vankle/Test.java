package com.vankle;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/** 
*
* @author 作者 denghaihui
* @version 创建时间：2017年7月1日 下午2:30:36 
* 类说明 
*
*/

public class Test {

	public static void main(String[] args)
    {
        File file = new File( "/Users/denghaihui/Downloads/2017-7-19-item-ok.csv");
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
            	String[] strArr = s.split(",");
                System.out.println(strArr.length);
                
         
            }
            br.close();    
        }catch(Exception e){
            e.printStackTrace();
        }
        
//        System.out.println(list1);
//        System.out.println(list2);
//        System.out.println(list3);
//        String table = "nfp_system_time_config";
//        System.out.println("DROP TABLE IF EXISTS `"+table+"`;");
//        System.out.println("CREATE TABLE `"+table+"` (");
//        for(int j=0;j<list1.size();j++){
//        	if(j==0)
//        		System.out.println("`"+list1.get(j).trim()+"` "+list3.get(j).trim()+" NOT NULL AUTO_INCREMENT COMMENT '"+list2.get(j).trim()+"',");
//        	else
//        		System.out.println("`"+list1.get(j).trim()+"` "+list3.get(j).trim()+" COMMENT '"+list2.get(j).trim()+"',");
//        }
//        System.out.println("PRIMARY KEY (`id`)");
//        System.out.println(") ENGINE=InnoDB DEFAULT CHARSET=utf8;");
  
    }
	
}
