package com.arcsoft.sdk_demo.delete;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.arcsoft.facerecognition.AFR_FSDKFace;
import com.arcsoft.sdk_demo.Application;
import com.arcsoft.sdk_demo.Crime;
import com.arcsoft.sdk_demo.CrimeLab;
import com.arcsoft.sdk_demo.DetecterActivity;
import com.arcsoft.sdk_demo.FaceDB;
import com.arcsoft.sdk_demo.R;
import com.arcsoft.sdk_demo.RegisterActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.graphics.Bitmap.createScaledBitmap;

public class DeleteListFragment extends Fragment {

    private static final String TAG = "PhotoGalleryFragment";
    private  PhotoAdapter mPhotoAdapter;
    private RecyclerView mPhotoRecyclerView;
    private List<Face> mItems = new ArrayList<>();

    public static DeleteListFragment newInstance() {
        return new DeleteListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        new FetchItemsTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mPhotoRecyclerView = (RecyclerView) v
                .findViewById(R.id.crime_recycler_view);
        mPhotoRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        setupAdapter();

        return v;
    }

    private void setupAdapter() {
        if (isAdded()) {
            mPhotoAdapter =new PhotoAdapter(mItems);
            mPhotoRecyclerView.setAdapter(mPhotoAdapter);
        }
    }

    private class PhotoHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mTitleTextView;
        private ImageView mImageButton;
        private  String mname;
        public PhotoHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView) itemView.findViewById(R.id.textView2);
            mImageButton=(ImageView) itemView.findViewById(R.id.list_item_delete);

        }

        public void bindLayout(String name,Bitmap bitmap)
        {
            mname=name;
            Bitmap b=createScaledBitmap(bitmap,600,600,true);
            mImageButton.setImageBitmap(b);
            mTitleTextView.setText(name);
        }

        @Override
        public void onClick(View v){

            final String name = mname;
 //           final int count = ((Application)mContext.getApplicationContext()).mFaceDB.mRegister.get(position).mFaceList.size();
  //          final Map<String, AFR_FSDKFace> face = ((Application)mContext.getApplicationContext()).mFaceDB.mRegister.get(position).mFaceList;
            new AlertDialog.Builder(DeleteListFragment.super.getActivity())
                    .setTitle("删除注册名:" + name)
   //                 .setMessage("包含:" + count + "个注册人脸特征信息")
       //             .setView(new ListView(mContext))
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ((Application)DeleteListFragment.super.getActivity().getApplicationContext()).mFaceDB.delete(name);
   //                         mRegisterViewAdapter.notifyDataSetChanged();
                            dialog.dismiss();
                            mPhotoAdapter.removeData(getLayoutPosition());
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();

        }
        }


    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {

        private    List<Face> mGalleryItems;

        public PhotoAdapter(List<Face> galleryItems) {
            mGalleryItems = galleryItems;
        }

        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater inflater=LayoutInflater.from(getActivity());
            View view=inflater.inflate(R.layout.delete_item_list,viewGroup,false);
            return new PhotoHolder(view);
        }

        @Override
        public void onBindViewHolder(PhotoHolder photoHolder, int position) {
            Face galleryItem = mGalleryItems.get(position);
            String a=galleryItem.getName();


            Bitmap b=galleryItem.getBitmap();
            photoHolder.bindLayout(a,b);
        }

        @Override
        public int getItemCount() {
            return mGalleryItems.size();
        }
        public void removeData(int position) {
            mGalleryItems.remove(position);
            //删除动画
            notifyItemRemoved(position);
            notifyDataSetChanged();
        }
    }

    private class FetchItemsTask extends AsyncTask<Void,Void,List<Face>> {

        @Override
        protected List<Face> doInBackground(Void... params) {
            Application application=(Application)DeleteListFragment.super.getActivity().getApplicationContext();
            return application.getFaces();
        }

        @Override
        protected void onPostExecute(List<Face> items) {
            mItems = items;
            setupAdapter();
        }

    }
}
