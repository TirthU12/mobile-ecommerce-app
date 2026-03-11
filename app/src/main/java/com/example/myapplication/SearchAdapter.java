package com.example.myapplication;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private List<Product> productList;

    public SearchAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.searchlayout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idImage = product.getId();
                Intent intent = new Intent(v.getContext(), detail.class);
                intent.putExtra("idImage", idImage);
                v.getContext().startActivity(intent);
            }
        });

        holder.productname.setText(product.getProductname());

        holder.imageview.setImageBitmap(product.getProductimage());

        holder.productprice.setText(String.valueOf(product.getProductprice()));

        holder.camera.setText(String.valueOf(product.getCamera()));

        holder.display.setText(String.valueOf(product.getDisplay()));

        holder.ram.setText(String.valueOf(product.getRam()));
        holder.rom.setText(String.valueOf(product.getRom()));


    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageview;
        TextView productname;
        TextView productprice;

        TextView camera;

        TextView display;
        TextView ram;
        TextView rom;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageview = itemView.findViewById(R.id.productimage);
            productname = itemView.findViewById(R.id.productname);
            productprice = itemView.findViewById(R.id.productprice);
            camera = itemView.findViewById(R.id.camera1);
            display = itemView.findViewById(R.id.display1);
            ram=itemView.findViewById(R.id.ram1);
            rom=itemView.findViewById(R.id.rom1);
        }
    }


}
