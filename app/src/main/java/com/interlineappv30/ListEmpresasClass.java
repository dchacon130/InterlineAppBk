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

public class ListEmpresasClass extends BaseAdapter {

    private Activity context;
    ArrayList<EmpresaClass> countries;

    public ListEmpresasClass(Activity context, ArrayList<EmpresaClass> countries) {
        //  super(context, R.layout.row_item, countries);
        this.context = context;
        this.countries = countries;

    }

    public static class ViewHolder
    {
        TextView tvId, tvCodEmpresa, tvNif, tvNombreEmpresa, tvDireccionEmp;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;

        LayoutInflater inflater = context.getLayoutInflater();
        ViewHolder vh;
            if(convertView==null) {
                vh=new ViewHolder();
                row = inflater.inflate(R.layout.list_item, null, true);
                vh.tvId = (TextView) row.findViewById(R.id.id);
                vh.tvCodEmpresa = (TextView) row.findViewById(R.id.codigo_cliente);
                vh.tvNif = (TextView) row.findViewById(R.id.nif);
                vh.tvNombreEmpresa = (TextView) row.findViewById(R.id.nombre);
                vh.tvDireccionEmp = (TextView) row.findViewById(R.id.direccion);
                // store the holder with the view.
                row.setTag(vh);
            }
            else {
                vh = (ViewHolder) convertView.getTag();
            }

        //vh.tvId.setText(countries.get(position).getId());
        vh.tvCodEmpresa.setText(""+countries.get(position).getCodEmpresa());
        vh.tvNif.setText(""+countries.get(position).getnif());
        vh.tvNombreEmpresa.setText(""+countries.get(position).getnombreEmpresa());
        vh.tvDireccionEmp.setText(""+countries.get(position).getdireccionEmp());
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
