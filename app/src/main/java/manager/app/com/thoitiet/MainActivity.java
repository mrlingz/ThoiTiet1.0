package manager.app.com.thoitiet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    EditText edttimkiem;
    Button btntimkiem, btnnext;
    TextView nuoc,thanhpho, nhietdo, trangthai, doam, may, gio, tgian;
    ImageView imgicon;
    String City = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        thucthi();
        btntimkiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = edttimkiem.getText().toString();
                // kiểm tra rỗng
                if(city.equals("")) {
                    City = "ha noi";
                    laydulieu(City);
                }
                else {
                    City = city;
                    laydulieu(City);
                }
            }
        });
        // gán thành phố mặc định
        laydulieu("ha noi");
        // truyền dữ liệu sang màn hình 2 khi click btn next
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                // lấy giá trị edittext
                String city = edttimkiem.getText().toString();
                // truyền qua bằng putExtra
                intent.putExtra("name", city);
                // hàm thực thi0
                startActivity(intent);
            }
        });
    }
    // lấy dữ liệu thời tiết, truyền vào data là tên địa điểm,...
    public void  laydulieu (String data) {
        // thực hiện các request bằng thư viện volley
        RequestQueue RequestQueue = Volley.newRequestQueue(MainActivity.this);
        String  url = "https://api.openweathermap.org/data/2.5/weather?q="+data+"&units=metric&appid=e9d48255e72a832ad71e63242f6ff05a";
        StringRequest StringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("ketqua", response);
                        //xử lý kết quả trả về của api (dạng json)
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            // lấy ngày dạng giây
                            String ngay = jsonObject.getString("dt");
                            // format ngày
                            long l = Long.valueOf(ngay);
                            Date date = new Date(l*1000L);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE dd-MM-yyyy HH:mm");
                            String day = simpleDateFormat.format(date);

                            tgian.setText("Thời gian cập nhật: "+day);
                            // lấy tên thành phố
                            String tp   = jsonObject.getString("name");
                            // set text cho thành phố
                            thanhpho.setText(tp);


                            //// lấy thời tiết và ảnh ( mảng json )
                            JSONArray jsonArrayWeather = jsonObject.getJSONArray("weather");
                            JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                            String thoitiet = jsonObjectWeather.getString("main");
                            trangthai.setText(thoitiet);
                            String icon = jsonObjectWeather.getString("icon");

                            // thư viện picasso load icon qua đường đẫn
                            Picasso.with(MainActivity.this).load("http://openweathermap.org/img/w/"+icon+".png").into(imgicon);


                            //// lấy nhiệt độ, độ ẩm ( mảng json )
                            JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                            String Nhietdo = jsonObjectMain.getString("temp");
                            String Doam = jsonObjectMain.getString("humidity");

                            // chuyển kiểu về double
                            Double a = Double.valueOf(Nhietdo);
                            String NhietDo = String.valueOf(a.intValue());
                            nhietdo.setText(NhietDo +" độ C");
                            doam.setText(Doam +" %");

                            // lấy tốc độ gió
                            JSONObject jsonObjectWind = jsonObject.getJSONObject("wind");
                            String Gio = jsonObjectWind.getString("speed");
                            gio.setText(Gio +" m/s");

                            // lấy mây
                            JSONObject jsonObjectClouds = jsonObject.getJSONObject("clouds");
                            String May = jsonObjectClouds.getString("all");
                            may.setText(May +" %");

                            // lấy tên nước
                            JSONObject jsonObjectsys = jsonObject.getJSONObject("sys");
                            String tennuoc = jsonObjectsys.getString("country");
                            nuoc.setText(tennuoc);







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
        RequestQueue.add(StringRequest);
    }

    private void thucthi() {
        edttimkiem   = findViewById(R.id.editText);
        btntimkiem   = findViewById(R.id.buttontk);
        btnnext      = findViewById(R.id.btnnext);
        nuoc         = findViewById(R.id.nuoc);
        thanhpho     = findViewById(R.id.thanhpho);
        nhietdo      = findViewById(R.id.nhietdo);
        trangthai    = findViewById(R.id.trangthai);
        doam         = findViewById(R.id.tvdoam);
        may          = findViewById(R.id.tvmay);
        gio          = findViewById(R.id.tvgio);
        tgian        = findViewById(R.id.tgian);
        imgicon      = findViewById(R.id.imgicon);
    }
}
