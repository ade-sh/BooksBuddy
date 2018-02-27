package com.adesh.projbk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import cz.msebera.android.httpclient.conn.ConnectTimeoutException;

public class MainActivity extends AppCompatActivity {
    public Getjson getjsonobj;
    public ArrayList<String> arrname;
    public ArrayList<String> arrid;
    public ArrayList<String> arrRatin;
    public ArrayList<String> arrurls;
    public ArrayList<String> arruploader;
    RecyclerView bkObj;
    bkCustomAdapter bkAdapter;
    int offset = 0;
    Menu menu;
    boolean LoginStatus;
    ImageView ivNavPp;
    TextView navUsr, navEmail;
    EndlessRecyclerViewScrollListener scrollListener;
    LinearLayoutManager llm;
    private Parcelable recyclerViewState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        bkObj = (RecyclerView) findViewById(R.id.bkObj);
        arrid = new ArrayList<>();
        arrname = new ArrayList<>();
        arrRatin = new ArrayList<>();
        arrurls = new ArrayList<>();
        arruploader = new ArrayList<>();
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(MainActivity.this, R.anim.layout_animation_fall_down);
        bkObj.setLayoutAnimation(animation);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_widget);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawer.setDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();
        llm = new LinearLayoutManager(this);
        bkObj.setLayoutManager(llm);
        bkAdapter = new bkCustomAdapter(MainActivity.this, arrname, arrurls, arrid, arrRatin, arruploader);
        bkObj.setAdapter(bkAdapter);
        getURLs();
        scrollListener = new EndlessRecyclerViewScrollListener(llm) {
            @Override
            public void onLoadMore(final int page, int totalItemsCount, RecyclerView view) {
                bkObj.post(new Runnable() {
                    @Override
                    public void run() {
                        offset = page;
                        int fsize = arrname.size();
                        getURLs();
                        try {
                            Thread.sleep(100);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        bkAdapter.notifyItemRangeInserted(fsize, 4);
                    }
                });
            }
            @Override
            public void onScrolled(RecyclerView view, int dx, int dy) {
                super.onScrolled(view, dx, dy);
            }
        };
        bkObj.addOnScrollListener(scrollListener);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                // Handle navigation view item clicks here.
                int id = item.getItemId();

                if (id == R.id.nav_account) {
                    Intent StartLogin = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(StartLogin);
                }
                if (id == R.id.nav_sell) {
                    Intent Startsell = new Intent(getApplicationContext(), sellActivity.class);
                    startActivity(Startsell);
                }
                if (id == R.id.nav_request) {
                    Intent startReq = new Intent(MainActivity.this, Request.class);
                    startActivity(startReq);
                }
                if (id == R.id.nav_setting) {
                    Intent startReq = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(startReq);
                }
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        SharedPreferences sp = getSharedPreferences("UserLogin", MODE_PRIVATE);
        LoginStatus = sp.getBoolean("IsLogged", false);
        View hView = navigationView.getHeaderView(0);
        navUsr = (TextView) hView.findViewById(R.id.tvnavUsrname);
        navEmail = (TextView) hView.findViewById(R.id.tvnavEmail);
        ivNavPp = (ImageView) hView.findViewById(R.id.ivNavPic);
        if (LoginStatus) {
            SharedPreferences spd = getSharedPreferences("Userdetail", MODE_PRIVATE);
            String spEmail = spd.getString("Email", "Email");
            String spusrname = spd.getString("Profilename", "Username");
            String spProfilep = spd.getString("ProfilePic", "");
            navUsr.setText(spusrname);
            navEmail.setText(spEmail);
            Picasso.with(MainActivity.this).load(getString(R.string.httpUrl) + spProfilep).resize(180, 180).into(ivNavPp, new Callback() {
                @Override
                public void onSuccess() {
                    Bitmap imageBitmap = ((BitmapDrawable) ivNavPp.getDrawable()).getBitmap();
                    RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
                    imageDrawable.setCircular(true);
                    imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                    ivNavPp.setImageDrawable(imageDrawable);
                }

                @Override
                public void onError() {

                }
            });
        }
        if (!LoginStatus) {
            navUsr.setText("Username");
            navEmail.setText("Email");
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.main_menu, menu);
        /*SearchManager searchmanager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setSearchableInfo(searchmanager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);*/
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.m_acc: {
                Intent StartLogin = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(StartLogin);
                return true;
            }
            case R.id.app_bar_search: {
                onSearchRequested();
            }
            case R.id.m_refresh: {
                bkObj.invalidate();
                scrollListener.resetState();
                offset = 0;
                arrid = new ArrayList<>();
                arrname = new ArrayList<>();
                arrRatin = new ArrayList<>();
                arrurls = new ArrayList<>();
                arruploader = new ArrayList<>();
                getURLs();
                bkAdapter.notifyDataSetChanged();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void getURLs() {
        recyclerViewState = bkObj.getLayoutManager().onSaveInstanceState();
        BufferedReader bufferedReader = null;
        try {
            URL url = new URL(getString(R.string.httpUrl) + "/getImages.inc.php");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoInput(true);
            con.setConnectTimeout(60000);
            con.setRequestMethod("POST");
            con.connect();
            Uri.Builder builder = new Uri.Builder().appendQueryParameter("Offset", offset + "");
            String query = builder.build().getEncodedQuery();

            //Open Connection for sending data
            OutputStream os = con.getOutputStream();
            BufferedWriter bwriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            bwriter.write(query);
            bwriter.flush();
            bwriter.close();
            os.close();
            con.connect();

            int response_code = con.getResponseCode();
            //Check if sucessfull connection made and read data
            if (response_code == HttpsURLConnection.HTTP_OK) {
            StringBuilder sb = new StringBuilder();
            bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String json;
            while ((json = bufferedReader.readLine()) != null) {
                sb.append(json + "\n");
            }
                Log.i("getJson in ma", sb.toString());
            getjsonobj = new Getjson(sb.toString().trim());
            getImages();
            }
        } catch (ConnectTimeoutException ce) {
            Toast.makeText(getApplication(), "Check your internet", Toast.LENGTH_LONG).show();
        } catch (SocketTimeoutException sc) {
            Toast.makeText(getApplication(), "Cannot connect", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.e("error in dib", e.getMessage());
        }

    }

    public void getImages() {
        class GetImages extends AsyncTask<Void, Void, Void> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(Void v) {
                super.onPostExecute(v);

                if (Getjson.arrname.size() != 0) {
                    arrname.add(Getjson.arrname.get(Getjson.arrname.size() - 2));
                    arrid.add(Getjson.arrid.get(Getjson.arrid.size() - 2));
                    arrurls.add(Getjson.arrurls.get(Getjson.arrurls.size() - 2));
                    arrRatin.add(Getjson.arrRatin.get(Getjson.arrRatin.size() - 2));
                    arruploader.add(Getjson.arrUploader.get(Getjson.arrRatin.size() - 2));
                    //second time
                arrname.add(Getjson.arrname.get(Getjson.arrname.size() - 1));
                arrid.add(Getjson.arrid.get(Getjson.arrid.size() - 1));
                arrurls.add(Getjson.arrurls.get(Getjson.arrurls.size() - 1));
                arrRatin.add(Getjson.arrRatin.get(Getjson.arrRatin.size() - 1));
                arruploader.add(Getjson.arrUploader.get(Getjson.arrRatin.size() - 1));
                bkAdapter = new bkCustomAdapter(MainActivity.this, arrname, arrurls, arrid, arrRatin, arruploader);
                    bkObj.setAdapter(bkAdapter);
                    bkObj.getLayoutManager().onRestoreInstanceState(recyclerViewState);

                } else {
                    Toast.makeText(MainActivity.this, "No more books available now", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected Void doInBackground(Void... voids) {
                getjsonobj.getAllImages();
                return null;
            }
        }
        GetImages getImages = new GetImages();
        getImages.execute();
        bkObj.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        scrollListener.resetState();
        SharedPreferences sp = getSharedPreferences("UserLogin", MODE_PRIVATE);
        runLayoutAnimation();
        LoginStatus = sp.getBoolean("IsLogged", false);
        if (!LoginStatus) {
            navUsr.setText("Username");
            navEmail.setText("Email");
            ivNavPp.setImageResource(R.drawable.ic_account_circle);
        }
        if (LoginStatus) {
            SharedPreferences spd = getSharedPreferences("Userdetail", MODE_PRIVATE);
            String spEmail = spd.getString("Email", "Email");
            String spusrname = spd.getString("Profilename", "Username");
            navUsr.setText(spusrname);
            navEmail.setText(spEmail);
        }
    }

    private void runLayoutAnimation() {
   /* LayoutAnimationController controller=AnimationUtils.loadLayoutAnimation(MainActivity.this,R.anim.item_animation_fall_down);
    bkObj.setLayoutAnimation(controller);
    bkObj.scheduleLayoutAnimation();*/
    }
    @Override
    public void onStart() {
        super.onStart();
        scrollListener.resetState();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        scrollListener.resetState();
        bkAdapter.notifyDataSetChanged();
        runLayoutAnimation();
    }
}