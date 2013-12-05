package com.medirx.mm.cardview;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.fima.cardsui.objects.CardStack;
import com.fima.cardsui.views.CardUI;
import com.medirx.mm.R;

import com.medirx.mm.utils.DatabaseHandler;
import com.medirx.mm.utils.SqlHelper;

public class DiagnoseFragment extends Fragment{
	

	private CardUI mCardView;
	 int idgelen;
	 String strgelen;
	
	
	  @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    return inflater.inflate(R.layout.card_diagnose, null);
	  }
	 
	 
	  @Override
	  public void onViewCreated(View view, Bundle savedInstanceState) {
	    super.onViewCreated(view, savedInstanceState);
		
		   super.onCreate(savedInstanceState);
		
		// Anasayfadan veri al
		Bundle veriler = getActivity().getIntent().getExtras();
      	 idgelen = veriler.getInt("keyidgonderilen");
        strgelen = veriler.getString("keystrgonderilen");
      	
		// init CardView
		mCardView = (CardUI) view.findViewById(R.id.cardsview);
		mCardView.setSwipeable(false);		

		CardStack stackPlay = new CardStack();
		stackPlay.setTitle("Taný");
		mCardView.addStack(stackPlay);	
		
      	
      	DatabaseHandler db = new DatabaseHandler(getActivity());
      	 List<SqlHelper> selectedDiseases = db.getSelectedDiseases(idgelen);       
    	 
         for (SqlHelper sd : selectedDiseases) {
        	 
       if (sd.getDiagnose() != null && !sd.getDiagnose().isEmpty()) {
        	 // add one card
     		mCardView
     				.addCard(new MyPlayCard(
     						strgelen,
     						sd.getDiagnose(),
     						"#33b6ea", "#33b6ea", true, false));     
       }
         }      	

		
		// draw cards
		mCardView.refresh();
	    
	}
	  

}
