package cn.gtgs.base.playpro.activity.login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.gt.okgo.OkGo;
import com.gt.okgo.listener.UploadListener;
import com.gt.okgo.request.PostRequest;
import com.gt.okgo.upload.UploadInfo;
import com.gt.okgo.upload.UploadManager;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.gtgs.base.playpro.PApplication;
import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.activity.login.model.RegisterInfo;
import cn.gtgs.base.playpro.http.Config;
import cn.gtgs.base.playpro.utils.BitmapUtil;
import cn.gtgs.base.playpro.utils.F;
import okhttp3.Response;

public class RegisterIconActivity extends AppCompatActivity {
    Context context;
    public static RegisterIconActivity instance;

    @BindView(R.id.iv_register_icon)
    ImageView iv_register_icon;
    @BindView(R.id.et_register_nickname)
    EditText et_nickname;
    @BindView(R.id.rg_register_sex)
    RadioGroup rg_sex;
    @BindView(R.id.tv_topbar_title)
    TextView mTitle;
    AlertDialog mydialog;
    String mPhotoPath;
    File mPhotoFile;
    String sdcardPath = Environment.getExternalStorageDirectory().getPath();
    String avatarPath;
    String urlPath;
    private UploadManager uploadManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PApplication.getInstance().mActiviyts.add(this);
        setContentView(R.layout.activity_register_icon);
        instance = this;
        ButterKnife.bind(this);
        uploadManager = UploadManager.getInstance();
        context = this;
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
            info.setGender(rg_sex.getCheckedRadioButtonId() == R.id.rb_register_f ? "f" : "m");
            Intent intent = new Intent(this, RegisterActivity.class);
            // 获取用户计算后的结果
            intent.putExtra("RegisterInfo", info);
            startActivity(intent);

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
            String pathurl = null;
            if (requestCode == 1) {
                pathurl = mPhotoPath;
            } else {
                Uri uri = data.getData();
                if (!TextUtils.isEmpty(uri.getAuthority())) {
                    pathurl = getPathFromUri(uri);
                } else {
                    pathurl = uri.getPath();
                }
            }
            BitmapUtil.saveBitmap(BitmapUtil.getBitmap(pathurl), new File(avatarPath));
            MyUploadListener listener = new MyUploadListener();
            listener.setUserTag(avatarPath);
            PostRequest postRequest = OkGo.post(Config.FileUpload).params("file", new File(avatarPath));
            uploadManager.addTask(avatarPath, postRequest, listener);

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
        @Override
        public void onProgress(UploadInfo uploadInfo) {
            Log.e("MyUploadListener", "onProgress:" + uploadInfo.getTotalLength() + " " + uploadInfo.getUploadLength() + " " + uploadInfo.getProgress());
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
                        Glide.with(context).load(Config.BASE + urlPath).asBitmap().centerCrop().into(new BitmapImageViewTarget(iv_register_icon) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                iv_register_icon.setImageDrawable(circularBitmapDrawable);
                            }
                        });
                        try {
                            if (mPhotoFile.exists())
                                mPhotoFile.delete();
                        } catch (Exception e) {

                        }

                    }

                }
            } catch (Exception e) {
                F.e(e.toString());
            }
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
