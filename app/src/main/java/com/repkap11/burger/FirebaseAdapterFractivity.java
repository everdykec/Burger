package com.repkap11.burger;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;


public abstract class FirebaseAdapterFractivity<AdapterHolder> extends Fractivity<FirebaseAdapterFractivity.FirebaseAdapterFragment> {

    @Override
    protected FirebaseAdapterFragment createFragment(Bundle savedInstanceState) {
        //use the bundle to create the fragment
        FirebaseAdapterFragment<AdapterHolder> fragment = createFirebaseFragment();
        return fragment;
    }

    protected abstract FirebaseAdapterFragment createFirebaseFragment();


    public static abstract class FirebaseAdapterFragment<AdapterHolder> extends Fractivity.FractivityFragment {
        FirebaseAdapter mAdapter;

        protected abstract String adapterReference();

        @Override
        protected void create(Bundle savedInstanceState) {
            mAdapter = new FirebaseAdapter<AdapterHolder>(this);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            final FirebaseMessaging messaging = FirebaseMessaging.getInstance();
            messaging.subscribeToTopic("lunch");

            String reference = adapterReference();
            final DatabaseReference databaseRef = database.getReference(reference);
            databaseRef.addChildEventListener(new ChildEventListener() {

                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                    mAdapter.add(dataSnapshot);
                }

                // This function is called each time a child item is removed.
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    mAdapter.remove(dataSnapshot);
                }

                // The following functions are also required in ChildEventListener implementations.
                public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                }

                public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("TAG:", "Failed to read value.", error.toException());
                }
            });
        }


        AbsListView mListView;

        @Override
        protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = createAdapterView(inflater, container, savedInstanceState);
            mListView = getListView(rootView);
            mListView.setAdapter(mAdapter);
            return rootView;
        }

        protected abstract AbsListView getListView(View rootView);

        protected abstract View createAdapterView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

        @Override
        protected void destroyView() {
            mListView = null;
        }

        public abstract int getListResource();

        public abstract <AdapterHolder> AdapterHolder populateHolder(View convertView);

        public abstract void populateView(View convertView, AdapterHolder holder, int position, FirebaseAdapter.AdapterData data);
    }
}
