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

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.rushapp.R;
import com.example.rushapp.data.db.DBOperations;
import com.example.rushapp.databinding.FragmentCustomerProfileScreenBinding;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.UploadTask;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class CustomerProfileScreen extends Fragment {

    FragmentCustomerProfileScreenBinding binding;

    private String userMail;
    private String userNickName;

    private String userJob;
    Uri selectedImageUri;
    ActivityResultLauncher<Intent> imagePickLauncher;

    MenuItem item;
    public CustomerProfileScreen(String userMail, String userNickName, String userJob,MenuItem item) {
        // Required empty public constructor
        this.userMail=userMail;
        this.userNickName=userNickName;
        this.item=item;
        this.userJob=userJob;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imagePickLauncher= registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result->{
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if(data!=null && data.getData()!=null){
                            selectedImageUri=data.getData();
                            Glide.with(getContext()).load(selectedImageUri).apply(RequestOptions.circleCropTransform()).into(binding.profilephoto);

                        }
                        DBOperations dbo=new DBOperations();
                        if(selectedImageUri!=null){

                            UploadTask uploadTask=dbo.getCurrentProfilePic().putFile(selectedImageUri);
                            dbo.setUserInformation(selectedImageUri,"profilePhoto");

                            uploadTask.addOnFailureListener(new OnFailureListener() {

                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle unsuccessful uploads
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                                    // ...
                                }
                            });
                        }

                    }
                });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding=FragmentCustomerProfileScreenBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    item.setEnabled(false);

            DBOperations dbOperations=new DBOperations();
            dbOperations.getCurrentProfilePic().getDownloadUrl()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            Uri uri=task.getResult();
                            Glide.with(getContext())
                                    .load(uri)
                                    .into(binding.profilephoto);
                            item.setEnabled(true);
                        }else{
                            item.setEnabled(true);
                        }
                    });







        binding.useremail.setText(userMail);
        binding.usernickname.setText(userNickName);
        binding.userJob.setText(userJob);
        eventHandler();




    }
    public void eventHandler(){

        binding.addphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(getActivity()).cropSquare().compress(512).maxResultSize(512,512)
                        .createIntent(new Function1<Intent, Unit>() {
                            @Override
                            public Unit invoke(Intent intent) {
                                imagePickLauncher.launch(intent);
                                return null;
                            }
                        });
            }
        });

        binding.profilephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(getActivity()).cropSquare().compress(512).maxResultSize(512,512)
                        .createIntent(new Function1<Intent, Unit>() {
                            @Override
                            public Unit invoke(Intent intent) {
                                imagePickLauncher.launch(intent);
                                return null;
                            }
                        });
            }
        });

        binding.editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Profil Düzenle");

                // set the custom layout
                final View customLayout = getLayoutInflater().inflate(R.layout.custom_dialog, null);
                builder.setView(customLayout);

                // add a button
                builder.setPositiveButton("OK", (dialog, which) -> {
                    // send data from the AlertDialog to the Activity
                    TextInputEditText editUserMailText = customLayout.findViewById(R.id.editUserMail);
                    TextInputEditText editUserNameText = customLayout.findViewById(R.id.editUserName);
                    TextInputEditText editUserJobText = customLayout.findViewById(R.id.editUserJob);
                    TextInputEditText editUserPassword=customLayout.findViewById(R.id.confirmpass);
                    DBOperations dbo =new DBOperations();

                    String email = editUserMailText.getText().toString();
                    String name = editUserNameText.getText().toString();
                    String job = editUserJobText.getText().toString();
                    String pass=editUserPassword.getText().toString();


                    if (email.isEmpty() || name.isEmpty() || job.isEmpty()) {
                        if (email.isEmpty()) {
                            email=binding.useremail.getText().toString();
                        }
                        if (name.isEmpty()) {
                            name=binding.usernickname.getText().toString();
                        }
                        if (job.isEmpty()) {
                            job=binding.userJob.getText().toString();
                        }
                        dbo.editProfile(email, name, job);
                        dbo.editMail(email,pass);
                        dbo.logOut();
                        Toast.makeText(getContext(),"Değişiklikler Başarılı Tekrar Giriş Yapnız",Toast.LENGTH_SHORT).show();
                        NavDirections action = CustomerPageDirections.actionCustomerPageToLoginScreen();
                        Navigation.findNavController(v).navigate(action);

                    } else {

                        dbo.editProfile(email, name, job);
                        dbo.editMail(email,pass);
                        dbo.logOut();
                        Toast.makeText(getContext(),"Değişiklikler Başarılı Tekrar Giriş Yapnız",Toast.LENGTH_SHORT).show();
                        NavDirections action = CustomerPageDirections.actionCustomerPageToLoginScreen();
                        Navigation.findNavController(v).navigate(action);

                    }


                });
                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        binding.logoutIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBOperations dbo=new DBOperations();
                dbo.logOut();
                Toast.makeText(getContext(),"Hesaptan Başarıyla çıkış yaptınız",Toast.LENGTH_SHORT).show();
                NavDirections action = CustomerPageDirections.actionCustomerPageToLoginScreen();
                Navigation.findNavController(v).navigate(action);
            }
        });
        binding.beProviderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBOperations dbo=new DBOperations();

                dbo.setUserInformation(false,"isCustomer");
                dbo.logOut();
                Toast.makeText(getContext(),"Tekrar giriş yaptığınızda Hizmet veren olacaksınız",Toast.LENGTH_SHORT).show();
                NavDirections action = CustomerPageDirections.actionCustomerPageToLoginScreen();
                Navigation.findNavController(v).navigate(action);


            }
        });

    }
}