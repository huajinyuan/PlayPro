package cn.gtgs.base.playpro.activity.home.fragment.presenter;


import com.gt.okgo.OkGo;
import com.gt.okgo.request.GetRequest;

import java.util.ArrayList;

import cn.gtgs.base.playpro.activity.home.fragment.view.StarDelegate;
import cn.gtgs.base.playpro.activity.home.model.Follow;
import cn.gtgs.base.playpro.http.Config;
import cn.gtgs.base.playpro.http.HttpMethods;
import cn.gtgs.base.playpro.http.Parsing;
import cn.gtgs.base.playpro.utils.F;
import okhttp3.Response;
import rx.Subscriber;

/**
 * Created by gtgs on 2017/2/10.
 */

public class StarPresenter implements IRecommented {
    StarDelegate delegate;

    public StarPresenter(StarDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public void initData() {
        getData();
    }

    private void getData() {

        GetRequest request = OkGo.get(Config.POST_ANCHOR_TOP);
//        GetRequest request = OkGo.get("https://api.yequtv.cn/v1/regions/350500/popular_anchors?key=z45CasVgh8K3q6300g0d95VkK197291A");
        HttpMethods.getInstance().doGet(request, false).subscribe(new Subscriber<Response>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Response response) {

                try {
                    F.e(response.body().toString());
                    ArrayList<Follow> lists = (ArrayList<Follow>) Parsing.getInstance().ResponseToList3(response, Follow.class).dataList;
                    delegate.setData(lists);
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                delegate.setData(lists);
            }
        });

    }

}
