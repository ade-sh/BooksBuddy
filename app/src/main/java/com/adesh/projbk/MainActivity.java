package com.adesh.projbk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.StrictMode;
import android.preference.PreferenceManager;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    public Getjson getjsonobj;
    public ArrayList<String> arrname;
    public ArrayList<String> arrid;
    public ArrayList<String> arrRatin;
    public ArrayList<String> arrurls;
    public ArrayList<String> arruploader;
    String recTine = "New", recType = "All", bkurl;
    String[] SpinarrTime = {"New", "Old"};
    String[] SpinarrType = {"All", "request", "users", "publishers"};
    RecyclerView bkObj;
    bkCustomAdapter bkAdapter;
    int offset = 0, prevOffset = -1;
    Menu menu;
    int initSize = 0;
    boolean LoginStatus;
    ImageView ivNavPp;
    TextView navUsr, navEmail;
    Spinner spinTime, spinType;
    EndlessRecyclerViewScrollListener scrollListener;
    LinearLayoutManager llm;
    private Parcelable recyclerViewState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        SharedPreferences settingPreference = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        boolean isDark = settingPreference.getBoolean("switch_preference_theme", false);
        if (isDark) {
            setTheme(R.style.Base_ThemeOverlay_AppCompat_Dark_);
        }
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        bkurl = getString(R.string.httpUrl) + "/getImages.inc.php";
        bkObj = (RecyclerView) findViewById(R.id.bkObj);
        spinTime = (Spinner) findViewById(R.id.SpinmainToolbar_Time);
        spinType = (Spinner) findViewById(R.id.SpinmainToolbar_Sort);
        //setup spinner data
        ArrayAdapter<String> spinTimeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, SpinarrTime);
        ArrayAdapter<String> spinTypeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, SpinarrType);
        spinTime.setAdapter(spinTimeAdapter);
        spinType.setAdapter(spinTypeAdapter);
        //setup array for main data
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

        scrollListener = new EndlessRecyclerViewScrollListener(llm) {
            @Override
            public void onLoadMore(final int page, int totalItemsCount, RecyclerView view) {
                bkObj.post(new Runnable() {
                    @Override
                    public void run() {
                        offset = initSize;
                        initSize = initSize + 1;
                        Log.d("scroll values ", "offset" + offset + " Prev" + prevOffset + " page " + page + " arrsize" + arrname.size());
                        getURLs();

                        // bkObj.getAdapter().notifyItemInserted(arrname.size()-3);
                        bkObj.getAdapter().notifyDataSetChanged();
                    }
                });
            }
        };
        if (offset == 0) {
            try {
                Thread.sleep(8000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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
                if (id == R.id.nav_cart) {
                    Intent intent = new Intent(MainActivity.this, CartActivity.class);
                    startActivity(intent);
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
        spinTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                recTine = SpinarrTime[position];
                refreshAPP();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                recTine = "New";
            }
        });
        spinType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                recType = SpinarrType[position];
                if (recType.contains("All")) {
                    bkurl = getString(R.string.httpUrl) + "/getImages.inc.php";
                    refreshAPP();
                } else if (recType.contains("publishers") || recType.contains("request") || recType.contains("users")) {
                    bkurl = getString(R.string.httpUrl) + "/getImagesSort.inc.php";
                    refreshAPP();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                recType = "ALL";
            }
        });
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
                refreshAPP();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void runLayoutAnimation() {
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(MainActivity.this, R.anim.layout_animation_fall_down);
        bkObj.setLayoutAnimation(controller);
        bkObj.scheduleLayoutAnimation();
    }
    public void getURLs() {
        recyclerViewState = bkObj.getLayoutManager().onSaveInstanceState();

        if (offset > prevOffset) {
            Log.d("bkurl", bkurl);
            OkHttpClient httpClient = new OkHttpClient();
            Log.d("spintag", recType + " " + recTine);
            RequestBody parameter = new FormBody.Builder().add("Offset", offset + "").add("Time", recTine).add("Type", recType).build();
            okhttp3.Request request = new okhttp3.Request.Builder().url(bkurl).post(parameter).build();
            String sb = "";
            try {
                Response response = httpClient.newCall(request).execute();
                sb = response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Cannot connect Check your Internet connection", Toast.LENGTH_LONG).show();
            }
            if (!sb.isEmpty() && sb != null) {
                getjsonobj = new Getjson(sb.trim());
            getImages();
            }
            prevOffset = offset;
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

                if (Getjson.arrname.size() != 0 && Getjson.arrname.size() > 1) {
                    for (int l = 1; l < 3; l++) {
                        if (!arrid.contains(Getjson.arrname.get(Getjson.arrid.size() - l))) {
                            arrname.add(Getjson.arrname.get(Getjson.arrname.size() - l));
                            arrid.add(Getjson.arrid.get(Getjson.arrid.size() - l));
                            arrurls.add(Getjson.arrurls.get(Getjson.arrurls.size() - l));
                            arrRatin.add(Getjson.arrRatin.get(Getjson.arrRatin.size() - l));
                            arruploader.add(Getjson.arrUploader.get(Getjson.arrRatin.size() - l));
                        }
                    }
                    bkAdapter = new bkCustomAdapter(MainActivity.this, arrname, arrurls, arrid, arrRatin, arruploader);
                    bkAdapter.setHasStableIds(true);
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
        // bkObj.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
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
        refreshAPP();
    }


    @Override
    public void onStart() {
        super.onStart();
        bkObj.invalidate();
        scrollListener.resetState();
        offset = 0;
        arrid = new ArrayList<>();
        arrname = new ArrayList<>();
        arrRatin = new ArrayList<>();
        arrurls = new ArrayList<>();
        arruploader = new ArrayList<>();
        getURLs();
    }

    public void refreshAPP() {
        bkObj.invalidate();
        scrollListener.resetState();
        offset = 0;
        initSize = -0;
        prevOffset = -1;
        arrid.clear();
        arrname.clear();
        arrRatin.clear();
        arrurls.clear();
        arruploader.clear();
        getURLs();
    }
}