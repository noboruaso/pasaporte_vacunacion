package com.example.pasaporte_vacunacion.Activities.Activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pasaporte_vacunacion.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AdaptadorNoticias extends RecyclerView.Adapter<AdaptadorNoticias.ViewHolderNoticias> {
    ArrayList<Noticia> lstNoti;

    public AdaptadorNoticias(ArrayList<Noticia> lstNoti) {
        this.lstNoti = lstNoti;
    }

    @Override
    public ViewHolderNoticias onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_noticias,null,false);
        return new ViewHolderNoticias(view);
    }

    @Override
    public void onBindViewHolder(AdaptadorNoticias.ViewHolderNoticias viewHolderNoticias, int i) {
        viewHolderNoticias.tvNombre.setText(lstNoti.get(i).getTitulo());
        viewHolderNoticias.tvDescripcion.setText(lstNoti.get(i).getDescripcion());
        viewHolderNoticias.tvfecha.setText(lstNoti.get(i).getFecha());
        viewHolderNoticias.ivFoto.setImageResource(lstNoti.get(i).getFoto());
    }

    @Override
    public int getItemCount() {
        return lstNoti.size();
    }
    public class ViewHolderNoticias extends RecyclerView.ViewHolder{
        TextView tvNombre, tvDescripcion, tvfecha;
        ImageView ivFoto;
        public ViewHolderNoticias(@NonNull View itemView){
            super(itemView);
            tvNombre = (TextView) itemView.findViewById(R.id.idNombre);
            tvDescripcion = (TextView) itemView.findViewById(R.id.idInfo);
            tvfecha = (TextView) itemView.findViewById(R.id.txtFechaVac);
        }
    }
}
