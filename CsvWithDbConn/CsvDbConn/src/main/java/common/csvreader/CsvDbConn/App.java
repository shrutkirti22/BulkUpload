package common.csvreader.CsvDbConn;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    
    public static void save_fileActionPerformed() {	
    	try {
    		 FileReader fr=new FileReader("./src/catalog_product import file.csv");   
			CSVReader reader = new CSVReader(new FileReader("./src/catalog_product import file.csv"));
			BufferedReader br = new BufferedReader(new FileReader("./src/catalog_product import file.csv"));
			
			String[]nextline;
			String line;
			int productId =0;
			while((line =br.readLine())!=null)
			{
				insertInCatalogProduct(line,productId);
				
				
			}	
			 	
    	}
    	catch(Exception e) {
    		System.out.println(e);
    	}
    }
    
    public static void insertInCatalogProduct(String line,int productId) throws Exception {
    	Connection con = getConnection();
    	String[]value=line.split(",");
		
		String 	getAttributeSetId =
				"select attribute_set_id from eav_attribute_set where attribute_set_name='Default' and\n" + 
				" entity_type_id=(\n" + 
				"select entity_type_id from eav_entity_type where entity_type_code='catalog_product')";
		
		PreparedStatement findAttId =con.prepareStatement(getAttributeSetId);  
		ResultSet attId = findAttId.executeQuery();
		int supplierID = 0;
		
		while (attId.next()) {
	         supplierID = attId.getInt("attribute_set_id");
	        System.out.println(supplierID);
		}
		 
		String validate =
				"select entity_id from catalog_product_entity where sku = '"+value[0]+"'";
		
		PreparedStatement validateProd =con.prepareStatement(validate);  
		ResultSet validatePid = validateProd.executeQuery();
		
		while (validatePid.next()) {
			productId = validatePid.getInt("entity_id");
		}
		
		String sql2 = 
				"INSERT INTO catalog_product_entity"
				+ " (type_id,attribute_set"
				+ "_id, sku,created_at,updated_at) VALUES"
				+ " ('"+value[3]+"','"+ supplierID +"','"+ value[0] +"','"+ value[23]+"','"+ value[24]+"')";

		if(productId == 0) {
		PreparedStatement insert =con.prepareStatement(sql2);  
		int getProduct =insert.executeUpdate();
		
		System.out.println("success-----------"+getProduct);
		}
		
		try {
		insertInCatalogProductEntityVarchar(line,productId);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
    }
    
    public static void createTable() throws Exception{
    		try {
    			
    			Connection con = getConnection();

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
    
    public static void insertInCatalogProductEntityVarchar(String line,int productId) throws Exception {
    	
    	Connection con = getConnection();
    	String[]value=line.split(",");
		
    	String getAttribute_Id;
     System.out.println("productId"+ productId);
    	String 	getAttributeId =
				"select attribute_id from eav_attribute where \n" + 
				"attribute_code='name' and \n"
				+ "entity_type_id=(select entity_type_id from eav_entity_type where entity_type_code='catalog_product')"; 
		
		PreparedStatement findAttId =con.prepareStatement(getAttributeId);  
		ResultSet attId = findAttId.executeQuery();
		int attID = 0;
		
		while (attId.next()) {
			attID = attId.getInt("attribute_id");
	        System.out.println("attID"+attID);
		}
    	
    	
    	System.out.println("productId"+productId+"value " );
		String 	insertCpev  =
				"INSERT INTO catalog_product_entity_varchar (attribute_id, entity_id, value) VALUES"
				+ " ('"+attID+"','"+ productId +"','"+ value[7]+"')";

	
		
		PreparedStatement insert =con.prepareStatement(insertCpev);  
		insert.executeUpdate();
		
		
//		--------------------------------------------
		String attrId = "select attribute_id from eav_attribute where\n" + 
				" attribute_code='meta_title' and \n" + 
				" entity_type_id= (select entity_type_id from eav_entity_type where entity_type_code='catalog_product')";
		
		PreparedStatement secondAttId =con.prepareStatement(attrId);  
		ResultSet sAttId = secondAttId.executeQuery();
		
		int secoAttID = 0;
		
		while (sAttId.next()) {
			secoAttID = sAttId.getInt("attribute_id");
	        System.out.println("secoAttID"+secoAttID);
		}
		System.out.println("second id"+secoAttID);
		
		
		
		String 	insertSecondRecord  =
				"INSERT INTO catalog_product_entity_varchar (attribute_id, entity_id, value) VALUES"
				+ " ('"+secoAttID+"','"+ productId +"','"+ value[7]+"')";

		PreparedStatement preparedInsertSecondRecord =con.prepareStatement(insertSecondRecord);  
		preparedInsertSecondRecord.executeUpdate();
		
//		-----------------------------------------
		
		String attrRecId = "select attribute_id from eav_attribute where\n" + 
				" attribute_code='meta_description' and \n" + 
				" entity_type_id= (select entity_type_id from eav_entity_type where entity_type_code='catalog_product')";
		
		PreparedStatement thirdAttId =con.prepareStatement(attrRecId);  
		ResultSet tAttId = thirdAttId.executeQuery();
		
		int thirAttID = 0;
		
		while (tAttId.next()) {
			thirAttID = tAttId.getInt("attribute_id");
	        System.out.println("thirAttID"+thirAttID);
		}
		System.out.println("thirAttID id"+thirAttID);
		
		String 	insertThirdRecord  =
				"INSERT INTO catalog_product_entity_varchar (attribute_id, entity_id, value) VALUES"
				+ " ('"+thirAttID+"','"+ productId +"','"+ value[7]+"')";

		PreparedStatement preparedInsertThirdRecord =con.prepareStatement(insertThirdRecord);  
		preparedInsertThirdRecord.executeUpdate();
		
//		-------------------------------------------
		
		String attrRecFId = "select attribute_id from eav_attribute where\n" + 
				" attribute_code='options_container' and \n" + 
				" entity_type_id= (select entity_type_id from eav_entity_type where entity_type_code='catalog_product')";
		
		PreparedStatement fourAttId =con.prepareStatement(attrRecFId);  
		ResultSet fAttId = fourAttId.executeQuery();
		
		int fouAttID = 0;
		
		while (fAttId.next()) {
			fouAttID = fAttId.getInt("attribute_id");
			
	        System.out.println("fouAttID"+fouAttID);
		}
	
		
		  String getDefaultValue =  "select default_value from eav_attribute where attribute_id="+fouAttID;
		  
		  PreparedStatement valueStatement =con.prepareStatement(getDefaultValue);  
			ResultSet valueOfFourthRecord = valueStatement.executeQuery();
			
			String valueOfProduct = "";
			
			while (valueOfFourthRecord.next()) {
				valueOfProduct = valueOfFourthRecord.getString("default_value");
		        System.out.println("valueOfProduct"+valueOfProduct);
			}
		
		String 	insertFourRecord  =
				"INSERT INTO catalog_product_entity_varchar (attribute_id, entity_id, value) VALUES"
				+ " ('"+fouAttID+"','"+ productId +"','"+ valueOfProduct+"')";

		PreparedStatement preparedInsertFourRecord =con.prepareStatement(insertFourRecord);  
		preparedInsertFourRecord.executeUpdate();
		
//		-----------------------------------------------
		
		String attrRecFifId = "select attribute_id from eav_attribute where\n" + 
				" attribute_code='msrp_display_actual_price_type' and \n" + 
				" entity_type_id= (select entity_type_id from eav_entity_type where entity_type_code='catalog_product')";
		
		PreparedStatement fifthAttId =con.prepareStatement(attrRecFifId);  
		ResultSet fifAttId = fifthAttId.executeQuery();
		
		int fiftAttID = 0;
		
		while (fifAttId.next()) {
			fiftAttID = fifAttId.getInt("attribute_id");
	        System.out.println("fiftAttID"+fiftAttID);
		}

		String valueDataOfFifth = value[38];
		if( valueDataOfFifth == "Use config")
		{
			valueDataOfFifth = "0";
		}
		
		String 	insertFifthRecord  =
				"INSERT INTO catalog_product_entity_varchar (attribute_id, entity_id, value) VALUES"
				+ " ('"+fiftAttID+"','"+ productId +"','"+  0 +"')";

		System.out.println("insertFifthRecord"+insertFifthRecord);
		PreparedStatement preparedInsertFifthRecord =con.prepareStatement(insertFifthRecord);  
		preparedInsertFifthRecord.executeUpdate();
		System.out.println("success");
		
    }
    
 
  }
    
    
