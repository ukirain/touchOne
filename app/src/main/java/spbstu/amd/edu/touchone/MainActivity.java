package spbstu.amd.edu.touchone;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.util.Locale;


public class MainActivity extends Activity implements MediaPlayer.OnCompletionListener, View.OnTouchListener
{
    // ********************************************
    // CONST
    // ********************************************
    public static final int	VIEW_INTRO		= 0;
    public static final int	VIEW_GAME       = 1;


    // *************************************************
    // DATA
    // *************************************************
    int						m_viewCur = -1;

    AppIntro				m_app;
    ViewIntro			    m_viewIntro;
    //ViewGame				m_viewGame;


    // screen dim
    int						m_screenW;
    int						m_screenH;
    boolean                 gamestarted = false;


    // *************************************************
    // METHODS
    // *************************************************
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //overridePendingTransition(0, 0);
        // No Status bar
        final Window win = getWindow();
        win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Application is never sleeps
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        m_screenW = point.x;
        m_screenH = point.y;

        Log.d("THREE", "Screen size is " + String.valueOf(m_screenW) + " * " +  String.valueOf(m_screenH) );

        // Detect language
        String strLang = Locale.getDefault().getDisplayLanguage();
        int language;
        if (strLang.equalsIgnoreCase("english"))
        {
            Log.d("THREE", "LOCALE: English");
            language = AppIntro.LANGUAGE_ENG;
        } else {
            Log.d("THREE", "LOCALE unknown: " + strLang);
            language = AppIntro.LANGUAGE_UNKNOWN;
            //AlertDialog alertDialog;
            //alertDialog = new AlertDialog.Builder(this).create();
            //alertDialog.setTitle("Language settings");
            //alertDialog.setMessage("This application available only in English or Russian language.");
            //alertDialog.show();
        }
        // Create application
        m_app = new AppIntro(this, language);
        // Create view
        setView(VIEW_INTRO);


    }
    public void setView(int viewID)
    {
        if (m_viewCur == viewID)
        {
            Log.d("THREE", "setView: already set");
            return;
        }

        m_viewCur = viewID;
        if (m_viewCur == VIEW_INTRO)
        {
            m_viewIntro = new ViewIntro(this);
            setContentView(m_viewIntro);
        }
        if (m_viewCur == VIEW_GAME)
        {
            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
            System.exit(0);
            /*View menu = new MenuActivity(this);
            setContentView(menu);*/

        }
    }

    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.

        // delayedHide(100);
    }
    public void onCompletion(MediaPlayer mp)
    {
        Log.d("THREE", "onCompletion: Video play is completed");
        //switchToGame();
    }


    public boolean onTouch(View v, MotionEvent evt)
    {
        int x = (int)evt.getX();
        int y = (int)evt.getY();
        int touchType = AppIntro.TOUCH_DOWN;

        //if (evt.getAction() == MotionEvent.ACTION_DOWN)
        //  Log.d("THREE", "Touch pressed (ACTION_DOWN) at (" + String.valueOf(x) + "," + String.valueOf(y) +  ")"  );

        if (evt.getAction() == MotionEvent.ACTION_MOVE)
            touchType = AppIntro.TOUCH_MOVE;
        if (evt.getAction() == MotionEvent.ACTION_UP)
            touchType = AppIntro.TOUCH_UP;

        if (m_viewCur == VIEW_INTRO)
            return m_viewIntro.onTouch( x, y, touchType);
        if (m_viewCur == VIEW_GAME)
        {

        }
        return true;
    }
    public boolean onKeyDown(int keyCode, KeyEvent evt)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            //Log.d("THREE", "Back key pressed");
            //boolean wantKill = m_app.onKey(Application.KEY_BACK);
            //if (wantKill)
            //		finish();
            //return true;
            System.exit(0);
        }
        boolean ret = super.onKeyDown(keyCode, evt);
        return ret;
    }
    public AppIntro getApp()
    {
        return m_app;
    }

    protected void onResume()
    {
        super.onResume();
        if (m_viewCur == VIEW_INTRO)
            m_viewIntro.start();
        if (m_viewCur == VIEW_GAME){}
            //m_viewGame.start();
        //Log.d("THREE", "App onResume");
    }
    protected void onPause()
    {
        // stop anims
        if (m_viewCur == VIEW_INTRO)
            m_viewIntro.stop();
        if (m_viewCur == VIEW_GAME)
            //m_viewGame.onPause();

        // complete system
        super.onPause();
        //Log.d("THREE", "App onPause");
    }
    protected void onDestroy()
    {
        if (m_viewCur == VIEW_GAME)
            //m_viewGame.onDestroy();
        super.onDestroy();
        //Log.d("THREE", "App onDestroy");
    }
    public void onConfigurationChanged(Configuration confNew)
    {
        super.onConfigurationChanged(confNew);
        m_viewIntro.onConfigurationChanged(confNew);
    }



}