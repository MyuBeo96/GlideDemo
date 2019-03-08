package com.myubeo.glidedemo;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends AppCompatActivity {
	private Context mContext;
	private Activity mActivity;

	private RelativeLayout mCLayout;
	private ProgressDialog mProgressDialog;
	private ImageView mImageView;

	private AsyncTask mMyTask;

	public static final String path = Environment.getExternalStorageDirectory().getAbsolutePath();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = getApplicationContext();
		mActivity = MainActivity.this;

		mCLayout = findViewById(R.id.layout);
		mImageView = findViewById(R.id.imgOther);

		mProgressDialog = new ProgressDialog(mActivity);
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setTitle("AsyncTask");
		mProgressDialog.setMessage("Please wait, we are downloading your image file...");

		int permission_internet = ContextCompat.checkSelfPermission(this,
				Manifest.permission.INTERNET);
		int permission_send_sms = ContextCompat.checkSelfPermission(this,
				Manifest.permission.WRITE_EXTERNAL_STORAGE);
		int permission_camera = ContextCompat.checkSelfPermission(this,
				Manifest.permission.READ_PHONE_STATE);

		if (permission_internet != PackageManager.PERMISSION_GRANTED
				|| permission_send_sms != PackageManager.PERMISSION_GRANTED
				|| permission_camera != PackageManager.PERMISSION_GRANTED) {
			makeRequest();
		}

		new ImageLoadTask(MainActivity.this,"https://ya-webdesign.com/images/doraemon-drawing-transparent-background-3.png",mImageView).execute();
	}

	protected void makeRequest() {
		ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET,
				Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE}, 1);
	}

	public static class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {
		private Context context;
		private String url;
		private ImageView imgShow;

		public ImageLoadTask(Context context, String url, ImageView imgShow) {
			this.url = url;
			this.context = context;
			this.imgShow=imgShow;
		}

		@Override
		protected Bitmap doInBackground(Void... params) {
			Bitmap myBitmap = null;

			try {
				URL urlConnection = new URL(url);
				HttpURLConnection connection = (HttpURLConnection) urlConnection
						.openConnection();
				connection.setDoInput(true);
				connection.connect();
				InputStream input = connection.getInputStream();
				myBitmap = BitmapFactory.decodeStream(input);
				if (myBitmap != null && context != null) {
					MediaStore.Images.Media.insertImage(context.getContentResolver(), myBitmap, "Anpanman", "Image");
				}
				return myBitmap;
			}catch (SecurityException e){
				e.printStackTrace();
				Log.d(getClass().getSimpleName(), "doInBackground: ");
			}
			catch (Exception e) {
				e.printStackTrace();

			}
			return myBitmap;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			super.onPostExecute(bitmap);
			if (bitmap != null && this.context != null) {
				Toast.makeText(this.context, "Download finish", Toast.LENGTH_SHORT).show();
				imgShow.setImageBitmap(bitmap);
			}else if (this.context!= null){
				Toast.makeText(this.context,"Download fail", Toast.LENGTH_SHORT).show();
			}
		}

	}

}
