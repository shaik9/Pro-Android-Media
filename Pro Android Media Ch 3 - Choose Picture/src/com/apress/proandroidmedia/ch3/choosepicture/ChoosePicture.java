package com.apress.proandroidmedia.ch3.choosepicture;

import java.io.FileNotFoundException;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class ChoosePicture extends Activity implements OnClickListener {

	ImageView chosenImageView;
	Button choosePicture;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		chosenImageView = (ImageView) this.findViewById(R.id.ChosenImageView);
		choosePicture = (Button) this.findViewById(R.id.ChoosePictureButton);

		choosePicture.setOnClickListener(this);
	}

	public void onClick(View v) {
		Intent choosePictureIntent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(choosePictureIntent, 0);
	}

	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		if (resultCode == RESULT_OK) {
			Uri imageFileUri = intent.getData();

			Display currentDisplay = getWindowManager().getDefaultDisplay();
			int dw = currentDisplay.getWidth();
			int dh = currentDisplay.getHeight() / 2 - 100;

			try {
				// Load up the image's dimensions not the image itself
				BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
				bmpFactoryOptions.inJustDecodeBounds = true;
				Bitmap bmp = BitmapFactory
						.decodeStream(getContentResolver().openInputStream(
								imageFileUri), null, bmpFactoryOptions);

				int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight
						/ (float) dh);
				int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth
						/ (float) dw);

				if (heightRatio > 1 && widthRatio > 1) {
					if (heightRatio > widthRatio) {
						bmpFactoryOptions.inSampleSize = heightRatio;
					} else {
						bmpFactoryOptions.inSampleSize = widthRatio;
					}
				}

				bmpFactoryOptions.inJustDecodeBounds = false;
				bmp = BitmapFactory
						.decodeStream(getContentResolver().openInputStream(
								imageFileUri), null, bmpFactoryOptions);

				chosenImageView.setImageBitmap(bmp);

			} catch (FileNotFoundException e) {
				Log.v("ERROR", e.toString());
			}
		}
	}
}
