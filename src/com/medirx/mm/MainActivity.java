package com.medirx.mm;

import java.util.ArrayList;
import java.util.List;

import br.com.dina.ui.widget.UITableView;
import br.com.dina.ui.widget.UITableView.ClickListener;

import com.actionbarsherlock.app.SherlockActivity;
import com.medirx.mm.cardview.CardViewActivity;
import com.medirx.mm.listview.view.ContactListActivity;
import com.medirx.mm.utils.DatabaseHandler;
import com.medirx.mm.utils.SqlHelper;



import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends SherlockActivity {
	
	
	
	DatabaseHandler db;
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		 db = new DatabaseHandler(this);
		 
	
	      /**
	         * Creating all buttons instances
	         * */
	        // Dashboard News feed button
	        Button btn_newsfeed = (Button) findViewById(R.id.btn_news_feed);
	        
	        // Dashboard Friends button
	        Button btn_friends = (Button) findViewById(R.id.btn_friends);
	        
	        // Dashboard Messages button
	        Button btn_messages = (Button) findViewById(R.id.btn_messages);
	        
	        // Dashboard Places button
	        Button btn_places = (Button) findViewById(R.id.btn_places);
	        
	        // Dashboard Events button
	        Button btn_events = (Button) findViewById(R.id.btn_events);
	        
	        // Dashboard Photos button
	        Button btn_photos = (Button) findViewById(R.id.btn_photos);
	        
	        /**
	         * Handling all button click events
	         * */
	        
	        // Listening to News Feed button click
	        btn_newsfeed.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View view) {
					// Launching News Feed Screen
					ContactListActivity.isFavList = false;
					Intent i = new Intent(getApplicationContext(), ContactListActivity.class);
					startActivity(i);
				}
			});
	        
	       // Listening Friends button click
	        btn_friends.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View view) {
					// Launching News Feed Screen
					ContactListActivity.isFavList = true;
					Intent i = new Intent(getApplicationContext(), ContactListActivity.class);
					startActivity(i);
				}
			});
	        
	        /*        // Listening Messages button click
	        btn_messages.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View view) {
					// Launching News Feed Screen
					Intent i = new Intent(getApplicationContext(), MessagesActivity.class);
					startActivity(i);
				}
			});
	        
	        // Listening to Places button click
	        btn_places.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View view) {
					// Launching News Feed Screen
					Intent i = new Intent(getApplicationContext(), PlacesActivity.class);
					startActivity(i);
				}
			});
	        
	        // Listening to Events button click
	        btn_events.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View view) {
					// Launching News Feed Screen
					Intent i = new Intent(getApplicationContext(), EventsActivity.class);
					startActivity(i);
				}
			});
	        
	        // Listening to Photos button click
	        btn_photos.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View view) {
					// Launching News Feed Screen
					Intent i = new Intent(getApplicationContext(), PhotosActivity.class);
					startActivity(i);
				}
			});*/
		
	        
	  
	     
	}
	
	/*   private void createList() {
	    	CustomClickListener listener = new CustomClickListener();
	    	tableView.setClickListener(listener);
	    	
	    	 List<SqlHelper> categories = db.getAllCategories();       
	    	 
	         for (SqlHelper sq : categories) {
	        	 
	        	 tableView.addBasicItem(sq.getCategoryName(), sq.getCategoryID() +"," +sq.getParentID());		
	        	 myid.add(sq.getCategoryID());    
	        	 mytitle.add(sq.getCategoryName()); 
	        	
	         }
	    }*/

/*	   private class CustomClickListener implements ClickListener {

			@Override
			public void onClick(int index) {
				Toast.makeText(MainActivity.this, "item clicked: " + index, Toast.LENGTH_SHORT).show();
				
				bundle.putInt("keyidgonderilen", myid.get(index));	
				bundle.putString("keystrgonderilen", mytitle.get(index));
				intent.putExtras(bundle);
				startActivity(intent);
			
			}
	    	
	    }*/
	
	   @Override
	    public void onBackPressed() {
	            super.onBackPressed();
	            this.finish();
	    }
	   
}
