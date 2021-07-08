
package in.jploft.esevak.pojo;

import com.google.gson.annotations.SerializedName;

public class GenerateChecksumData {

    @SerializedName("CHECKSUMHASH")
    private String mCHECKSUMHASH;
    @SerializedName("ORDER_ID")
    private Object mORDERID;
    @SerializedName("payt_STATUS")
    private String mPaytSTATUS;

    public String getCHECKSUMHASH() {
        return mCHECKSUMHASH;
    }

    public void setCHECKSUMHASH(String cHECKSUMHASH) {
        mCHECKSUMHASH = cHECKSUMHASH;
    }

    public Object getORDERID() {
        return mORDERID;
    }

    public void setORDERID(Object oRDERID) {
        mORDERID = oRDERID;
    }

    public String getPaytSTATUS() {
        return mPaytSTATUS;
    }

    public void setPaytSTATUS(String paytSTATUS) {
        mPaytSTATUS = paytSTATUS;
    }

}
