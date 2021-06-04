package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> list;
    private Activity activity;
    private SQLite_Product sqLite_product;
    private Locale localeEN = new Locale("en", "EN");
    private NumberFormat en = NumberFormat.getInstance(localeEN);
    public ProductAdapter(List<Product> list, Activity activity) {
        this.list = list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View v=inflater.inflate(R.layout.view_product,parent,false);
        return new ProductViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product p = list.get(position);
        Activity activity = this.activity;
        byte[] bytes = p.getImg();
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        holder.img.setImageBitmap(bitmap);
        holder.name.setText(p.getName());
        holder.count.setText(en.format(p.getCount()));
        holder.price.setText(en.format(Long.parseLong(p.getPrice())) + " đồng");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, Update_Product_Staff_Activity.class);
                intent.putExtra("id", p.getId());
                activity.startActivity(intent);
            }
        });
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(activity)
                        .setTitle("Cảnh báo")
                        .setMessage("Bạn chắc muốn xóa sản phẩm này không?")
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                sqLite_product = new SQLite_Product(activity);
                                sqLite_product.deleteProduct(p.getId());
                                list.remove(holder.getAdapterPosition());
                                notifyItemRemoved(holder.getAdapterPosition());
                                notifyItemRangeChanged(holder.getAdapterPosition(), list.size());
                            }
                        })
                        .setNegativeButton("Không", null)
                        .setIcon(R.drawable.ic_baseline_warning_amber_24)
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if(list!=null)  return list.size();
        return 0;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        private ImageView img, remove;
        private TextView name, count, price;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img_view);
            name = itemView.findViewById(R.id.name_view);
            count = itemView.findViewById(R.id.count_view);
            price = itemView.findViewById(R.id.price_view);
            remove = itemView.findViewById(R.id.remove_view);
        }
    }
}
