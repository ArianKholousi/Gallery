package com.arian.gandomgallery;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class FullScreenFragment extends Fragment {

    private static final String ARGS_IMAGE_URL = "args_image_url";
    private ImageView imageView;
    private String imageURL;

    public FullScreenFragment() {
        // Required empty public constructor
    }


    public static FullScreenFragment newInstance(String imageURL) {

        Bundle args = new Bundle();
        FullScreenFragment fragment = new FullScreenFragment();
        args.putString(ARGS_IMAGE_URL, imageURL);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_full_screen, container, false);

        imageView = (ImageView) view.findViewById(R.id.imageView_full_screen);
        imageURL = getArguments().getString(ARGS_IMAGE_URL);

        Picasso.with(getActivity()).load(imageURL).into(imageView);

        return view;
    }

}
