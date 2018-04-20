package com.arian.gandomgallery;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

    public static final String EXTRA_IMAGE_URL = "extra_image_url";
    public static final int TYPE_ITEM = 0;
    public static final int TYPE_DESCRIPTION = 1;

    private RecyclerView recyclerView;
    private PhotoAdapter photoAdapter;
    private List<GalleryItem> galleryItems;
    private List<String> galleryitemStrings;
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
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_gallery_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        galleryItems = new ArrayList<>();
        galleryitemStrings = new ArrayList<>();

        url = "http://gandom.co/devTest/1/home";

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("list");
                    galleryItems = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<GalleryItem>>() {
                    }.getType());

                    for (int i = 0; i < galleryItems.size(); i++) {
                        String header = galleryItems.get(i).getDescription();
                        galleryitemStrings.add(header);

                        for (int j = 0; j < galleryItems.get(i).getImages().size(); j++) {
                            String url = "http://gandom.co/devTest/1/images/" + galleryItems.get(i).getImages().get(j);
                            galleryitemStrings.add(url);
                        }
                    }

                    recyclerView.getAdapter().notifyDataSetChanged();

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

        updateRecyclerView();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateRecyclerView();
    }

    private class PhotoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageView;
        private String imageURL;

        public PhotoHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView_gallery_item);
            itemView.setOnClickListener(this);
        }

        public void bindPhoto(String imageURL) {
            this.imageURL = imageURL;
            Picasso.with(getActivity()).load(imageURL).into(imageView);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), FullScreenActivity.class);
            intent.putExtra(EXTRA_IMAGE_URL, imageURL);
            startActivity(intent);
        }
    }

    private class HeaderHolder extends RecyclerView.ViewHolder {
        TextView tvHeader;
        String header;

        public HeaderHolder(View itemView) {
            super(itemView);
            tvHeader = (TextView) itemView.findViewById(R.id.tv_header);
        }

        public void bindText(String header) {
            this.header = header;
            tvHeader.setText(header);
        }

    }

    private class PhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<String> itemList;

        public PhotoAdapter(List<String> itemList) {
            this.itemList = itemList;
        }

        public void setItemList(List<String> itemList) {
            this.itemList = itemList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            switch (viewType) {
                case TYPE_ITEM: {
                    View view = inflater.inflate(R.layout.gallery_item, parent, false);
                    return new PhotoHolder(view);
                }
                case TYPE_DESCRIPTION: {
                    View view = inflater.inflate(R.layout.gallery_header, parent, false);
                    return new HeaderHolder(view);
                }
                default:
                    throw new IllegalStateException("unsupported item type");
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            int viewType = getItemViewType(position);
            switch (viewType) {
                case TYPE_ITEM: {
                    String imageURL = itemList.get(position);
                    PhotoHolder viewHolder = (PhotoHolder) holder;
                    viewHolder.bindPhoto(imageURL);
                    break;
                }

                case TYPE_DESCRIPTION: {
                    String header = itemList.get(position);
                    HeaderHolder viewHolder = (HeaderHolder) holder;
                    viewHolder.bindText(header);
                    break;
                }
                default:
                    throw new IllegalStateException("unsupported item type");
            }

        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (itemList.get(position).contains(".jpg"))
                return TYPE_ITEM;
            else
                return TYPE_DESCRIPTION;
        }
    }

    public void updateRecyclerView() {
        if (photoAdapter == null) {
            photoAdapter = new PhotoAdapter(galleryitemStrings);
            recyclerView.setAdapter(photoAdapter);
        } else {
            photoAdapter.setItemList(galleryitemStrings);
            photoAdapter.notifyDataSetChanged();
        }
    }
}
