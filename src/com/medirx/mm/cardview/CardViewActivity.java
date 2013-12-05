package com.medirx.mm.cardview;
import android.content.Intent;
import android.os.Bundle;
import com.medirx.mm.R;
import com.medirx.mm.listview.view.ContactListActivity;
import com.medirx.mm.utils.DatabaseHandler;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.viewpagerindicator.TitlePageIndicator;

public class CardViewActivity extends SherlockFragmentActivity {
	
	int favGelen ;
	int idGelen ;
	DatabaseHandler db;

	 
	 ViewPager CardViewPager; 
	 enum PageInfo {

		    Crouton(R.string.diagnose), About(R.string.treatment);

		    int titleResId;

		    PageInfo(int titleResId) {
		      this.titleResId = titleResId;
		    }
		  }
	 
	 
	 
	 
	 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		   super.onCreate(savedInstanceState);
		    setContentView(R.layout.card_main);
		    CardViewPager = (ViewPager) findViewById(R.id.cardview_pager);
		    CardViewPager.setAdapter(new CardViewPagerAdapter(getSupportFragmentManager()));
		    ((TitlePageIndicator) findViewById(R.id.titles)).setViewPager(CardViewPager);
		    
		    
		    setBarFavCheckBox();
		    
		    

	    
	}


	   public void setBarFavCheckBox() {
	    	
			Bundle veriler = this.getIntent().getExtras();
	      	 favGelen = veriler.getInt("keyFavgonderilen");
	      	 idGelen = veriler.getInt("keyidgonderilen");
	      
		   
		     View customNav = LayoutInflater.from(this).inflate(R.layout.custom_checkbox, null);
		     CheckBox Fav = (CheckBox) customNav.findViewById(R.id.markReview);
		     
		     if (favGelen == 0) {
		    	   Fav.setChecked(false);
		     } else { Fav.setChecked(true); }
		 
		 	 db = new DatabaseHandler(this);
		 	
		     
		    Fav.setOnCheckedChangeListener(new OnCheckedChangeListener() { 

		    	 @Override 
		    	 public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) { 
		    		 
		    		 int val = isChecked? 1 : 0;
		    			 db.updateFav(idGelen, val);
		    		 if (isChecked) {
		    			 Toast.makeText(CardViewActivity.this,"Favorilere Eklendi", Toast.LENGTH_SHORT).show();
		    		 }
		    		 else { Toast.makeText(CardViewActivity.this,"Favorilerden Çýkarýldý", Toast.LENGTH_SHORT).show(); }

		    	 }
		    	 
		     });
		     
		        //Attach to the action bar
		        getSupportActionBar().setCustomView(customNav);
		        getSupportActionBar().setDisplayShowCustomEnabled(true);
	    	
	    }
	
	
	
	
	 class CardViewPagerAdapter extends FragmentPagerAdapter {

		    public CardViewPagerAdapter(FragmentManager fm) {
		      super(fm);
		    }

		    @Override
		    public Fragment getItem(int position) {

		      if (PageInfo.Crouton.ordinal() == position) {
		        return new DiagnoseFragment();
		      } else if (PageInfo.About.ordinal() == position) {
		        return new TreatmentFragment();
		      }
		      return null;
		    }

		    @Override
		    public int getCount() {
		      return PageInfo.values().length;
		    }

		    @Override
		    public CharSequence getPageTitle(int position) {
		      return CardViewActivity.this.getString(PageInfo.values()[position].titleResId);
		    }
		  }
	
	
	 @Override
	 public void onBackPressed() {
		 finish();
	     startActivity(new Intent(this, ContactListActivity.class));
	 }
	 
}
