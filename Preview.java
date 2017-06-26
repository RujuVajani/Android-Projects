package com.in.sight.android;

import java.io.IOException;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.media.FaceDetector;
import android.media.FaceDetector.Face;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class Preview extends Activity implements SurfaceHolder.Callback {
	/** Called when the activity is first created. */
	private Camera camera;
	Button b;
	Intent intent;
	int fps;
	boolean face_found = false;
	SurfaceView surface;
	public Handler handler;
	public static int x, y;
	public static boolean list_started = false;
	int coxl, coxr, coyl, coyr;
	public static int blink_stage=0;
	public static SurfaceHolder holder;
	Bitmap eye_window;
	int count=0;
	// Bundle extras;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		intent = new Intent(this, ListMenu.class);
		surface = (SurfaceView) findViewById(R.id.surface);
		holder = surface.getHolder();
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		holder.setFixedSize(400, 300);
		// extras = getIntent().getExtras();

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			camera = Camera.open();

			camera.setPreviewDisplay(holder);
			Parameters p = camera.getParameters();
			p.setSceneMode(Parameters.SCENE_MODE_LANDSCAPE);
			p.set("orientation", "portrait");

			fps = p.getPreviewFrameRate();
			p.setPreviewFrameRate(1);
			camera.setParameters(p);
			final Size s = p.getPreviewSize();
			camera.startPreview();

			camera.setPreviewCallback(new PreviewCallback() {
				@Override
				public void onPreviewFrame(byte[] data, Camera _camera) {
					detecteye(data, s);
				}
			});

		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.i("in surface", "created");
		if (Preview.this.face_found == true && list_started == false) {
			camera.stopPreview();
			camera.release();

		}

	}

	public void detecteye(byte[] d, Size siz) {
		int h = siz.height;
		int w = siz.width;

		final int color[] = new int[w * h];
		// decodeYUV420SP(color, d, w, h);
		stripYUV(color, d, w, h);
		Bitmap oldbitmap = Bitmap.createBitmap(color, w, h,
				Bitmap.Config.RGB_565);

		final Matrix matrix = new Matrix();
		matrix.preRotate(90);
		// recreate the new Bitmap
		
		Bitmap bitmap = Bitmap.createBitmap(oldbitmap, 0, 0, w, h,
				matrix, true);
		
		if (bitmap == null)
			Toast.makeText(this, "No eyes found!!", Toast.LENGTH_LONG).show();
		else {
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			FaceDetector detector = new FaceDetector(width, height, 1);
			Face[] faces = new Face[1];
			int facesFound = 0;
			try {
				facesFound = detector.findFaces(bitmap, faces);
			} catch (Exception e) {
				Log.i("face exception", "" + e);
			}

			PointF midPoint = new PointF();
			Log.i("faces", facesFound + " " + midPoint);

			if (facesFound > 0) {
				count=0;
				//bitmap=bitmap.extractAlpha();
				//img.setImageBitmap(bitmap);
				//for (int index = 0; index < facesFound; ++index) { // Get the
																	// eye
																	// distance,
																	// detected
																	// eye mid
																	// point and
																	// confidence
					faces[0].getMidPoint(midPoint);
					float eyeDistance = faces[0].eyesDistance();
					//confidence = faces[index].confidence();
					
					x = (int) midPoint.x;
					y = (int) midPoint.y;
					Log.i("face found", face_found + "");
					if (face_found == false) {
					Thread runthread = new Thread() {
						
						public void run() {
							camera.stopPreview();
							Preview.this.startActivity(intent);
							
							
							try {
								sleep(2500);
							} catch (InterruptedException e) {
								// do nothing
							} finally {
								
								Parameters p = camera.getParameters();
								p.setPreviewFrameRate(10);
								camera.setParameters(p);
								camera.startPreview();
								face_found = true;
					        	}
							

							}
						
					};
					runthread.start();
					
				}
					
						
					else {
						/*Rect r=new Rect();
						r.left=(int) (midPoint.x-eyeDistance);
						r.top=(int) (midPoint.y-eyeDistance/5);
						r.right=(int) (midPoint.x+eyeDistance);
						r.bottom=(int) (midPoint.y+eyeDistance/5);
						if(r.right>bitmap.getWidth())r.right=bitmap.getWidth();
						if(r.left<bitmap.getWidth())r.left=0;
						if(r.bottom>bitmap.getHeight())r.bottom=bitmap.getHeight();
						if(r.top<bitmap.getHeight())r.top=0;
						
						eye_window=Bitmap.createBitmap(bitmap, r.left, r.right, r.width(), r.height());
						applyThreshold(eye_window);
						if(count>=(eye_window.getHeight()*eye_window.getWidth()/5)){
							if(blink_stage==2)
								blink_stage=3;
							else
								blink_stage=1;
						}
						else{
							if(blink_stage==1){
								blink_stage=2;
							}
							else blink_stage=0;
						}*/
						ListMenu.change();
					}

				//}

			} else {

				Toast.makeText(getApplicationContext(),
						"No faces move in front of camera!!",
						Toast.LENGTH_SHORT).show();

			}
		}

	}
/*
	private void applyThreshold(Bitmap b) {
		int[] n=new int[b.getHeight()*b.getWidth()];
		b.getPixels(n, 0, b.getWidth(), 0, 0, b.getWidth(), b.getHeight());
		int i=0;
		for(i=0;i<n.length;i++){
			n[i]=Math.abs(n[i]);
			//n[i]=b.getPixel(15,20);
			if(n[i]<=0x00909090){
				n[i]=Color.BLACK;
				count++;
			}else {
				n[i]=Color.WHITE;
				
			}
			}
		Log.i("black",i+" "+count);
		
		}
		*/
	

	

	public static void stripYUV(int[] rgb, byte[] yuv, int width, int height)
			throws NullPointerException, IllegalArgumentException {
		int Y;
		for (int j = 0; j < height; j++) {
			int pixPtr = j * width;
			for (int i = 0; i < width; i++) {
				Y = yuv[pixPtr];
				if (Y < 0)
					Y += 255;
				rgb[pixPtr++] = 0xff000000 + Y * 0x00010101;// (Y << 16) + (Y <<
															// + Y;
			}
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		Log.i("in surface", "destroy");
		camera.stopPreview();
		camera.release();

	}
	public boolean onKeyDown(int arg0, KeyEvent arg1) {
		// TODO Auto-generated method stub
		System.exit(0);
		return true;
	}
}