package br.com.thiengo.tcmaterialdesign.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
                // GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();

                /*
                StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();

                // Equivalente ao findLastCompletelyVisibleItemPosition()
                int[] aux = layoutManager.findFirstCompletelyVisibleItemPositions(null);
                int max = -1;
                for (int i = 0; i < aux.length; i++){
                    max = aux[i] > max ? aux[i] : max;
                }
                */
                CarAdapter adapter = (CarAdapter) recyclerView.getAdapter();

                // Nesse caso estamos mostrando o ultimo item
                if (cars.size() == layoutManager.findLastCompletelyVisibleItemPosition() + 1) {
                //if (cars.size() == max + 1) {
                    // Carregar mais 10
                    List<Car> listAux = ((MainActivity) getActivity()).getSetCarList(10);

                    for (int i = 0; i < listAux.size(); i++) {
                        adapter.addListItem(listAux.get(i), cars.size());
                    }
                }
            }

        });

        /* TRATAMENTO DO CLIQUE */
        recyclerView.addOnItemTouchListener(new RecycleViewTouchListener(getActivity(), recyclerView, this));

        /* GERENCIA A APRESENTAÇÃO DOS ITENS */
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // layoutManager.setReverseLayout(true); // Preenche de baixo para cima e não o inverso
        recyclerView.setLayoutManager(layoutManager);

        /* GERENCIA A APRESENTAÇÃO DOS ITENS */
        /* O PROBLEMA DESSA IMPLEMENTAÇÃO É QUE NA APRESENTAÇÃO SÃO VISIVEIS ALGUNS GAPS
        final int colunas = 3;
        final boolean setReverseLayout = false;
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), colunas,GridLayoutManager.VERTICAL, setReverseLayout);
        recyclerView.setLayoutManager(layoutManager);
        */

        /* GERENCIA A APRESENTAÇÃO DOS ITENS */
        /*
        final int colunas = 3;
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(colunas,StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(layoutManager);
        */

        cars = ((MainActivity) getActivity()).getSetCarList(10);
        CarAdapter adapter = new CarAdapter(getActivity(), cars); // Criação do adapter
        // adapter.setRecyclerViewOnClickListenerHack(this);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onClickListener(View view, int position) {
        Toast.makeText(getActivity(), "onClickListener(): " + position, Toast.LENGTH_SHORT).show();

        CarAdapter adapter = (CarAdapter) recyclerView.getAdapter();
        adapter.removeListItem(position);
    }

    @Override
    public void onLongPressClickListener(View view, int position) {
        Toast.makeText(getActivity(), "onLongPressClickListener(): " + position, Toast.LENGTH_SHORT).show();

        CarAdapter adapter = (CarAdapter) recyclerView.getAdapter();
        adapter.removeListItem(position);
    }

    /* Outra forma de click listener e pressed click listener */
    private static class RecycleViewTouchListener implements RecyclerView.OnItemTouchListener {
        private Context context;
        private GestureDetector gestureDetector;
        private RecyclerViewOnClickListenerHack listenerHack;

        public RecycleViewTouchListener(Context cntxt,final RecyclerView recyclerView, RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack) {
            context = cntxt;
            listenerHack = recyclerViewOnClickListenerHack;

            /* ATRAVEZ DESSE GESTYRE DETECTOR É POSSIVEL IDENTIFICAR QUE TIPO DE CLIQUE FOI EXECUTADO */
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
                @Override
                public void onLongPress(MotionEvent event) {
                    super.onLongPress(event);
                    /* RETORNA A CHILD VIEW QUE ESTA ABAIXO DO LOCAL ONDE FOI CLICADO */
                    View view = recyclerView.findChildViewUnder(event.getX(), event.getY());

                    if(view != null && listenerHack != null) {
                        listenerHack.onLongPressClickListener(view, recyclerView.getChildLayoutPosition(view)); /* ERRRRRRRRRRRRRRRRRRRRRRROR */
                    }
                }

                @Override
                public boolean onSingleTapUp(MotionEvent event) {
                    /* RETORNA A CHILD VIEW QUE ESTA ABAIXO DO LOCAL ONDE FOI CLICADO */
                    View view = recyclerView.findChildViewUnder(event.getX(), event.getY());

                    if(view != null && listenerHack != null) {
                        listenerHack.onClickListener(view, recyclerView.getChildLayoutPosition(view)); /* ERRRRRRRRRRRRRRRRRRRRRRROR */
                    }

                    return true; /* SIGNIFICA QUE INTERCEPTAMOS A AÇÃO E ESTAMOS TRABALHANDO NELA */
                    // return super.onSingleTapUp(event);
                }
            });
        }

        /* QUANDO CLICADO, SEMPRE PASSARA NESSA CLASSE */
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            gestureDetector.onTouchEvent(e); /* AQUI SERA FEITO O REDIRECIONAMENTO PARA OS METODOS QUE TRATAM CLIQUE SIMPLES OU LONGO */
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
