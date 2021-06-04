package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link List_Product_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class List_Product_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View v;
    private RecyclerView recyclerView1, recyclerView2, recyclerView3;
    private ProductAdapter adapter;
    private SQLite_Product sqLite_product;

    public List_Product_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment List_Product_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static List_Product_Fragment newInstance(String param1, String param2) {
        List_Product_Fragment fragment = new List_Product_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_list_product, container, false);
        recyclerView1 = v.findViewById(R.id.list_computer);
        recyclerView2 = v.findViewById(R.id.list_phone);
        recyclerView3 = v.findViewById(R.id.list_access);
        sqLite_product = new SQLite_Product(getContext());
        return v;
    }

    @Override
    public void onResume() {
        List<Product> products = sqLite_product.getAllProduct();

        List<Product> list_computer = new ArrayList<>();
        for(int i = 0; i < products.size(); i++){
            if(products.get(i).getType().equals("Máy tính"))   list_computer.add(products.get(i));
        }

        adapter = new ProductAdapter(list_computer, getActivity());
        recyclerView1.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView1.setAdapter(adapter);

        List<Product> list_phone = new ArrayList<>();
        for(int i = 0; i < products.size(); i++){
            if(products.get(i).getType().equals("Điện thoại"))   list_phone.add(products.get(i));
        }
        adapter = new ProductAdapter(list_phone, getActivity());
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView2.setAdapter(adapter);

        List<Product> list_access = new ArrayList<>();
        for(int i = 0; i < products.size(); i++){
            if(products.get(i).getType().equals("Phụ kiện"))   list_access.add(products.get(i));
        }
        adapter = new ProductAdapter(list_access, getActivity());
        recyclerView3.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView3.setAdapter(adapter);
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_search_option, menu);
        MenuItem item = menu.findItem(R.id.mSearch);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<Product> list = sqLite_product.getProductByName(newText);
                List<Product> list_computer = new ArrayList<>();
                List<Product> list_phone = new ArrayList<>();
                List<Product> list_access = new ArrayList<>();
                for(int i = 0; i < list.size(); i++){
                    if(list.get(i).getType().equals("Máy tính"))   list_computer.add(list.get(i));
                }
                for(int i = 0; i < list.size(); i++){
                    if(list.get(i).getType().equals("Điện thoại"))   list_phone.add(list.get(i));
                }
                for(int i = 0; i < list.size(); i++){
                    if(list.get(i).getType().equals("Phụ kiện"))   list_access.add(list.get(i));
                }
                adapter = new ProductAdapter(list_computer, getActivity());
                recyclerView1.setAdapter(adapter);
                adapter = new ProductAdapter(list_phone, getActivity());
                recyclerView2.setAdapter(adapter);
                adapter = new ProductAdapter(list_access, getActivity());
                recyclerView3.setAdapter(adapter);
                return true;
            }
        });
    }

}