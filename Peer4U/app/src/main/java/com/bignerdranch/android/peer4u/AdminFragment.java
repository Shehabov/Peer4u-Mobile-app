package com.bignerdranch.android.peer4u;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class AdminFragment extends Fragment {
    private RecyclerView mSubjectListRecyclerView;
    private ImageButton mAddSubjectButton;
    private SubjectAdapter mAdapter;
    private TextView mEnrolledSubjectsText;
    private ImageView mNoSubjectsEnrolledImageView;
    private boolean mSubtitleVisible;
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    private static final String DIALOG_SUBJECT = "DialogSubject";
    private static final int REQUEST_SUBJECT = 0;

    public static SubjectListFragment newInstance(){
        Bundle args = new Bundle();

        SubjectListFragment fragment = new SubjectListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_subject_list, container, false);


        mSubjectListRecyclerView = (RecyclerView) view.findViewById(R.id.subject_recycler_view);
        mSubjectListRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mSubjectListRecyclerView.setHasFixedSize(true);

        mAddSubjectButton = (ImageButton) view.findViewById(R.id.add_subject_button);

        mAddSubjectButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                FragmentManager manager = getFragmentManager();
                NewSubjectFragment dialog = NewSubjectFragment.newInstance();
                dialog.setTargetFragment(AdminFragment.this, REQUEST_SUBJECT);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                @Override
                                                public void onDismiss(DialogInterface dialog) {
                                                    updateUI();
                                                }
                                            });
                dialog.show(manager, DIALOG_SUBJECT);
            }
        });

        mNoSubjectsEnrolledImageView = (ImageView) view.findViewById(R.id.no_subjects_enrolled_imageview);

        mEnrolledSubjectsText = (TextView) view.findViewById(R.id.enrolled_subjects_title);
        mEnrolledSubjectsText.setText(R.string.available_subjects_title);
        if (savedInstanceState != null){
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }
        updateUI();

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_subject_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if (mSubtitleVisible){
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            //create new subject
            case R.id.new_subject:
                FragmentManager manager = getFragmentManager();
                NewSubjectFragment dialog = NewSubjectFragment.newInstance();
                dialog.setTargetFragment(AdminFragment.this, REQUEST_SUBJECT);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        updateUI();
                    }
                });
                dialog.show(manager, DIALOG_SUBJECT);
                return true;
            case R.id.show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateSubtitle(){
        SubjectLab subjectLab = SubjectLab.get(getActivity());
        int subjectCount = subjectLab.getSubjects().size();
        String subtitle = getString(R.string.subtitle_format_subject, subjectCount);

        if (!mSubtitleVisible){
            subtitle = null;
        }
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    private void updateUI(){
        SubjectLab subjectLab = SubjectLab.get(getActivity());
        List<Subject> subjects = subjectLab.getSubjects();

        if (mAdapter == null){
            mAdapter = new SubjectAdapter(subjects);
            mSubjectListRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setSubjects(subjects);
            mAdapter.notifyDataSetChanged();
        }

        if (subjectLab.getSubjects().size() == 0){
            mAddSubjectButton.setVisibility(View.VISIBLE);
            mEnrolledSubjectsText.setVisibility(View.GONE);
            mNoSubjectsEnrolledImageView.setVisibility(View.VISIBLE);
        } else {
            mAddSubjectButton.setVisibility(View.GONE);
            mEnrolledSubjectsText.setVisibility(View.VISIBLE);
            mNoSubjectsEnrolledImageView.setVisibility(View.GONE);
        }
        updateSubtitle();
    }

    private class SubjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mSubjectNameTextView;
        private TextView mSubjectCodeTextView;
        private Subject mSubject;


        public SubjectHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_subject, parent, false));
            itemView.setOnClickListener(this);
            mSubjectNameTextView = (TextView) itemView.findViewById(R.id.subject_name);
            mSubjectCodeTextView = (TextView) itemView.findViewById(R.id.subject_code);

        }
        public void bind(Subject subject){
            mSubject = subject;
            mSubjectNameTextView.setText(mSubject.getSubjectName());
            mSubjectCodeTextView.setText(mSubject.getSubjectCode());
        }
        @Override
        public void onClick(View view){
            Intent intent = SubjectActivity.newIntent(getActivity(), mSubject.getId());
            //startActivity(intent);
            startActivity(intent);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == REQUEST_SUBJECT){
            // Handle result

        }
    }

    private class SubjectAdapter extends RecyclerView.Adapter<SubjectHolder>{
        private List<Subject> mSubjects;

        public SubjectAdapter(List<Subject> subjects){
            mSubjects = subjects;
        }

        @Override
        public SubjectHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new SubjectHolder(layoutInflater, parent);
        }
        @Override
        public void onBindViewHolder(SubjectHolder holder, int position){
            Subject subject = mSubjects.get(position);
            holder.bind(subject);
        }
        @Override
        public int getItemCount(){
            return mSubjects.size();
        }

        public void setSubjects(List<Subject> subjects){
            mSubjects = subjects;
        }
    } //end of class SubjectAdapter

} //end of class SubjectListFragment extends Fragment
