package com.example.proyectofb;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proyectofb.ui.main.UserThumbnail;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
 * {@link SearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    //  Recycler View user search

    RecyclerView recyclerViewSearch;
    UserThumbnailAdapter userThumbnailAdapter;
    PostAdapter  postAdapter;
    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        this.userThumbnailAdapter = new UserThumbnailAdapter();
        this.postAdapter = new PostAdapter();

        this.recyclerViewSearch = view.findViewById(R.id.recyclerViewSearch);
        this.recyclerViewSearch.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.recyclerViewSearch.setAdapter(this.userThumbnailAdapter);


        Button buttonSearchUsers = view.findViewById(R.id.buttonSearchUsers);
        buttonSearchUsers.setOnClickListener(this);

        Button buttonSearchPost = view.findViewById(R.id.buttonSearchPosts);
        buttonSearchPost.setOnClickListener(this);
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


    public void FirebaseSearchPost(final String text){

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference();
        databaseReference.child("posts").orderByKey().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                final String userIdTemporal = dataSnapshot.getKey();
                //Toast.makeText(getContext(),userIdTemporal,Toast.LENGTH_LONG).show();

                databaseReference.child("posts").child(userIdTemporal).orderByKey().addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        Map<String,Object> postMap = (Map<String, Object>) dataSnapshot.getValue();
                        String postText = (String)postMap.get("text");
                        postText  = postText.toLowerCase();
                       // Toast.makeText(getContext(),postText,Toast.LENGTH_LONG).show();
                        if(postText.contains(text)){


                            String postId = dataSnapshot.getKey();
                            String userNamePost = (String)postMap.get("name");
                            String lastNamePost = (String)postMap.get("lastName");
                            String type = (String)postMap.get("type");
                            String profilePhotoUrl = (String) postMap.get("profilePhotoUrl"); // Con este link se descarga la foto del usuario, pero como estamos en el perfil , ya se descargo previamente y esta ubicada en el imageView de foto de perfil
                            String likes = (String) postMap.get("likes");
                            String disLikes = (String) postMap.get("dislikes");
                            String imageUrl = (String) postMap.get("imageUrl");
                            String totalTime = (String) postMap.get("totalTime");
                            String fileName = (String) postMap.get("fileName");

                            String year = (String) postMap.get("year");
                            String month = (String) postMap.get("month");
                            String day = (String) postMap.get("day");
                            String hour = (String) postMap.get("hour");
                            String minute = (String) postMap.get("minute");



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

                            Post post = new Post(userNamePost,lastNamePost,type,bitmapProfilePhoto,postText,bitmapPostImage,
                                    likes,disLikes,userIdTemporal,postId,totalTime,year,month,day,hour,minute,fileName);

                            postAdapter.AddPost(post);


                        }

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
    public void onClick(View v) {


        EditText editText;
        String text;
        switch(v.getId()){
            case R.id.buttonSearchPosts:

                 editText = getActivity().findViewById(R.id.editTextSearch);
                 text = editText.getText().toString();
                text = text.toLowerCase();
                this.recyclerViewSearch.setAdapter(this.postAdapter);
                this.FirebaseSearchPost(text);
                break;

            case R.id.buttonSearchUsers:

                 editText = getActivity().findViewById(R.id.editTextSearch);

                 text = editText.getText().toString();
                text = text.toLowerCase();
                Toast.makeText(getActivity(), "Buscando usuarios...", Toast.LENGTH_LONG).show();
                this.FireBaseSearchUser(text);
                break;

                //https://www.youtube.com/watch?v=X-hYIQcmXUw
        }
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


    public boolean FireBaseSearchUser(final String text){


        this.userThumbnailAdapter.clearItems();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        final String id = user.getUid();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference  databaseReference = database.getReference();
        databaseReference.child("users").orderByKey().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String currentUserID = dataSnapshot.getKey();
                if(!currentUserID.equals(id)){

                    Map<String,Object> userMap = (Map<String,Object>)dataSnapshot.getValue();
                    String name = (String)userMap.get("name");
                    String lastName = (String) userMap.get("lastName");
                    String nameLower = name.toLowerCase();
                    String lastNameLower = lastName.toLowerCase();

                    if(nameLower.contains(text) || lastNameLower.contains(text)){ // Usuario Encontrado

                        String photoUrl = (String)userMap.get("profilePhotoUrl");
                        ImageDownloader imageDownloader = new ImageDownloader();
                        Bitmap bitmap = null;
                        try {
                            bitmap = imageDownloader.execute(photoUrl).get();

                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        String id = dataSnapshot.getKey();
                        UserThumbnail userThumbnail  = new UserThumbnail(name,lastName,bitmap,id);
                        userThumbnailAdapter.AddUserThumbnail(userThumbnail);
                    }

                }

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

        return false;
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