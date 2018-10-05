package com.example.invitado.eventos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;



public class ListViewAdapter extends BaseAdapter {
    // Declare Variable
    String[] lvPlatillos;
    int[] imagenes;
    LayoutInflater inflater;

    public ListViewAdapter( String[] lvPlatillos, int[] imagenes) {
        this.lvPlatillos = lvPlatillos;
        this.imagenes = imagenes;
    }

    @Override
    public int getCount() {
        return lvPlatillos.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // Declare Variables
        TextView txtTitle;
        ImageView imgImg;

        View itemView = inflater.inflate(R.layout.list_row, parent, false);

        // Locate the TextViews in listview_item.xml
        txtTitle = (TextView) itemView.findViewById(R.id.list_row_title);
        imgImg = (ImageView) itemView.findViewById(R.id.list_row_image);

        // Capture position and set to the TextViews
        txtTitle.setText(lvPlatillos[position]);
        imgImg.setImageResource(imagenes[position]);

        return itemView;
    }
}