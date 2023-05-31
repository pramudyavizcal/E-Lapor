package com.project.elapor.ui.dashboard;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.elapor.R;
import com.project.elapor.SettingActivity;
import com.project.elapor.databinding.FragmentDashboardBinding;
import com.project.elapor.ui.pengaduan_terkirim.PengaduanAdapterTerkirim;
import com.project.elapor.ui.pengaduan_masuk.PengaduanFragment;
import com.project.elapor.ui.pengaduan_terkirim.PengaduanViewModelTerkirim;

import java.util.Locale;
import java.util.Objects;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private DashboardAdapter adapter;
    private PengaduanAdapterTerkirim pengaduanAdapter;
    private String role;
    private ImageView imgbtnsetting;
    private String image;
    private String name;
    private String unit;
    private String queries;

    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        populateUserProfile();

        imgbtnsetting = binding.settingAkun;
        imgbtnsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
            }
        });


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        binding.responseTime.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getActivity(), ResponseTimeActivity.class));
//            }
//        });

        binding.searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable query) {
                if(query.toString().isEmpty()) {
                    queries = "all";
                } else {
                    queries = query.toString().trim().toLowerCase(Locale.ROOT);
                }
                if(!Objects.equals(role, "admin")) {
                    initRecyclerView(image, name, unit);
                    initViewModel();
                } else {
                    initRecyclerViewAdminSide();
                    initViewModelAdminSide();
                }

            }
        });
    }

    private void initRecyclerView(String image, String name, String unit) {
        binding.rvAdmin.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new DashboardAdapter(user.getUid(), image, name, unit);
        binding.rvAdmin.setAdapter(adapter);
    }

    private void initViewModel() {
        DashboardViewModel viewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        binding.progressBar.setVisibility(View.VISIBLE);
        if(queries.equals("all")) {
            viewModel.setListDashboardAdmin();
        } else {
            viewModel.setListDashboardAdminByQuery(queries);
        }

        viewModel.getDashboard().observe(this, dashboardModelArrayList -> {
            if (dashboardModelArrayList.size() > 0) {
                binding.noDataAdmin.setVisibility(View.GONE);
                adapter.setData(dashboardModelArrayList);
            } else {
                binding.noDataAdmin.setVisibility(View.VISIBLE);
            }
            binding.progressBar.setVisibility(View.GONE);
        });
    }

    private void populateUserProfile() {
        FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(user.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        image = "" + documentSnapshot.get("image");
                        name = "" + documentSnapshot.get("name");
                        String nip = "" + documentSnapshot.get("nip");
                        unit = "" + documentSnapshot.get("unit");
                        role = "" + documentSnapshot.get("role");

                        queries = "all";

                        if (role.equals("user")) {
                            binding.textView4.setVisibility(View.VISIBLE);
                            //binding.responseTime.setVisibility(View.VISIBLE);
                            initRecyclerView(image, name, unit);
                            initViewModel();
                        }
                        else {
                            binding.textView5.setVisibility(View.VISIBLE);
                            //binding.responseTime.setVisibility(View.VISIBLE);
                            initRecyclerViewAdminSide();
                            initViewModelAdminSide();
                        }

                        if (image.equals("null")) {
                            binding.image.setVisibility(View.INVISIBLE);
                            binding.imagex.setVisibility(View.VISIBLE);
                        }else{
                            Glide.with(requireActivity())
                                    .load(image)
                                    .into(binding.image);
                            binding.image.setVisibility(View.VISIBLE);
                            binding.imagex.setVisibility(View.INVISIBLE);
                        }
                        binding.name.setText(name);
                        binding.nip.setText("NIP: " + nip);
                        binding.unit.setText("Unit: " + unit);

                    }
                });
    }

    private void initRecyclerViewAdminSide() {
        binding.rvAdmin.setLayoutManager(new LinearLayoutManager(getActivity()));
        pengaduanAdapter = new PengaduanAdapterTerkirim("admin", "dashboard");
        binding.rvAdmin.setAdapter(pengaduanAdapter);
    }

    private void initViewModelAdminSide() {
        PengaduanViewModelTerkirim viewModel = new ViewModelProvider(this).get(PengaduanViewModelTerkirim.class);
        binding.progressBar.setVisibility(View.VISIBLE);
        if(queries.equals("all")) {
            viewModel.setListPengaduanAdminByUidComplete(user.getUid());
        } else {
            viewModel.setListPengaduanAdminByUidCompleteAndQuery(user.getUid(), queries);
        }
        viewModel.getPengaduan().observe(this, pengaduanModelArrayList -> {
            if (pengaduanModelArrayList.size() > 0) {
                binding.noDataUser.setVisibility(View.GONE);
                pengaduanAdapter.setData(pengaduanModelArrayList);
            } else {
                binding.noDataUser.setVisibility(View.VISIBLE);
            }
            binding.progressBar.setVisibility(View.GONE);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}