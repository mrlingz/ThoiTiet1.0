package manager.app.com.thoitiet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Main2Activity extends AppCompatActivity {
    String thanhpho = "";
    ImageView imgBack;
    TextView txtTen;
    ListView lv;
    CustomAdapter customAdapter;
    ArrayList<Thoitiet> mangthoitiet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        thucthi();
        Intent intent = getIntent();
        String city = intent.getStringExtra("name");
        Log.d("ketqua", city);
        if (city.equals("")) {
            thanhpho = "ha noi";
            get7day(thanhpho);
        }
        else  {
            thanhpho = city;
            get7day(thanhpho);
        }
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
    private  void  get7day(String data) {
        String url = "http://api.openweathermap.org/data/2.5/forecast?q="+data+"&units=metric&cnt=7&appid=e9d48255e72a832ad71e63242f6ff05a";
        RequestQueue requestQueue = Volley.newRequestQueue(Main2Activity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            // lấy tên tp
                            JSONObject jsonObjectCity = jsonObject.getJSONObject("city");
                            String name = jsonObjectCity.getString("name");
                            txtTen.setText(name);
                            ///////////////////////////////////////////////
                            JSONArray jsonArrayList = jsonObject.getJSONArray("list");
                            for(int i = 0; i < jsonArrayList.length(); i ++) {
                                JSONObject jsonObjectList = jsonArrayList.getJSONObject(i);
                                String ngay = jsonObjectList.getString("dt");
                                // format ngay string => Kiểu long => chuyển miligiây dang sang giây; EEEE là thứ
                                long l = Long.valueOf(ngay);
                                Date date = new Date(l*1000L);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE dd-MM-yyyy HH:mm");
                                String day = simpleDateFormat.format(date);

                                // lấy nhiệt độ
                                JSONObject jsonObjectNhietDo = jsonObjectList.getJSONObject("main");
                                String min = jsonObjectNhietDo.getString("temp_min");
                                String max = jsonObjectNhietDo.getString("temp_max");
                                // chuyển kiểu về double
                                Double a = Double.valueOf(max);
                                Double b = Double.valueOf(min);
                                String NhietDoMax = String.valueOf(a.intValue());
                                String NhietDoMin = String.valueOf(b.intValue());

                                // lấy ra thời tiết
                                JSONArray jsonArrayThoiTiet = jsonObjectList.getJSONArray("weather");
                                JSONObject jsonObjectThoiTiet = jsonArrayThoiTiet.getJSONObject(0);
                                String trangthai = jsonObjectThoiTiet.getString("description");
                                String icon = jsonObjectThoiTiet.getString("icon");

                                mangthoitiet.add(new Thoitiet(day, trangthai, icon, NhietDoMax, NhietDoMin));



                            }
                            customAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(stringRequest);
    }

    private void thucthi() {
        imgBack         = findViewById(R.id.back);
        txtTen          = findViewById(R.id.tentp);
        lv              = findViewById(R.id.lv);
        mangthoitiet    = new ArrayList<Thoitiet>();
        customAdapter   = new CustomAdapter(Main2Activity.this, mangthoitiet);
        lv.setAdapter(customAdapter);
    }
}
