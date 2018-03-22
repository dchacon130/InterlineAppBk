package com.interlineappv30;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by INTEL on 6/11/2017.
 */

public class ListoPagosClass extends BaseAdapter {
    private Activity context;
    ArrayList<PagosListClass> countries;
    public ListoPagosClass(Activity context, ArrayList<PagosListClass> countries) {
        //  super(context, R.layout.row_item, countries);
        this.context = context;
        this.countries = countries;
    }
    public static class ViewHolder
    {
        TextView tvCodEmpresa, tvConsecutivo, tvFecha, tvPago;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        ListoPagosClass.ViewHolder vh;
        if(convertView==null) {
            vh=new ListoPagosClass.ViewHolder();
            row = inflater.inflate(R.layout.list_item_pagos, null, true);
            vh.tvCodEmpresa = (TextView) row.findViewById(R.id.codigo_cliente);
            vh.tvConsecutivo = (TextView) row.findViewById(R.id.consecutivo);
            vh.tvFecha = (TextView) row.findViewById(R.id.fecha_sys);
            vh.tvPago = (TextView) row.findViewById(R.id.pago);
            // store the holder with the view.
            row.setTag(vh);
        }
        else {
            vh = (ListoPagosClass.ViewHolder) convertView.getTag();
        }
        //vh.tvId.setText(countries.get(position).getId());
        vh.tvCodEmpresa.setText(""+countries.get(position).getcodigo_cliente());
        vh.tvConsecutivo.setText(""+countries.get(position).getconsecutivo());
        vh.tvFecha.setText(""+countries.get(position).getfecha_sys());
        vh.tvPago.setText(""+countries.get(position).getpago());
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
