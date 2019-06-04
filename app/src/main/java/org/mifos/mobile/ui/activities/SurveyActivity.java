package org.mifos.mobile.ui.activities;

/*
 * Created by saksham on 16/June/2018
 */

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import org.mifos.mobile.R;
import org.mifos.mobile.api.DataManager;
import org.mifos.mobile.api.local.PreferencesHelper;
import org.mifos.mobile.survey.ScorecardValues;
import org.mifos.mobile.survey.SubmitSurveyPayload;
import org.mifos.mobile.survey.Survey;
import org.mifos.mobile.presenters.SurveyPresenter;
import org.mifos.mobile.ui.activities.base.BaseActivity;
import org.mifos.mobile.ui.adapters.SurveyQuestionAdapter;
import org.mifos.mobile.ui.fragments.SurveyQuestionFragment;
import org.mifos.mobile.ui.views.SurveyView;
import org.mifos.mobile.utils.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SurveyActivity extends BaseActivity implements StepperLayout.StepperListener,
        SurveyView {

    @BindView(R.id.stepper_layout_survey)
    StepperLayout stepperLayout;

    SurveyQuestionAdapter surveyQuestionAdapter;

    @Inject
    DataManager dataManager;

    @Inject
    SurveyPresenter presenter;

    @Inject
    PreferencesHelper preferencesHelper;

    Survey survey;
    List<Integer> optionSelected = new ArrayList<>();
    SubmitSurveyPayload payload;
    List<ScorecardValues> scorecardValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        ButterKnife.bind(this);
        setToolbarTitle(getString(R.string.survey));
        showBackButton();
        getActivityComponent().inject(this);
        presenter.attachView(this);
        presenter.loadSurveyQuestion();

        stepperLayout.setListener(this);
    }

    @Override
    public void onCompleted(View completeButton) {
        new MaterialDialog.Builder().init(this)
                .setMessage(getString(R.string.dialog_submit_survey))
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < survey.getQuestionDatas().size(); i++) {
                            optionSelected.add(((SurveyQuestionFragment) surveyQuestionAdapter
                                    .findStep(i)).getSelectedOption());
                        }
                        presenter.submitSurvey(getSubmitSurveyPayload());
                    }
                }).setNegativeButton(getString(R.string.cancel)).createMaterialDialog().show();
    }

    public SubmitSurveyPayload getSubmitSurveyPayload() {
        payload = new SubmitSurveyPayload();

        payload.setId(String.valueOf(Math.round(Math.random() * 100)));
        payload.setClientId(String.valueOf(preferencesHelper.getClientId()));

        payload.setUserId(String.valueOf(preferencesHelper.getUserId()));
        payload.setUsername(preferencesHelper.getUserName());

        payload.setScorecardValues(getScorecardValues());

        payload.setSurveyId(survey.getId());
        payload.setSurveyName(survey.getName());

        return payload;
    }

    public List<ScorecardValues> getScorecardValues() {
        scorecardValues = new ArrayList<>();
        for (int i = 0; i < survey.getQuestionDatas().size(); i++) {
            ScorecardValues item = new ScorecardValues();

            item.setCreatedOn(String.valueOf(System.currentTimeMillis()));
            item.setQuestionId(survey.getQuestionDatas().get(i).getId());
            item.setResponseId(survey.getQuestionDatas().get(i).getResponseDatas()
                    .get(optionSelected.get(i)).getId());

            item.setValue(String.valueOf(Math.round(Math.random() * 10)));
            scorecardValues.add(item);
        }
        return scorecardValues;
    }

    @Override
    public void onError(VerificationError verificationError) {
        showError(verificationError.getErrorMessage());
    }

    @Override
    public void onStepSelected(int newStepPosition) {

    }

    @Override
    public void onReturn() {
    }

    @Override
    public void showSurvey(Survey list) {
        this.survey = list;
        surveyQuestionAdapter = new SurveyQuestionAdapter(getSupportFragmentManager(),
                this, this.survey);
        stepperLayout.setAdapter(surveyQuestionAdapter);
    }

    @Override
    public void showError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        showProgressDialog(getString(R.string.progress_message_loading));
    }

    @Override
    public void hideProgress() {
        hideProgressDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
