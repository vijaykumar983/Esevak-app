
package in.jploft.esevak.pojo;

import com.google.gson.annotations.SerializedName;

public class MyProfileData {

    @SerializedName("data")
    private Data mData;
    @SerializedName("Iscart")
    private Long mIscart;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("statusCode")
    private Long mStatusCode;

    public Data getData() {
        return mData;
    }

    public void setData(Data data) {
        mData = data;
    }

    public Long getIscart() {
        return mIscart;
    }

    public void setIscart(Long iscart) {
        mIscart = iscart;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public Long getStatusCode() {
        return mStatusCode;
    }

    public void setStatusCode(Long statusCode) {
        mStatusCode = statusCode;
    }



    public static class Data {

        @SerializedName("addphone")
        private String mAddphone;
        @SerializedName("address")
        private String mAddress;
        @SerializedName("created_at")
        private String mCreatedAt;
        @SerializedName("delete")
        private String mDelete;
        @SerializedName("deviceId")
        private String mDeviceId;
        @SerializedName("dob")
        private String mDob;
        @SerializedName("email")
        private String mEmail;
        @SerializedName("first_name")
        private String mFirstName;
        @SerializedName("fullname")
        private String mFullname;
        @SerializedName("gender")
        private String mGender;
        @SerializedName("id")
        private String mId;
        @SerializedName("last_name")
        private String mLastName;
        @SerializedName("otp")
        private String mOtp;
        @SerializedName("password")
        private String mPassword;
        @SerializedName("phone")
        private String mPhone;
        @SerializedName("pin")
        private String mPin;
        @SerializedName("profile_image")
        private String mProfileImage;
        @SerializedName("referCode")
        private String mReferCode;
        @SerializedName("rewallet")
        private String mRewallet;
        @SerializedName("status")
        private String mStatus;
        @SerializedName("temp_password")
        private String mTempPassword;
        @SerializedName("updated_at")
        private String mUpdatedAt;
        @SerializedName("upi")
        private String mUpi;
        @SerializedName("verifyStatus")
        private String mVerifyStatus;
        @SerializedName("wallet")
        private String mWallet;

        public String getAddphone() {
            return mAddphone;
        }

        public void setAddphone(String addphone) {
            mAddphone = addphone;
        }

        public String getAddress() {
            return mAddress;
        }

        public void setAddress(String address) {
            mAddress = address;
        }

        public String getCreatedAt() {
            return mCreatedAt;
        }

        public void setCreatedAt(String createdAt) {
            mCreatedAt = createdAt;
        }

        public String getDelete() {
            return mDelete;
        }

        public void setDelete(String delete) {
            mDelete = delete;
        }

        public String getDeviceId() {
            return mDeviceId;
        }

        public void setDeviceId(String deviceId) {
            mDeviceId = deviceId;
        }

        public String getDob() {
            return mDob;
        }

        public void setDob(String dob) {
            mDob = dob;
        }

        public String getEmail() {
            return mEmail;
        }

        public void setEmail(String email) {
            mEmail = email;
        }

        public String getFirstName() {
            return mFirstName;
        }

        public void setFirstName(String firstName) {
            mFirstName = firstName;
        }

        public String getFullname() {
            return mFullname;
        }

        public void setFullname(String fullname) {
            mFullname = fullname;
        }

        public String getGender() {
            return mGender;
        }

        public void setGender(String gender) {
            mGender = gender;
        }

        public String getId() {
            return mId;
        }

        public void setId(String id) {
            mId = id;
        }

        public String getLastName() {
            return mLastName;
        }

        public void setLastName(String lastName) {
            mLastName = lastName;
        }

        public String getOtp() {
            return mOtp;
        }

        public void setOtp(String otp) {
            mOtp = otp;
        }

        public String getPassword() {
            return mPassword;
        }

        public void setPassword(String password) {
            mPassword = password;
        }

        public String getPhone() {
            return mPhone;
        }

        public void setPhone(String phone) {
            mPhone = phone;
        }

        public String getPin() {
            return mPin;
        }

        public void setPin(String pin) {
            mPin = pin;
        }

        public String getProfileImage() {
            return mProfileImage;
        }

        public void setProfileImage(String profileImage) {
            mProfileImage = profileImage;
        }

        public String getReferCode() {
            return mReferCode;
        }

        public void setReferCode(String referCode) {
            mReferCode = referCode;
        }

        public String getRewallet() {
            return mRewallet;
        }

        public void setRewallet(String rewallet) {
            mRewallet = rewallet;
        }

        public String getStatus() {
            return mStatus;
        }

        public void setStatus(String status) {
            mStatus = status;
        }

        public String getTempPassword() {
            return mTempPassword;
        }

        public void setTempPassword(String tempPassword) {
            mTempPassword = tempPassword;
        }

        public String getUpdatedAt() {
            return mUpdatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            mUpdatedAt = updatedAt;
        }

        public String getUpi() {
            return mUpi;
        }

        public void setUpi(String upi) {
            mUpi = upi;
        }

        public String getVerifyStatus() {
            return mVerifyStatus;
        }

        public void setVerifyStatus(String verifyStatus) {
            mVerifyStatus = verifyStatus;
        }

        public String getWallet() {
            return mWallet;
        }

        public void setWallet(String wallet) {
            mWallet = wallet;
        }

    }


}
