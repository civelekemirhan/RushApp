package com.example.rushapp.screen;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.rushapp.callback.ProviderInfoCallback;
import com.example.rushapp.data.db.DBOperations;
import com.example.rushapp.data.model.Provider;
import com.example.rushapp.data.model.ServiceCard;
import com.example.rushapp.databinding.FragmentProviderServeScreenBinding;


public class ProviderServeScreen extends Fragment {

    FragmentProviderServeScreenBinding binding;
    MenuItem item;

    public ProviderServeScreen(MenuItem item) {
        // Boş, gerektiğinde uygulamanıza uygun şekilde kullanılabilir
        this.item=item;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Fragment için görünüm oluşturuluyor
        binding = FragmentProviderServeScreenBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Veri alımı ve UI güncellemeleri
        loadProviderInformation();
    }

    private void loadProviderInformation() {
        // ProgressDialog gösteriliyor


                item.setEnabled(false);


        // Profil fotoğrafının yüklenmesi
        DBOperations dbo = new DBOperations();
        dbo.getCurrentProfilePic().getDownloadUrl()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Uri uri = task.getResult();
                        Glide.with(requireContext())
                                .load(uri)
                                .into(binding.providerProfilePhoto);
                        item.setEnabled(true);
                    }else{
                        item.setEnabled(true);
                    }
                });

        // Servis bilgilerinin yüklenmesi ve buton tıklaması işlemleri
        dbo.getProviderInformation(new ProviderInfoCallback() {
            @Override
            public void onProviderReceived(Provider provider) {
                 // ProgressDialog gizleniyor

                binding.serviceButton.setOnClickListener(v -> {
                    String serviceName = binding.serviceName.getText().toString();
                    String serviceExplain = binding.serviceExplain.getText().toString();
                    String servicePrice = binding.servicePrice.getText().toString();
                    String serviceField = binding.serviceField.getText().toString();

                    if (!serviceName.isEmpty() && !serviceExplain.isEmpty() && !servicePrice.isEmpty() && !serviceField.isEmpty()) {
                        // Yeni servis kartı oluşturuluyor
                        ServiceCard card = new ServiceCard(serviceName, provider.getName(), provider.getMail(), serviceExplain, serviceField, servicePrice, provider.getJob(), provider.getProfilePhoto(),provider.getUserUid());
                        provider.serve(card);

                        // Girdi alanları temizleniyor
                        binding.serviceName.setText("");
                        binding.serviceExplain.setText("");
                        binding.servicePrice.setText("");
                        binding.serviceField.setText("");

                        Toast.makeText(requireContext(), "Servis başarıyla oluşturuldu", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireContext(), "Boş alan bırakmayınız", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
