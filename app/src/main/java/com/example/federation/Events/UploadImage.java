package com.example.federation.Events;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.federation.R;
import com.example.federation.SQLite.DatabaseLRHandler;
import com.example.federation.Volley.MyVolleyClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UploadImage extends AppCompatActivity {

    private EditText eDesc;
    private ImageView iB;
    private Button bSubmit;
    private Bitmap bitmap;
    final private int PICK_IMAGE = 1;
    private Uri imageUri;
    private static final int GALLERY_REQUEST=1;
    private ProgressDialog progressDialog;
    String selectedPath="";
    private String uploadUrl="https://tsafindia.org/tsafadminpanel/tsaf/Appcontrollers/uploadImage";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);
        eDesc=(EditText)findViewById(R.id.descriptionField);
        iB=(ImageView) findViewById(R.id.imageSelect);
        bSubmit=(Button)findViewById(R.id.submitImage);
        iB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, ""), PICK_IMAGE);

            }
        });

        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(selectedPath.trim().equalsIgnoreCase(""))){
                    Runnable uploadImageThread=new Runnable() {
                        @Override
                        public void run() {
                            uploadImage();
                        }
                    };
                    uploadImageThread.run();
                }else{
                    Toast.makeText(getApplicationContext(),"Please select  files to upload.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_REQUEST && resultCode==RESULT_OK && data!=null){
            Uri path=data.getData();
            try {
                bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                iB.setImageBitmap(bitmap);
                selectedPath=path.getPath();
                Log.d("path",path.getPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage(){
        float height=bitmap.getHeight();
        float width=bitmap.getWidth();
        float ratio=width/height;
        Bitmap resizedImage=getResizedBitmap(bitmap,400,400*ratio);
        final String imageInString=bitmapToString(resizedImage);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, uploadUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response",response);
                Toast.makeText(getApplicationContext(),"Image uploaded successfully!",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(),EventInformationActivity.class);
                intent.putExtra("eventCode",getIntent().getStringExtra("eventCode"));
                intent.putExtra("eventName",getIntent().getStringExtra("eventName"));
                intent.putExtra("eventId",getIntent().getIntExtra("eventId",0));
                startActivity(intent);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error",error.toString());
                Toast.makeText(getApplicationContext(),"Something went wrong try again!",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("imageDescription",eDesc.getText().toString().trim());
                map.put("imageData",imageInString);
                map.put("eventCode",getIntent().getStringExtra("eventCode"));
                DatabaseLRHandler db=new DatabaseLRHandler(getApplicationContext());
                JSONArray jsonArray=db.getEventData(getIntent().getIntExtra("eventId",0));
                for(int i=0;i<jsonArray.length();i++){
                    try {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        map.put("eventName",jsonObject.getString("eventName"));
                        map.put("eventVenue",jsonObject.getString("eventVenue"));
                        map.put("eventStartingDate",jsonObject.getString("eventStartingDate"));
                        map.put("eventEndingDate",jsonObject.getString("eventEndingDate"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                SharedPreferences pref=getApplicationContext().getSharedPreferences("Tata",MODE_PRIVATE);
                map.put("instructorName",pref.getString("name",""));
                map.put("instructorEmail",pref.getString("email",""));

                return map;
            }
        };
        MyVolleyClass.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
    private String bitmapToString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] byteArray=byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray,Base64.DEFAULT);
    }


    public Bitmap getResizedBitmap(Bitmap bm, float newHeight, float newWidth) {

        int width = bm.getWidth();

        int height = bm.getHeight();

        float scaleWidth = ( (float)newWidth) / width;

        float scaleHeight = ( (float)newHeight) / height;

// CREATE A MATRIX FOR THE MANIPULATION

        Matrix matrix = new Matrix();

// RESIZE THE BIT MAP

        matrix.postScale(scaleWidth, scaleHeight);

// RECREATE THE NEW BITMAP

        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);

        return resizedBitmap;

    }


    private Bitmap loadImage(String imgPath) {
        BitmapFactory.Options options;
        try {
            options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            Bitmap bitmap = BitmapFactory.decodeFile(imgPath, options);
            return bitmap;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getAbsolutePath(Uri uri) {
        String[] projection = { MediaStore.MediaColumns.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

/*

    class ImageUploadTask extends AsyncTask<Void, Void, String> {
        private String webAddressToPost = "http://your-website-here.com";

        // private ProgressDialog dialog;
        private ProgressDialog dialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Uploading...");
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                HttpPost httpPost = new HttpPost(webAddressToPost);

                MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(CompressFormat.JPEG, 100, bos);
                byte[] data = bos.toByteArray();
                String file = Base64.encodeBytes(data);

                entity.addPart("uploaded", new StringBody(file));
                entity.addPart("someOtherStringToSend", new StringBody("your string here"));

                httpPost.setEntity(entity);
                HttpResponse response = httpClient.execute(httpPost,localContext);
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        response.getEntity().getContent(), "UTF-8"));

                String sResponse = reader.readLine();
                return sResponse;
            } catch (Exception e) {
                // something went wrong. connection with the server error
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            Toast.makeText(getApplicationContext(), "file uploaded",Toast.LENGTH_LONG).show();
        }
    }
 /*   private void doFileUpload(){

        File file = new File(selectedPath);
        String urlString = "http://10.0.2.2/upload_test/upload_media_test.php";
        try
        {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(urlString);
            FileBody bin = new FileBody(file);

            MultipartEntity reqEntity = new MultipartEntity();
            reqEntity.addPart("uploadedfile", bin);

            reqEntity.addPart("user", new StringBody("User"));
            post.setEntity(reqEntity);
            HttpResponse response = client.execute(post);
            resEntity = response.getEntity();
            final String response_str = EntityUtils.toString(resEntity);
            if (resEntity != null) {
                Log.i("RESPONSE",response_str);
                runOnUiThread(new Runnable(){
                    public void run() {
                        try {
                            res.setTextColor(Color.GREEN);
                            res.setText("n Response from server : n " + response_str);
                            Toast.makeText(getApplicationContext(),"Upload Complete. Check the server uploads directory.", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
        catch (Exception ex){
            Log.e("Debug", "error: " + ex.getMessage(), ex);
        }
    }*/
}
