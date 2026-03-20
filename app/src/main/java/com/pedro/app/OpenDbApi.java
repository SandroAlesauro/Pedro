package com.pedro.app;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenDbApi {

    @GET("api.php")
    Call<ApiResponse> getQuestions(
        @Query("amount") int amount,
        @Query("category") Integer category,
        @Query("difficulty") String difficulty,
        @Query("type") String type
    );

    class ApiResponse {
        @SerializedName("response_code")
        public int responseCode;
        @SerializedName("results")
        public List<ApiQuestion> results;
    }

    class ApiQuestion {
        @SerializedName("category")
        public String category;
        @SerializedName("type")
        public String type;
        @SerializedName("difficulty")
        public String difficulty;
        @SerializedName("question")
        public String question;
        @SerializedName("correct_answer")
        public String correctAnswer;
        @SerializedName("incorrect_answers")
        public List<String> incorrectAnswers;
    }
}
