package org.mifos.mobilebanking.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.models.SurveyQuestion;
import org.mifos.mobilebanking.ui.fragments.base.BaseFragment;
import org.mifos.mobilebanking.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
 * Created by saksham on 23/May/2018
 */

public class SurveyQuestionFragment extends BaseFragment
        implements RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.tv_question_number)
    TextView tvQuestionNumber;

    @BindView(R.id.tv_question)
    TextView tvQuestion;

    @BindView(R.id.rg_option)
    RadioGroup rg;

    View rootView;

    SurveyQuestion question;
    int totalQuestions;

    public static SurveyQuestionFragment newInstance(SurveyQuestion question, int totalQuestions) {
        SurveyQuestionFragment surveyQuestionFragment = new SurveyQuestionFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.SURVEY_TOTAL_QUESTION, totalQuestions);
        args.putSerializable(Constants.SURVEY_QUESTION, question);
        surveyQuestionFragment.setArguments(args);
        return surveyQuestionFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            question = (SurveyQuestion) getArguments().getSerializable(Constants.SURVEY_QUESTION);
            totalQuestions = getArguments().getInt(Constants.SURVEY_TOTAL_QUESTION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.item_survey_question, container, false);
        ButterKnife.bind(this, rootView);
        showUserInterface();

        return rootView;
    }

    public void showUserInterface() {
        tvQuestionNumber.setText(question.getQuestionNumber() + "/" + totalQuestions);
        tvQuestion.setText(question.getQuestion());
        rg.setOnCheckedChangeListener(this);
        addRadioButtons();
    }

    //this method dynamically adds RadioButton to RadioGroup
    public void addRadioButtons() {
        int counter = 0;
        for (String option : question.getOptions()) {
            RadioButton rb = new RadioButton(getContext());
            rb.setId(counter);
            rb.setText(option);
            rg.addView(rb);
            counter++;
        }
        if (question.getSelectedOption() != -1) {
            rg.check(question.getSelectedOption());
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        question.setSelectedOption(checkedId);
    }
}
