package com.aj.evaidya.patreg.beans;


public class PatRegRequestBean {

	private String nameText;
	private String dateText;
	private String monthText;
	private String yearText;
	private String address1Text;
	private String address2Text;
	private String stateId;
	private String stateText;
	private String pincode;
	private String sex;
	private String tel1Text;
	private String tel2Text;
	private String nameId;
	private String fatNameText;
	private String xlFilePath;
	private String excelRowNum;
	private String dateOfBirth;
	
	
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getStateText() {
		return stateText;
	}
	public void setStateText(String stateText) {
		this.stateText = stateText;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getFatNameText() {
		return fatNameText;
	}
	public void setFatNameText(String fatNameText) {
		this.fatNameText = fatNameText;
	}
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
	
	public String getXlFilePath() {
		return xlFilePath;
	}
	public void setXlFilePath(String xlFilePath) {
		this.xlFilePath = xlFilePath;
	}
	
	public String getExcelRowNum() {
		return excelRowNum;
	}
	public void setExcelRowNum(String excelRowNum) {
		this.excelRowNum = excelRowNum;
	}
	@Override
	public String toString() {
		return "PatRegRequestBean ["
				+ (nameText != null ? "nameText=" + nameText + ", " : "")
				+ (dateText != null ? "dateText=" + dateText + ", " : "")
				+ (monthText != null ? "monthText=" + monthText + ", " : "")
				+ (yearText != null ? "yearText=" + yearText + ", " : "")
				+ (address1Text != null ? "address1Text=" + address1Text + ", "
						: "")
				+ (address2Text != null ? "address2Text=" + address2Text + ", "
						: "")
				+ (stateId != null ? "stateId=" + stateId + ", " : "")
				+ (stateText != null ? "stateText=" + stateText + ", " : "")
				+ (pincode != null ? "pincode=" + pincode + ", " : "")
				+ (sex != null ? "sex=" + sex + ", " : "")
				+ (tel1Text != null ? "tel1Text=" + tel1Text + ", " : "")
				+ (tel2Text != null ? "tel2Text=" + tel2Text + ", " : "")
				+ (nameId != null ? "nameId=" + nameId + ", " : "")
				+ (fatNameText != null ? "fatNameText=" + fatNameText + ", "
						: "")
				+ (xlFilePath != null ? "xlFilePath=" + xlFilePath + ", " : "")
				+ (excelRowNum != null ? "excelRowNum=" + excelRowNum + ", "
						: "")
				+ (dateOfBirth != null ? "dateOfBirth=" + dateOfBirth : "")
				+ "]";
	}
	
}
