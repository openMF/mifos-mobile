package org.mifos.mobilebanking.ui.fragments;

/*
 * Created by saksham on 24/July/2018
 */

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.models.guarantor.GuarantorPayload;
import org.mifos.mobilebanking.presenters.GuarantorDetailPresenter;
import org.mifos.mobilebanking.ui.activities.base.BaseActivity;
import org.mifos.mobilebanking.ui.enums.GuarantorState;
import org.mifos.mobilebanking.ui.fragments.base.BaseFragment;
import org.mifos.mobilebanking.ui.views.GuarantorDetailView;
import org.mifos.mobilebanking.utils.Constants;
import org.mifos.mobilebanking.utils.DateHelper;
import org.mifos.mobilebanking.utils.MaterialDialog;
import org.mifos.mobilebanking.utils.RxBus;
import org.mifos.mobilebanking.utils.RxEvent;
import org.mifos.mobilebanking.utils.Toaster;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class GuarantorDetailFragment extends BaseFragment implements GuarantorDetailView {

    @BindView(R.id.tv_first_name)
    TextView tvFirstName;

    @BindView(R.id.tv_last_name)
    TextView tvLastName;

    @BindView(R.id.tv_joined_date)
    TextView tvJoinedDate;

    @BindView(R.id.tv_guarantor_type)
    TextView tvGuarantorType;

    @BindView(R.id.tv_office_name)
    TextView tvOfficeName;

    @Inject
    GuarantorDetailPresenter presenter;

    View rootView;
    long loanId;
    long guarantorId;
    int index;
    GuarantorPayload payload;
    Disposable disposableUpdateGuarantor;
    boolean isFirstTime = true;

    public static GuarantorDetailFragment newInstance(int index, long loanId,
                                                      GuarantorPayload payload) {
        GuarantorDetailFragment fragment = new GuarantorDetailFragment();
        Bundle args = new Bundle();
        args.putLong(Constants.LOAN_ID, loanId);
        args.putParcelable(Constants.GUARANTOR_DETAILS, payload);
        args.putInt(Constants.INDEX, index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            loanId = getArguments().getLong(Constants.LOAN_ID);
            payload = getArguments().getParcelable(Constants.GUARANTOR_DETAILS);
            index = getArguments().getInt(Constants.INDEX);
            guarantorId = payload.getId();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_guarantor_detail, container, false);
        setToolbarTitle(getString(R.string.guarantor_details));
        setHasOptionsMenu(true);
        ButterKnife.bind(this, rootView);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);

        if (isFirstTime) {
            isFirstTime = false;
            setUpRxBus();
        }

        presenter.attachView(this);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tvFirstName.setText(payload.getFirstname());
        tvLastName.setText(payload.getLastname());
        tvGuarantorType.setText(payload.getGuarantorType().getValue());
        tvJoinedDate.setText(DateHelper.getDateAsString(payload.getJoinedDate()));
        tvOfficeName.setText(payload.getOfficeName());
    }

    private void setUpRxBus() {
        disposableUpdateGuarantor = RxBus.listen(RxEvent.UpdateGuarantorEvent.class)
                .subscribe(new Consumer<RxEvent.UpdateGuarantorEvent>() {
                    @Override
                    public void accept(RxEvent.UpdateGuarantorEvent updateGuarantorEvent)
                            throws Exception {
                        payload.setFirstname(updateGuarantorEvent.getPayload().getFirstName());
                        payload.setLastname(updateGuarantorEvent.getPayload().getLastName());
                        payload.setGuarantorType(updateGuarantorEvent.getPayload()
                                .getGuarantorType());
                        payload.setOfficeName(updateGuarantorEvent.getPayload().getOfficeName());
                    }
                });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_guarantor, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.menu_delete_guarantor:
                new MaterialDialog.Builder()
                        .init(getContext())
                        .setTitle(getString(R.string.delete_guarantor))
                        .setMessage(getString(R.string.dialog_are_you_sure_that_you_want_to_string,
                                getString(R.string.delete_guarantor)))
                        .setPositiveButton(getString(R.string.yes),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        presenter.deleteGuarantor(loanId, guarantorId);
                                    }
                                })
                        .setNegativeButton(getString(R.string.cancel))
                        .createMaterialDialog()
                        .show();
                break;
            case R.id.menu_update_guarantor:
                ((BaseActivity) getActivity()).replaceFragment(AddGuarantorFragment
                                .newInstance(index, GuarantorState.UPDATE, payload, loanId),
                        true, R.id.container);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void guarantorDeletedSuccessfully(String message) {
        getActivity().getSupportFragmentManager().popBackStack();
        RxBus.publish(new RxEvent.DeleteGuarantorEvent(index));
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showError(String message) {
        Toaster.show(rootView, message);
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
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        if (!disposableUpdateGuarantor.isDisposed())
            disposableUpdateGuarantor.dispose();
        hideProgressBar();
    }
}
