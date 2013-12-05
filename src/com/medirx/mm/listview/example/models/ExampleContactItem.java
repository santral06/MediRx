package com.medirx.mm.listview.example.models;

import com.medirx.mm.listview.widget.ContactItemInterface;



public class ExampleContactItem implements ContactItemInterface{

	private String nickName;
	private String fullName;
	private int itemId;
	int isFav;
	
	public ExampleContactItem(String nickName, String fullName, int itemId, int isFav) {
		super();
		this.nickName = nickName;
		this.setFullName(fullName);
		this.itemId = itemId;
		this.isFav = isFav;
	}

	// index the list by nickname
	@Override
	public String getItemForIndex() {
		return nickName;
	}

	public String getNickName() {
		return nickName;
	}
	
	public int getitemId() {
		return itemId;
	}
	
	public int getisFav() {
		return isFav;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	public void setitemId(int itemId) {
		this.itemId = itemId;
	}
	public void setisFav(int isFav) {
		this.isFav = isFav;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

}
