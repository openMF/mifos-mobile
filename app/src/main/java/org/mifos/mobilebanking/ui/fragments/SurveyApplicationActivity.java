package org.mifos.mobilebanking.ui.fragments;

/*
 * Created by saksham on 20/May/2018
 */

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.models.SurveyQuestion;
import org.mifos.mobilebanking.ui.activities.base.BaseActivity;
import org.mifos.mobilebanking.ui.adapters.SurveyApplicationAdapter;
import org.mifos.mobilebanking.ui.views.SurveyApplicationView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SurveyApplicationActivity extends BaseActivity implements SurveyApplicationView {

    @BindView(R.id.vp_survey_questions)
    ViewPager vpSurveyQuestions;

    View rootView;

    List<SurveyQuestion> list;

    @Inject
    SurveyApplicationAdapter surveyApplicationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_application);
        ButterKnife.bind(this);

        getActivityComponent().inject(this);

        showBackButton();
        setToolbarTitle(getString(R.string.survey));
        list = getQuestions();
        surveyApplicationAdapter.setSurveyQuestions(list);
        vpSurveyQuestions.setAdapter(surveyApplicationAdapter);
    }

    @OnClick(R.id.btn_submit_survey)
    void submitSurvey() {
        for (int i = 0; i < getQuestions().size(); i++) {
            SurveyQuestion surveyQuestion = list.get(i);
            if (surveyQuestion.getSelectedOption() == -1) {
                Toast.makeText(this, getString(R.string.answer_all_survey_questions),
                        Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Toast.makeText(this, getString(R.string.submitted), Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    //mocking survey data
    public List<SurveyQuestion> getQuestions() {
        List<SurveyQuestion> al = new ArrayList<>();

        ArrayList<String> options1 = new ArrayList<>();
        options1.add("a");
        options1.add("b");
        options1.add("c");
        SurveyQuestion q1 = new SurveyQuestion("Question 1 comes here", options1);

        ArrayList<String> options2 = new ArrayList<>();
        options2.add("a");
        options2.add("b");
        options2.add("c");
        options2.add("d");
        options2.add("e");
        SurveyQuestion q2 = new SurveyQuestion("Question 2 comes here", options2);

        ArrayList<String> options3 = new ArrayList<>();
        options3.add("a");
        options3.add("b");
        options3.add("c");
        options3.add("d");
        SurveyQuestion q3 = new SurveyQuestion("Question 3 comes here", options3);

        ArrayList<String> options4 = new ArrayList<>();
        options4.add("a");
        options4.add("b");
        options4.add("c");
        options4.add("d");
        options4.add("d");
        SurveyQuestion q4 = new SurveyQuestion("Question 4 comes here", options4);

        ArrayList<String> options5 = new ArrayList<>();
        options5.add("a");
        options5.add("d");
        options5.add("b");
        options5.add("c");
        SurveyQuestion q5 = new SurveyQuestion("Question 5 comes here", options5);

        ArrayList<String> options6 = new ArrayList<>();
        options6.add("a");
        options6.add("b");
        options6.add("c");
        SurveyQuestion q6 = new SurveyQuestion("Question 6 comes here", options6);

        ArrayList<String> options7 = new ArrayList<>();
        options7.add("a");
        options7.add("b");
        options7.add("c");
        options7.add("d");
        options7.add("e");
        SurveyQuestion q7 = new SurveyQuestion("Question 7 comes here", options7);

        al.add(q1); al.add(q2); al.add(q3);
        al.add(q4); al.add(q5); al.add(q6);
        al.add(q7);
        return al;
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }
}
