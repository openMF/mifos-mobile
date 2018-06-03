package org.mifos.mobilebanking.api.services;

/*
 * Created by saksham on 02/June/2018
 */


import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

public interface ReportService {

    @Streaming
    @GET("/{reportName}")
    Observable<Response<ResponseBody>> downloadReport(@Path("reportName") String reportName,
                                                      @Query("id") String id,
                                                      @Query("export") String export);

    @GET
    Observable<List<String>> getReportList();
}
