package br.com.thiengo.tcmaterialdesign.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import br.com.thiengo.tcmaterialdesign.MainActivity;
import br.com.thiengo.tcmaterialdesign.R;
import br.com.thiengo.tcmaterialdesign.adapters.CarAdapter;
import br.com.thiengo.tcmaterialdesign.domain.Car;
import br.com.thiengo.tcmaterialdesign.interfaces.RecyclerViewOnClickListenerHack;

/**
 * Created by elyervesson on 15/05/17.
 */

/* Facilita quando iniciar o desenvolvimento do navegation drawer, pois, na main activity vão ser usados fragments */
public class CarFragment extends Fragment implements RecyclerViewOnClickListenerHack {

    private RecyclerView recyclerView;
    private List<Car> cars; // Este List esta na mesma posição de memoria do List de CarAdapter

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        recyclerView.setHasFixedSize(true);// Seta que o tamanho do recicleView não vai mudar

        // Mostrar carregando mais itens no final da lista
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            /* CARREGAR MAIS CARROS */
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                CarAdapter adapter = (CarAdapter) recyclerView.getAdapter();

                // Nesse caso estamos mostrando o ultimo item
                if (cars.size() == layoutManager.findLastCompletelyVisibleItemPosition() + 1) {
                    // Carregar mais 10
                    List<Car> listAux = ((MainActivity) getActivity()).getSetCarList(10);

                    for (int i = 0; i < listAux.size(); i++) {
                        adapter.addListItem(listAux.get(i), cars.size());
                    }
                }
            }

        });


        /* GERENCIA A APRESENTAÇÃO DOS ITENS */
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        cars = ((MainActivity) getActivity()).getSetCarList(10);
        CarAdapter adapter = new CarAdapter(getActivity(), cars); // Criação do adapter
        adapter.setRecyclerViewOnClickListenerHack(this);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onClickListener(View view, int position) {
        Toast.makeText(getActivity(), "Position: " + position, Toast.LENGTH_SHORT).show();

        CarAdapter adapter = (CarAdapter) recyclerView.getAdapter();
        adapter.removeListItem(position);
    }
}
