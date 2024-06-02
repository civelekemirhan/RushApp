package com.example.rushapp.screen;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.rushapp.R;
import com.example.rushapp.adapter.MyViewPagerAdapter;
import com.example.rushapp.data.db.DBOperations;
import com.example.rushapp.data.model.Provider;
import com.example.rushapp.databinding.FragmentProviderProfileScreenBinding;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.UploadTask;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class ProviderProfileScreen extends Fragment {

    MyViewPagerAdapter myViewPagerAdapter;
    FragmentProviderProfileScreenBinding binding;


    private Provider provider;

    ActivityResultLauncher<Intent> imagePickLauncher;
    MenuItem item;
    Uri selectedImageUri;

    public ProviderProfileScreen(Provider provider , MenuItem item) {
        this.provider=provider;
        this.item = item;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imagePickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getData() != null) {
                            selectedImageUri = data.getData();
                            Glide.with(getContext()).load(selectedImageUri).apply(RequestOptions.circleCropTransform()).into(binding.profilephoto);
                        }

                        if (selectedImageUri != null) {
                            UploadTask uploadTask = DBOperations.getCurrentProfilePic().putFile(selectedImageUri);
                            DBOperations.setUserInformation(selectedImageUri, "profilePhoto");

                            uploadTask.addOnFailureListener(exception -> {

                            }).addOnSuccessListener(taskSnapshot -> {

                            });
                        }
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProviderProfileScreenBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        item.setEnabled(false);

        DBOperations.getCurrentProfilePic().getDownloadUrl()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Uri uri = task.getResult();
                        Glide.with(getContext())
                                .load(uri)
                                .into(binding.profilephoto);
                        item.setEnabled(true);
                    } else {
                        item.setEnabled(true);
                    }
                });

        binding.useremail.setText(provider.getMail());
        binding.usernickname.setText(provider.getName());
        binding.userJob.setText(provider.getJob());

        eventHandler();
        myViewPagerAdapter=new MyViewPagerAdapter(requireActivity(),provider);
        binding.viewpager.setAdapter(myViewPagerAdapter);
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        binding.viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                binding.tabLayout.getTabAt(position).select();
            }
        });

    }

    public void eventHandler() {

        binding.addphoto.setOnClickListener(v -> ImagePicker.with(getActivity()).cropSquare().compress(512).maxResultSize(512, 512)
                .createIntent(intent -> {
                    imagePickLauncher.launch(intent);
                    return null;
                }));

        binding.profilephoto.setOnClickListener(v -> ImagePicker.with(getActivity()).cropSquare().compress(512).maxResultSize(512, 512)
                .createIntent(intent -> {
                    imagePickLauncher.launch(intent);
                    return null;
                }));

        binding.editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Profil Düzenle");


                final View customLayout = getLayoutInflater().inflate(R.layout.custom_dialog, null);
                builder.setView(customLayout);


                builder.setPositiveButton("OK", (dialog, which) -> {


                    TextInputEditText editUserNameText = customLayout.findViewById(R.id.editUserName);
                    TextInputEditText editUserJobText = customLayout.findViewById(R.id.editUserJob);
                    TextInputEditText editUserPassword=customLayout.findViewById(R.id.confirmpass);



                    String name = editUserNameText.getText().toString();
                    String job = editUserJobText.getText().toString();
                    String pass=editUserPassword.getText().toString();
                    Log.d("PASSES","provider.getPassword() = "+provider.getPassword()+"\npass = "+""+pass);



                    if ( name.isEmpty() || job.isEmpty() || pass.isEmpty()) {

                        if (name.isEmpty()) {
                            name=binding.usernickname.getText().toString();
                        }
                        if (job.isEmpty()) {
                            job=binding.userJob.getText().toString();
                        }
                        if(pass.isEmpty()){
                            Toast.makeText(getContext(),"Şifre kısmı boş geçilemez", Toast.LENGTH_SHORT).show();
                        }else{
                            if(pass.equals(provider.getPassword())){

                                DBOperations.editProfile(name, job);

                                DBOperations.logOut();
                                Toast.makeText(getContext(),"Değişiklikler Başarılı Tekrar Giriş Yapınız",Toast.LENGTH_SHORT).show();
                                NavDirections action = ProviderPageDirections.actionProviderPageToLoginScreen();
                                Navigation.findNavController(v).navigate(action);
                            }else{
                                Toast.makeText(getContext(),"Şifrenizi Yanlış girdiniz, Bu işlem gerçekleştirilemez", Toast.LENGTH_SHORT).show();
                            }

                        }

                    } else {

                        if(pass.equals(provider.getPassword())){

                            DBOperations.editProfile( name, job);
                            DBOperations.logOut();
                            Toast.makeText(getContext(),"Değişiklikler Başarılı Tekrar Giriş Yapınız",Toast.LENGTH_SHORT).show();
                            NavDirections action = ProviderPageDirections.actionProviderPageToLoginScreen();
                            Navigation.findNavController(v).navigate(action);
                        }else{
                            Toast.makeText(getContext(),"Şifrenizi Yanlış girdiniz, Bu işlem gerçekleştirilemez", Toast.LENGTH_SHORT).show();

                        }

                    }


                });
                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        binding.logoutIcon.setOnClickListener(v -> {
            DBOperations dbo = new DBOperations();
            dbo.logOut();
            Toast.makeText(getContext(), "Hesaptan Başarıyla çıkış yaptınız", Toast.LENGTH_SHORT).show();
            NavDirections action = ProviderPageDirections.actionProviderPageToLoginScreen();
            Navigation.findNavController(v).navigate(action);
        });

    }
}
