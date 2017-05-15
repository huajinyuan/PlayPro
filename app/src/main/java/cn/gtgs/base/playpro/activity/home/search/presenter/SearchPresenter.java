package cn.gtgs.base.playpro.activity.home.search.presenter;


import com.gt.okgo.OkGo;
import com.gt.okgo.model.HttpParams;
import com.gt.okgo.request.PostRequest;

import java.util.ArrayList;

import cn.gtgs.base.playpro.activity.home.model.Follow;
import cn.gtgs.base.playpro.activity.home.search.view.SearchDelegate;
import cn.gtgs.base.playpro.activity.login.model.UserInfo;
import cn.gtgs.base.playpro.http.Config;
import cn.gtgs.base.playpro.http.HttpMethods;
import cn.gtgs.base.playpro.http.Parsing;
import cn.gtgs.base.playpro.utils.ACache;
import cn.gtgs.base.playpro.utils.ACacheKey;
import cn.gtgs.base.playpro.utils.F;
import cn.gtgs.base.playpro.utils.ToastUtil;
import okhttp3.Response;
import rx.Subscriber;

/**
 * Created by  on 2017/2/10.
 */

public class SearchPresenter implements ISearch {
    SearchDelegate delegate;
    ISearchItemListenser listener;
    UserInfo info;
    ACache aCache;

    public SearchPresenter(SearchDelegate delegate, ISearchItemListenser listener) {
        this.delegate = delegate;
        aCache = ACache.get(delegate.getActivity());
        Follow fl = (Follow) aCache.getAsObject(ACacheKey.CURRENT_ACCOUNT);
        info = fl.getMember();
        this.listener = listener;

    }

    @Override
    public void search() {

        String content = delegate.getSearchContent();
//        if (!StringUtils.isEmpty(content)) {
            delegate.getmBtnSearch().setClickable(false);
            HttpParams params = new HttpParams();
            params.put("searchKey", content);
            PostRequest request = OkGo.post(Config.POST_SEARCH).params(params);
            HttpMethods.getInstance().doPost(request, false).subscribe(new Subscriber<Response>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    ToastUtil.showToast("请求失败，请检查网络",delegate.getActivity());
                    delegate.hideProgressbar();
                    delegate.getmBtnSearch().setClickable(true);
                }

                @Override
                public void onNext(Response response) {
                    delegate.hideProgressbar();
                    delegate.getmBtnSearch().setClickable(true);
                    try {
                        F.e(response.body().toString());
                        ArrayList<Follow> lists = (ArrayList<Follow>) Parsing.getInstance().ResponseToList3(response, Follow.class).dataList;
                        delegate.setData(lists, listener);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
//        }


    }
}
