package com.example.e_commerce;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {

    private Context context;
    private ArrayList<Producto> productos;

    // Constructor
    public ProductoAdapter(Context context, ArrayList<Producto> productos) {
        this.context = context;
        this.productos = productos;
    }

    // Inflar la vista de un item y devolver el ViewHolder
    @Override
    public ProductoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false);
        return new ProductoViewHolder(view);
    }

    // Asignar los datos a cada item del RecyclerView
    @Override
    public void onBindViewHolder(ProductoViewHolder holder, int position) {
        Producto producto = productos.get(position);

        // Cargar la imagen desde la URL usando Picasso
        Picasso.get().load(producto.getImagenUrl()).into(holder.imageViewProducto);

        // Mostrar el nombre, categoría y precio
        holder.textViewNombre.setText(producto.getNombre());
        holder.textViewCategoria.setText(producto.getCategoria());
        holder.textViewPrecio.setText("$" + producto.getPrecio());
    }

    // Número de items en el RecyclerView
    @Override
    public int getItemCount() {
        return productos.size();
    }

    // ViewHolder para manejar las vistas del item
    public static class ProductoViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewProducto;
        TextView textViewNombre;
        TextView textViewCategoria;
        TextView textViewPrecio;

        public ProductoViewHolder(View itemView) {
            super(itemView);
            imageViewProducto = itemView.findViewById(R.id.img);
            textViewNombre = itemView.findViewById(R.id.nombre);
            textViewCategoria = itemView.findViewById(R.id.categoria);
            textViewPrecio = itemView.findViewById(R.id.precio);
        }
    }
}
