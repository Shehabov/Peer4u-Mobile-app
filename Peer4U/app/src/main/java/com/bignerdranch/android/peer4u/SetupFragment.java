package com.bignerdranch.android.peer4u;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import android.text.format.DateFormat;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class SetupFragment extends Fragment {
    private static final String ARG_STUDENT_ID = "student_id";
    private static final int DIALOG_PROFILEPIC = 0;
    private static final int REQUEST_GALLERY = 1;
    private static final int REQUEST_PHOTO = 2;
    private EditText mNameField;
    private Student mStudent;
    private EditText mUniversityField;
    private EditText mIntroductionField;
    private Button mDoneButton;
    private ImageButton mPhotoButton;
    private File mPhotoFile;
    private ImageView mPhotoView;

    public static SetupFragment newInstance(UUID studentId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_STUDENT_ID, studentId);
        SetupFragment fragment = new SetupFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID studentId = (UUID) getArguments().getSerializable(ARG_STUDENT_ID);
        mStudent = StudentLab.get(getActivity()).getStudent(studentId);
        mPhotoFile = StudentLab.get(getActivity()).getPhotoFile(mStudent);
    }

    @Override
    public void onPause(){
        super.onPause();

        StudentLab.get(getActivity()).updateStudent(mStudent);
        updatePhotoView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_setup, container, false);

        mNameField = (EditText) v.findViewById(R.id.setup_name);
        mNameField.setText(mStudent.getFirstName());
        mNameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {
                //This space is intentionally left blank
            }

            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                mStudent.setFirstName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s){
                // This one too
            }

        });



        mUniversityField = (EditText) v.findViewById(R.id.setup_university);
        mUniversityField.setText(mStudent.getUniveristy());
        mUniversityField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {
                //This space is intentionally left blank
            }

            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                mStudent.setUniveristy(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s){
                // This one too
            }

        });

        mIntroductionField = (EditText) v.findViewById(R.id.setup_introduction);
        mIntroductionField.setText(mStudent.getIntroduction());
        mIntroductionField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {
                //This space is intentionally left blank
            }

            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                mStudent.setIntroduction(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s){
                // This one too
            }

        });

        mDoneButton = (Button) v.findViewById(R.id.setup_done_button);
        mDoneButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = SubjectListActivity.newIntent(getActivity(), mStudent.getId());
                //startActivity(intent);
                startActivity(intent);
            }
        });



        PackageManager packageManager = getActivity().getPackageManager();


        mPhotoButton = (ImageButton) v.findViewById(R.id.setup_profilepic_button);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
/**
        boolean canTakePhoto = (mPhotoFile != null &&
                captureImage.resolveActivity(packageManager) != null);
        boolean canChoosePhoto = (mPhotoFile != null &&
                pickPhoto.resolveActivity(packageManager) != null);
        mPhotoButton.setEnabled(canTakePhoto || canChoosePhoto); **/

        mPhotoButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.dialog_profilepic_title)
                        .setItems(R.array.setprofilepic, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                switch (id){
                                    case 0:


                                        Uri uri = FileProvider.getUriForFile(getActivity(),
                                                "com.bignerdranch.android.peer4u.fileprovider",
                                                mPhotoFile);
                                        captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                                        List<ResolveInfo> cameraActivities = getActivity()
                                                .getPackageManager().queryIntentActivities(captureImage,
                                                        PackageManager.MATCH_DEFAULT_ONLY);

                                        for (ResolveInfo activity : cameraActivities) {
                                            getActivity().grantUriPermission(activity.activityInfo.packageName,
                                                    uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                                        }

                                        getActivity().startActivityForResult(captureImage, REQUEST_PHOTO);

                                    case 1:


                                        Uri uri2 = FileProvider.getUriForFile(getActivity(),
                                                "com.bignerdranch.android.peer4u.fileprovider",
                                                mPhotoFile);
                                        pickPhoto.putExtra(MediaStore.EXTRA_OUTPUT, uri2);

                                        List<ResolveInfo> cameraActivities2 = getActivity()
                                                .getPackageManager().queryIntentActivities(pickPhoto,
                                                        PackageManager.MATCH_DEFAULT_ONLY);

                                        for (ResolveInfo activity : cameraActivities2) {
                                            getActivity().grantUriPermission(activity.activityInfo.packageName,
                                                    uri2, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                                        }
                                        getActivity().startActivityForResult(pickPhoto, REQUEST_GALLERY);

                                }
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        mPhotoView = (ImageView) v.findViewById(R.id.profile_photo);
        updatePhotoView();

        return v;
    } //end of onCreateView
    @Override
    public void onResume(){
        super.onResume();
        updatePhotoView();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode != Activity.RESULT_OK){
            return;
        }
        if (requestCode == REQUEST_PHOTO){
            Uri uri = FileProvider.getUriForFile(getActivity(),
                    "com.bignerdranch.android.peer4u.fileprovider",
                    mPhotoFile);
            getActivity().revokeUriPermission(uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            updatePhotoView();
        } else if (requestCode == REQUEST_GALLERY) {
            try{
                mPhotoFile = null;

                mPhotoFile = new File(getActivity().getFilesDir(), mStudent.getPhotoFilename());;

                InputStream inputStream = getActivity().getContentResolver().openInputStream(data.getData());
                FileOutputStream fileOutputStream = new FileOutputStream(mPhotoFile);
                // Copying
                copyStream(inputStream, fileOutputStream);
                fileOutputStream.close();
                inputStream.close();

            } catch (Exception e){
                Log.d("PEER4U", "onActivityResult:" +e.toString());
            }
            updatePhotoView();
        }
    }



    public static void copyStream(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }


    private void updatePhotoView(){
        if (mPhotoFile == null || !mPhotoFile.exists()){
            mPhotoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }
}
