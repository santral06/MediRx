package com.medirx.mm.listview.example.adapter;

import java.util.List;

import com.medirx.mm.R;
import com.medirx.mm.listview.example.models.ExampleContactItem;
import com.medirx.mm.listview.widget.ContactItemInterface;
import com.medirx.mm.listview.widget.ContactListAdapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;



public class ExampleContactAdapter extends ContactListAdapter{

	Context context;
	public ExampleContactAdapter(Context _context, int _resource,
			List<ContactItemInterface> _items) {
		super(_context, _resource, _items);
		context = _context;
	}
	
	// override this for custom drawing
	public void populateDataForRow(View parentView, ContactItemInterface item , int position){
		// default just draw the item only
		View infoView = parentView.findViewById(R.id.infoRowContainer);
		TextView fullNameView = (TextView)infoView.findViewById(R.id.fullNameView);
		TextView nicknameView = (TextView)infoView.findViewById(R.id.nickNameView);
		
		 ImageView imageview= (ImageView) infoView.findViewById(R.id.favImg);  
	    		    	
		
		nicknameView.setText(item.getItemForIndex());
	
		
		if(item instanceof ExampleContactItem){
			ExampleContactItem contactItem = (ExampleContactItem)item;
			fullNameView.setText(contactItem.getisFav()+"Full name: " + contactItem.getFullName());
			
			if (contactItem.getisFav() == 0){
				imageview.setImageDrawable(context.getResources().getDrawable(R.drawable.nonfav));
			} else {
				imageview.setImageDrawable(context.getResources().getDrawable(R.drawable.fav));
			}
		}
		
	}

}
