package in.jploft.esevak.utils;


import android.util.Property;
import android.view.View;

import java.util.ArrayList;

import in.jploft.esevak.R;

public class Constants {
    public static final int Success = 200;
    public static final int Failure = 400;
    public static ArrayList<String> allSelectPosition = new ArrayList<>();
    public static final String imageUrl = "http://stageofproject.com/ekta/assets/profile_image/";

    public static final String M_ID = "eNnjXe00637647587210"; //Paytm Merchand Id we got it in paytm credentials
    public static final String CHANNEL_ID = "WAP"; //Paytm Channel Id, got it in paytm credentials
    public static final String INDUSTRY_TYPE_ID = "Retail"; //Paytm industry type got it in paytm credential
    public static final String WEBSITE = "APPSTAGING";
    public static final String CALLBACK_URL = "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";

    public static final String PAYMENT_SUCCESS = "http://stageofproject.com/esevak/Webservice/paymentstatus/1";
    public static final String PAYMENT_FAILURE = "http://stageofproject.com/esevak/Webservice/paymentstatus/0";
    public static final String ADD_WALLET = "http://stageofproject.com/esevak/Webservice/addwallet/";

    //http://stageofproject.com/esevak/Webservice/placeOrder/101/1/5/address/paymentMode/code
    public static final String PAYMENT_ORDER = "http://stageofproject.com/esevak/Webservice/placeOrder/";
    //public static String selectedDayId = "";
    public static String selectedDayId = "";
    public static String selectedDayName = "";
    public static String selectedTime = "";

}