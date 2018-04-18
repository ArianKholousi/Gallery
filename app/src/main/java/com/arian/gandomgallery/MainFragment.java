package com.arian.gandomgallery;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private RecyclerView recyclerView;
    private PhotoAdapter photoAdapter;
    private List<GalleryItem> galleryItems;
    private JsonObjectRequest jsonObjectRequest;
    private String url;

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

        galleryItems = new ArrayList<>();
        url = "http://gandom.co/devTest/1/home";


        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("list");
                    galleryItems = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<GalleryItem>>(){}.getType());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        SingletonRequestQueue.getInstance(getActivity()).getRequestQueue().add(jsonObjectRequest);


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

            for (int i=0; i<item.getImages().size();i++){
                String url = "http://gandom.co/devTest/1/images/" + item.getImages().get(i);
                Picasso.with(getActivity()).load(url).into(imageView);
            }
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
            photoAdapter = new PhotoAdapter(galleryItems);
            recyclerView.setAdapter(photoAdapter);
        }else {
            photoAdapter.setItemList(galleryItems);
            photoAdapter.notifyDataSetChanged();
        }

    }





}
