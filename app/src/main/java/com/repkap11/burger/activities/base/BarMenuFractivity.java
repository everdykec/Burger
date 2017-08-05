package com.repkap11.burger.activities.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.repkap11.burger.R;
import com.repkap11.burger.activities.SettingsActivity;
import com.repkap11.burger.activities.SignInFractivity;

import java.util.concurrent.TimeUnit;

/**
 * Created by paul on 8/5/17.
 */

public abstract class BarMenuFractivity extends Fractivity<Fractivity.FractivityFragment> {

    public abstract static class BarMenuFragment extends Fractivity.FractivityFragment implements GoogleApiClient.OnConnectionFailedListener {
        private static final String TAG = BarMenuFractivity.class.getSimpleName();
        private GoogleApiClient mGoogleAPIClient;

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.menu_main, menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_settings:
                    Toast.makeText(getActivity(), "Settings selected", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), SettingsActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.action_sign_out:
                    FirebaseAuth.getInstance().signOut();
                    Auth.GoogleSignInApi.signOut(mGoogleAPIClient).setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            Intent intent = new Intent(getActivity(), SignInFractivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    });
                    Toast.makeText(getActivity(), "Sign Out selected", Toast.LENGTH_SHORT).show();

                    break;
                default:
                    Toast.makeText(getActivity(), "Other selected", Toast.LENGTH_SHORT).show();
                    break;

            }
            return false;
        }

        @Override
        protected void create(Bundle savedInstanceState) {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            mGoogleAPIClient = new GoogleApiClient.Builder(
                    getActivity())
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
            mGoogleAPIClient.connect();
        }

        protected final View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = createBarView(inflater, container, savedInstanceState);
            setHasOptionsMenu(true);
            Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
            if (toolbar != null) {
                ((Fractivity) getActivity()).setSupportActionBar(toolbar);
                ((Fractivity) getActivity()).getSupportActionBar();
            }
            return rootView;
        }

        @Override
        protected void destroyView() {

        }

        protected abstract View createBarView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);


        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        }
    }

}
