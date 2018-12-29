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
		String selectSql = "  select  "
				+ " a.id as id, " /**  商品类目关联ID  */ 
				+ " b.item_id as productId, " /**  商品ID  */
				+ " b.spu as spu, " /**  spu */ 
				+ " b.sell_price as sellPrice, "
				+ " b.create_time as createTime, "
				+ " ifnull(e.`name`,b.`name`) as name ,"
				+ " b.is_backorder as isBackorder ,"  
				+ " ifnull(c.discount_percentage,1)*100 as discountPercentage, " 
				+ " ifnull(c.discount_amount,b.sell_price) as discountAmount, "
				+ " ( select small_image from catalog_product_entity_image  where   deleted_status = 1 and  product_id =  a.product_id order by position limit 1 ) as  remoteUrl   " 
				+ " from catalog_product_position a " 
				+ " left join  catalog_product_entity  b on a.product_id = b.id " 
				+ " left join  catalog_product_entity_discount c  on a.product_id = c.product_id "   
				+ " left join  ( select * from catalog_product_entity_language where language_id =  #{languageId}  ) e  on a.product_id = e.product_id " //  
				+ " where  a.position_type =  #{positionType}  and  b.status = 1     ";
		
		//String  spu = "Man's".trim().replaceAll("'", "\\\\\\\'");
		
		System.out.println(selectSql);
		
		
//        File file = new File( "/Users/denghaihui/Downloads/2017-7-19-item-ok.csv");
//        try{
//            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
//            String s = null;
//            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
//            	String[] strArr = s.split(",");
//                System.out.println(strArr.length);
//                
//         
//            }
//            br.close();    
//        }catch(Exception e){
//            e.printStackTrace();
//        }
        
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
