package org.mifos.mobilebanking.ui.fragments;

/*
 * Created by saksham on 23/July/2018
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.therajanmaurya.sweeterror.SweetUIErrorHandler;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.models.guarantor.GuarantorPayload;
import org.mifos.mobilebanking.presenters.GuarantorListPresenter;
import org.mifos.mobilebanking.ui.activities.base.BaseActivity;
import org.mifos.mobilebanking.ui.adapters.GuarantorListAdapter;
import org.mifos.mobilebanking.ui.enums.GuarantorState;
import org.mifos.mobilebanking.ui.fragments.base.BaseFragment;
import org.mifos.mobilebanking.ui.views.GuarantorListView;
import org.mifos.mobilebanking.utils.Constants;
import org.mifos.mobilebanking.utils.DateHelper;
import org.mifos.mobilebanking.utils.RxBus;
import org.mifos.mobilebanking.utils.RxEvent;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class GuarantorListFragment extends BaseFragment implements GuarantorListView {

    @BindView(R.id.layout_error)
    View layoutError;

    @BindView(R.id.ll_container)
    LinearLayout llContainer;

    @BindView(R.id.rv_guarantors)
    RecyclerView rvGuarantors;

    @Inject
    GuarantorListPresenter presenter;

    GuarantorListAdapter adapter;

    View rootView;
    long loanId;
    SweetUIErrorHandler sweetUIErrorHandler;
    List<GuarantorPayload> list;
    Disposable disposableAddGuarantor, disposableDeleteGuarantor;

    public static GuarantorListFragment newInstance(long loanId) {
        GuarantorListFragment fragment = new GuarantorListFragment();
        Bundle args = new Bundle();
        args.putLong(Constants.LOAN_ID, loanId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            loanId = getArguments().getLong(Constants.LOAN_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_guarantor_list, container, false);
        ButterKnife.bind(this, rootView);
        setToolbarTitle(getString(R.string.view_guarantor));
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        presenter.attachView(this);
        if (list == null) {
            presenter.getGuarantorList(loanId);
            adapter = new GuarantorListAdapter(getContext(),
                    new GuarantorListAdapter.OnClickListener() {
                        @Override
                        public void setOnClickListener(int position) {
                            ((BaseActivity) getActivity()).replaceFragment(GuarantorDetailFragment
                                    .newInstance(position, loanId, list.get(position)),
                                    true, R.id.container);
                        }
                    });
            setUpRxBus();
        }
        sweetUIErrorHandler = new SweetUIErrorHandler(getActivity(), rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (list != null && list.size() == 0) {
            sweetUIErrorHandler.showSweetCustomErrorUI(getString(R.string.no_guarantors),
                    getString(R.string.tap_to_add_guarantor),
                    R.drawable.ic_person_black_24dp, llContainer, layoutError);
        }
        rvGuarantors.setAdapter(adapter);
        rvGuarantors.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void setUpRxBus() {
        disposableAddGuarantor = RxBus.listen(RxEvent.AddGuarantorEvent.class)
                .subscribe(new Consumer<RxEvent.AddGuarantorEvent>() {
                    @Override
                    public void accept(RxEvent.AddGuarantorEvent event)
                            throws Exception {
                        //TODO wrong guarantor id is assigned, although it wont affect the working
                        list.add(event.getIndex(), new GuarantorPayload(list.size(),
                                event.getPayload().getOfficeName(),
                                event.getPayload().getLastName(),
                                event.getPayload().getGuarantorType(),
                                event.getPayload().getFirstName(),
                                DateHelper.getCurrentDate("yyyy-MM-dd", "-"),
                                loanId));
                        adapter.setGuarantorList(list);
                    }
                });
        disposableDeleteGuarantor = RxBus.listen(RxEvent.DeleteGuarantorEvent.class)
                .subscribe(new Consumer<RxEvent.DeleteGuarantorEvent>() {
                    @Override
                    public void accept(RxEvent.DeleteGuarantorEvent deleteGuarantorEvent)
                            throws Exception {
                        int index = deleteGuarantorEvent.getIndex();
                        list.remove(index);
                        adapter.setGuarantorList(list);
                    }
                });
    }

    @OnClick(R.id.fab_add_loan_guarantor)
    void addGuarantor() {
        ((BaseActivity) getActivity()).replaceFragment(AddGuarantorFragment
                .newInstance(0, GuarantorState.CREATE, null, loanId), true, R.id.container);
    }

    @Override
    public void showProgress() {
        showProgressBar();
    }

    @Override
    public void hideProgress() {
        hideProgressBar();

    }

    @Override
    public void showGuarantorListSuccessfully(final List<GuarantorPayload> list) {
        this.list = list;
        if (list.size() == 0) {
            sweetUIErrorHandler.showSweetCustomErrorUI(getString(R.string.no_guarantors),
                    getString(R.string.tap_to_add_guarantor),
                    R.drawable.ic_person_black_24dp, llContainer, layoutError);
        } else {
            adapter.setGuarantorList(list);
        }
    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        if (!disposableAddGuarantor.isDisposed())
            disposableAddGuarantor.dispose();
        if (!disposableDeleteGuarantor.isDisposed())
            disposableDeleteGuarantor.dispose();
        hideProgress();
    }
}
