package org.mifos.mobilebanking.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.mifos.mobilebanking.models.SurveyQuestion;
import org.mifos.mobilebanking.ui.fragments.SurveyQuestionFragment;

import java.util.List;

import javax.inject.Inject;

/*
 * Created by saksham on 20/May/2018
 */

public class SurveyApplicationAdapter extends FragmentStatePagerAdapter {

    List<SurveyQuestion> surveyQuestions;

    @Inject
    public SurveyApplicationAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        surveyQuestions.get(position).setQuestionNumber(position + 1);
        return SurveyQuestionFragment.newInstance(surveyQuestions.get(position), getCount());
    }

    @Override
    public int getCount() {
        return surveyQuestions.size();
    }

    public void setSurveyQuestions(List<SurveyQuestion> surveyQuestions) {
        this.surveyQuestions = surveyQuestions;
    }

}