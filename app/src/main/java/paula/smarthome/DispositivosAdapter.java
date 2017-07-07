package paula.smarthome;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
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
        final Dispositivo dispositivo = dispositivos.get(position);

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.layout_dispositivos, null);
        }
        System.out.println("estado" + dispositivo.getEstado());
        System.out.println("id" + dispositivo.getId());

        final TextView ubicacionTextView = (TextView)convertView.findViewById(R.id.textview_ubicacion);
        final TextView idTextView = (TextView)convertView.findViewById(R.id.textview_id);
        final ImageView outlet = (ImageView)convertView.findViewById(R.id.outlet);
        ubicacionTextView.setText(dispositivo.getUbicacion());
        idTextView.setText(dispositivo.getId().toString());
        if(dispositivo.getEstado()==true){
            outlet.setImageResource(R.drawable.outletprendido);
        }else if(dispositivo.getEstado()==false){
            outlet.setImageResource(R.drawable.outletapagado);
        }

        return convertView;
    }


}