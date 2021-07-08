package in.jploft.esevak.ui.components.fragments.profile;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.text.InputFilter;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
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
import in.jploft.esevak.databinding.DialogSuccessBinding;
import in.jploft.esevak.databinding.FragmentProfileBinding;
import in.jploft.esevak.network.ApiResponse;
import in.jploft.esevak.pojo.UserData;
import in.jploft.esevak.ui.base.BaseFragment;
import in.jploft.esevak.ui.components.activities.home.HomeActivity;
import in.jploft.esevak.utils.Constants;
import in.jploft.esevak.utils.EmojiExcludeFilter;
import in.jploft.esevak.utils.ProgressDialog;
import in.jploft.esevak.utils.SessionManager;
import in.jploft.esevak.utils.Utility;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends BaseFragment implements BSImagePicker.OnSingleImageSelectedListener,
        BSImagePicker.OnMultiImageSelectedListener,
        BSImagePicker.ImageLoaderDelegate {
    private static final String TAG = ProfileFragment.class.getName();
    private FragmentProfileBinding binding;
    private ProfileViewModel viewModel;
    private View.OnClickListener onClickListener = null;
    private HomeActivity homeActivity;
    private SessionManager sessionManager;

    public static String selectedImagePath = "";
    private static final int REQ_CODE_GALLERY_PICKER3 = 30;
    private File mFileTemp;
    public static final String TEMP_PHOTO_FILE_NAME = "GoTo.png";

    @Override
    protected ViewDataBinding setBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        onClickListener = this;
        binding.setLifecycleOwner(this);
        return binding;
    }

    @Override
    protected void createActivityObject() {
        mActivity = (AppCompatActivity) getActivity();
    }

    @Override
    protected void initializeObject() {
        sessionManager = new SessionManager();
        mFileTemp = Utility.createAppDir(mActivity, mFileTemp, TEMP_PHOTO_FILE_NAME);
        binding.edtName.setFilters(new InputFilter[]{new EmojiExcludeFilter()});
        binding.edtAddress.setFilters(new InputFilter[]{new EmojiExcludeFilter()});
    }


    @Override
    public void onResume() {
        super.onResume();
        Utility.setAppBar(mActivity);
        binding.appBar.tvTitle.setText("Update Profile");
        binding.appBar.ivCart.setVisibility(View.GONE);

        Log.e(TAG, "check getProfile - " + sessionManager.getUserData().getProfileImage());
        Log.e(TAG, "check selectImage - " + selectedImagePath);

        if (sessionManager.getUserData().getFullname().isEmpty() || sessionManager.getUserData().getEmail().isEmpty() || sessionManager.getUserData().getPhone().isEmpty()
                || sessionManager.getUserData().getAddress().isEmpty() || sessionManager.getUserData().getProfileImage().isEmpty()) {
            myProfileAPI();
        } else {
            setProfileData();
        }

    }


    @Override
    protected void initializeOnCreateObject() {
        homeActivity = (HomeActivity) getActivity();
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        viewModel.responseLiveData.observe(this, new Observer<ApiResponse<UserData>>() {
            @Override
            public void onChanged(ApiResponse<UserData> it) {
                handleResult(it);
            }
        });
        viewModel.responseMyProfileLiveData.observe(this, new Observer<ApiResponse<UserData>>() {
            @Override
            public void onChanged(ApiResponse<UserData> it) {
                handleMyProfileResult(it);
            }
        });
    }


    @Override
    protected void setListeners() {
        binding.appBar.ivBack.setOnClickListener(this);
        binding.btnUpdateProfile.setOnClickListener(this);
        binding.tvChangeProfile.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        Utility.hideKeyboard(mActivity);
        switch (view.getId()) {
            case R.id.ivBack:
                homeActivity.onBackPressed();
                break;
            case R.id.btnUpdateProfile:
                updateProfileAPI();
                break;
            case R.id.tvChangeProfile:
                BSImagePicker pickerDialog = new BSImagePicker.Builder("in.jploft.esevak")
                        .build();
                pickerDialog.show(getChildFragmentManager(), "picker");
                break;
        }
    }

    private void myProfileAPI() {
        String userId = sessionManager.getUserData().getId();
        if (userId != null && !userId.equals("")) {

            HashMap<String, String> reqData = new HashMap<>();
            reqData.put("userId", userId);

            Log.e(TAG, "Api parameters - " + reqData.toString());
            viewModel.myProfileApi(reqData);
        }
    }

    private void handleMyProfileResult(ApiResponse<UserData> result) {
        switch (result.getStatus()) {
            case ERROR:
                ProgressDialog.hideProgressDialog();
                Utility.showToastMessageError(mActivity, result.getError().getMessage());
                Log.e(TAG, "error - " + result.getError().getMessage());
                break;
            case LOADING:
                ProgressDialog.showProgressDialog(mActivity);
                break;
            case SUCCESS:
                ProgressDialog.hideProgressDialog();
                if (result.getData().getStatusCode() == Constants.Success) {
                    Log.e(TAG, "Response - " + new Gson().toJson(result));

                    sessionManager.setUserData(result.getData().getData());
                    Utility.loadImage(HomeActivity.ivUserProfile, sessionManager.getUserData().getProfileImage());
                    Utility.loadImage(HomeActivity.ivHeaderProfile, sessionManager.getUserData().getProfileImage());
                    HomeActivity.txtuserName.setText(sessionManager.getUserData().getFullname());
                    HomeActivity.tvEmail.setText(sessionManager.getUserData().getEmail());

                    setProfileData();

                } else {
                    Utility.showToastMessageError(mActivity, result.getData().getMessage());
                }
                break;
        }
    }

    private void setProfileData() {
        binding.edtName.setText(sessionManager.getUserData().getFullname());
        binding.edtEmail.setText(sessionManager.getUserData().getEmail());
        binding.edtMobile.setText(sessionManager.getUserData().getPhone());
        binding.edtAddress.setText(sessionManager.getUserData().getAddress());
        if (selectedImagePath.isEmpty()) {
            if (sessionManager.getUserData().getProfileImage() != null && !sessionManager.getUserData().getProfileImage().isEmpty())
                Utility.loadImage(binding.ivProfile, sessionManager.getUserData().getProfileImage());
        }

    }


    private void updateProfileAPI() {
        String name = binding.edtName.getText().toString().trim();
        String email = binding.edtEmail.getText().toString().trim();
        String address = binding.edtAddress.getText().toString().trim();
        String phone = binding.edtMobile.getText().toString().trim();

        if (selectedImagePath.isEmpty()) {
            if (sessionManager.getUserData().getProfileImage() != null && !sessionManager.getUserData().getProfileImage().isEmpty())
                selectedImagePath = sessionManager.getUserData().getProfileImage();
        }

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
                ProgressDialog.showProgressDialog(mActivity);
                break;
            case SUCCESS:
                ProgressDialog.hideProgressDialog();
                if (result.getData() != null) {
                    if (result.getData().getStatusCode() == Constants.Success) {
                        Log.e(TAG, "Response - " + new Gson().toJson(result));

                        sessionManager.setUserData(result.getData().getData());
                        Utility.loadImage(HomeActivity.ivUserProfile, sessionManager.getUserData().getProfileImage());
                        Utility.loadImage(HomeActivity.ivHeaderProfile, sessionManager.getUserData().getProfileImage());
                        HomeActivity.txtuserName.setText(sessionManager.getUserData().getFullname());
                        HomeActivity.tvEmail.setText(sessionManager.getUserData().getEmail());
                        showSuccessDialog();

                    } else {
                        Utility.showToastMessageError(mActivity, result.getData().getMessage());
                        Log.e(TAG, "error failure - " + result.getError().getMessage());
                    }
                } else {
                    Utility.showToastMessageError(mActivity, "Not Data Found!");
                }
                break;
        }
    }

    private void showSuccessDialog() {
        final Dialog dialog = new Dialog(mActivity, R.style.Theme_Dialog);
        DialogSuccessBinding dialogBinding = DataBindingUtil.inflate(LayoutInflater.from(mActivity), R.layout.dialog_success, null, false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(dialogBinding.getRoot());

        dialogBinding.tvTitle.setText("Successfully");
        dialogBinding.tvMessage.setText("User Profile successfully updated.");
        dialogBinding.llOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                homeActivity.onBackPressed();
            }
        });

        dialog.show();
    }

    @Override
    public void loadImage(Uri imageUri, ImageView ivImage) {
        Glide.with(mActivity).load(imageUri).into(ivImage);

    }

    @Override
    public void onMultiImageSelected(List<Uri> uriList, String tag) {

    }

    @Override
    public void onSingleImageSelected(Uri uri, String tag) {
        selectedImagePath = uri.getPath();

        InputStream inputStream = null;
        try {
            inputStream = mActivity.getContentResolver().openInputStream(uri);
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
                    .start(mActivity, this, REQ_CODE_GALLERY_PICKER3);
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
            Bitmap bitmap = Utility.decodeUriToBitmap(mActivity, resultUri);
            ByteArrayOutputStream bOut = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bOut);
            selectedImagePath = Base64.encodeToString(bOut.toByteArray(), Base64.DEFAULT);

            binding.ivProfile.setImageURI(resultUri);
            Log.e(TAG, "imageview selectImage - " + resultUri);
        }
    }

    @Override
    public void onDestroy() {
        viewModel.disposeSubscriber();
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        selectedImagePath = "";
        super.onDestroyView();
    }
}