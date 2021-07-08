
package in.jploft.esevak.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TransactionHistoryData {

    @SerializedName("data")
    private Data mData;
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

        @SerializedName("transactions")
        private List<Transaction> mTransactions;

        public List<Transaction> getTransactions() {
            return mTransactions;
        }

        public void setTransactions(List<Transaction> transactions) {
            mTransactions = transactions;
        }

    }

    public static class Transaction {

        @SerializedName("amount")
        private String mAmount;
        @SerializedName("date")
        private String mDate;
        @SerializedName("id")
        private String mId;
        @SerializedName("status")
        private String mStatus;
        @SerializedName("type")
        private String mType;

        public String getAmount() {
            return mAmount;
        }

        public void setAmount(String amount) {
            mAmount = amount;
        }

        public String getDate() {
            return mDate;
        }

        public void setDate(String date) {
            mDate = date;
        }

        public String getId() {
            return mId;
        }

        public void setId(String id) {
            mId = id;
        }

        public String getStatus() {
            return mStatus;
        }

        public void setStatus(String status) {
            mStatus = status;
        }

        public String getType() {
            return mType;
        }

        public void setType(String type) {
            mType = type;
        }

    }


}
