
package in.jploft.esevak.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class MyOrdersData {

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

        @SerializedName("orders")
        private List<Order> mOrders;

        public List<Order> getOrders() {
            return mOrders;
        }

        public void setOrders(List<Order> orders) {
            mOrders = orders;
        }

    }

    public static class Order {

        @SerializedName("date")
        private String mDate;
        @SerializedName("id")
        private String mId;
        @SerializedName("image")
        private String mImage;
        @SerializedName("price")
        private String mPrice;
        @SerializedName("product")
        private String mProduct;
        @SerializedName("status")
        private String mStatus;

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

        public String getImage() {
            return mImage;
        }

        public void setImage(String image) {
            mImage = image;
        }

        public String getPrice() {
            return mPrice;
        }

        public void setPrice(String price) {
            mPrice = price;
        }

        public String getProduct() {
            return mProduct;
        }

        public void setProduct(String product) {
            mProduct = product;
        }

        public String getStatus() {
            return mStatus;
        }

        public void setStatus(String status) {
            mStatus = status;
        }

    }


}
