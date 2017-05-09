package cn.gtgs.base.playpro.activity.center;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

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

import butterknife.OnClick;
import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.activity.center.presenter.ApprovePresenter;
import cn.gtgs.base.playpro.activity.center.presenter.IApprove;
import cn.gtgs.base.playpro.activity.center.view.AnchorApproveDelegate;
import cn.gtgs.base.playpro.base.presenter.ActivityPresenter;
import cn.gtgs.base.playpro.http.Config;
import cn.gtgs.base.playpro.utils.BitmapUtil;
import cn.gtgs.base.playpro.utils.F;
import okhttp3.Response;

public class AnchorApproveActivity extends ActivityPresenter<AnchorApproveDelegate> {
    IApprove presenter;
    AlertDialog mydialog;
    String mPhotoPath;
    File mPhotoFile;
    String sdcardPath = Environment.getExternalStorageDirectory().getPath();
    String avatarPath;
    String urlPath;
    Context context;
    private UploadManager uploadManager;

    @Override
    protected void onInitPresenters() {
        presenter = new ApprovePresenter(viewDelegate);
    }

    @Override
    protected void initData() {
        uploadManager = UploadManager.getInstance();
        context = this;
        avatarPath = getFilesDir().getPath() + "/icon.png";
        viewDelegate.setmTvTitle("主播认证");
    }

    @Override
    protected Class<AnchorApproveDelegate> getDelegateClass() {
        return AnchorApproveDelegate.class;
    }

    @OnClick({R.id.tv_approve_submit, R.id.img_topbar_back, R.id.img_approve_icon})
    public void Onclick(View v) {
        switch (v.getId()) {
            case R.id.tv_approve_submit:
                presenter.Submit();
                break;
            case R.id.img_topbar_back:
                this.finish();
                break;
            case R.id.img_approve_icon:
                showPhotodialog();
                break;
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
                        Glide.with(context).load(Config.BASE + urlPath).asBitmap().centerCrop().into(new BitmapImageViewTarget(viewDelegate.getIcon()) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                viewDelegate.getIcon().setImageDrawable(circularBitmapDrawable);
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
