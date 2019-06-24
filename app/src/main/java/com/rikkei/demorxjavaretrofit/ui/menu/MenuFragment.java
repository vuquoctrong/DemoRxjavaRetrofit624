package com.rikkei.demorxjavaretrofit.ui.menu;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.rikkei.demorxjavaretrofit.R;
import com.rikkei.demorxjavaretrofit.adapter.ItemAdapter;
import com.rikkei.demorxjavaretrofit.data.network.SOService;
import com.rikkei.demorxjavaretrofit.data.network.model.Item;
import com.rikkei.demorxjavaretrofit.data.network.model.SOAnswersResponse;
import com.rikkei.demorxjavaretrofit.ui.base.BaseFragment;
import com.rikkei.demorxjavaretrofit.util.ApiUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MenuFragment extends BaseFragment {
    public static final String TAG = MenuFragment.class.toString();
    private SOService soService;
    private CompositeDisposable disposable = new CompositeDisposable();
    private ItemAdapter adapter;
    private List<Item> itemList;

    @BindView(R.id.rcitem)
    RecyclerView rcitem;

    @Override
    protected int layoutRes() {
        return R.layout.fragment_menu;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void initView() {

        soService = ApiUtils.getSOService();
        itemList = new ArrayList<>();
        adapter = new ItemAdapter();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rcitem.setLayoutManager(mLayoutManager);
        rcitem.setItemAnimator(new DefaultItemAnimator());
        rcitem.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        rcitem.setAdapter(adapter);
        fetchAllNotes();

    }

    @Override
    protected CompositeDisposable dispose() {
        return disposable;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void fetchAllNotes() {
        disposable.add(
                soService.fetchAllNotes()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<SOAnswersResponse>() {
                            @Override
                            public void onSuccess(SOAnswersResponse soAnswersResponse) {
                                itemList.clear();
                                adapter.setNotes(soAnswersResponse.getItems());
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, "onError: " + e.getMessage());

                            }
                        })
        );
    }

}
