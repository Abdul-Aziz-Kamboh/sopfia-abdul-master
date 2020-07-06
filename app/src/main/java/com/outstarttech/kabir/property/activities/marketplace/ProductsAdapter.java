package com.outstarttech.kabir.property.activities.marketplace;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.inventioncore.kabir.sopfia.R;
import com.squareup.picasso.Picasso;

public class ProductsAdapter extends ArrayAdapter<String> {
    Context context;
    String rId[];
    String rName[];
    String rDescription[];
    String rPrice[];
    String rUrl[];
    String rWatermarked[];

    ProductsAdapter(Context c, String id[], String name[], String description[], String price[], String url[], String watermarked[]) {
        super(c, R.layout.rowproducts, R.id.productName, name);
        this.context = c;
        this.rId = id;
        this.rName = name;
        this.rDescription = description;
        this.rPrice = price;
        this.rUrl = url;
        this.rWatermarked = watermarked;
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.rowproducts, parent, false);
        ImageView productImage = row.findViewById(R.id.productImage);
        final TextView productName = row.findViewById(R.id.productName);
        TextView productDes = row.findViewById(R.id.description);
        final TextView productPrice = row.findViewById(R.id.productPrice);
        LinearLayout productItem = row.findViewById(R.id.productItem);

        // now set our resources on views
//        images.setImageResource(rImgs[position]);
//        Glide.with(getContext()).load(rWatermarked[position]).into(productImage);
//        String imageUri = "https://i.imgur.com/tGbaZCY.jpg";
        Picasso.with(context).load(rWatermarked[position]).into(productImage);
        //  Log.d("urls",""+rImgs[position]);
        productName.setText(rName[position]);
        productDes.setText(rDescription[position]);
        productPrice.setText(rPrice[position]);

        productItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "product price  : " + rPrice[position], Toast.LENGTH_SHORT).show();
            }
        });



        return row;
    }

}



