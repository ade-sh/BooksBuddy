package com.adesh.projbk;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Getjson {
    public static final String JSON_ARRAY = "result";
    public static final String IMAGEURL = "ImagePath";
    public static final String BookName = "bookName";
    public static final String B_id = "b_id";
    public static ArrayList<String> arrname;
    public static ArrayList<String> arrid;
    public static ArrayList<String> arrRatin;
    public static ArrayList<String> arrUploader;
    public static ArrayList<String> arrurls;
    public static ArrayList<String> arrDisc;
    public static int[] Img_Ratin;
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
            Log.i("GetJson", urls.toString());
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
            arrDisc = new ArrayList<>();
            arrUploader = new ArrayList<>();

            for (int i = 0; i < urls.length(); i++) {
                //inserting into Arraylist
                arrname.add(urls.getJSONObject(i).getString(BookName));
                arrurls.add(urls.getJSONObject(i).getString(IMAGEURL));
                arrRatin.add(urls.getJSONObject(i).getString("Rating"));
                arrid.add(urls.getJSONObject(i).getString(B_id));
                arrDisc.add(urls.getJSONObject(i).getString("b_disc"));
                arrUploader.add(urls.getJSONObject(i).getString("uploader"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
