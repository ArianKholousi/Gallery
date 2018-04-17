package com.arian.gandomgallery;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private RecyclerView recyclerView;
    private PhotoAdapter photoAdapter;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance() {
        Bundle args = new Bundle();
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_main, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_gallery_items);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        updateRecyclerView();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateRecyclerView();
    }

    private class PhotoHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private GalleryItem galleryItem;

        public PhotoHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView_gallery_item);
        }

        public void bindPhoto(GalleryItem item){
            this.galleryItem = item;
        }
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder>{

        private List<GalleryItem> itemList;

        public PhotoAdapter(List<GalleryItem> itemList) {
            this.itemList = itemList;
        }

        public void setItemList(List<GalleryItem> itemList) {
            this.itemList = itemList;
        }

        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.gallery_item,parent,false);
            return new PhotoHolder(view);
        }

        @Override
        public void onBindViewHolder(PhotoHolder holder, int position) {
            GalleryItem item = itemList.get(position);
            holder.bindPhoto(item);
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }
    }

    public void updateRecyclerView(){

        if (photoAdapter == null){
            recyclerView.setAdapter(photoAdapter);
        }else {
            photoAdapter.notifyDataSetChanged();
        }

    }





}
