package org.mifos.mobilebanking.ui.adapters;

/*
 * Created by saksham on 16/June/2018
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

import org.mifos.mobilebanking.R;
import org.mifos.mobilebanking.models.survey.Survey;
import org.mifos.mobilebanking.ui.fragments.SurveyQuestionFragment;

public class SurveyQuestionAdapter extends AbstractFragmentStepAdapter {

    Context context;
    Survey surveys;

    public SurveyQuestionAdapter(@NonNull FragmentManager fm, @NonNull Context context,
                                 Survey surveys) {
        super(fm, context);
        this.context = context;
        this.surveys = surveys;
    }

    @Override
    public Step createStep(int position) {
        return SurveyQuestionFragment.newInstance(surveys.getQuestionDatas().get(position));
    }

    @Override
    public int getCount() {
        return surveys.getQuestionDatas().size();
    }

    @NonNull
    @Override
    public StepViewModel getViewModel(int position) {
        StepViewModel.Builder builder = new StepViewModel.Builder(context);

        if (position == (getCount() - 1)) {
            builder.setEndButtonLabel(context.getString(R.string.submit));
        }
        return builder.create();
    }

}