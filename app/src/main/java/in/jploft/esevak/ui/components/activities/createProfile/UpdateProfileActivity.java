package in.jploft.esevak.ui.components.activities.createProfile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.asksira.bsimagepicker.BSImagePicker;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import in.jploft.esevak.R;
import in.jploft.esevak.databinding.ActivityUpdateProfileBinding;
import in.jploft.esevak.network.ApiResponse;
import in.jploft.esevak.pojo.UserData;
import in.jploft.esevak.ui.base.BaseBindingActivity;
import in.jploft.esevak.ui.components.activities.home.HomeActivity;
import in.jploft.esevak.utils.Constants;
import in.jploft.esevak.utils.EmojiExcludeFilter;
import in.jploft.esevak.utils.ProgressDialog;
import in.jploft.esevak.utils.SessionManager;
import in.jploft.esevak.utils.Utility;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UpdateProfileActivity extends BaseBindingActivity implements BSImagePicker.OnSingleImageSelectedListener,
        BSImagePicker.OnMultiImageSelectedListener,
        BSImagePicker.ImageLoaderDelegate {
    private static final String TAG = UpdateProfileActivity.class.getName();
    private ActivityUpdateProfileBinding binding;
    private SessionManager sessionManager;
    private UpdateProfileViewModel viewModel;

    public static String selectedImagePath = "";
    private static final int REQ_CODE_GALLERY_PICKER3 = 30;
    private File mFileTemp;
    public static final String TEMP_PHOTO_FILE_NAME = "GoTo.png";


    @Override
    protected void setBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_profile);
        viewModel = new ViewModelProvider(this).get(UpdateProfileViewModel.class);
        binding.setLifecycleOwner(this);
    }

    @Override
    protected void createActivityObject(@Nullable Bundle savedInstanceState) {
        mActivity = this;
    }

    @SuppressLint("ResourceType")
    @Override
    protected void initializeObject() {
        sessionManager = new SessionManager();
        mFileTemp = Utility.createAppDir(mActivity, mFileTemp, TEMP_PHOTO_FILE_NAME);

        viewModel.responseLiveData.observe(this, new Observer<ApiResponse<UserData>>() {
            @Override
            public void onChanged(ApiResponse<UserData> it) {
                handleResult(it);
            }
        });

        binding.edtName.setFilters(new InputFilter[]{new EmojiExcludeFilter()});
        binding.edtAddress.setFilters(new InputFilter[]{new EmojiExcludeFilter()});
    }


    @Override
    protected void setListeners() {
        binding.btnCreateProfile.setOnClickListener(this);
        binding.tvSkip.setOnClickListener(this);
        binding.ivProfile.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        Utility.hideKeyboard(mActivity);
        switch (view.getId()) {
            case R.id.btnCreateProfile:
                updateProfileAPI();
                break;
            case R.id.tvSkip:
                sessionManager.setLogin();
                HomeActivity.startActivity(mActivity, null, true);
                finish();
                break;
            case R.id.ivProfile:
                BSImagePicker pickerDialog = new BSImagePicker.Builder("in.jploft.esevak")
                        .build();
                pickerDialog.show(getSupportFragmentManager(), "picker");
                break;
        }
    }

    private void updateProfileAPI() {
        String name = binding.edtName.getText().toString().trim();
        String email = binding.edtEmail.getText().toString().trim();
        String address = binding.edtAddress.getText().toString().trim();
        String phone = binding.edtMobile.getText().toString().trim();


        if (viewModel.isValidFormData(mActivity, selectedImagePath, name, email, address, phone)) {

            HashMap<String, String> reqData = new HashMap<>();

            reqData.put("userId", sessionManager.getUserData().getId());
            reqData.put("name", name);
            reqData.put("email", email);
            reqData.put("address", address);
            reqData.put("phone", phone);
            /*if (selectedImagePath != null)
                reqData.put("image", selectedImagePath);
            else
                reqData.put("image", "");*/

            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), mFileTemp);

            MultipartBody.Part req_id = MultipartBody.Part.createFormData("userId", sessionManager.getUserData().getId());
            MultipartBody.Part req_name = MultipartBody.Part.createFormData("name", name);
            MultipartBody.Part req_email = MultipartBody.Part.createFormData("email", email);
            MultipartBody.Part req_phone = MultipartBody.Part.createFormData("phone", phone);
            MultipartBody.Part req_address = MultipartBody.Part.createFormData("address", address);
            MultipartBody.Part profile_photo = null;
            if (selectedImagePath.isEmpty()) {
            } else {
                profile_photo = MultipartBody.Part.createFormData("image", mFileTemp.getName(), requestBody);
            }

            Log.e(TAG, "Api parameters - " + reqData.toString());
            Log.e(TAG, "profile - " + profile_photo);
            viewModel.updateProfile(req_id, req_name, req_email, req_phone, req_address, profile_photo);
        }
    }

    private void handleResult(ApiResponse<UserData> result) {
        switch (result.getStatus()) {
            case ERROR:
                ProgressDialog.hideProgressDialog();
                Utility.showToastMessageError(mActivity, result.getError().getMessage());
                Log.e(TAG, "error - " + result.getError().getMessage());
                break;
            case LOADING:
                ProgressDialog.showProgressDialog(this);
                break;
            case SUCCESS:
                ProgressDialog.hideProgressDialog();
                if (result.getData().getStatusCode() == Constants.Success) {
                    Log.e(TAG, "Response - " + new Gson().toJson(result));

                    sessionManager.setUserData(result.getData().getData());
                    sessionManager.setLogin();
                    HomeActivity.startActivity(mActivity, null, true);
                    finish();

                } else {
                    Utility.showToastMessageError(mActivity, result.getData().getMessage());
                    Log.e(TAG, "error failure - " + result.getError().getMessage());
                }
                break;
        }
    }

    @Override
    public void loadImage(Uri imageUri, ImageView ivImage) {
        Glide.with(UpdateProfileActivity.this).load(imageUri).into(ivImage);

    }

    @Override
    public void onMultiImageSelected(List<Uri> uriList, String tag) {

    }

    @Override
    public void onSingleImageSelected(Uri uri, String tag) {
        selectedImagePath = uri.getPath();

        InputStream inputStream = null;
        try {
            inputStream = UpdateProfileActivity.this.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(mFileTemp);

            Utility.copyStream(inputStream, fileOutputStream);

            fileOutputStream.close();
            inputStream.close();
            UCrop.of(Uri.fromFile(mFileTemp), Uri.fromFile(mFileTemp))
                    .withAspectRatio(4, 4)
                    .start(UpdateProfileActivity.this, REQ_CODE_GALLERY_PICKER3);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == REQ_CODE_GALLERY_PICKER3) {
            final Uri resultUri = UCrop.getOutput(data);
            selectedImagePath = resultUri.getPath();
            Bitmap bitmap = Utility.decodeUriToBitmap(UpdateProfileActivity.this, resultUri);
            ByteArrayOutputStream bOut = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bOut);
            selectedImagePath = Base64.encodeToString(bOut.toByteArray(), Base64.DEFAULT);

            binding.ivProfile.setImageURI(resultUri);
            binding.ivCamera.setVisibility(View.GONE);
        }
    }

    public static void startActivity(Activity activity, Bundle bundle, boolean isClear) {
        Intent intent = new Intent(activity, UpdateProfileActivity.class);
        if (bundle != null) intent.putExtra("bundle", bundle);
        if (isClear)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }

    @Override
    public void onDestroy() {
        viewModel.disposeSubscriber();
        selectedImagePath = "";
        super.onDestroy();
    }
}
