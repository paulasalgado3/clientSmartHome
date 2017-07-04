package paula.smarthome;

import android.content.Context;
import android.media.Image;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Paula on 04/07/2017.
 */

public class DispositivosAdapter extends BaseAdapter {
    private final Context mContext;
    private final ArrayList<Dispositivo> dispositivos;

    public DispositivosAdapter(Context c, ArrayList<Dispositivo> d) {
        this.mContext = c;
        this.dispositivos = d;
    }

    public int getCount() {
        return dispositivos.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }


    public View getView(int position, View convertView, ViewGroup parent) {


        TextView dummyTextView = new TextView(mContext);
        //dummyTextView.setText(String.valueOf(position));
        try{
            dummyTextView.setText(dispositivos.get(position).getId());

        System.out.println("position: " + position + "id: "+ dispositivos.get(position).getId());
        }catch (final Exception e){}
        return dummyTextView;
    }


}