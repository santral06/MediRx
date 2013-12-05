package com.medirx.mm.cardview;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fima.cardsui.objects.CardStack;
import com.fima.cardsui.views.CardUI;
import com.medirx.mm.R;
import com.medirx.mm.utils.DatabaseHandler;
import com.medirx.mm.utils.SqlHelper;

public class TreatmentFragment extends Fragment{
	
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
		stackPlay.setTitle("Tedavi");
		mCardView.addStack(stackPlay);	
		
     	
     	DatabaseHandler db = new DatabaseHandler(getActivity());
     	 List<SqlHelper> selectedDiseases = db.getSelectedDiseases(idgelen);       
   	 
        for (SqlHelper sd : selectedDiseases) {      	 
        
    		
    		// RECETE
    		mCardView
				.addCard(new MyPlayCard(
						"RX " +strgelen,
						sd.getTreatment(),
						"#e00707", "#e00707", true, false));     
            
       	
        }      	

		
		// draw cards
		mCardView.refresh();
	    
	}
	
	

}
