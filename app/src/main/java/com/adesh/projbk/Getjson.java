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
    public static final String ReqName = "RequestName";
    public static final String ReqId = "RequestId";
    public static final String ReqDisc = "RequestDisc";
    public static final String ReqImg1 = "RqImg1";
    public static final String ReqImg2 = "RqImg2";
    public static final String ReqImg3 = "RqImg3";
    public static final String B_id = "b_id";
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
    public static String JUserName = "";
    public static String JEmail = "";
    public static String JProfilePic = "";
    public static String JPhone = "";

    public String json;
    public JSONArray urls;

    public Getjson(String json) {
        this.json = json;
        try {
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


}
