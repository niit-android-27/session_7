package com.luan.myapp.session_7;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ghi file
        /*String chuoi = "Hello World";
        try{
            FileOutputStream fos = openFileOutput("myfile.txt",MODE_PRIVATE);
            fos.write(chuoi.getBytes());
            fos.close();
        }catch (IOException e){
            e.printStackTrace();
        }*/

        //doc file
        /*try {
            FileInputStream fileInputStream = openFileInput("myfile.txt");
            byte[] counter = new byte[fileInputStream.available()];
            while(fileInputStream.read(counter)!=-1){
                Log.e("READER",new String(counter));
            }
            fileInputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }*/
       // checkPermission();
        new DownloadFile("http://learning-reimagined.com/wp-content/uploads/2016/07/vietnam-flag-870x400-1.jpg").execute();
    }

    void checkPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
           if( checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                   != PackageManager.PERMISSION_GRANTED ){
                requestPermissions(
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        2);
           }else{
               //ghi file o day
               writeExternal();
           }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==2){
            //ket qua tra ve tu request write external storage
            if(grantResults.length!=0){
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    //ghi file o day
                    writeExternal();
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void writeExternal(){
        Log.e("FOLDER",Environment.getExternalStorageDirectory().getAbsolutePath());
       // if(Environment.getExternalStorageState()
       //         == Environment.MEDIA_MOUNTED) {
            try {
                //Environment.getExternalStoragePublicDirectory(Environment.DIREC)
                FileOutputStream fileOutputStream = new FileOutputStream(
                        new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                                , "mytext.txt")
                );
                String name = "NIIT";
                fileOutputStream.write(name.getBytes());
                fileOutputStream.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        //}
    }

    public void readExternal(){
        try {
            FileInputStream fileInputStream = new FileInputStream(
                    new File(Environment.getExternalStorageDirectory().getAbsolutePath(),
                    "mytext.txt"));
            byte[] counter = new byte[fileInputStream.available()];
            while (fileInputStream.read(counter)!=-1){
                Log.e("READER",new String(counter));
            }
            fileInputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private  class DownloadFile extends AsyncTask<Void,Void,Void>{
        String url;
        public DownloadFile(String url) {
            this.url=url;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                URL url = new URL(this.url);
                //mo connection lay ve input stream
                InputStream inputStream= url.openConnection().getInputStream();
                //tao file de ghi
                FileOutputStream fos = new FileOutputStream(
                        new File(Environment.getExternalStorageDirectory().getAbsolutePath(),
                                "my_image.jpg"));
                byte[] myBytes = new byte[1024*4];
                int count;
                while ((count=inputStream.read(myBytes))!=-1){
                    //ghi du lieu nhi phan
                    fos.write(myBytes,0,count);
                }
                inputStream.close();
                fos.close();
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.e("TAG","DONE");
        }
    }

    //write prefencese
    public void writePrefences(){
        SharedPreferences preferences = getSharedPreferences("myname", Context.MODE_PRIVATE);
        preferences.edit().putString("name","Luan").commit();
        preferences.edit().putInt("age",0).commit();
    }

    public void readPrefences(){
        SharedPreferences preferences = getSharedPreferences("name",Context.MODE_PRIVATE);
        String name = preferences.getString("name","");
        int age= preferences.getInt("age",0);
    }
}
