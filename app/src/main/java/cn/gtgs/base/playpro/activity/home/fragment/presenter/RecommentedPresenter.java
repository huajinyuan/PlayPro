package cn.gtgs.base.playpro.activity.home.fragment.presenter;


import com.gt.okgo.OkGo;
import com.gt.okgo.model.HttpParams;
import com.gt.okgo.request.PostRequest;

import java.util.ArrayList;

import cn.gtgs.base.playpro.activity.home.fragment.view.RecommentedDelegate;
import cn.gtgs.base.playpro.activity.home.model.Follow;
import cn.gtgs.base.playpro.http.BaseList;
import cn.gtgs.base.playpro.http.Config;
import cn.gtgs.base.playpro.http.HttpMethods;
import cn.gtgs.base.playpro.http.Parsing;
import cn.gtgs.base.playpro.utils.F;
import okhttp3.Response;
import rx.Subscriber;

/**
 * Created by gtgs on 2017/2/10.
 */

public class RecommentedPresenter implements IRecommented {
    RecommentedDelegate delegate;

    public RecommentedPresenter(RecommentedDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public void initData() {
        getData();
    }

    @Override
    public void doFollow(String anId) {

    }

    private void getData() {

//        GetRequest request = OkGo.get("https://api.yequtv.cn/v1/regions/350500/popular_anchors?key=z45CasVgh8K3q6300g0d95VkK197291A");

        HttpParams params = new HttpParams();
        params.put("page","1");
        params.put("count","100");
        params.put("isRecommend","1");

//        PostRequest request = OkGo.post(Config.POST_ANCHOR_LIST).params(params);
        PostRequest request = OkGo.post(Config.POST_ANCHOR_LIST_LIVE).params(params);
        HttpMethods.getInstance().doPost(request, false).subscribe(new Subscriber<Response>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Response response) {

//                        F.e(response.body().toString());
//                try {
//                    ArrayList<AnchorItem> lists = Parsing.getInstance().ResponseToList(response, AnchorItem.class);
//                    delegate.setData(lists);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

//                ArrayList<AnchorItem> lists = Parsing.getInstance().ResponseToList(response, AnchorItem.class);
//
                BaseList<Follow> bList = Parsing.getInstance().ResponseToList2(response,Follow.class);
                F.e("-----------------------------"+bList.toString());
                ArrayList<Follow> follows = (ArrayList<Follow>) bList.getDataList();
                F.e("-----------------------------"+follows.toString());
                delegate.setData(follows);
            }
        });

    }

}
