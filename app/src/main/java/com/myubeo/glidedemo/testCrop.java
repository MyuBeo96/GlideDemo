package com.myubeo.glidedemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class testCrop extends AppCompatActivity {
	ImageView iv;
	String imagePath = "https://style.pk/wp-content/uploads/2015/07/omer-Shahzad-performed-umrah-600x548.jpg";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		iv = findViewById(R.id.imgOther);
		imageDownload image = new imageDownload(testCrop.this, iv);
		image.execute(imagePath);
	}

	class imageDownload extends AsyncTask<String, Integer, Bitmap> {
		Context context;
		ImageView imageView;
		Bitmap bitmap;
		InputStream in = null;
		int responseCode = -1;
		//constructor.
		public imageDownload(Context context, ImageView imageView) {
			this.context = context;
			this.imageView = imageView;
		}
		@Override
		protected void onPreExecute() {


		}
		@Override
		protected Bitmap doInBackground(String... params) {

			URL url = null;
			try {
				url = new URL(params[0]);

				HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
				httpURLConnection.setDoOutput(true);
				httpURLConnection.connect();
				responseCode = httpURLConnection.getResponseCode();
				if (responseCode == HttpURLConnection.HTTP_OK) {
					in = httpURLConnection.getInputStream();
					bitmap = BitmapFactory.decodeStream(in);
					in.close();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return bitmap;
		}
		@Override
		protected void onPostExecute(Bitmap data) {
			imageView.setImageBitmap(data);
		}
	}
}
