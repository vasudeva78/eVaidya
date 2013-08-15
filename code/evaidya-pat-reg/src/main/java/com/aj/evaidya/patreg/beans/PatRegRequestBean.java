package com.aj.evaidya.patreg.beans;


public class PatRegRequestBean {

	private String nameText;
	private String dateText;
	private String monthText;
	private String yearText;
	private String address1Text;
	private String address2Text;
	private String stateId;
	private String pincode;
	private String tel1Text;
	private String tel2Text;
	private String nameId;
	public String getNameText() {
		return nameText;
	}
	public void setNameText(String nameText) {
		this.nameText = nameText;
	}
	public String getDateText() {
		return dateText;
	}
	public void setDateText(String dateText) {
		this.dateText = dateText;
	}
	public String getMonthText() {
		return monthText;
	}
	public void setMonthText(String monthText) {
		this.monthText = monthText;
	}
	public String getYearText() {
		return yearText;
	}
	public void setYearText(String yearText) {
		this.yearText = yearText;
	}
	public String getAddress1Text() {
		return address1Text;
	}
	public void setAddress1Text(String address1Text) {
		this.address1Text = address1Text;
	}
	public String getAddress2Text() {
		return address2Text;
	}
	public void setAddress2Text(String address2Text) {
		this.address2Text = address2Text;
	}
	public String getStateId() {
		return stateId;
	}
	public void setStateId(String stateId) {
		this.stateId = stateId;
	}
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	public String getTel1Text() {
		return tel1Text;
	}
	public void setTel1Text(String tel1Text) {
		this.tel1Text = tel1Text;
	}
	public String getTel2Text() {
		return tel2Text;
	}
	public void setTel2Text(String tel2Text) {
		this.tel2Text = tel2Text;
	}
	public String getNameId() {
		return nameId;
	}
	public void setNameId(String nameId) {
		this.nameId = nameId;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PatRegRequestBean [nameText=").append(nameText)
				.append(", dateText=").append(dateText).append(", monthText=")
				.append(monthText).append(", yearText=").append(yearText)
				.append(", address1Text=").append(address1Text)
				.append(", address2Text=").append(address2Text)
				.append(", stateId=").append(stateId).append(", pincode=")
				.append(pincode).append(", tel1Text=").append(tel1Text)
				.append(", tel2Text=").append(tel2Text).append(", nameId=")
				.append(nameId).append("]");
		return builder.toString();
	}

}
