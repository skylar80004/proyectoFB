package com.example.proyectofb;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Map;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;


    private View view;
    private int profilePhotoCode = 1;
    private int profileOptionsSettingsCode = 2;
    private Bitmap bitmapProfilePhoto;

    PostAdapter postAdapter;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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

        Intent intent = getActivity().getIntent();
        String id = intent.getStringExtra("userID");
        Toast.makeText(getActivity(), id, Toast.LENGTH_LONG).show();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Toast.makeText(getActivity(), "Cargando perfil...",Toast.LENGTH_LONG).show();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        Button buttonFriends = view.findViewById(R.id.buttonProfileFriends);
        buttonFriends.setOnClickListener(this);

        Button buttonProfileOptions = view.findViewById(R.id.buttonProfileOptions);
        buttonProfileOptions.setOnClickListener(this);

        ImageView imageViewProfilePhoto = view.findViewById(R.id.imageViewProfilePhoto);
        imageViewProfilePhoto.setOnClickListener(this);

        Button buttonPhotos = view.findViewById(R.id.buttonProfilePhotos);
        buttonPhotos.setOnClickListener(this);

        postAdapter = new PostAdapter();
        RecyclerView recyclerViewProfile = view.findViewById(R.id.recyclerViewProfile);
        recyclerViewProfile.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewProfile.setAdapter(this.postAdapter);


        ReadTest(view);
        // Consultar posts del usuario
        this.GetCurrentUserPosts();
        this.view = view;

        return view;
    }


    public void GetCurrentUserPosts(){

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        final String userId = user.getUid();
        databaseReference.child("posts").child(userId).orderByKey().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Map<String,Object> postMap = (Map<String,Object>) dataSnapshot.getValue();

                String postId = dataSnapshot.getKey();
                String userNamePost = (String)postMap.get("name");
                String lastNamePost = (String)postMap.get("lastName");
                String type = (String)postMap.get("type");
                String profilePhotoUrl = (String) postMap.get("profilePhotoUrl"); // Con este link se descarga la foto del usuario, pero como estamos en el perfil , ya se descargo previamente y esta ubicada en el imageView de foto de perfil
                String text = (String) postMap.get("text");
                String likes = (String) postMap.get("likes");
                String disLikes = (String) postMap.get("dislikes");

                Post post = new Post(userNamePost,lastNamePost,type,bitmapProfilePhoto,text,null,
                        likes,disLikes,userId,postId);
                postAdapter.AddPost(post);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String key = dataSnapshot.getKey();

                Map<String,Object> postMap = (Map<String,Object>) dataSnapshot.getValue();

                String postId = dataSnapshot.getKey();
                String userNamePost = (String)postMap.get("name");
                String lastNamePost = (String)postMap.get("lastName");
                String type = (String)postMap.get("type");
                String profilePhotoUrl = (String) postMap.get("profilePhotoUrl"); // Con este link se descarga la foto del usuario, pero como estamos en el perfil , ya se descargo previamente y esta ubicada en el imageView de foto de perfil
                String text = (String) postMap.get("text");
                String likes = (String) postMap.get("likes");
                String disLikes = (String) postMap.get("dislikes");

                Post post = new Post(userNamePost,lastNamePost,type,bitmapProfilePhoto,text,null,
                        likes,disLikes,userId,postId);

                postAdapter.UpdatePost(key,post);
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

    public void ReadTest(final View view){

        // Personal Info
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseRef = database.getReference("users");
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getInstance().getCurrentUser();
        String id = firebaseUser.getUid();

        databaseRef.child(id).orderByValue().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String key = dataSnapshot.getKey();
                String value = "";
                if(key.equals("name")){
                    value = (String)dataSnapshot.getValue();
                    TextView textView= view.findViewById(R.id.textViewProfileName);
                    textView.setText(value);

                }
                else if(key.equals("lastName")){

                    value = (String)dataSnapshot.getValue();
                    TextView textView= view.findViewById(R.id.textViewProfileName);
                    textView.setText(textView.getText() + " " + value);
                }
                else if(key.equals("city")){
                    value = (String)dataSnapshot.getValue();
                    TextView textView= view.findViewById(R.id.textViewProfileCity);
                    textView.setText(value);

                }
                else if(key.equals("birthDate")){

                    value = (String)dataSnapshot.getValue();
                    TextView textView= view.findViewById(R.id.textViewProfileBirthDate);
                    textView.setText(value);
                }
                else if(key.equals("email")){

                    value = (String)dataSnapshot.getValue();
                    TextView textView= view.findViewById(R.id.textViewProfileEmail);
                    textView.setText(value);
                }
                else if(key.equals("gender")){

                    value = (String)dataSnapshot.getValue();
                    TextView textView= view.findViewById(R.id.textViewProfileGender);
                    textView.setText(value);
                }
                else if(key.equals("phone")){

                    value = (String)dataSnapshot.getValue();
                    TextView textView= view.findViewById(R.id.textViewProfilePhone);
                    textView.setText(value);
                }
                else if(key.equals("profilePhotoUrl")){

                    value = (String)dataSnapshot.getValue();
                    ImageView imageView = view.findViewById(R.id.imageViewProfilePhoto);

                    ImageDownloader imageDownloader = new ImageDownloader();

                    try {
                        Bitmap bitmap = imageDownloader.execute(value).get();
                        bitmapProfilePhoto = bitmap;
                        imageView.setImageBitmap(bitmap);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
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

        // Education Info
        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference("education");

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getInstance().getCurrentUser();
        id = firebaseUser.getUid();

        LinearLayout linearLayoutEducationProfile = view.findViewById(R.id.linearLayoutEducationProfile);
        linearLayoutEducationProfile.removeAllViews();

        databaseRef.child(id).orderByValue().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                LinearLayout linearLayoutEducationProfile = view.findViewById(R.id.linearLayoutEducationProfile);
                String value = (String)dataSnapshot.getValue();
                TextView textViewEducation = new TextView(getActivity());
                textViewEducation.setText(" " + value + " ");
                linearLayoutEducationProfile.addView(textViewEducation);

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

    @Override
    public void onClick(View v) {


        switch(v.getId()){

            case R.id.imageViewProfilePhoto:
                Intent intent = new Intent(getActivity(), ProfilePhotoActivity.class);
                startActivityForResult(intent,this.profilePhotoCode);
                break;
            case R.id.buttonProfilePhotos:
                Intent intentProfilePhotos = new Intent(getActivity(), PhotosActivity.class);
                startActivity(intentProfilePhotos);
                break;
            case R.id.buttonProfileOptions:
                Intent intentOptions = new Intent(getActivity(), OptionsActivity.class);
                startActivityForResult(intentOptions, this.profileOptionsSettingsCode);
                break;
            case R.id.buttonProfileFriends:
                Intent intentFriends = new Intent(getActivity(), FriendsListActivity.class);
                startActivity(intentFriends);
                break;

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

    @Override
    public void onHiddenChanged(boolean hidden) { // Recargar fragment para actualizar datos
        super.onHiddenChanged(hidden);
        if(!hidden && this.view != null){
            this.ReadTest(view);
            this.postAdapter.clearItems();
            this.GetCurrentUserPosts();

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == this.profilePhotoCode){
            if(resultCode == Activity.RESULT_OK){
                this.ReadTest(this.view);
            }
        }
        else if(requestCode == this.profileOptionsSettingsCode){
            if(resultCode == Activity.RESULT_OK){

                //Toast.makeText(getActivity(),"jose", Toast.LENGTH_LONG).show();
                this.ReadTest(this.view);
            }
        }
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
