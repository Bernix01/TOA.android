package toa.toa.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import toa.toa.Objects.MrConsejo;
import toa.toa.R;
import toa.toa.activities.DetailconsejosNutricionalesActivity;

interface ItemClickListener {
    void onItemClick(View view, int position);
}

/**
 * Adaptador del recycler view
 */
public class MetaAdapter extends RecyclerView.Adapter<MetaAdapter.MetaViewHolder>
        implements ItemClickListener {

    /**
     * Lista de objetos {@link MrConsejo} que representan la fuente de datos
     * de inflado
     */
    private List<MrConsejo> items;

    /*
    Contexto donde actua el recycler view
     */
    private Context context;


    public MetaAdapter(List<MrConsejo> items, Context context) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public MetaViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_list, viewGroup, false);
        return new MetaViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(MetaViewHolder viewHolder, int i) {
        viewHolder.titulo.setText(items.get(i).getTitulo());
        viewHolder.prioridad.setText(items.get(i).getAutor());
        viewHolder.fechaLim.setText(items.get(i).getFechaLim());
        viewHolder.categoria.setText(items.get(i).getCategoria());
    }

    /**
     * Sobrescritura del método de la interfaz {@link ItemClickListener}
     *
     * @param view     item actual
     * @param position posición del item actual
     */
    @Override
    public void onItemClick(View view, int position) {
        DetailconsejosNutricionalesActivity.launch(
                (Activity) context, items.get(position).getIdConsejo());
    }


    public static class MetaViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        // Campos respectivos de un item
        public TextView titulo;
        public TextView prioridad;
        public TextView fechaLim;
        public TextView categoria;
        public ItemClickListener listener;

        public MetaViewHolder(View v, ItemClickListener listener) {
            super(v);
            titulo = (TextView) v.findViewById(R.id.titulo);
            prioridad = (TextView) v.findViewById(R.id.autor);
            fechaLim = (TextView) v.findViewById(R.id.fecha);
            categoria = (TextView) v.findViewById(R.id.categoria);
            this.listener = listener;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(v, getAdapterPosition());
        }
    }
}
