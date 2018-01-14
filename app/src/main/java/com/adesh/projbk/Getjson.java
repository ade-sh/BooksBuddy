package com.adesh.projbk;

import android.graphics.Bitmap;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Getjson {
    public static final String JSON_ARRAY = "result";
    public static final String IMAGEURL = "ImagePath";
    public static final String BookName = "bookName";
    public static final String B_id = "b_id";
    public static String[] Image_Url;
    public static Bitmap[] bitmaps;
    public static String[] Image_Name;
    public static String[] Img_id;
    public static String[] Img_disc;
    public static int[] Img_Ratin;
    public static String JUserName = "";
    public static String JEmail = "";
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
    /*
    public Bitmap getImage(JSONObject jo){
        URL url = null;
        HttpURLConnection conn;
        Bitmap image = null;
        try {
            String ustr=jo.getString(IMAGEURL).substring(15);
          url = new URL("http://10.0.3.2"+ustr);
            Log.i("Url inside getImg:",url.toString());
            image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (JSONException e){
            e.printStackTrace();
        }
        return image;
    }*/

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
            Image_Name = new String[urls.length()];
            Image_Url = new String[urls.length()];
            Img_id = new String[urls.length()];
            Img_disc = new String[urls.length()];
            Img_Ratin = new int[urls.length()];

            for (int i = 0; i < urls.length(); i++) {
                Image_Name[i] = urls.getJSONObject(i).getString(BookName);
                Image_Url[i] = urls.getJSONObject(i).getString(IMAGEURL);
                Img_id[i] = urls.getJSONObject(i).getString(B_id);
                Img_disc[i] = urls.getJSONObject(i).getString("b_disc");
                Img_Ratin[i] = Integer.parseInt(urls.getJSONObject(i).getString("Rating"));
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
