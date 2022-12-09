package gcfv2;

import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import java.io.BufferedWriter;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class HelloHttpFunction implements HttpFunction {
 public void service(final HttpRequest request, final HttpResponse response) throws Exception {
    createConnection();
    BufferedWriter writer = response.getWriter();
    writer.write("Hello. I have successfully completed your work!");
 }

  // Saving credentials in environment variables is convenient, but not secure - consider a more
  // secure solution such as https://cloud.google.com/kms/ to help keep secrets safe.
  private static final String INSTANCE_CONNECTION_NAME ="<<YOUR_INSTANCE>>";
  private static final String DB_USER = "root";
  private static final String DB_PASS = "<<YOUR_PASSWORD>>";
  private static final String DB_NAME = "cricket_db";
  private static final String INSTANCE_UNIX_SOCKET = null;
  private HikariDataSource connectionPool;
  private String tableName;
  
  
  public void createConnection() throws SQLException {
    // The configuration object specifies behaviors for the connection pool.
    HikariConfig config = new HikariConfig();
    config.setJdbcUrl(String.format("jdbc:mysql:///%s", DB_NAME));
    config.setUsername(DB_USER); // e.g. "root", "mysql"
    config.setPassword(DB_PASS); // e.g. "my-password"
    config.addDataSourceProperty("socketFactory", "com.google.cloud.sql.mysql.SocketFactory");
    config.addDataSourceProperty("cloudSqlInstance", INSTANCE_CONNECTION_NAME);
    if (INSTANCE_UNIX_SOCKET != null) {
      config.addDataSourceProperty("unixSocketPath", INSTANCE_UNIX_SOCKET);
    }
    config.addDataSourceProperty("ipTypes", "PUBLIC,PRIVATE");
    config.setMaximumPoolSize(5); 
    //config.setMinimumIdle(5);
    config.setConnectionTimeout(600000); // in milliseconds
    config.setIdleTimeout(1200000); // 
    config.setMaxLifetime(3600000); //   
    DataSource pool = new HikariDataSource(config); 
    this.connectionPool = new HikariDataSource(config);

int keynumber = 0;
int comment_id = 0;
int match_id = 0;
String match_name = "";
String home_team = "";
String away_team = "";
String current_innings = "";
int innings_id = 0;
int overs = 0;
int ball = 0;
int runs = 0;
String shortText = "";
int isBoundary = 0;
int isWide = 0;
int isNoball = 0;
int batsman1_id = 0;
String batsman1_name = "";
int batsman1_runs = 0;
int batsman1_balls = 0;
int bowler1_id = 0;
String bowler1_name = "";
double bowler1_overs = 0.0;
double bowler1_maidens = 0;
int bowler1_runs = 0;
int bowler1_wkts = 0;
int batsman2_id = 0;
String batsman2_name = "";
int batsman2_runs = 0;
int batsman2_balls = 0;
int bowler2_id = 0;
String bowler2_name = "";
double bowler2_overs = 0.0;
double bowler2_maidens = 0.0;
int bowler2_runs = 0;
int bowler2_wkts = 0;
double wicket_id = 0;
String wkt_batsman_name = "";
String wkt_bowler_name = "";
double wkt_batsman_runs = 0.0;
double wkt_batsman_balls = 0.0;
String wkt_text = "";
int isRetiredHurt = 0;
String text = "";
int overs_left = 0;
int wickets_lost = 0;
int innings_1_runs = 0;
double innings_2_res = 0;
double innings_1_res = 100;
int innings_2_target = 0;

try (Connection conn = connectionPool.getConnection()) {
      // PreparedStatements can be more efficient and project against injections.
      String query =  "select x.*, (select RESOURCE_PERC from cricket_db.STD_DLS_RESOURCE where OVERS_LEFT = x.overs_left and WICKETS_LOST = x.wickets_lost) as innings_2_res"
                      +" from ("
                      +" SELECT a.keynumber,a.comment_id,a.match_id,a.match_name,a.home_team,a.away_team, a.current_innings,a.innings_id,a.overs,a.ball,a.runs,a.shortText,a.isBoundary,a.isWide,a.isNoball, a.batsman1_id,a.batsman1_name,a.batsman1_runs,a.batsman1_balls,a.bowler1_id,a.bowler1_name, a.bowler1_overs,a.bowler1_maidens,a.bowler1_runs,a.bowler1_wkts,a.batsman2_id,a.batsman2_name, a.batsman2_runs,a.batsman2_balls,a.bowler2_id,a.bowler2_name,a.bowler2_overs,a.bowler2_maidens, a.bowler2_runs,a.bowler2_wkts,a.wicket_id,a.wkt_batsman_name,a.wkt_bowler_name,a.wkt_batsman_runs, a.wkt_batsman_balls,a.wkt_text,a.isRetiredHurt,a.text "
                      +" , (20-a.overs) as overs_left, (select count(wicket_id) from cricket_db.dls where wicket_id <> 0 and "
                      +" innings_id = 2 and a.match_name = 'INDIA v PAK') as wickets_lost, "
                      +" (select sum(runs) from cricket_db.dls where innings_id = 1 and match_name = 'INDIA v PAK') as innings_1_runs "
                      +" FROM cricket_db.dls_src a"
                      +" WHERE a.innings_id = 2 and a.keynumber > "
                      +" (select max(keynumber) from cricket_db.dls where match_name='INDIA v PAK') "
                      +" and a.match_name = 'INDIA v PAK'  order by a.keynumber) as x;";
     
      PreparedStatement pStmt = conn.prepareStatement(query);
      ResultSet rs = pStmt.executeQuery();
    
         while(rs.next()) {
            //Set values from result
            keynumber           = rs.getInt("keynumber");   
            comment_id          = rs.getInt("comment_id"); 
            match_id            = rs.getInt("match_id");  
            match_name          = rs.getString("match_name");   
            home_team         	= rs.getString("home_team");         
            away_team         	= rs.getString("away_team");         
            current_innings   	= rs.getString("current_innings");   
            innings_id        	= rs.getInt("innings_id");        
            overs             	= rs.getInt("overs");             
            ball              	= rs.getInt("ball");              
            runs              	= rs.getInt("runs");              
            shortText         	= rs.getString("shortText");       
            isBoundary        	= rs.getInt("isBoundary");        
            isWide            	= rs.getInt("isWide");            
            isNoball          	= rs.getInt("isNoball");          
            batsman1_id       	= rs.getInt("batsman1_id");       
            batsman1_name     	= rs.getString("batsman1_name");   
            batsman1_runs     	= rs.getInt("batsman1_runs");     
            batsman1_balls    	= rs.getInt("batsman1_balls");    
            bowler1_id        	= rs.getInt("bowler1_id");        
            bowler1_name      	= rs.getString("bowler1_name");    
            bowler1_overs     	= rs.getDouble("bowler1_overs");   
            bowler1_maidens   	= rs.getDouble("bowler1_maidens");   
            bowler1_runs      	= rs.getInt("bowler1_runs");      
            bowler1_wkts      	= rs.getInt("bowler1_wkts");      
            batsman2_id       	= rs.getInt("batsman2_id");       
            batsman2_name     	= rs.getString("batsman2_name");   
            batsman2_runs     	= rs.getInt("batsman2_runs");     
            batsman2_balls    	= rs.getInt("batsman2_balls");    
            bowler2_id        	= rs.getInt("bowler2_id");        
            bowler2_name      	= rs.getString("bowler2_name");     
            bowler2_overs     	= rs.getDouble("bowler2_overs");     
            bowler2_maidens   	= rs.getDouble("bowler2_maidens");   
            bowler2_runs      	= rs.getInt("bowler2_runs");      
            bowler2_wkts      	= rs.getInt("bowler2_wkts");      
            wicket_id         	= rs.getDouble("wicket_id");         
            wkt_batsman_name  	= rs.getString("wkt_batsman_name"); 
            wkt_bowler_name   	= rs.getString("wkt_bowler_name");  
            wkt_batsman_runs  	= rs.getDouble("wkt_batsman_runs");  
            wkt_batsman_balls 	= rs.getDouble("wkt_batsman_balls"); 
            wkt_text          	= rs.getString("wkt_text").toString();         
            isRetiredHurt     	= rs.getInt("isRetiredHurt");     
            text              	= rs.getString("text"); 
            overs_left          = rs.getInt("overs_left");
            wickets_lost        = rs.getInt("wickets_lost");
            innings_1_runs      = rs.getInt("innings_1_runs"); 
            innings_2_res       = rs.getDouble("innings_2_res");
            
            //Calculate DLS
            double temp = (100 - innings_2_res) / innings_1_res;
            innings_2_target = (int) (innings_1_runs * temp);

            //Insert into DLS table
            try {
            // PreparedStatements can be more efficient and project against injections.
            String insertStmt = "INSERT INTO cricket_db.dls (keynumber,comment_id,match_id,match_name,home_team,away_team, "
              + " current_innings,innings_id,overs,ball,runs,shortText,isBoundary,isWide,isNoball, "
              + " batsman1_id,batsman1_name,batsman1_runs,batsman1_balls,bowler1_id,bowler1_name, "
              + " bowler1_overs,bowler1_maidens,bowler1_runs,bowler1_wkts,batsman2_id,batsman2_name, "
              + " batsman2_runs,batsman2_balls,bowler2_id,bowler2_name,bowler2_overs,bowler2_maidens, "
              + " bowler2_runs,bowler2_wkts,wicket_id,wkt_batsman_name,wkt_bowler_name,wkt_batsman_runs, "
              + " wkt_batsman_balls,wkt_text,isRetiredHurt,text, innings_2_target) VALUES  "
              + " (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
            try (PreparedStatement valueStmt = conn.prepareStatement(insertStmt);) {
              valueStmt.setInt(1, keynumber);   
              valueStmt.setInt(2, comment_id); 
              valueStmt.setInt(3, match_id);  
              valueStmt.setString(4, match_name);
              valueStmt.setString(5, home_team);         
              valueStmt.setString(6, away_team);         
              valueStmt.setString(7, current_innings);   
              valueStmt.setInt(8, innings_id);        
              valueStmt.setInt(9, overs);             
              valueStmt.setInt(10, ball);              
              valueStmt.setInt(11, runs);              
              valueStmt.setString(12, shortText);       
              valueStmt.setInt(13, isBoundary);        
              valueStmt.setInt(14, isWide);            
              valueStmt.setInt(15, isNoball);          
              valueStmt.setInt(16, batsman1_id);       
              valueStmt.setString(17, batsman1_name);   
              valueStmt.setInt(18, batsman1_runs);     
              valueStmt.setInt(19, batsman1_balls);    
              valueStmt.setInt(20, bowler1_id);        
              valueStmt.setString(21, bowler1_name);    
              valueStmt.setDouble(22, bowler1_overs);   
              valueStmt.setDouble(23, bowler1_maidens);   
              valueStmt.setInt(24, bowler1_runs);      
              valueStmt.setInt(25, bowler1_wkts);      
              valueStmt.setInt(26, batsman2_id);       
              valueStmt.setString(27, batsman2_name);   
              valueStmt.setInt(28, batsman2_runs);     
              valueStmt.setInt(29, batsman2_balls);    
              valueStmt.setInt(30, bowler2_id);        
              valueStmt.setString(31, bowler2_name);     
              valueStmt.setDouble(32, bowler2_overs);     
              valueStmt.setDouble(33, bowler2_maidens);   
              valueStmt.setInt(34, bowler2_runs);      
              valueStmt.setInt(35, bowler2_wkts);      
              valueStmt.setDouble(36, wicket_id);         
              valueStmt.setString(37, wkt_batsman_name); 
              valueStmt.setString(38, wkt_bowler_name);  
              valueStmt.setDouble(39, wkt_batsman_runs);  
              valueStmt.setDouble(40, wkt_batsman_balls); 
              valueStmt.setString(41, wkt_text);         
              valueStmt.setInt(42, isRetiredHurt);     
              valueStmt.setString(43, text); 
              valueStmt.setInt(44, innings_2_target); 

              // Finally, execute the statement. If it fails, an error will be thrown.
              valueStmt.execute();
            }
          } catch (SQLException ex) {
            // If something goes wrong, handle the error in this section. This might involve retrying or
            // adjusting parameters depending on the situation.
            throw new RuntimeException(
          "Unable to process. Run Time Error in Insert.",
          ex);
          }
          try{
          TimeUnit.SECONDS.sleep(5);
          }catch(Exception e){
             throw new RuntimeException(
          "Unable to process. Run Time Error in creating delay.",
          e);
          }
         }
   
    } catch (SQLException ex) {
      // If something goes wrong, handle the error in this section. This might involve retrying or
      // adjusting parameters depending on the situation.
      throw new RuntimeException(
          "Unable to process. Run Time Error in Insert.",
          ex);
    }






  }
}

