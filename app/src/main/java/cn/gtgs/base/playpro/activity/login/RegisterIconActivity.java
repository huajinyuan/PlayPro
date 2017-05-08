package cn.gtgs.base.playpro.activity.login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gt.okgo.OkGo;
import com.gt.okgo.listener.UploadListener;
import com.gt.okgo.request.PostRequest;
import com.gt.okgo.upload.UploadInfo;
import com.gt.okgo.upload.UploadManager;

import java.io.File;
import java.io.FileOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.gtgs.base.playpro.PApplication;
import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.activity.login.model.RegisterInfo;
import cn.gtgs.base.playpro.http.Config;
import cn.gtgs.base.playpro.utils.F;
import okhttp3.Response;

public class RegisterIconActivity extends AppCompatActivity {
    Context context;
    public static RegisterIconActivity instance;

    @BindView(R.id.iv_register_icon)
    ImageView iv_register_icon;
    @BindView(R.id.rl_cuticon)
    RelativeLayout rl_cuticon;
    @BindView(R.id.iv_icon)
    ImageView iv_icon;
    @BindView(R.id.et_register_nickname)
    EditText et_nickname;
    @BindView(R.id.rg_register_sex)
    RadioGroup rg_sex;
    @BindView(R.id.tv_topbar_title)
    TextView mTitle;

    AlertDialog mydialog;
    String mPhotoPath;
    File mPhotoFile, smallimagefile;
    String sdcardPath = Environment.getExternalStorageDirectory().getPath();
    String avatarPath;
    boolean haveimage = false;

    Bitmap bitmap_normal;

    Matrix matrix = new Matrix();
    int mode = 0;
    int DRAG = 1;
    int ZOOM = 2;
    PointF startPoint = new PointF();   //起始点
    float startDis = 0;
    String urlPath;
    private UploadManager uploadManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PApplication.getInstance().mActiviyts.add(this);
        setContentView(R.layout.activity_register_icon);
        uploadManager = UploadManager.getInstance();
        context = this;
        instance = this;
        ButterKnife.bind(this);
        avatarPath = getFilesDir().getPath() + "/icon.png";
        mTitle.setText("信息完善");
    }

    @OnClick(R.id.iv_register_icon)
    void seticon() {
        showPhotodialog();
    }

    @OnClick(R.id.img_topbar_back)
    void setback() {
        finish();
    }

    @OnClick(R.id.iv_cancel)
    void setcancel() {
        finish();
    }

    @OnClick(R.id.iv_ok)
//确定裁剪
    void setok() {
        haveimage = true;
        matrix.postTranslate(-113, -86);//选中中心位置
        matrix.postScale(0.5f, 0.5f);
        try {
            Bitmap smallbitmap = Bitmap.createBitmap(250, 250, Bitmap.Config.RGB_565);//裁剪250*250
            Canvas canvas = new Canvas(smallbitmap);
            canvas.drawBitmap(bitmap_normal, matrix, null);
            FileOutputStream os = new FileOutputStream(avatarPath);
            smallbitmap.compress(Bitmap.CompressFormat.PNG, 80, os);
            os.close();
            matrix.reset();//清空matrix

            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), smallbitmap);
            roundedBitmapDrawable.setCircular(true);
            iv_register_icon.setImageDrawable(roundedBitmapDrawable);//设置头像
