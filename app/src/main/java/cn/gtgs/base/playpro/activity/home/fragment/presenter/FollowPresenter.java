package cn.gtgs.base.playpro.activity.home.fragment.presenter;


import com.gt.okgo.OkGo;
import com.gt.okgo.model.HttpParams;
import com.gt.okgo.request.GetRequest;

import java.util.ArrayList;

import cn.gtgs.base.playpro.activity.home.fragment.view.FollowDelegate;
import cn.gtgs.base.playpro.activity.home.model.Follow;
import cn.gtgs.base.playpro.http.Config;
import cn.gtgs.base.playpro.http.HttpMethods;
import cn.gtgs.base.playpro.http.Parsing;
import okhttp3.Response;
import rx.Subscriber;

/**
 * Created by gtgs on 2017/2/10.
 */

public class FollowPresenter implements IFollow {
    FollowDelegate delegate;

    public FollowPresenter(FollowDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public void initData() {
        getData();
    }

    private void getData() {

//        GetRequest request = OkGo.get("https://api.yequtv.cn/v1/regions/350500/popular_anchors?key=z45CasVgh8K3q6300g0d95VkK197291A");
        HttpParams params = HttpMethods.getInstance().getHttpParams();
        params.put("mbId", "2");
        GetRequest request = OkGo.get(Config.POST_ANCHOR_MEMBER_FAVLIST).params(params);
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
                    ArrayList<Follow> lists = (ArrayList<Follow>) Parsing.getInstance().ResponseToList2(response, Follow.class).dataList;
                    delegate.setData(lists);
                } catch (Exception e) {
                    e.printStackTrace();
                }

//                ArrayList<AnchorItem> lists = Parsing.getInstance().ResponseToList(response, AnchorItem.class);
            }
        });

    }

}
