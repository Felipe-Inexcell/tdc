package cl.tdc.felipe.tdc.adapters;

import android.content.Context;
import android.opengl.Visibility;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cl.tdc.felipe.tdc.R;

/**
 * Created by Felipe on 14/02/2015.
 */
public class AdapterListAgenda extends ArrayAdapter<ItemListAgenda> {

    private final Context context;
    private final ArrayList<ItemListAgenda> items;
    public ImageView iv;
    public TextView when,where,what;

    public AdapterListAgenda(Context context, ArrayList<ItemListAgenda> itemsArrayList) {

        super(context, R.layout.itemlist_agenda, itemsArrayList);

        this.context = context;
        this.items = itemsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ItemListAgenda datos = items.get(position);
        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.itemlist_agenda, parent, false);

        // 3. Get the two text view from the rowView
        iv = (ImageView) rowView.findViewById(R.id.itemlistagenda_icon);
        what = (TextView) rowView.findViewById(R.id.itemlistagenda_what);
        where = (TextView) rowView.findViewById(R.id.itemlistagenda_where);
        when = (TextView) rowView.findViewById(R.id.itemlistagenda_when);

        // 4. Set the text for Views
        if(datos.getUrgencia() == 0){
            iv.setImageResource(R.drawable.ic_punto_ubicacion_celeste);
        }else if(datos.getUrgencia() == 1){
            iv.setImageResource(R.drawable.ic_punto_ubicacion_red);
        }
        else{
            iv.setVisibility(View.INVISIBLE);
        }

        when.setText(datos.getWhen());
        where.setText(datos.getWhere());
        what.setText(datos.getWhat());

        // 5. retrn rowView
        return rowView;
    }
}
