package com.example.handsome.thenewtest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.handsome.thenewtest.entity.Movie;
import com.example.handsome.thenewtest.helper.AssetsHelper;
import com.example.handsome.thenewtest.helper.DataHelper;
import com.example.handsome.thenewtest.helper.DatabaseHelper;
import com.example.handsome.thenewtest.helper.GAEHelper;
import com.example.handsome.thenewtest.helper.JSONHelper;
import com.example.handsome.thenewtest.helper.SharedPreferencesHelper;
import com.example.handsome.thenewtest.util.AppController;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    public static   List<Movie> MV_LIST = new ArrayList<>();
    public static    ArrayList<String> TH_TIME_LIST = new ArrayList<>();
    final static String TAG = "hs";
    Context context;
    boolean lock = true;
    private Handler handler;
    private LinearLayout layout;
    private Button btn;
    private ProgressDialog pDialog;
    RequestQueue mQueue;
    private SharedPreferencesHelper prefHelper;
    DatabaseHelper helper ;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("hs", "on creating");
        setContentView(R.layout.activity_main);
        prefHelper = new SharedPreferencesHelper(this);context = this;
        pDialog = new ProgressDialog(this);
        mQueue = Volley.newRequestQueue(context);
        mQueue = AppController.getInstance().getRequestQueue();
        handler = new Handler();
        helper = new DatabaseHelper(this);
        db = helper.getWritableDatabase();

        layout = (LinearLayout) findViewById(R.id.posterLayout);
        btn =  (Button)findViewById(R.id.button);

        initiateApp();
        setData();//fake data.
        getPoster();
        TH_TIME_LIST = setThTimeData();

        if (prefHelper.isFirstTime()) {//initiate the theater data.
            initThread.start();
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAllThTimeByJSON();
                startActivity(new Intent(MainActivity.this,
                        MovieListActivity.class));
            }
        });
    }

    private boolean initiateApp() {
        Log.i("hs", "Initiating App");
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        AssetsHelper asHelper = new AssetsHelper(this, dbHelper);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            try {
                asHelper.loadTheaterData(db);
                return true;
            } catch (IOException e) {
                Log.e("hs", e.toString());
            }
        } finally {
            db.close();
        }
        return false;
    }

    private Thread initThread = new Thread() {
        @Override
        public void run() {
            Log.i(TAG, "Strat App for the first time");
            if (initiateApp()) {
                Log.i(TAG, "Initiate successfully");
                prefHelper.setFirstTime(false);

            } else {
                Log.e(TAG, "Initiate unsuccessfully");
            }
        }

    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_refresh) {
          //  Toast.makeText(this, "refresh", Toast.LENGTH_SHORT).show();
            getPoster();
          //  makeJsonArrayRequest();

        }

        return super.onOptionsItemSelected(item);
    }

    private void getPoster() {
        if (!lock)
            return;

        lock = false;
        // wtitle.setText(R.string.loading);

        new Thread() {

            @Override
            public void run() {
                try {
                    final Drawable d = GAEHelper.getPoster();

                    handler.post(new Runnable() {

                        @Override
                        public void run() {

                            layout.setBackground(d);
                        }

                    });
                } catch (Exception e) {
                } finally {
                    lock = true;
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            //wtitle.setText(R.string.app_name);
                        }

                    });

                }
            }

        }.start();
    }

    private void getAllMvInfoByJSON() {
        Gson gson = new Gson();

        Log.i("hs", "makeJsonArrayRequest()");
        final String mmUrL = "https://movingmovie99.appspot.com/";
        final String allmV = "all-mv";
        showpDialog();
        String url = mmUrL+allmV;
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Log.i(TAG, response.toString());
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                Movie mv = new Movie();

                                mv.setMvName(obj.getString("mvName"));
                                mv.setEnName(obj.getString("enName"));
                                mv.setPlayingDate(obj.getString("playingDate"));
                                mv.setIMDbRating(Double.parseDouble(obj.getString("IMDbRating")));
                                mv.setImgLink(obj.getString("imgLink"));
                                mv.setState(obj.getString("state"));
                                mv.setYoutubeUrlList(JSONHelper.getStringListFromJsonArray(obj.getJSONArray("youtubeUrlList")));

                                MV_LIST.add(mv);

                            } catch (JSONException e) {
                                e.printStackTrace();

                                //   Toast.makeText(getApplicationContext(),"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
                            }

                        }
                        //mAdapter.notifyDataSetChanged();
                        hidepDialog();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // VolleyLog.d(TAG, "Error: " + error.getMessage());
                Log.i("hs", "Error = " +  error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                hidepDialog();
            }

        });

        AppController.getInstance().addToRequestQueue(req);
    }


    private void getAllThTimeByJSON() {

        Log.i("hs", " getAllThTime");
        final String mmUrL = "https://movingmovie99.appspot.com/";
        final String allmV = "all-th";
        showpDialog();
        String url = mmUrL+allmV;
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Log.i(TAG, response.toString());
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);

                                String thId =  obj.getString("thId");


                                Gson gson = new Gson();
                                String inputString = gson.toJson(JSONHelper.getStringListFromJsonArray(obj.getJSONArray("thMvShowtimeList")));
                                Log.i("hs", " thId" + thId );
                                Log.i("hs", " inputString" + inputString );
                                helper.insertTheaterTimeInfo(db, thId.substring(10), inputString);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.i("hs", " JSONException" + e);
                                //   Toast.makeText(getApplicationContext(),"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
                            }

                        }
                        //mAdapter.notifyDataSetChanged();
                        hidepDialog();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // VolleyLog.d(TAG, "Error: " + error.getMessage());
                Log.i("hs", "Error = " +  error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                hidepDialog();
            }

        });

        AppController.getInstance().addToRequestQueue(req);
    }


    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    void setData(){

        new Thread() {

            @Override
            public void run() {
                try {

                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            String[] stateStr={"thisWeek", "firstRun", "notYet", "SecondRun"};
                            Random r = new Random();
                            int stateNum ;

                            for (int i = 0; i<20; i++){
                                Movie mv = new Movie();
                                stateNum = r.nextInt(4);
                                mv.setMvName("中文" + i);
                                mv.setEnName("enName" + i);
                                mv.setPlayingDate("2015/10/10|" + i);
                                mv.setIMDbRating(Double.parseDouble("9.0"));
                                mv.setImgLink("https://s.yimg.com/vu/movies/fp/mpost2/57/16/5716.jpg");
                                mv.setState(stateStr[stateNum]);
                                mv.setStory("殺人錦標賽》史考特曼恩執導，《派特的幸福劇本》勞勃狄尼洛主演。 賭場發牌莊家路克范恩（傑佛瑞迪恩摩根 飾）為醫治重病的女兒，需要在周五的晚上七點之前籌足30萬美金，早已散盡家財，甚至典當以往陸軍勳章的他，決定向賭場老闆教皇（勞勃狄尼洛 飾）求助，他曾為了教皇入獄多年，希望老闆能念舊情借錢給他，但教皇行事向來不顧情面，風格心狠手辣，路克非但沒借到錢，更被教皇的接班人「忠狗王子」戴瑞克開除，趕出賭場。 正巧知曉賭場金錢流向的同事考克斯（戴夫巴斯帝塔 飾）找上路克，計畫一起到賭場搶劫，走頭無路之下，路克只能鋌而走險，他們搶走了賭場的300萬美金，這筆現金足以動搖教皇的事業命脈，更是不能被警方發現的黑錢。 戴瑞克立刻出動追補他們，路克等人被逼到劫持了657號巴士，在大街上飛奔逃命，同時巡邏女警克莉絲（吉娜卡拉諾 飾）發現巴士有異狀，展開調查及追捕，加上考克斯失控的瘋狂行為，還有車上的人質狀況不斷，多方人馬展開一場更加無法預料的亡命之路，路克要如何即時把錢送進醫院呢？");
                                mv.setActor("jasperActor handsomeActor  jhonBerryActor");
                                mv.setDirector("handsomeDirector");
                                mv.setGate(DataHelper.getGateTilteByTag("R"));
                                mv.setMvlength("124分");
                                mv.setWriter("writer");
                                mv.setAllMvThShowtimeList(setMvTimeList());
                                MV_LIST.add(mv);
                            }
                        }

                    });
                } catch (Exception e) {
                } finally {
                    lock = true;
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            //wtitle.setText(R.string.app_name);
                        }

                    });

                }
            }

        }.start();


    }

    List<String> setMvTimeList(){
        String s = "\"台北|/showtime/t02f23/a02/|三重幸福戲院|Ａ２廳 |10:10 14:00 17:50 21:40\",\"台北|/showtime/t02f08/a02/|湳山戲院|Ａ廳 |11:25 15:10 18:55 22:40\",\"台北|/showtime/t02f08/a02/|湳山戲院|Ａ廳 |11:25 15:10 18:55 22:40 00:00\",\"台北|/showtime/t02f08/a02/|湳山戲院|Ａ廳 |11:25 15:10 18:55 22:40\",\"台北|/showtime/t02f07/a02/|朝代戲院|Ａ廳 |10:00 13:50 17:50 21:30 21:30 21:30 21:31 21:32 \",\"台北|/showtime/t02f05/a02/|景美佳佳戲院|藍廳 |11:00 15:00 19:00\",\"台北|/showtime/t02e02/a02/|林園電影城|Ｅ廳 |12:35 17:10 21:40\",\"桃園|/showtime/t03322/a03/|中源戲院|B廳 |12:30 16:50 21:10\",\"桃園|/showtime/t03323/a03/|民和戲院|Ｂ廳 |12:00 17:55\",\"台中|/showtime/t04424/a04/|全球影城|國語版 B1紅寶廳 |10:00 12:10 14:20 16:30 18:40 20:50 23:00\",\"台中|/showtime/t04422/a04/|萬代福影城|龍廳 |12:10 18:05 20:20 22:30\",\"台中|/showtime/t04422/a04/|萬代福影城|金廳 |00:20\",\"嘉義|/showtime/t05521/a05/|新榮戲院|３廳 |12:40 17:00 21:20\",\"台南|/showtime/t06623/a06/|全美戲院|Ａ廳 |15:15 19:45\",\"高雄|/showtime/t07721/a07/|和春影城|Ｂ廳 |10:00 14:00 18:00 22:00\",\"高雄|/showtime/t07722/a07/|十全電影城|Ａ廳 |10:00 14:05 18:10 22:15\",\"高雄|/showtime/t07722/a07/|十全電影城|Ａ廳 |10:00 14:05 18:10 22:15\",\"高雄|/showtime/t07722/a07/|十全電影城|Ａ廳 |10:00 14:05 18:10 22:15\"";
        String[] mvTime = s.split(",");
        List<String> mvTimeList = new ArrayList<>();

        for (String time : mvTime){
            time = time.replaceAll("\"", "");
            //Log.i("hs", "time= " + time);
            mvTimeList.add(time);
        }

        return mvTimeList;
    }

    ArrayList<String> setThTimeData(){
        String str = "/showtime/theater_t02a01_a02.html!fcen42554274|腥紅山莊|http://www.photowant.com/photo101/fcen42554274/pm_fcen42554274_0013.jpg|國語版|09：25 10：10 11：45 12：30 14：05 14：50 15：40 16：30 17：10 18：50 19：30 20：20 21：10 21：50 22：40 23：30 00：10 01：00 @fsus03682448|間諜橋|http://www.photowant.com/photo101/fsus03682448/pm_fsus03682448_0005.jpg||09：50 11：10 12：30 13：50 15：10 16：30 17：50 19：10 20：30 21：50 23：10 00：30 ";
        String str2 = "/showtime/theater_t02a01_a02/20141030!fcen42554274|腥紅山莊|http://www.photowant.com/photo101/fcen42554274/pm_fcen42554274_0013.jpg||09：25 10：10 11：45 12：30 14：05 14：50 15：40 16：30 17：10 18：50 19：30 20：20 21：10 21：50 22：40 23：30 00：10 01：00 @fsus03682448|間諜橋|http://www.photowant.com/photo101/fsus03682448/pm_fsus03682448_0005.jpg||09：50 11：10 12：30 13：50 15：10 16：30 17：50 19：10 20：30 21：50 23：10 00：30 ";
        ArrayList<String> l = new ArrayList<>();
        l.add(str);
        l.add(str2);

        return l;

    }

}
