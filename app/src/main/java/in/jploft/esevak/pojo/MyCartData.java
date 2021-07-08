
package in.jploft.esevak.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyCartData {

    @SerializedName("data")
    private List<Datum> mData;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("statusCode")
    private Long mStatusCode;
    @SerializedName("texi")
    private Long mTexi;
    @SerializedName("totalAmount")
    private Long mTotalAmount;
    @SerializedName("totalCart")
    private Long mTotalCart;

    public List<Datum> getData() {
        return mData;
    }

    public void setData(List<Datum> data) {
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

    public Long getTexi() {
        return mTexi;
    }

    public void setTexi(Long texi) {
        mTexi = texi;
    }

    public Long getTotalAmount() {
        return mTotalAmount;
    }

    public void setTotalAmount(Long totalAmount) {
        mTotalAmount = totalAmount;
    }

    public Long getTotalCart() {
        return mTotalCart;
    }

    public void setTotalCart(Long totalCart) {
        mTotalCart = totalCart;
    }


    public static class Datum {

        @SerializedName("description")
        private String mDescription;
        @SerializedName("id")
        private String mId;
        @SerializedName("image")
        private String mImage;
        @SerializedName("price")
        private String mPrice;
        @SerializedName("product")
        private String mProduct;
        @SerializedName("qty")
        private String mQty;

        public String getDescription() {
            return mDescription;
        }

        public void setDescription(String description) {
            mDescription = description;
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

        public String getQty() {
            return mQty;
        }

        public void setQty(String qty) {
            mQty = qty;
        }

    }


}
