
package in.jploft.esevak.pojo;

import com.google.gson.annotations.SerializedName;


public class ApplyCouponData {

    @SerializedName("discount")
    private String mDiscount;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("newprice")
    private String mNewprice;
    @SerializedName("statusCode")
    private String mStatusCode;

    public String getDiscount() {
        return mDiscount;
    }

    public void setDiscount(String discount) {
        mDiscount = discount;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public String getNewprice() {
        return mNewprice;
    }

    public void setNewprice(String newprice) {
        mNewprice = newprice;
    }

    public String getStatusCode() {
        return mStatusCode;
    }

    public void setStatusCode(String statusCode) {
        mStatusCode = statusCode;
    }

}
