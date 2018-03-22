package com.interlineappv30;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
/**
 * Created by INTEL on 11/08/2017.
 */

public class LoteListaClass extends BaseAdapter {

    private Activity context;
    ArrayList<LoteClass> countries;

    public LoteListaClass(Activity context, ArrayList<LoteClass> countries) {
        //  super(context, R.layout.row_item, countries);
        this.context = context;
        this.countries = countries;

    }

    public static class ViewHolder
    {
        TextView tvProducto, tvNombre, tvLote, tvFecha;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;

        LayoutInflater inflater = context.getLayoutInflater();
        ViewHolder vh;
        if(convertView==null) {
            vh=new ViewHolder();
            row = inflater.inflate(R.layout.list_item_lote, null, true);
            vh.tvProducto = (TextView) row.findViewById(R.id.txtProducto);
            vh.tvNombre = (TextView) row.findViewById(R.id.txtNombreLote);
            vh.tvLote = (TextView) row.findViewById(R.id.txtLote);
            vh.tvFecha = (TextView) row.findViewById(R.id.txtFechaVenc);
            // store the holder with the view.
            row.setTag(vh);
        }
        else {
            vh = (ViewHolder) convertView.getTag();
        }

        //vh.tvId.setText(countries.get(position).getId());
        vh.tvProducto.setText(""+countries.get(position).getproducto());
        vh.tvNombre.setText(""+countries.get(position).getnombrePro());
        vh.tvLote.setText(""+countries.get(position).getlote());
        vh.tvFecha.setText(""+countries.get(position).getfechaExp());
        return  row;
    }

    public long getItemId(int position) {
        return position;
    }

    public Object getItem(int position) {
        return position;
    }

    public int getCount() {

        if(countries.size()<=0)
            return 1;
        return countries.size();
    }
}

