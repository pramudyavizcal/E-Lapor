package com.project.elapor.ui.dashboard;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.elapor.databinding.ActivityListLaporanSelesaiBinding;

public class ListLaporanSelesai extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    SwipeRefreshLayout swipeLayout;
    private ActivityListLaporanSelesaiBinding binding;
    private ListLaporanSelesaiAdapter adapter;
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    public void onResume() {
        super.onResume();
        checkRole();
    }

    @SuppressLint("ResourceAsColor")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = ActivityListLaporanSelesaiBinding.inflate(inflater, container, false);
        swipeLayout = (SwipeRefreshLayout) binding.swipeToRefreshLayout1;
        swipeLayout.setOnRefreshListener(this);

        return binding.getRoot();
    }

    private void checkRole() {
        FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(user.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String role = "" + documentSnapshot.get("role");
                        String unit = "" + documentSnapshot.get("unit");
                        if(role.equals("user")) {
                            binding.title.setText("Pengaduan Diselesaikan");
                            binding.keterangan.setText("Pengaduan Diselesaikan");
                            initRecyclerView(role);
                            initViewModel(role);
                        }
                        else {
                            binding.title.setText("Pengaduan Diselesaikan");
                            binding.keterangan.setVisibility(View.GONE);
                            //binding.keterangan.setText("Pengaduan Masuk\ndari Pegawai");
                        }
                    }
                });
    }

    private void initRecyclerView(String role) {
        binding.rvPengaduan.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ListLaporanSelesaiAdapter(role, "");
        binding.rvPengaduan.setAdapter(adapter);
    }

    private void initViewModel(String role) {
        ListLaporanSelesaiViewModel viewModel = new ViewModelProvider(this).get(ListLaporanSelesaiViewModel.class);
        binding.progressBar.setVisibility(View.VISIBLE);
        if(role.equals("user")) {
            viewModel.setListPengaduanUserByUidx(user.getUid());
        }
        else {
            viewModel.setListPengaduanAdminByUidx(user.getUid());
        }
        viewModel.getPengaduan().observe(this, pengaduanModelArrayList -> {
            if (pengaduanModelArrayList.size() > 0) {
                if(role.equals("user")) {
                    binding.noDataUser.setVisibility(View.GONE);
                } else {
                    binding.noDataAdmin.setVisibility(View.GONE);
                }
                adapter.setData(pengaduanModelArrayList);
            } else {
                if(role.equals("user")) {
                    binding.noDataUser.setVisibility(View.VISIBLE);
                } else {
                    binding.noDataAdmin.setVisibility(View.VISIBLE);
                }
            }
            binding.progressBar.setVisibility(View.GONE);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onRefresh() {
        checkRole();
        swipeLayout.setRefreshing(false);
    }

}