//            OkGo.
            MyUploadListener listener = new MyUploadListener();
            listener.setUserTag(avatarPath);
            PostRequest postRequest = OkGo.post(Config.FileUpload).params("file", new File(avatarPath));
            uploadManager.addTask(avatarPath, postRequest, listener);


            iv_icon.setImageBitmap(null);
            rl_cuticon.setVisibility(View.GONE);
            if ((bitmap_normal != null) && (bitmap_normal.isRecycled() == false)) {//释放bitmap_normal
                Log.e("sda", "clear bitmap_normal");
                bitmap_normal.recycle();
                bitmap_normal = null;
            }

        } catch (Exception e) {
            Log.e("dda", "wrong at line 103");
        }
    }

    @OnClick(R.id.bt_next)
    void setnext() {
        String nickname = et_nickname.getText().toString().trim();
        if (nickname.equals(""))
            Toast.makeText(context, "取一个昵称吧", Toast.LENGTH_SHORT).show();
        else if (null == urlPath)
            Toast.makeText(context, "设置一个头像吧", Toast.LENGTH_SHORT).show();
        else {
            RegisterInfo info = new RegisterInfo();
            info.setAvatar_path(urlPath);
            info.setName(nickname);

//            String sex =rg_sex.getCheckedRadioButtonId() == R.id.rb_register_f ? "f":"m";
            info.setGender(rg_sex.getCheckedRadioButtonId() == R.id.rb_register_f ? "f" : "m");
//            if (rg_sex.getCheckedRadioButtonId() == R.id.rb_register_f)
//                RegisterInfo.instance().gender = "f";
//            else RegisterInfo.instance().gender = "m";
//            startActivity(new Intent(context, LoginActivity.class));


            Intent intent = new Intent();
            // 获取用户计算后的结果
            intent.putExtra("RegisterInfo", info);
            setResult(2, intent);

            finish();
        }
    }

    // /////////////////////////////////////////////////////////////自定义Dialog
    public void showPhotodialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogTransBackGround);
        mydialog = builder.create();
        View view = LayoutInflater.from(this).inflate(R.layout.item_dialog_pickimg, null);
        mydialog.setCanceledOnTouchOutside(false);
        //->
        Window window = mydialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.y = 50;
        window.setAttributes(params);
        //->
        mydialog.show();
        mydialog.setContentView(view);

        // dialog内部的点击事件
        view.findViewById(R.id.bt_dialog_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mydialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 2);
            }
        });
        view.findViewById(R.id.bt_dialog_capture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mydialog.dismiss();
                takePicture();
            }
        });
        view.findViewById(R.id.bt_dialog_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mydialog.dismiss();
            }
        });
    }

    //
    public void takePicture() {
        try {
            mPhotoPath = sdcardPath + "/icon.png";
            mPhotoFile = new File(mPhotoPath);
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
            startActivityForResult(intent, 1);
        } catch (Exception e) {
        }
    }

    // onActivityResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            rl_cuticon.setVisibility(View.VISIBLE);
            if (requestCode == 1) {
                bitmap_normal = BitmapFactory.decodeFile(mPhotoPath);
            } else {
                Uri uri = data.getData();
                String path;
                if (!TextUtils.isEmpty(uri.getAuthority())) {
                    path = getPathFromUri(uri);
                } else {
                    path = uri.getPath();
                }


                bitmap_normal = BitmapFactory.decodeFile(path);
            }
            iv_icon.setImageBitmap(bitmap_normal);

            int oldwidth = bitmap_normal.getWidth();
            int oldheight = bitmap_normal.getHeight();

            float scale = oldwidth >= oldheight ? 720f / oldheight : 720f / oldwidth;
            matrix.postScale(scale, scale);
            iv_icon.setImageMatrix(matrix);

            iv_icon.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_DOWN:
                            Log.e("sds", "ACTION_DOWN");
                            mode = DRAG;
                            startPoint.set(event.getX(), event.getY());
                            break;
                        case MotionEvent.ACTION_POINTER_DOWN:
                            Log.e("sds", "ACTION_POINTER_DOWN");
                            mode = ZOOM;
                            startDis = distance(event);
                            break;
                        case MotionEvent.ACTION_MOVE:
                            Log.e("dsad", mode + "");
                            if (mode == DRAG) {
                                float dx = event.getX() - startPoint.x;
                                float dy = event.getY() - startPoint.y;
                                matrix.postTranslate(dx, dy);
                                startPoint.set(event.getX(), event.getY());
                                iv_icon.setImageMatrix(matrix);
                            } else if (mode == ZOOM) {
                                float endDis = distance(event);
                                float mscale = endDis / startDis;
                                matrix.postScale(mscale, mscale);
                                iv_icon.setImageMatrix(matrix);
                                startDis = endDis;
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            Log.e("dw", "ACTION_UP");
                            mode = 0;
                            break;
                        case MotionEvent.ACTION_POINTER_UP:
                            Log.e("dw", "ACTION_POINTER_UP");
                            mode = 0;
                            break;
                    }
                    return true;
                }
            });
            try {
                if (mPhotoFile.exists())
                    mPhotoFile.delete();
            } catch (Exception e) {

            }

        }
    }

    // getPathFromUri
    public String getPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = this.getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private static float distance(MotionEvent event) {
        //两根线的距离
        float dx = event.getX(1) - event.getX(0);
        float dy = event.getY(1) - event.getY(0);
        return (float) Math.sqrt(dx * dx + dy * dy);
    }


    private class MyUploadListener extends UploadListener<String> {

//        private ViewHolder holder;

        @Override
        public void onProgress(UploadInfo uploadInfo) {
            Log.e("MyUploadListener", "onProgress:" + uploadInfo.getTotalLength() + " " + uploadInfo.getUploadLength() + " " + uploadInfo.getProgress());
//            holder = (ViewHolder) ((View) getUserTag()).getTag();
//            holder.refresh(uploadInfo);
        }

        @Override
        public void onFinish(String s) {
            Log.e("MyUploadListener", "finish:" + s);

            try {
                JSONObject json = JSON.parseObject(s);
                if (json.containsKey("data")) {
                    JSONObject ob = json.getJSONObject("data");
                    if (ob.containsKey("filePath")) {
                        urlPath = ob.getString("filePath");
                    }

                }
            } catch (Exception e) {
                F.e(e.toString());
            }


//            holder.finish();
        }

        @Override
        public void onError(UploadInfo uploadInfo, String errorMsg, Exception e) {
            Log.e("MyUploadListener", "onError:" + errorMsg);
        }

        @Override
        public String parseNetworkResponse(Response response) throws Exception {
            Log.e("MyUploadListener", "convertSuccess");
            return response.body().string();
        }
    }

}
