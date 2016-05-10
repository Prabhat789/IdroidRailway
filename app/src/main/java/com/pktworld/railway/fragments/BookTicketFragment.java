package com.pktworld.railway.fragments;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.pktworld.railway.R;
import com.pktworld.railway.util.ApplicationConstants;

/**
 * Created by ubuntu1 on 24/3/16.
 */
public class BookTicketFragment extends Fragment {
    private static final String TAG = BookTicketFragment.class.getSimpleName();
    private WebView webView;
    private ProgressDialog mProgressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_book_ticket, container, false);

        webView = (WebView)rootView.findViewById(R.id.webview);
        webView.setWebViewClient(new MyWebViewClient());
        webView.loadUrl(ApplicationConstants.IRCTC_URL);
        mProgressDialog = ProgressDialog.show(getActivity(), "",
                getResources().getString(R.string.processing), true);

        return rootView;

    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

            mProgressDialog.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            if (mProgressDialog.isShowing()){
                mProgressDialog.dismiss();
            }

            super.onPageFinished(view, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
