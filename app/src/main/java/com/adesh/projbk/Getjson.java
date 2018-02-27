package com.adesh.projbk;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Getjson {
    public static final String JSON_ARRAY = "result";
    public static final String IMAGEURL = "ImagePath";
    public static final String IMAGEURL2 = "ImagePath2";
    public static final String IMAGEURL3 = "ImagePath3";
    public static final String BookName = "bookName";
    public static final String Usr_id = "u_id";
    public static final String bkTime = "time";
    public static final String price = "price";
    public static final String stype = "type";
    public static final String sStatus = "status";
    public static final String B_id = "b_id";
    private static final String rvBody = "body";
    private static final String rvHead = "head";
    private static final String rvRating = "Rating";
    private static final String rvUser = "user";
    private static final String rvCount = "RevCount";
    public static ArrayList<String> arrname;
    public static ArrayList<String> arrid;
    public static ArrayList<String> arrRatin;
    public static ArrayList<String> arrUploader;
    public static ArrayList<String> arrurls;
    public static ArrayList<String> arrurls2;
    public static ArrayList<String> arrurls3;
    public static ArrayList<String> arrDisc;
    public static ArrayList<String> arruid;
    public static ArrayList<String> arrTime;
    public static ArrayList<String> arrType;
    public static ArrayList<String> arrStatus;
    public static ArrayList<String> arrPrice;
    public static ArrayList<String> arrRvBody;
    public static ArrayList<String> arrRvHead;
    public static ArrayList<String> arrRvUser;
    public static ArrayList<Integer> arrRvRating;
    public static ArrayList<String> arrRvCount;
    public static String JUserName = "";
    public static String JEmail = "";
    public static String JProfilePic = "";
    public static String JPhone = "";

    public String json;
    public JSONArray urls;

    public Getjson(String json) {
        this.json = json;
        try {
            Log.d("json object", json);
            JSONObject jsonObject = new JSONObject(json);
            urls = jsonObject.getJSONArray(JSON_ARRAY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getJsonId(int length) {
        int Bid = 0;
        try {
            Bid = Integer.parseInt(urls.getJSONObject(length).getString(B_id));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Bid;
    }


    public void getAllImages() {
        try {
            arrid = new ArrayList<>();
            arrname = new ArrayList<>();
            arrRatin = new ArrayList<>();
            arrurls = new ArrayList<>();
            arrurls2 = new ArrayList<>();
            arrurls3 = new ArrayList<>();
            arrDisc = new ArrayList<>();
            arrUploader = new ArrayList<>();
            arruid = new ArrayList<>();
            arrTime = new ArrayList<>();
            arrPrice = new ArrayList<>();
            arrRvCount = new ArrayList<>();
            Log.i("urls Length", urls.length() + "");
            for (int i = 0; i < urls.length(); i++) {
                //inserting into Arraylist
                Log.e("arrname in Loop", urls.getJSONObject(i).getString(BookName));
                arrname.add(urls.getJSONObject(i).getString(BookName));
                arrurls.add(urls.getJSONObject(i).getString(IMAGEURL));
                arrurls2.add(urls.getJSONObject(i).getString(IMAGEURL2));
                arrurls3.add(urls.getJSONObject(i).getString(IMAGEURL3));
                arrRatin.add(urls.getJSONObject(i).getString("Rating"));
                arrid.add(urls.getJSONObject(i).getString(B_id));
                arrDisc.add(urls.getJSONObject(i).getString("b_disc"));
                arrUploader.add(urls.getJSONObject(i).getString("uploader"));
                arruid.add(urls.getJSONObject(i).getString(Usr_id));
                arrTime.add(urls.getJSONObject(i).getString(bkTime));
                arrPrice.add(urls.getJSONObject(i).getString(price));
                arrRvCount.add(urls.getJSONObject(i).getString(rvCount));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void clrUser() {
        JUserName = "";
        JEmail = "";
        JPhone = "";
        JProfilePic = "";
    }

    public void clrRev() {
        arrRvHead.clear();
        arrRvBody.clear();
        arrRvRating.clear();
        arrRvUser.clear();
    }
    public void getUser() {
        try {
            JUserName = urls.getJSONObject(0).getString("Name");
            JEmail = urls.getJSONObject(0).getString("Email");
            JPhone = urls.getJSONObject(0).getString("phone");
            JProfilePic = urls.getJSONObject(0).getString("profile_pic");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getTransDetail() {
        try {
            arrname = new ArrayList<>();
            arrTime = new ArrayList<>();
            arrStatus = new ArrayList<>();
            arrType = new ArrayList<>();
            for (int i = 0; i < urls.length(); i++) {
                arrname.add(urls.getJSONObject(i).getString(BookName));
                arrTime.add(urls.getJSONObject(i).getString(bkTime));
                arrType.add(urls.getJSONObject(i).getString(stype));
                arrStatus.add(urls.getJSONObject(i).getString(sStatus));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getReviews() {
        try {
            arrRvHead = new ArrayList<>();
            arrRvBody = new ArrayList<>();
            arrRvRating = new ArrayList<>();
            arrRvUser = new ArrayList<>();
            for (int i = 0; i < urls.length(); i++) {
                arrRvHead.add(urls.getJSONObject(i).getString(rvHead));
                arrRvBody.add(urls.getJSONObject(i).getString(rvBody));
                arrRvRating.add(Integer.parseInt(urls.getJSONObject(i).getString(rvRating)));
                arrRvUser.add(urls.getJSONObject(i).getString(rvUser));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
