package com.medirx.mm.utils;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;



import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper  {
	
	/*	MediRx XML parser*/
	private static final String DATABASE_NAME="data";
	private static final int DATABASE_VERSION = 1;
	SQLiteDatabase db;
	
	// Tables
	protected static final String TABLE_NAME_DISEASE = "DiseaseTable";
	protected static final String TABLE_NAME_TOPCATEGORY = "CategoryTable";
	
	
	// Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_CATID = "CategoryId";
	private static final String KEY_DIAGNOSE = "Diagnose";
	private static final String KEY_TREATMENT = "Treatment";
	private static final String KEY_STAR = "Star";
	
	private static final String KEY_PARENTID = "ParentId";
	private static final String KEY_CATNAME = "CategoryName";
	private static final String KEY_FAV = "Fav";
	
	
	// XML
	// All static variables
	static final String URL = "https://dl.dropboxusercontent.com/u/73707153/datas.xml";
	// XML node keys
	static final String TAG_ITEM = "disease"; // parent node
	static final String TAG_TOPCAT = "top_category";
	static final String TAG_DISEASE = "disease_name";
	static final String TAG_DIAGNOSE = "diagnose";
	static final String TAG_TREATMENT = "treatment";
	
	
	// CREATE CATEGORY TABLE
	private static final String CREATE_TABLE_TOPCATEGORY = 
			"CREATE table " + TABLE_NAME_TOPCATEGORY +
			"(" +KEY_ID + " integer primary key autoincrement," +	
			KEY_PARENTID + " integer not null DEFAULT '0', " +				
			KEY_CATNAME + " text not null, " +
			KEY_FAV + " integer not null DEFAULT '0' );" ;

	// CREATE DISEASE TABLE
		private static final String CREATE_TABLE_DISEASE = 
				"CREATE table " + TABLE_NAME_DISEASE +
				"("+ KEY_ID +" integer primary key autoincrement," +				
				KEY_CATID + " integer, " +				
				KEY_DIAGNOSE +" text not null," +
				KEY_TREATMENT +" text not null," +			
				KEY_STAR +" REAL DEFAULT '0.0');" ;
		private static final String TAG = null;
	
		public DatabaseHandler(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		  
		  
		  public void onCreate(SQLiteDatabase db) {
			  
			  // Will delete or asynctask
			  StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			  StrictMode.setThreadPolicy(policy);
			
			  // Create Tables
	          db.execSQL(CREATE_TABLE_TOPCATEGORY);
	          db.execSQL(CREATE_TABLE_DISEASE);
	             
	          try {
				insertData(db, URL);
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	          
	          // Import xml to db
	         // insertDatas(db);
	          
	       
	          
	          
		  }
	
		    @Override
		    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
		        // Drop older table if existed
		        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_DISEASE);
		        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_TOPCATEGORY);
		        // Create tables again
		        onCreate(db);
		    }
		    
		    /**
			 * All CRUD(Create, Read, Update, Delete) Operations
			 */

	 // Adding new disease
			 public void addDisease(SqlHelper sqlhelper) {
				SQLiteDatabase db = this.getWritableDatabase();

				ContentValues values = new ContentValues();
				values.put(KEY_DIAGNOSE, sqlhelper.getDiagnose()); 
				values.put(KEY_TREATMENT, sqlhelper.getTreatment()); 

				// Inserting Row
				db.insert(TABLE_NAME_DISEASE, null, values);
				db.close(); // Closing database connection
			}

			// Getting single contact
			SqlHelper getDisease(int id) {
				SQLiteDatabase db = this.getReadableDatabase();

				Cursor cursor = db.query(TABLE_NAME_DISEASE, new String[] { KEY_ID,
						KEY_CATID, KEY_DIAGNOSE, KEY_TREATMENT, KEY_STAR }, KEY_ID + "=?",
						new String[] { String.valueOf(id) }, null, null, null, null);
				if (cursor != null)
					cursor.moveToFirst();

				SqlHelper sqlhelper = new SqlHelper(cursor.getInt(0), cursor.getInt(1),
						cursor.getString(2), cursor.getString(3), cursor.getString(4) );
				 cursor.close();
				// return contact
				return sqlhelper;
			}
			
			
			// Getting All Categories
			public List<SqlHelper> getAllCategories() {
				List<SqlHelper> categoryList = new ArrayList<SqlHelper>();
				// Select All Query
				String selectQuery = "SELECT  * FROM " + TABLE_NAME_TOPCATEGORY;

				SQLiteDatabase db = this.getWritableDatabase();
				Cursor cursor = db.rawQuery(selectQuery, null);

				// looping through all rows and adding to list
				if (cursor.moveToFirst()) {
					do {
						SqlHelper sqlhelper = new SqlHelper();
						sqlhelper.setCategoryID(Integer.parseInt(cursor.getString(0)));
						sqlhelper.setParentID(Integer.parseInt(cursor.getString(1)));
						sqlhelper.setCategoryName(cursor.getString(2));
						sqlhelper.setFav(cursor.getInt(3));
						
						// Adding contact to list
						categoryList.add(sqlhelper);
					} while (cursor.moveToNext());
				}
                 cursor.close();
				// return contact list
                 db.close();
				return categoryList;
			}
			
			// Getting Selected Disease
						public List<SqlHelper> getSelectedDiseases(int selectedId) {
							List<SqlHelper> selectedDiseaseList = new ArrayList<SqlHelper>();
							// Select All Query
							String selectQuery = "SELECT  * FROM " + TABLE_NAME_DISEASE + " WHERE DiseaseTable.CategoryId = '"+ selectedId +"'" ;

							SQLiteDatabase db = this.getWritableDatabase();
							Cursor cursor = db.rawQuery(selectQuery, null);

							// looping through all rows and adding to list
							if (cursor.moveToFirst()) {
								do {
									SqlHelper sqlhelper = new SqlHelper();
									sqlhelper.setID(Integer.parseInt(cursor.getString(0)));
									sqlhelper.setCatID(Integer.parseInt(cursor.getString(1)));
									sqlhelper.setDiagnose(cursor.getString(2));
									sqlhelper.setTreatment(cursor.getString(3));
									sqlhelper.setStar(cursor.getString(4));
									
									// Adding contact to list
									selectedDiseaseList.add(sqlhelper);
								} while (cursor.moveToNext());
							}
							 cursor.close();
							 db.close();
							// return contact list
							return selectedDiseaseList;
						}
			
			
			// Getting All Diseases
			public List<SqlHelper> getAllDiseases() {
				List<SqlHelper> diseaseList = new ArrayList<SqlHelper>();
				// Select All Query
				String selectQuery = "SELECT  * FROM " + TABLE_NAME_DISEASE;

				SQLiteDatabase db = this.getWritableDatabase();
				Cursor cursor = db.rawQuery(selectQuery, null);

				// looping through all rows and adding to list
				if (cursor.moveToFirst()) {
					do {
						SqlHelper sqlhelper = new SqlHelper();
						sqlhelper.setID(Integer.parseInt(cursor.getString(0)));
						sqlhelper.setCatID(Integer.parseInt(cursor.getString(1)));
						sqlhelper.setDiagnose(cursor.getString(2));
						sqlhelper.setTreatment(cursor.getString(3));
						sqlhelper.setStar(cursor.getString(4));
				
						// Adding contact to list
						diseaseList.add(sqlhelper);
					} while (cursor.moveToNext());
				}
				 cursor.close();
				 db.close();
				// return contact list				 
				return diseaseList;
			}
			
			
			// Getting All Diseases
						public List<SqlHelper> getFavs() {
							List<SqlHelper> favList = new ArrayList<SqlHelper>();
							// Select All Query
							String selectQuery = "SELECT  * FROM " + TABLE_NAME_TOPCATEGORY + " WHERE CategoryTable.Fav = '1'" ;

							SQLiteDatabase db = this.getWritableDatabase();
							Cursor cursor = db.rawQuery(selectQuery, null);

							// looping through all rows and adding to list
							if (cursor.moveToFirst()) {
								do {
									SqlHelper sqlhelper = new SqlHelper();
									sqlhelper.setCategoryID(Integer.parseInt(cursor.getString(0)));
									sqlhelper.setParentID(Integer.parseInt(cursor.getString(1)));
									sqlhelper.setCategoryName(cursor.getString(2));
									sqlhelper.setFav(cursor.getInt(3));
									
									// Adding contact to list
									favList.add(sqlhelper);
								} while (cursor.moveToNext());
							}
			                 cursor.close();
							// return contact list
			                 db.close();
							return favList;
						}
			
			
						// Updating single contact
						public int updateFav(int id, int isChecked) {
							SQLiteDatabase db = this.getWritableDatabase();

							ContentValues values = new ContentValues();
							values.put(KEY_FAV, isChecked);								
							
							// updating row
							return db.update(TABLE_NAME_TOPCATEGORY, values, KEY_ID + " = " +id, null);
							
						}
			
						
						
						
						
						

			// Updating single contact
			public int updateDisease(SqlHelper sqlhelper) {
				SQLiteDatabase db = this.getWritableDatabase();

				ContentValues values = new ContentValues();
				values.put(KEY_DIAGNOSE, sqlhelper.getDiagnose());
				values.put(KEY_TREATMENT, sqlhelper.getTreatment());		
				
				// updating row
				return db.update(TABLE_NAME_DISEASE, values, KEY_ID + " = ?",
						new String[] { String.valueOf(sqlhelper.getID()) });
				
			}

			// Deleting single contact
			public void deleteDisease(SqlHelper sqlhelper) {
				SQLiteDatabase db = this.getWritableDatabase();
				db.delete(TABLE_NAME_DISEASE, KEY_ID + " = ?",
						new String[] { String.valueOf(sqlhelper.getID()) });
				db.close();
			}


			// Getting contacts Count
			public int getDiseasesCount() {
				String countQuery = "SELECT  * FROM " + TABLE_NAME_DISEASE;
				SQLiteDatabase db = this.getReadableDatabase();
				Cursor cursor = db.rawQuery(countQuery, null);
				cursor.close();

				// return count
				return cursor.getCount();
			}
			
			
			
			
			
			// MediRX XMLPARSER
			 public void insertData(SQLiteDatabase db, String url)  throws XmlPullParserException, IOException, URISyntaxException  {			   	
	
			          ContentValues _Values = new ContentValues();  
			          ContentValues _Categories = new ContentValues(); 			     
			          
			          
			          XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
	        		  factory.setNamespaceAware(true);
	        		  XmlPullParser xpp = factory.newPullParser(); 
	        		  xpp.setInput(new InputStreamReader(getUrlData(url))); 
			  
			          try
			          {
			              //Check for end of document
			              int eventType = xpp.getEventType();		             			          	 
			              
			              while (eventType != XmlPullParser.END_DOCUMENT) {		              				            
			            	  
			                  if ((eventType == XmlPullParser.START_TAG) &&(xpp.getName().equals("Disease"))){			                	  
			                		
			      				String _TopCategory = xpp.getAttributeValue(null, TAG_TOPCAT);
			                      String _DiseaseName = xpp.getAttributeValue(null, TAG_DISEASE);
			                      String _Diagnose = xpp.getAttributeValue(null, TAG_DIAGNOSE);
			                      String _Treatment = xpp.getAttributeValue(null, TAG_TREATMENT);	
			                      
			                     
			                      // Top category yoksa ekle
			                      final String query = "SELECT  CategoryTable.id, CategoryTable.ParentId, CategoryTable.CategoryName " +
			                               " FROM CategoryTable" +
			                               " WHERE CategoryTable.CategoryName = '"+ _TopCategory  
			                               +"'";			                                                       
			                   	
			                   	Cursor cursorKayita = db.rawQuery(query, null);
			                   	
			                   	if ( cursorKayita.moveToFirst() ) {	                   				                   
			                    		
			                   	} else {
			                   		_Categories.put("CategoryName", _TopCategory);
			                   		_Categories.put("ParentId", "0");
			                  		  db.insert("CategoryTable", null, _Categories); 
			                  		
			                   
			                   	}			                   
			                   	 cursorKayita.close();      
			                   	// 
			                   	
			                   	 // Diseasename yoksa parentid ile ekle
			                   	final String queryc =  "SELECT  CategoryTable.id, CategoryTable.ParentId, CategoryTable.CategoryName " +
			                              " FROM CategoryTable" +
			                              " WHERE CategoryTable.CategoryName = '"+ _DiseaseName +"'" ;
			                      
			                  	Cursor cursorKayitc = db.rawQuery(queryc, null);
			                  	
			                 	if ( cursorKayitc.moveToFirst() ) {
			                   	    // its not empty
			                   	} else {
			                   	
			                         final String queryb = "SELECT  * " +
			                                 " FROM CategoryTable" +
			                                 " WHERE CategoryTable.CategoryName = '"+ _TopCategory  
			                                 +"'";	                     			                     	
			                     	Cursor cursorKayitb = db.rawQuery(queryb, null);    
			                     
			                     	if ( cursorKayitb.moveToFirst() ) {
			                     		
			                  		int subcatID = cursorKayitb.getInt(0);
			                    		_Categories.put("CategoryName", _DiseaseName);
			                    		_Categories.put("ParentId", subcatID);
			                    		db.insert("CategoryTable", null, _Categories); 		                    		
			                     
			                    	}
			                      	cursorKayitb.close();			                   
			                   	}			                    	 
			                 	cursorKayitc.close();
			                   	 
			                      //
			                 	
			                 	// DiseaseTable taný tedavi ekle
			                 	
			               	  final String queryd = "SELECT  CategoryTable.id, CategoryTable.ParentId, CategoryTable.CategoryName " +
			                            " FROM CategoryTable" +
			                            " WHERE CategoryTable.CategoryName = '"+ _DiseaseName +"'" ;
			                                                    
			                	
			                	Cursor cursorKayitd = db.rawQuery(queryd, null);
			                	int catID = 0;
			              	if ( cursorKayitd.moveToFirst() ) {
			              		catID = cursorKayitd.getInt(0);
			              	}
			                 	
			                   _Values.put(KEY_CATID, catID);
			              	 _Values.put(KEY_DIAGNOSE, _Diagnose);
			                   _Values.put(KEY_TREATMENT, _Treatment);
			                   db.insert(TABLE_NAME_DISEASE, null, _Values);       
			                	
			                   cursorKayitd.close();
			                  }
			                  
			                  
			                  
			                  eventType =  xpp.next();
			              }
			          }
			          //Catch errors
			          catch (XmlPullParserException e)
			          {       
			              Log.e(TAG, e.getMessage(), e);
			          }
			          catch (IOException e)
			          {
			              Log.e(TAG, e.getMessage(), e);
			               
			          }           
			          finally
			          {           
			              //Close the xml file
			             
			          }
			    	
			     //     db.close();
			        
			    }			    
			
				public  InputStream getUrlData(String url) 
						throws URISyntaxException, ClientProtocolException, IOException {
						  DefaultHttpClient client = new DefaultHttpClient();
						  HttpGet method = new HttpGet(new URI(url));
						  HttpResponse res = client.execute(method);
						  return  res.getEntity().getContent();
						}	
			 
}
