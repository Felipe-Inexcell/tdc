package cl.tdc.felipe.tdc.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cl.tdc.felipe.tdc.R;

public class ComponentsAdapter extends RecyclerView.Adapter<ComponentsAdapter.ViewHolder> {
    List<ComponenteCantidad> mComponente = Collections.emptyList();
    private Context mContext;
    private LayoutInflater inflater;

    public ComponentsAdapter(Context context) {
        this.mContext = context;
        this.inflater = LayoutInflater.from(mContext);
    }

    public void addItem(ComponenteCantidad c){
        if(mComponente.size() == 0){
            mComponente = new ArrayList<>();
        }
        mComponente.add(getItemCount(),c);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = inflater.inflate(R.layout.componentes_view, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        ComponenteCantidad current = mComponente.get(position);
        Componente componente = current.getComponente();
        vh.componente.setText(componente.getNombre());
        vh.cantidad.setText(""+current.getCantidad());
    }

    @Override
    public int getItemCount() {
        return mComponente.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public List<ComponenteCantidad> getAll(){
        return mComponente;
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView componente, cantidad;

        public ViewHolder(View v) {
            super(v);
            componente = (TextView)v.findViewById(R.id.componente);
            cantidad = (TextView)v.findViewById(R.id.cantidad);
        }
    }

}
