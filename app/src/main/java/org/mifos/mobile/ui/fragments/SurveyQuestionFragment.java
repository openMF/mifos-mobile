package org.mifos.mobile.ui.fragments;

/*
 * Created by saksham on 16/June/2018
 */

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import org.mifos.mobile.survey.QuestionDatas;
import org.mifos.mobile.R;
import org.mifos.mobile.ui.fragments.base.BaseFragment;
import org.mifos.mobile.ui.views.SurveyQuestionView;
import org.mifos.mobile.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SurveyQuestionFragment extends BaseFragment implements Step,
        RadioGroup.OnCheckedChangeListener, SurveyQuestionView {

    @BindView(R.id.tv_survey_question)
    TextView tvSurveyQuestion;
    
    @BindView(R.id.rg_options)
    RadioGroup rgOptions;

    View rootView;
    private QuestionDatas questionDatas;
    private int selectedOption = -1;

    public static SurveyQuestionFragment newInstance(QuestionDatas question) {
        SurveyQuestionFragment fragment = new SurveyQuestionFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.SURVEY_QUESTION, question);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        questionDatas = (QuestionDatas) getArguments().getSerializable(Constants.SURVEY_QUESTION);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_survey_question, container, false);
        ButterKnife.bind(this, rootView);

        showSurveyQuestionInterface();
        return rootView;
    }

    public void showSurveyQuestionInterface() {
        tvSurveyQuestion.setText(questionDatas.getText());
        rgOptions.setOnCheckedChangeListener(this);
        addRadioButton();
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        if (selectedOption != -1) {
            return null;
        }
        return new VerificationError(getString(R.string.error_answer_question));
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {
    }

    public void addRadioButton() {

        for (int i = 0; i < questionDatas.getResponseDatas().size(); i++) {
            RadioButton radioButton = new RadioButton(getActivity());
            radioButton.setId(i);
            radioButton.setText(questionDatas.getResponseDatas().get(i).getText());
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            rgOptions.addView(radioButton, params);
        }
    }

    public int getSelectedOption() {
        return selectedOption;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        selectedOption = checkedId;
    }

    @Override
    public void showError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }
}
