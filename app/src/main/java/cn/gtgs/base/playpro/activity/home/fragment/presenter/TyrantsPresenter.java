package cn.gtgs.base.playpro.activity.home.fragment.presenter;


import com.gt.okgo.OkGo;
import com.gt.okgo.request.GetRequest;

import java.util.ArrayList;

import cn.gtgs.base.playpro.activity.home.fragment.view.TyrantsDelegate;
import cn.gtgs.base.playpro.activity.login.model.UserInfo;
import cn.gtgs.base.playpro.http.Config;
import cn.gtgs.base.playpro.http.HttpMethods;
import cn.gtgs.base.playpro.http.Parsing;
import okhttp3.Response;
import rx.Subscriber;

/**
 * Created by gtgs on 2017/2/10.
 */

public class TyrantsPresenter implements IRecommented {
    TyrantsDelegate delegate;
    IFollowItemListener listener;

    public TyrantsPresenter(TyrantsDelegate delegate, IFollowItemListener listener) {
        this.delegate = delegate;
        this.listener = listener;
    }


    @Override
    public void initData() {
        getData();
    }

    @Override
    public void doFollow(String anId) {

    }

    private void getData() {

        GetRequest request = OkGo.get(Config.POST_MEMBER_TOP);
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

//                        F.e(response.body().toString());
                try {
                    ArrayList<UserInfo> list = (ArrayList<UserInfo>) Parsing.getInstance().ResponseToList3(response, UserInfo.class).dataList;
//                    HttpBase<ArrayList<AnchorItem>> lists = Parsing.getInstance().ResponseToList(response, AnchorItem.class);
                    delegate.setData(list, listener);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }

}
