package com.example.face_dedection;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView abdImage;
    Button processButton;
    Bitmap myBitmap;
    Canvas canvas;
    Paint rectPaint;
    Bitmap tempBitmap;
    Dialog openDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        abdImage=findViewById(R.id.abdImage);
        processButton=findViewById(R.id.processButton);


            myBitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.abd);
        abdImage.setImageBitmap(myBitmap);

        rectPaint = new Paint();
        rectPaint.setStrokeWidth(5);
        rectPaint.setColor(Color.GREEN);
        rectPaint.setStyle(Paint.Style.STROKE);

        tempBitmap=Bitmap.createBitmap(myBitmap.getWidth(),myBitmap.getHeight(),Bitmap.Config.RGB_565);
        canvas=new Canvas(tempBitmap);
        canvas.drawBitmap(myBitmap,0,0,null);

        processButton.setOnClickListener(this);
    }


    public void setPopup(View view) {
        openDialog = new Dialog(MainActivity.this);
        openDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        openDialog.setContentView(R.layout.failure_popup);
    }
    @Override
    public void onClick(View v) {
        FaceDetector faceDetector=new FaceDetector.Builder(getApplicationContext())
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .setMode(FaceDetector.FAST_MODE)
                .build();
        if(!faceDetector.isOperational())
        {
             //   setPopup(view);
                Toast.makeText(this, "Face Detector could not be set up on your device please restart your application", Toast.LENGTH_SHORT).show();
                return;
        }
        Frame frame=new Frame.Builder().setBitmap(myBitmap).build();
        SparseArray<Face> sparseArray =faceDetector.detect(frame);
        if (sparseArray.size() > 0){
        for(int i=0;i<sparseArray.size();i++){
            Face face=sparseArray.valueAt(i);
            float x1=face.getPosition().x;
            float y1=face.getPosition().y;
            float x2=x1+face.getWidth();
            float y2=y1+face.getHeight();
            RectF rectF=new RectF(x1,y1,x2,y2);
            canvas.drawRoundRect(rectF,2,2,rectPaint);
            Toast.makeText(this, "Face captured successfully !", Toast.LENGTH_LONG).show();

        }

        }else {
            Toast.makeText(this,"Can't detect any face in the picture", Toast.LENGTH_LONG).show();
        }

        abdImage.setImageDrawable(new BitmapDrawable(getResources(),tempBitmap));
    }
}
