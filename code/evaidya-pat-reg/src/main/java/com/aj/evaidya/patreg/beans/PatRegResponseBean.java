package com.aj.evaidya.patreg.beans;

public class PatRegResponseBean {
	private String status;
	private String message;
	private String nameText;
	private String qualiText;
	private String address1Text;
	private String address2Text;
	private String address3Text;
	private String stateId;
	private String pincode;
	private String tel1Text;
	private String tel2Text;
	private String email;
	private String patDay;
	private String patMon;
	private String patYear;
	
	
	public String getPatDay() {
		return patDay;
	}
	public void setPatDay(String patDay) {
		this.patDay = patDay;
	}
	public String getPatMon() {
		return patMon;
	}
	public void setPatMon(String patMon) {
		this.patMon = patMon;
	}
	public String getPatYear() {
		return patYear;
	}
	public void setPatYear(String patYear) {
		this.patYear = patYear;
	}
	public String getQualiText() {
		return qualiText;
	}
	public void setQualiText(String qualiText) {
		this.qualiText = qualiText;
	}
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNameText() {
		return nameText;
	}
	public void setNameText(String nameText) {
		this.nameText = nameText;
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
	public String getAddress3Text() {
		return address3Text;
	}
	public void setAddress3Text(String address3Text) {
		this.address3Text = address3Text;
	}
	public String getStateId() {
		return stateId;
	}
	public void setStateId(String stateId) {
		this.stateId = stateId;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PatRegResponseBean [status=").append(status)
				.append(", message=").append(message).append(", nameText=")
				.append(nameText).append(", qualiText=").append(qualiText)
				.append(", address1Text=").append(address1Text)
				.append(", address2Text=").append(address2Text)
				.append(", address3Text=").append(address3Text)
				.append(", stateId=").append(stateId).append(", pincode=")
				.append(pincode).append(", tel1Text=").append(tel1Text)
				.append(", tel2Text=").append(tel2Text).append(", email=")
				.append(email).append(", patDay=").append(patDay)
				.append(", patMon=").append(patMon).append(", patYear=")
				.append(patYear).append("]");
		return builder.toString();
	}
	
}
