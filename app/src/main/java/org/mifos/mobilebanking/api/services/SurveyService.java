package org.mifos.mobilebanking.api.services;

/*
 * Created by saksham on 17/June/2018
 */

import org.mifos.mobilebanking.models.survey.SubmitSurveyPayload;
import org.mifos.mobilebanking.models.survey.Survey;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface SurveyService {

    @GET("surveys")
    Observable<List<Survey>> getSurveyQuestions();

    @POST("surveys/scorecards/{clientId}")
    Observable<ResponseBody> submitSurvey(@Path("clientId") long clientId,
                                          @Body SubmitSurveyPayload submitSurveyPayload);
}
