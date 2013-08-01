package com.aj.evaidya.docreg.beans;


public class DocRegRequestBean {

	private String nameText;
	private String qualiText;
	private String address1Text;
	private String address2Text;
	private String address3Text;
	private String stateId;
	private String pincode;
	private String tel1Text;
	private String tel2Text;
	private String nameId;
	private String email;
	private String dbUrl;
	private String dbUsername;
	private String dbPwd;
	
	
	public String getQualiText() {
		return qualiText;
	}
	public void setQualiText(String qualiText) {
		this.qualiText = qualiText;
	}
	public String getNameId() {
		return nameId;
	}
	public void setNameId(String nameId) {
		this.nameId = nameId;
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
	
	public String getDbUrl() {
		return dbUrl;
	}
	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}
	public String getDbUsername() {
		return dbUsername;
	}
	public void setDbUsername(String dbUsername) {
		this.dbUsername = dbUsername;
	}
	public String getDbPwd() {
		return dbPwd;
	}
	public void setDbPwd(String dbPwd) {
		this.dbPwd = dbPwd;
	}
	@Override
	public String toString() {
		return "DocRegRequestBean ["
				+ (nameText != null ? "nameText=" + nameText + ", " : "")
				+ (qualiText != null ? "qualiText=" + qualiText + ", " : "")
				+ (address1Text != null ? "address1Text=" + address1Text + ", "
						: "")
				+ (address2Text != null ? "address2Text=" + address2Text + ", "
						: "")
				+ (address3Text != null ? "address3Text=" + address3Text + ", "
						: "")
				+ (stateId != null ? "stateId=" + stateId + ", " : "")
				+ (pincode != null ? "pincode=" + pincode + ", " : "")
				+ (tel1Text != null ? "tel1Text=" + tel1Text + ", " : "")
				+ (tel2Text != null ? "tel2Text=" + tel2Text + ", " : "")
				+ (nameId != null ? "nameId=" + nameId + ", " : "")
				+ (email != null ? "email=" + email + ", " : "")
				+ (dbUrl != null ? "dbUrl=" + dbUrl + ", " : "")
				+ (dbUsername != null ? "dbUsername=" + dbUsername + ", " : "")
				+ (dbPwd != null ? "dbPwd=" + dbPwd : "") + "]";
	}
	
	
}
