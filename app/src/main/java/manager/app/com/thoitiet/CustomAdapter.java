package manager.app.com.thoitiet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    Context context;
    ArrayList<Thoitiet> arrayList;

    public CustomAdapter(Context context, ArrayList<Thoitiet> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.listviewthoitiet, null);
        Thoitiet thoitiet = arrayList.get(i);

        TextView txtNgay        = view.findViewById(R.id.ngay);
        TextView txtTrangThai   = view.findViewById(R.id.trangthai);
        TextView txtCao         = view.findViewById(R.id.cao);
        TextView txtThap        = view.findViewById(R.id.thap);
        ImageView imgStatus     = view.findViewById(R.id.image);

        txtNgay.setText(thoitiet.Ngay);
        txtTrangThai.setText(thoitiet.TrangThai);
        txtCao.setText(thoitiet.CaoNhat+" ºC");
        txtThap.setText(thoitiet.ThapNhat+" ºC -- ");

        Picasso.with(context).load("http://openweathermap.org/img/w/"+thoitiet.Anh+".png").into(imgStatus);

        return view;
    }
}
