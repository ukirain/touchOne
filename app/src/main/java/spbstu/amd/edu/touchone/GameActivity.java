package spbstu.amd.edu.touchone;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

class Recta{
    Rect r;
    boolean selected;
    Paint color;

    Recta(int left, int top, int right, int bottom){
        r = new Rect(left, top, right, bottom);
        selected = false;
        color = new Paint();
        color.setColor(Color.BLUE);
    }

    void select(){
        selected = true;
        color.setColor(Color.GREEN);
    }

    void unselect(){
        selected = false;
        color.setColor(Color.BLUE);
    }
}


public class GameActivity extends AppCompatActivity {

    ActionBar actionBar;

    DrawingView dv ;
    private Paint mPaint;

    private Paint p;
    boolean gameOver = false;
    private Recta recta[];

    int numRect = 10;
    boolean win;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        dv = new DrawingView(this);
        setContentView(dv);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(12);
    }

    public class DrawingView extends View {

        public int width;
        public  int height;
        private Bitmap mBitmap;
        private Canvas mCanvas;
        private Path mPath;
        private Paint   mBitmapPaint;
        Context context;
        private Paint circlePaint;
        private Path circlePath;


        public DrawingView(GameActivity c) {
            super(c);
            context=c;
            mPath = new Path();
            mBitmapPaint = new Paint(Paint.DITHER_FLAG);
            circlePaint = new Paint();
            circlePath = new Path();
            circlePaint.setAntiAlias(true);
            circlePaint.setColor(Color.BLUE);
            circlePaint.setStyle(Paint.Style.STROKE);
            circlePaint.setStrokeJoin(Paint.Join.MITER);
            circlePaint.setStrokeWidth(4f);

            recta = new Recta[numRect];
            p = new Paint();
            for(int i = 0; i < numRect; i++) {
                recta[i] = new Recta( i * 50, i * 50, i * 50 + 50, i * 50 + 50);
            }
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);

            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);

            p.setColor(Color.RED);
            p.setStrokeWidth(10);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            canvas.drawBitmap( mBitmap, 0, 0, mBitmapPaint);
            for(int i = 0; i < numRect; i++) {
                canvas.drawRect(recta[i].r.left, recta[i].r.top, recta[i].r.right, recta[i].r.bottom, recta[i].color);
            }
            canvas.drawPath( mPath,  mPaint);
            canvas.drawPath( circlePath,  circlePaint);
        }

        private float mX, mY;
        private static final float TOUCH_TOLERANCE = 4;

        private void touch_start(float x, float y) {
            mPath.reset();
            mPath.moveTo(x, y);
            mX = x;
            mY = y;
        }

        private void touch_move(float x, float y) {
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            float R = 2;
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                float L = (float)Math.sqrt((x - mX) * (x - mX) + (y - mY) * (y - mY));
                float bias = 1 - R / L;
                mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
                mX = x;
                mY = y;

                circlePath.reset();
                circlePath.addCircle(mX  + 100 * ((x - mX) / dx), mY + 100 * ((y - mY) / dy), R, Path.Direction.CW);
            }
        }

        private void collider(float x, float y) {

            Region rPath = new Region(); //path of our finger
            Region clipPath = new Region(0, 0, dv.getWidth(), dv.getHeight());
            rPath.setPath(mPath, clipPath);

            Region circlePath = new Region(); //path of our finger
            circlePath.setPath(mPath, clipPath);

            //rects
            for(int i = 0; i < numRect; i++) {
                if (recta[i].r.contains((int) x, (int) y)) {
                    //pick the square
                    recta[i].select();

                }
            }

            //самопересечение
            if(rPath.contains((int)x,(int)y)){
                //crash
                gameOver = true;
            } else {
                //not crash
                gameOver = false;
            }
            gameContinuum();
        }

        private void touch_up() {
            mPath.lineTo(mX, mY);
            circlePath.reset();
            // commit the path to our offscreen
            //mCanvas.drawPath(mPath,  mPaint);
            // kill this so we don't double draw
            mPath.reset();
            for(int i = 0; i < numRect; i++) {
                    recta[i].unselect();
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();


            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touch_start(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    touch_move(x, y);
                    collider(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touch_up();
                    invalidate();
                    break;
            }
            return true;
        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void gameContinuum(){


        if(gameOver){
            mPaint.setColor(Color.RED);
        } else {
            mPaint.setColor(Color.YELLOW);
        }

        win = true;
        for(int i = 0; i < numRect; i++){
            if(recta[i].selected == false){
                win = false;
                break;
            }
        }
        if(win == true){
            Toast.makeText(this, "YOU WIN", Toast.LENGTH_SHORT).show();
            finish();
        }
        if(gameOver == true){
            Toast.makeText(this, "YOU LOSE", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

}
