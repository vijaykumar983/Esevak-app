package in.jploft.esevak.pojo;

import com.google.gson.annotations.SerializedName;

public class UpdateCityData{

	@SerializedName("data")
	private Data data;

	@SerializedName("message")
	private String message;

	@SerializedName("statusCode")
	private int statusCode;

	public void setData(Data data){
		this.data = data;
	}

	public Data getData(){
		return data;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setStatusCode(int statusCode){
		this.statusCode = statusCode;
	}

	public int getStatusCode(){
		return statusCode;
	}

	public static class Data{

		@SerializedName("temp_password")
		private String tempPassword;

		@SerializedName("gender")
		private String gender;

		@SerializedName("selectdcity")
		private String selectdcity;

		@SerializedName("referCode")
		private String referCode;

		@SerializedName("created_at")
		private String createdAt;

		@SerializedName("deviceId")
		private String deviceId;

		@SerializedName("delete")
		private String delete;

		@SerializedName("password")
		private String password;

		@SerializedName("profile_image")
		private String profileImage;

		@SerializedName("updated_at")
		private String updatedAt;

		@SerializedName("verifyStatus")
		private String verifyStatus;

		@SerializedName("pin")
		private String pin;

		@SerializedName("rewallet")
		private String rewallet;

		@SerializedName("id")
		private String id;

		@SerializedName("first_name")
		private String firstName;

		@SerializedName("email")
		private String email;

		@SerializedName("wallet")
		private String wallet;

		@SerializedName("address")
		private String address;

		@SerializedName("last_name")
		private String lastName;

		@SerializedName("otp")
		private String otp;

		@SerializedName("addphone")
		private String addphone;

		@SerializedName("upi")
		private String upi;

		@SerializedName("phone")
		private String phone;

		@SerializedName("dob")
		private String dob;

		@SerializedName("fullname")
		private String fullname;

		@SerializedName("status")
		private String status;

		public void setTempPassword(String tempPassword){
			this.tempPassword = tempPassword;
		}

		public String getTempPassword(){
			return tempPassword;
		}

		public void setGender(String gender){
			this.gender = gender;
		}

		public String getGender(){
			return gender;
		}

		public void setSelectdcity(String selectdcity){
			this.selectdcity = selectdcity;
		}

		public String getSelectdcity(){
			return selectdcity;
		}

		public void setReferCode(String referCode){
			this.referCode = referCode;
		}

		public String getReferCode(){
			return referCode;
		}

		public void setCreatedAt(String createdAt){
			this.createdAt = createdAt;
		}

		public String getCreatedAt(){
			return createdAt;
		}

		public void setDeviceId(String deviceId){
			this.deviceId = deviceId;
		}

		public String getDeviceId(){
			return deviceId;
		}

		public void setDelete(String delete){
			this.delete = delete;
		}

		public String getDelete(){
			return delete;
		}

		public void setPassword(String password){
			this.password = password;
		}

		public String getPassword(){
			return password;
		}

		public void setProfileImage(String profileImage){
			this.profileImage = profileImage;
		}

		public String getProfileImage(){
			return profileImage;
		}

		public void setUpdatedAt(String updatedAt){
			this.updatedAt = updatedAt;
		}

		public String getUpdatedAt(){
			return updatedAt;
		}

		public void setVerifyStatus(String verifyStatus){
			this.verifyStatus = verifyStatus;
		}

		public String getVerifyStatus(){
			return verifyStatus;
		}

		public void setPin(String pin){
			this.pin = pin;
		}

		public String getPin(){
			return pin;
		}

		public void setRewallet(String rewallet){
			this.rewallet = rewallet;
		}

		public String getRewallet(){
			return rewallet;
		}

		public void setId(String id){
			this.id = id;
		}

		public String getId(){
			return id;
		}

		public void setFirstName(String firstName){
			this.firstName = firstName;
		}

		public String getFirstName(){
			return firstName;
		}

		public void setEmail(String email){
			this.email = email;
		}

		public String getEmail(){
			return email;
		}

		public void setWallet(String wallet){
			this.wallet = wallet;
		}

		public String getWallet(){
			return wallet;
		}

		public void setAddress(String address){
			this.address = address;
		}

		public String getAddress(){
			return address;
		}

		public void setLastName(String lastName){
			this.lastName = lastName;
		}

		public String getLastName(){
			return lastName;
		}

		public void setOtp(String otp){
			this.otp = otp;
		}

		public String getOtp(){
			return otp;
		}

		public void setAddphone(String addphone){
			this.addphone = addphone;
		}

		public String getAddphone(){
			return addphone;
		}

		public void setUpi(String upi){
			this.upi = upi;
		}

		public String getUpi(){
			return upi;
		}

		public void setPhone(String phone){
			this.phone = phone;
		}

		public String getPhone(){
			return phone;
		}

		public void setDob(String dob){
			this.dob = dob;
		}

		public String getDob(){
			return dob;
		}

		public void setFullname(String fullname){
			this.fullname = fullname;
		}

		public String getFullname(){
			return fullname;
		}

		public void setStatus(String status){
			this.status = status;
		}

		public String getStatus(){
			return status;
		}
	}
	
}