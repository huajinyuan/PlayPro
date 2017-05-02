package cn.gtgs.base.playpro.activity.home.fragment.presenter;


import com.gt.okgo.OkGo;
import com.gt.okgo.request.GetRequest;

import java.util.ArrayList;

import cn.gtgs.base.playpro.activity.home.fragment.view.TyrantsDelegate;
import cn.gtgs.base.playpro.activity.home.model.AnchorItem;
import cn.gtgs.base.playpro.http.HttpMethods;
import cn.gtgs.base.playpro.http.Parsing;
import okhttp3.Response;
import rx.Subscriber;

/**
 * Created by gtgs on 2017/2/10.
 */

public class TyrantsPresenter implements IRecommented {
    TyrantsDelegate delegate;

    public TyrantsPresenter(TyrantsDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public void initData() {
        getData();
    }

    private void getData() {

        GetRequest request = OkGo.get("https://api.yequtv.cn/v1/regions/350500/popular_anchors?key=z45CasVgh8K3q6300g0d95VkK197291A");
        HttpMethods.getInstance().doGet(request, false).subscribe(new Subscriber<Response>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Response response) {

//                        F.e(response.body().toString());
                try {
                    ArrayList<AnchorItem> list = Parsing.getInstance().ResponseToList(response, AnchorItem.class);
//                    HttpBase<ArrayList<AnchorItem>> lists = Parsing.getInstance().ResponseToList(response, AnchorItem.class);
                    delegate.setData(list);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }

}
