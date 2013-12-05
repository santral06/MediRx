package com.medirx.mm.utils;

public class SqlHelper {
	//private variables
		int _id;
		int _catid;		
		String _diagnose;
		String _treatment;
		String _star;
		int _fav;	
		
		
		int _CategoryID;
		int _ParentID;
		String _CategoryName;
		
		
		// Empty constructor
		public SqlHelper(){
			
		}
		// constructor
		public SqlHelper(int id, int catid, String diagnose, String treatment, String star){
			this._id = id;
			this._catid = catid;
			this._diagnose = diagnose;
			this._treatment = treatment;
			this._star = star;
			
		}
		
		// constructor
		public SqlHelper(String diagnose, String treatment){
			this._diagnose = diagnose;
			this._treatment = treatment;
		}
		// getting ID
		public int getID(){
			return this._id;
		}
		
		// setting id
		public void setID(int id){
			this._id = id;
		}
		
		// getting ID
		public int getCatID(){
			return this._catid;
		}
		
		// setting id
		public void setCatID(int catid){
			this._catid = catid;
		}
		
		
		// getting name
		public String getDiagnose(){
			return this._diagnose;
		}
		
		// setting name
		public void setDiagnose(String diagnose){
			this._diagnose = diagnose;
		}
		
		// getting phone number
		public String getTreatment(){
			return this._treatment;
		}
		
		// setting phone number
		public void setTreatment(String treatment){
			this._treatment = treatment;
		}
		
		// getting phone number
		public String getStar(){
			return this._treatment;
		}
		
		// setting phone number
		public void setStar(String star){
			this._star = star;
		}
		
		// getting phone number
		public int getFav(){
			return this._fav;
		}
		
		// setting phone number
		public void setFav (int fav){
			this._fav = fav;
		}
		
		// getting CategoryID
				public int getCategoryID(){
					return this._CategoryID;
				}
				
				// setting CategoryID
				public void setCategoryID(int CategoryID){
					this._CategoryID = CategoryID;
				}
				
				// getting ParentID
				public int getParentID(){
					return this._ParentID;
				}
				
				// setting ParentID
				public void setParentID(int ParentID){
					this._ParentID = ParentID;
				}
				// getting CategoryName
				public String getCategoryName(){
					return this._CategoryName;
				}
				
				// setting CategoryName
				public void setCategoryName(String CategoryName){
					this._CategoryName = CategoryName;
				}
		
}

