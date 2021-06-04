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
 * Use the {@link List_Staff_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class List_Staff_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView1, recyclerView2;
    private StaffAdapter adapter;
    private SQLite_Staff sqLite_staff;
    private View v;
    private int id_login;

    public List_Staff_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Contact_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static List_Staff_Fragment newInstance(String param1, String param2) {
        List_Staff_Fragment fragment = new List_Staff_Fragment();
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
            id_login = getArguments().getInt("id_login");
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_list_staff, container, false);
        recyclerView1 = v.findViewById(R.id.list_admin);
        recyclerView2 = v.findViewById(R.id.list_staff);
        sqLite_staff = new SQLite_Staff(getContext());
        return v;
    }

    @Override
    public void onResume() {
        List<Staff> staffs = sqLite_staff.getAllStaff();
        //
        List<Staff> list_admin = new ArrayList<>();
        for(int i = 0; i < staffs.size(); i++){
            if(staffs.get(i).getPosition().equals("Admin"))   list_admin.add(staffs.get(i));
        }
        adapter = new StaffAdapter(list_admin, getActivity(), id_login);
        recyclerView1.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView1.setAdapter(adapter);
        //
        List<Staff> list_staff = new ArrayList<>();
        for(int i = 0; i < staffs.size(); i++){
            if(staffs.get(i).getPosition().equals("Nhân viên"))   list_staff.add(staffs.get(i));
        }
        adapter = new StaffAdapter(list_staff, getActivity(), id_login);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView2.setAdapter(adapter);
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
                List<Staff> list = sqLite_staff.getStaffByName(newText);
                List<Staff> list_admin = new ArrayList<>();
                List<Staff> list_staff = new ArrayList<>();
                for(int i = 0; i < list.size(); i++){
                    if(list.get(i).getPosition().equals("Admin"))   list_admin.add(list.get(i));
                }
                for(int i = 0; i < list.size(); i++){
                    if(list.get(i).getPosition().equals("Nhân viên"))   list_staff.add(list.get(i));
                }
                adapter = new StaffAdapter(list_admin, getActivity(), id_login);
                recyclerView1.setAdapter(adapter);
                adapter = new StaffAdapter(list_staff, getActivity(), id_login);
                recyclerView2.setAdapter(adapter);
                return true;
            }
        });
    }
}