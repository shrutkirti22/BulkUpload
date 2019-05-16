package common.csvreader.CsvDbConn;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Arrays;

import com.opencsv.CSVReader;
/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception
    {
        System.out.println("Hello World!");
        save_fileActionPerformed();
//        createTable();
    }
    
    public static void save_fileActionPerformed() {	
    	try {
    		System.out.println("csv function");
    		 FileReader fr=new FileReader("./src/catalog_product import file.csv");   
			 System.out.println("yoo");
			 System.out.println(fr);
			CSVReader reader = new CSVReader(new FileReader("./src/catalog_product import file.csv"));
			BufferedReader br = new BufferedReader(new FileReader("./src/catalog_product import file.csv"));
			System.out.println(reader);
			String[]nextline;
			String line;
			while((line =br.readLine())!=null)
			{
				Connection con = getConnection();
				String[]value=line.split(",");
				System.out.println(value[0] + value[2] + value[23] +value[24]);

				String sql2 = 
						"INSERT INTO catalog_product_entity"
						+ " (type_id, sku,created_at,updated_at) VALUES"
						+ " ('"+value[3]+"','"+ value[0] +"','"+ value[23]+"','"+ value[24]+"')";
	
					
						
				System.out.println(sql2);	
				PreparedStatement insert =con.prepareStatement(sql2);  
    			insert.executeUpdate();
    			System.out.println("success");
			}
			 
//			while((nextline= reader.readNext())!= null)
//			{
//				System.out.println("inside nextline "+nextline);
//				if(nextline !=null)
//				{	
////					String[]line=nextline.split(",");
//					System.out.println(nextline);
//					System.out.println(nextline[0]);
//					Connection con = getConnection();
//					String sql = "INSERT INTO Product (entity_Id, sku, created_at) VALUES (nextline[0], nextline[1], nextline[2]);\n" ;
//	    			System.out.println(sql);
//					PreparedStatement insert =con.prepareStatement(sql);  
//	    			insert.executeUpdate();
//					System.out.println(Arrays.toString(nextline));
//				}
//			}
    	}
    	catch(Exception e) {
    		System.out.println(e);
    	}
    }
    
    public static void createTable() throws Exception{
    		try {
    			
    			Connection con = getConnection();
//    			 String sql = "CREATE TABLE IF NOT EXISTS catalog_product_entity " +
//    	                   "(entity_Id INTEGER not NULL, " +
//    	                   " sku VARCHAR(255), " + 
//    	                   " created_at Date, "+
//    	                   " PRIMARY KEY (entity_Id))"; 
    			 String sql1 ="DROP TABLE IF EXISTS catalog_product_entity;\n" + 
    			 		" SET character_set_client = utf8mb4 ;\n" + 
    			 		"CREATE TABLE catalog_product_entity (\n" + 
    			 		"  entity_id int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT Entity ID,\n" + 
    			 		"  attribute_set_id smallint(5) unsigned NOT NULL DEFAULT 0 COMMENT Attribute Set ID,\n" + 
    			 		"  type_id varchar(32) NOT NULL DEFAULT simpl COMMENT Type ID,\n" + 
    			 		"  sku varchar(64) DEFAULT NULL COMMENT SKU,\n" + 
    			 		"  has_options smallint(6) NOT NULL DEFAULT 0 COMMENT Has Options,\n" + 
    			 		"  required_options smallint(5) unsigned NOT NULL DEFAULT 0 COMMENT Required Options,\n" + 
    			 		"  created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT Creation Time,\n" + 
    			 		"  updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT Update Time,\n" + 
    			 		"  PRIMARY KEY (entity_id),\n" + 
    			 		"  KEY CATALOG_PRODUCT_ENTITY_ATTRIBUTE_SET_ID (attribute_set_id),\n" + 
    			 		"  KEY CATALOG_PRODUCT_ENTITY_SKU (sku)\n" + 
    			 		") ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT=Catalog Product Table";
    			PreparedStatement create =con.prepareStatement(sql1);  
    			create.executeUpdate();

    		}catch(Exception e) {
    			System.out.println(e);
    		}
    		finally{System.out.println("Function complete");};
    }
    
    public static Connection getConnection() throws Exception{
    	try {
    		String driver = "com.mysql.jdbc.Driver";
    		String url ="jdbc:mysql://localhost:3306/testo";
    		String username = "root";
    		String password = "Abc@1234";
    		Class.forName(driver);
        	Connection conn = DriverManager.getConnection(url,username,password);
        	System.out.println("Connected");
        	return conn;
    		
    	}catch(Exception e) {
    		System.out.println(e);
    	}
		return null;
    	
    }
}
