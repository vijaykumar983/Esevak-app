
package in.jploft.esevak.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServiceData {

    @SerializedName("banner")
    private String mBanner;
    @SerializedName("data")
    private Data mData;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("statusCode")
    private Long mStatusCode;

    public String getBanner() {
        return mBanner;
    }

    public void setBanner(String banner) {
        mBanner = banner;
    }

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

        @SerializedName("product")
        private List<Product> mProduct;

        public List<Product> getProduct() {
            return mProduct;
        }

        public void setProduct(List<Product> product) {
            mProduct = product;
        }

    }

    public static class Product {

        @SerializedName("id")
        private String mId;
        @SerializedName("image")
        private String mImage;
        @SerializedName("name")
        private String mName;
        @SerializedName("price")
        private String mPrice;

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

        public String getName() {
            return mName;
        }

        public void setName(String name) {
            mName = name;
        }

        public String getPrice() {
            return mPrice;
        }

        public void setPrice(String price) {
            mPrice = price;
        }

    }


}
