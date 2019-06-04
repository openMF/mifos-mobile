package org.mifos.mobile.ui.adapters;

/*
 * Created by saksham on 16/June/2018
 */

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

import org.mifos.mobile.survey.Survey;
import org.mifos.mobile.R;
import org.mifos.mobile.ui.fragments.SurveyQuestionFragment;

public class SurveyQuestionAdapter extends AbstractFragmentStepAdapter {

    Context context;
    private Survey surveys;

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