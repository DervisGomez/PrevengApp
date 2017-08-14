package org.app.prevengapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by dervis on 25/01/17.
 */
public class TipsAdater extends BaseAdapter {

    private TipsActivity context;
    private List<String> items;

    public TipsAdater(TipsActivity context, List<String> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public Object getItem(int position) {
        return this.items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;

        if (convertView == null) {
            // Create a new view into the list.
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.adapter_fotos, parent, false);

        }

        ImageView imageView=(ImageView)rowView.findViewById(R.id.ivImagenTips);
        ImageView delete=(ImageView)rowView.findViewById(R.id.ivDeleteTips);
        if (items.get(position).equals("1")){
            imageView.setImageResource(R.drawable.deslizamientoapp);
        }else if (items.get(position).equals("2")){
            imageView.setImageResource(R.drawable.incendioapp);
        }else if (items.get(position).equals("3")){
            imageView.setImageResource(R.drawable.inundacionapp);
        }else if (items.get(position).equals("4")){
            imageView.setImageResource(R.drawable.sismoapp);
        }
        PhotoViewAttacher photoViewAttacher;
        photoViewAttacher=new PhotoViewAttacher(imageView);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.cerrarFoto();
            }
        });

        // Set data into the view


        return rowView;
    }
}
