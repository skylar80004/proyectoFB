package com.example.proyectofb;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TimeLiFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TimeLiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimeLiFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public PostAdapter postAdapter;
    public AuxiliarElements auxiliarElements;

    public TimeLiFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TimeLiFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TimeLiFragment newInstance(String param1, String param2) {
        TimeLiFragment fragment = new TimeLiFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    public void FirebaseGetFriendsPosts(){

        final ArrayList<String> friendsIds = new ArrayList<>();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String currentUserId = firebaseUser.getUid();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference();

        databaseReference.child("friends").child(currentUserId).orderByKey().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                final String friendId = dataSnapshot.getKey();

                databaseReference.child("posts").child(friendId).orderByKey().addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        Map<String,Object> postMap = (Map<String, Object>) dataSnapshot.getValue();
                        String postId = dataSnapshot.getKey();
                        String userNamePost = (String)postMap.get("name");
                        String lastNamePost = (String)postMap.get("lastName");
                        String type = (String)postMap.get("type");
                        String profilePhotoUrl = (String) postMap.get("profilePhotoUrl"); // Con este link se descarga la foto del usuario, pero como estamos en el perfil , ya se descargo previamente y esta ubicada en el imageView de foto de perfil
                        String text = (String) postMap.get("text");
                        String likes = (String) postMap.get("likes");
                        String disLikes = (String) postMap.get("dislikes");
                        String imageUrl = (String) postMap.get("imageUrl");


                        ImageDownloader imageDownloader = new ImageDownloader();
                        Bitmap bitmapProfilePhoto = null;
                        try {
                            bitmapProfilePhoto = imageDownloader.execute(profilePhotoUrl).get();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        ImageDownloader imageDownloader2 = new ImageDownloader();
                        Bitmap bitmapPostImage = null;
                        try {
                            bitmapPostImage = imageDownloader2.execute(imageUrl).get();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        Post post = new Post(userNamePost,lastNamePost,type,bitmapProfilePhoto,text,bitmapPostImage,
                                likes,disLikes,friendId,postId);

                        postAdapter.AddPost(post);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View view = inflater.inflate(R.layout.fragment_time_li, container, false);

        this.postAdapter = new PostAdapter();
        this.auxiliarElements = new AuxiliarElements();

        RecyclerView recyclerViewTimeLine = view.findViewById(R.id.recyclerViewTimeLi);
        recyclerViewTimeLine.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewTimeLine.setAdapter(postAdapter);

        Button buttonNewPost = view.findViewById(R.id.buttonTimeLineNewPost);

        buttonNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(view.getContext(),NewPostActivity.class);
                startActivityForResult(intent, 100);

            }
        });


        FirebaseGetFriendsPosts();

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class ImageDownloader extends AsyncTask<String,Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {

            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

    }
}
