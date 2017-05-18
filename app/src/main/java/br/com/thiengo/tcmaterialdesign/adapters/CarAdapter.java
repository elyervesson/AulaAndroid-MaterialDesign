package br.com.thiengo.tcmaterialdesign.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.List;

import br.com.thiengo.tcmaterialdesign.R;
import br.com.thiengo.tcmaterialdesign.domain.Car;
import br.com.thiengo.tcmaterialdesign.extras.ImageHelper;
import br.com.thiengo.tcmaterialdesign.interfaces.RecyclerViewOnClickListenerHack;

/**
 * Created by elyervesson on 15/05/17.
 */
// ViewHolder: trabalha a cash, guardando a view para ser reutilizada
public class CarAdapter extends RecyclerView.Adapter<CarAdapter.MyViewHolder>{

    private Context cntext;
    private List<Car> cars; // Este List esta na mesma posição de memoria do List de CarFragment
    private LayoutInflater layoutInflater;
    private RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack;
    private float scale;
    private int width;
    private int height;

    public CarAdapter(Context context, List<Car> list){
        cntext = context;
        cars = list;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        scale = cntext.getResources().getDisplayMetrics().density;
        width = cntext.getResources().getDisplayMetrics().widthPixels - (int)(14 * scale + 0.5f) ;
        height = (width / 16) * 9;// Como estamos utilizando o aspect 16:9
    }


    /* CHAMADO PARA CRIAR UMA NOVA VIEW*/
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i("CarAdapter", "onCreateViewHolder");
        // O terceiro parametro é para utilizar o layout parameters do recicleView parent
        // View view = layoutInflater.inflate(R.layout.item_car, parent, false);
        View view = layoutInflater.inflate(R.layout.item_car_card, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    /* VINCULA OS DADOS DA NOSSA LISTA A VIEW */
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Log.i("CarAdapter", "onBindViewHolder");
        // holder.imageViewCar.setImageResource(cars.get(position).getPhoto());

        holder.textViewModel.setText(cars.get(position).getModel());
        holder.textViewBrand.setText(cars.get(position).getBrand());

        // No exemplo feito pelo thiengo era necessario deixar o if, porem para mim funcionou melhor assim
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.imageViewCar.setImageResource(cars.get(position).getPhoto());
        //} else {
            Bitmap bitmap = BitmapFactory.decodeResource(cntext.getResources(), cars.get(position).getPhoto());
            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);

            // Não devemos utilizar este metodo para imagens muito grandes pois pode ficar muito pesado
            bitmap = ImageHelper.getRoundedCornerBitmap(cntext, bitmap, 10, width, height, false, false, true, true);
            holder.imageViewCar.setImageBitmap(bitmap);
        //}

        try {
            YoYo.with(Techniques.Tada)
                    .duration(700)
                    .repeat(1)
                    .playOn(holder.itemView);
        }catch (Exception e){

        }
    }

    /* TAMANHO DO SET (LISTA DE CARROS) */
    @Override
    public int getItemCount() {
        return cars.size();
    }

    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r) {
        recyclerViewOnClickListenerHack = r;
    }

    public void addListItem(Car car, int position) {
        cars.add(car);
        notifyItemInserted(position);
    }

    public void removeListItem(int position) {
        cars.remove(position);
        notifyItemRemoved(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView imageViewCar;
        public TextView textViewModel;
        public TextView textViewBrand;

        public MyViewHolder(View itemView) {
            super(itemView);

            imageViewCar = (ImageView) itemView.findViewById(R.id.iv_car);
            textViewModel = (TextView) itemView.findViewById(R.id.tv_model);
            textViewBrand = (TextView) itemView.findViewById(R.id.tv_brand);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(recyclerViewOnClickListenerHack != null) {
                recyclerViewOnClickListenerHack.onClickListener(v, getAdapterPosition());
            }
        }
    }
}
