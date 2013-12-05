package com.medirx.mm.listview.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.actionbarsherlock.app.SherlockActivity;
import com.medirx.mm.MainActivity;
import com.medirx.mm.R;
import com.medirx.mm.cardview.CardViewActivity;
import com.medirx.mm.listview.example.adapter.ExampleContactAdapter;
import com.medirx.mm.listview.example.models.ExampleContactItem;

import com.medirx.mm.listview.widget.ContactItemInterface;
import com.medirx.mm.utils.DatabaseHandler;
import com.medirx.mm.utils.SqlHelper;



import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class ContactListActivity extends SherlockActivity implements TextWatcher {

	private ExampleContactListView listview;
	
	private EditText searchBox;
	private String searchString;
	
	private Object searchLock = new Object();
	boolean inSearchMode = false;
	
	private final static String TAG = "ContactListActivity";
	
	List<ContactItemInterface> contactList;
	List<ContactItemInterface> filterList;
	private SearchListTask curSearchTask = null;
	
	private Intent intent;
	private Bundle bundle;
	private static ArrayList<Integer> myid = new ArrayList<Integer>();
	private static ArrayList<String> mytitle = new ArrayList<String>();
	
	 static DatabaseHandler db;
	 
	 public static boolean isFavList = false;
	
	 static ImageView imageview ;


	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_list);
                
   	 intent = new Intent(ContactListActivity.this, CardViewActivity.class);  
     bundle = new Bundle(); 
     
         db = new DatabaseHandler(this);
		
		filterList = new ArrayList<ContactItemInterface>();
		contactList = getSampleContactList();
		
		ExampleContactAdapter adapter = new ExampleContactAdapter(this, R.layout.listview_contact_item, contactList);
		
		listview = (ExampleContactListView) this.findViewById(R.id.listview);
		listview.setFastScrollEnabled(true);
		listview.setAdapter(adapter);
		
		// use this to process individual clicks
		// cannot use OnClickListener as the touch event is overrided by IndexScroller
		// use last touch X and Y if want to handle click for an individual item within the row
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView parent, View v, int position,
					long id) {
				
				List<ContactItemInterface> searchList = inSearchMode ? filterList : contactList ;
				float lastTouchX = listview.getScroller().getLastTouchDownEventX();
				if(lastTouchX < 45 && lastTouchX > -1){
					 Toast.makeText(ContactListActivity.this, "Resim ( " + searchList.get(position).getItemForIndex()  + ")", Toast.LENGTH_SHORT).show();
				}
				else					
					 Toast.makeText(ContactListActivity.this, "Konu: " + searchList.get(position).getItemForIndex() , Toast.LENGTH_SHORT).show();
				
				bundle.putInt("keyidgonderilen", searchList.get(position).getitemId() );	
				bundle.putString("keystrgonderilen", searchList.get(position).getItemForIndex());
				bundle.putInt("keyFavgonderilen", searchList.get(position).getisFav());
				
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		
		
		searchBox = (EditText) findViewById(R.id.input_search_query);
		searchBox.addTextChangedListener(this);
    }
    
    public static List<ContactItemInterface> getSampleContactList(){
		 List<ContactItemInterface>  list = new  ArrayList<ContactItemInterface> ();		
		 List<SqlHelper> categories;
		 
		
	
	     	
		 if ( !isFavList){	 	 
	
		 categories = db.getAllCategories();  	
		 
		 } else { 
			
			categories = db.getFavs();    
		 }
   	 
		   for (SqlHelper sq : categories) {
		       	 list.add(new ExampleContactItem(sq.getCategoryName(), sq.getCategoryName()+ sq.getCategoryID() +"," +sq.getParentID(), sq.getCategoryID(), sq.getFav() ) ); 
		       	 
		    
		       	 }
		       	 
		 
		 return list;
	}

    @Override
	public void afterTextChanged(Editable s) {
		

		searchString = searchBox.getText().toString().trim().toUpperCase();

		if(curSearchTask!=null && curSearchTask.getStatus() != AsyncTask.Status.FINISHED)
		{
			try{
				curSearchTask.cancel(true);
			}
			catch(Exception e){
				Log.i(TAG, "Fail to cancel running search task");
			}
			
		}
		curSearchTask = new SearchListTask();
		curSearchTask.execute(searchString); // put it in a task so that ui is not freeze
    }
    
    
    @Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
    	// do nothing
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// do nothing
	}
    
	private class SearchListTask extends AsyncTask<String, Void, String> {

		
		
		@Override
		protected String doInBackground(String... params) {
			filterList.clear();
			
			String keyword = params[0];
			
			inSearchMode = (keyword.length() > 0);

			if (inSearchMode) {
				// get all the items matching this
				for (ContactItemInterface item : contactList) {
					ExampleContactItem contact = (ExampleContactItem)item;
					
					if ((contact.getFullName().toUpperCase().indexOf(keyword) > -1) ) {
						filterList.add(item);
					}

				}


			} 
			return null;
		}
		
		protected void onPostExecute(String result) {
			
			synchronized(searchLock)
			{
			
				if(inSearchMode){
					
					ExampleContactAdapter adapter = new ExampleContactAdapter(ContactListActivity.this, R.layout.listview_contact_item, filterList);
					adapter.setInSearchMode(true);
					listview.setInSearchMode(true);
					listview.setAdapter(adapter);
				}
				else{
					ExampleContactAdapter adapter = new ExampleContactAdapter(ContactListActivity.this, R.layout.listview_contact_item, contactList);
					adapter.setInSearchMode(false);
					listview.setInSearchMode(false);
					listview.setAdapter(adapter);
				}
			}
			
		}
	}
	
	 @Override
	 public void onBackPressed() {
		 finish();
	     startActivity(new Intent(this, MainActivity.class));
	 }
}
