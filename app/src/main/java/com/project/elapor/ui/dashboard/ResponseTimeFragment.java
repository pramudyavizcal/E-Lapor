package com.project.elapor.ui.dashboard;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.elapor.R;
import com.project.elapor.databinding.FragmentResponseTimeBinding;
import com.project.elapor.ui.pengaduan_terkirim.PengaduanAdapterTerkirim;
import com.project.elapor.ui.pengaduan_terkirim.PengaduanFragmentTerkirim;

public class ResponseTimeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private PengaduanAdapterTerkirim pengaduanAdapter;
    private String role;
    private ImageView imgbtnsetting;
    private String image;
    private String name;
    private String unit;
    private String queries;
    SwipeRefreshLayout swipeLayout;
    LinearLayout laporan;
    private FragmentResponseTimeBinding binding;
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    public void onResume() {
        super.onResume();
        checkRole();
        getTotalTaskFinished();
        getTotalTaskFinishedUser();
        convertResponseTime();
    }

    @SuppressLint("ResourceAsColor")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentResponseTimeBinding.inflate(inflater, container, false);
        binding.klikLaporan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), ListLaporanSelesai.class);
//                startActivity(intent);
                Fragment fragment = new ListLaporanSelesai();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        swipeLayout = (SwipeRefreshLayout) binding.swipeToRefreshLayout;
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
                        if (role.equals("user")) {
                            binding.title.setText("Response Time");
                            binding.keterangan.setVisibility(View.GONE);
                        }
                        else {
                            binding.title.setText("Response Time");
                            binding.keterangan.setVisibility(View.GONE);
                            //binding.keterangan.setText("Pengaduan Masuk\ndari Pegawai");
                        }
                    }
                });
    }
    private void convertResponseTime() {
        FirebaseFirestore
                .getInstance()
                .collection("response_time")
                .whereEqualTo("uid", uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.getResult().size() > 0) {
                            long responseTimeTotal = 0;
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                responseTimeTotal += document.getLong("responseTime");
                            }


                            long responseTotalInMinute = responseTimeTotal / 60;
                            if(responseTotalInMinute <=60) {
                                /// menit
                                binding.time.setText(responseTotalInMinute + " Menit");
                            } else  {
                                /// jam
                                long responseTotalInHour = responseTotalInMinute / 60;
                                long minute = responseTotalInMinute % 60;
                                binding.time.setText(responseTotalInHour + " Jam " + minute + " Menit");
                            }
                        }
                    }
                });
    }

    private void getTotalTaskFinished() {
        FirebaseFirestore
                .getInstance()
                .collection("report")
                .whereEqualTo("adminUid", uid)
                .whereEqualTo("status", "finish")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.size() > 0) {
                            int totalTaskFinished = queryDocumentSnapshots.size();
                            binding.task.setText(totalTaskFinished + " Laporan");
                        }
                    }
                });
    }

    private void getTotalTaskFinishedUser() {
        FirebaseFirestore
                .getInstance()
                .collection("report")
                .whereEqualTo("userUid", uid)
                .whereEqualTo("status", "finish")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.size() > 0) {
                            int totalTaskFinished = queryDocumentSnapshots.size();
                            binding.task.setText(totalTaskFinished + " Laporan");
                        }
                    }
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