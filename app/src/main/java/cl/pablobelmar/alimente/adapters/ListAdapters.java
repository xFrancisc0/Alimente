package cl.pablobelmar.alimente.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import cl.pablobelmar.alimente.R;
import cl.pablobelmar.alimente.controlador.Utilidades;
import cl.pablobelmar.alimente.models.ProductoUsuario;


public class ListAdapters extends RecyclerView.Adapter<ListAdapters.ViewHolder> {

    List<ProductoUsuario> ShowList;
    Context context;
    int position;
    private OnItemClickListener itemClickListener;

    /*Constructor que es el que se llama donde se quiera ocupar el recyclerview*/
    public ListAdapters(List<ProductoUsuario> showList, OnItemClickListener itemClickListener) {
        ShowList = showList;
        this.itemClickListener = itemClickListener;
    }

    /*se crea el viewholder que es el que se encargara de mostrar la información de cada elemento con el diseño respectivo*/
    @NonNull
    @Override
    public ListAdapters.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        /*aqui se asocia el la vista individual que en este caso es el layout: list_item_alumno */
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_nota, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        /*se guarda el contexto y la posicion actual*/
        context = viewGroup.getContext();
        position = i;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapters.ViewHolder viewHolder, final int i) {
        /*se pasa a cada uno de los elementos el evento listener junto con el respectivo elemento*/
        viewHolder.bind(ShowList.get(i), itemClickListener);
        position = i;
    }

    /*metodo para obtener el tamaño de la lista*/
    @Override
    public int getItemCount() {
        return ShowList.size();
    }

    /*eliminar un elemento del listado*/
    public void removeItem(int position) {
        ShowList.remove(position);
        notifyItemRemoved(position);
    }
    /*Se remplaza el listado*/
    public void updateList(List<ProductoUsuario> data) {
        ShowList = data;
        notifyDataSetChanged();
    }

    /*Aqui es donde se ocupa el holder para cada elemento*/
    public class ViewHolder extends RecyclerView.ViewHolder {
        /* los atributos son los elemento que se encuentran en list_item_alumno.xml*/
        TextView textid;
        TextView textNombre;
        ImageView imageEdit;
        ImageView imageDelete;
        CardView cv;

        /*se referencian los elemento, tomar en cuenta que cada elemento posee su propia vista por eso se antepone itemview*/
        public ViewHolder(View itemView) {
            super(itemView);

            textid = itemView.findViewById(R.id.textid);
            textNombre = itemView.findViewById(R.id.textNombre);
            imageEdit = itemView.findViewById(R.id.imageEdit);
            imageDelete = itemView.findViewById(R.id.imageDelete);

            cv = itemView.findViewById(R.id.cardviewItemA);
        }

        /*se carga la información de cada alumno y se crean los listener que se deseen para cada uno de los atributos*/
        public void bind(final ProductoUsuario hisausuario, final OnItemClickListener listener) {


            textid.setText(Integer.valueOf(hisausuario.getNumeroLista() + 1) + ".-");

            textNombre.setText(context.getString(R.string.nombre_nota, Utilidades.primeraMayuscula(hisausuario.getNombreProducto())));

            imageEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.OnEditClick(hisausuario, getAdapterPosition());

                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.OnItemClick(hisausuario, getAdapterPosition());
                }
            });
            imageDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.OnDeleteClick(hisausuario, getAdapterPosition());
                }
            });
        }
    }

    /*estas interfaces son las que se generan de forma automática al momento de llamar al adaptador en la actividad*/
    /*son las mismas que se encuentran en bind en holder*/
    public interface OnItemClickListener {
        void OnItemClick(ProductoUsuario hisausuario, int position);

        void OnDeleteClick(ProductoUsuario hisausuario, int position);

        void OnEditClick(ProductoUsuario hisausuario, int position);
    }

}

