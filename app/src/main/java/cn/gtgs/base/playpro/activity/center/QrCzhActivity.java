package cn.gtgs.base.playpro.activity.center;

import android.content.ContentResolver;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;

import butterknife.OnClick;
import cn.gtgs.base.playpro.R;
import cn.gtgs.base.playpro.activity.center.presenter.QrCzhPresenter;
import cn.gtgs.base.playpro.activity.center.view.QrCzhDelegate;
import cn.gtgs.base.playpro.base.presenter.ActivityPresenter;
import cn.gtgs.base.playpro.utils.BitmapUtil;
import cn.gtgs.base.playpro.utils.ToastUtil;

public class QrCzhActivity extends ActivityPresenter<QrCzhDelegate> {

    QrCzhPresenter presenter;

    String path1 = Environment.getExternalStorageDirectory().getPath() + "/icon1.png";
    String path2 = Environment.getExternalStorageDirectory().getPath()+ "/icon2.png";
//String sdcardPath = Environment.getExternalStorageDirectory().getPath();
    @Override
    protected void onInitPresenters() {

        presenter = new QrCzhPresenter(viewDelegate);
    }

    @Override
    protected void initData() {
        viewDelegate.init();
        presenter.getPic();
    }

    @Override
    protected Class<QrCzhDelegate> getDelegateClass() {
        return QrCzhDelegate.class;
    }

    @OnClick({R.id.img_topbar_back,R.id.tv_qr_save})
    public void Onclick(View v) {
        switch (v.getId()) {
            case R.id.img_topbar_back:
                this.finish();
                break;
                case R.id.tv_qr_save:
//                    File file = new File(path1);
//                    File file2 = new File(path2);
//                    BitmapUtil.saveBitmap(BitmapUtil.getViewBitmap(viewDelegate.getmImgQr()),file);
//                    BitmapUtil.saveBitmap(BitmapUtil.getViewBitmap(viewDelegate.getmImgQr2()),file2);
//                    ToastUtil.showToast("图片以保存到手机内部存储，请到相册中查找图片icon1.png ，icon2.png",this);
                    try {
                        ContentResolver cr = this.getContentResolver();
                        String    url  = MediaStore.Images.Media.insertImage(cr, BitmapUtil.getViewBitmap(viewDelegate.getmImgQr()), "icon1.png", "");
                        String    url2  = MediaStore.Images.Media.insertImage(cr, BitmapUtil.getViewBitmap(viewDelegate.getmImgQr2()), "icon1.png", "");
                        ToastUtil.showToast("图片已保存",this);
                    }
                    catch (Exception e){

                    }


//                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                    Uri uri = Uri.fromFile(file);
//                    intent.setData(uri);
//                    sendBroadcast(intent);
//                    Intent intent2 = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                    Uri uri2 = Uri.fromFile(file2);
//                    intent2.setData(uri);
//                    sendBroadcast(intent);
                break;
        }
    }


}
