package com.interlineappv30;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by INTEL on 21/10/2017.
 */

public class ListValoresClass extends BaseAdapter {
    private Activity context;
    ArrayList<ValoresClass> valores;

    public ListValoresClass(Activity context, ArrayList<ValoresClass> valores) {
        //  super(context, R.layout.row_item, countries);
        this.context = context;
        this.valores = valores;
    }

    public static class ViewHolder
    {
        TextView tvdocumento, tvreferencia_factura, tvfecha_documento, tvfecha_vc_mto, tvsaldo;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        LayoutInflater inflater = context.getLayoutInflater();
        ListValoresClass.ViewHolder vh;
        if(convertView == null) {
            vh = new ListValoresClass.ViewHolder();
            row = inflater.inflate(R.layout.list_item_valores, null, true);

            vh.tvdocumento = (TextView) row.findViewById(R.id.iddocumento);
            vh.tvreferencia_factura = (TextView) row.findViewById(R.id.idreferencia_factura);
            vh.tvfecha_documento = (TextView) row.findViewById(R.id.idfecha_documento);
            vh.tvfecha_vc_mto = (TextView) row.findViewById(R.id.idfecha_vc_mto);
            vh.tvsaldo = (TextView) row.findViewById(R.id.idsaldo);
            // store the holder with the view.
            row.setTag(vh);
        }
        else {
            vh = (ListValoresClass.ViewHolder) convertView.getTag();
        }

        vh.tvdocumento.setText(valores.get(position).getdocumento());
        vh.tvreferencia_factura.setText(""+valores.get(position).getreferencia_factura());
        vh.tvfecha_documento.setText(""+valores.get(position).getfecha_documento());
        vh.tvfecha_vc_mto.setText(""+valores.get(position).getfecha_vc_mto());
        vh.tvsaldo.setText(""+valores.get(position).getsaldo());
        return  row;
    }

    public long getItemId(int position) {
        return position;
    }

    public Object getItem(int position) {
        return position;
    }

    public int getCount() {

        if(valores.size()<=0)
            return 1;
        return valores.size();
    }
}
