package com.example.lab7;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface APIService {
    @GET("/positions.json/")
    Call<List<Todo>> getTodos();
    @GET("positions.json?")
    Call<List<Todo>> getJobsFromSearch(@Query("search") String search);
    @GET("positions.json?")
    Call<List<Todo>> getFilteredJobs(@Query("description") String description,
                                    @Query("location") String location,
                                    @Query("full_time") boolean type);

//    @GET("todos/")
//    Call<List<Todo>> getTodos();
//
//
//    @GET("todos/{id}/")
//    Call<Todo> getTodoById(@Path("id") int todoId);
//
//
//    @Headers({"Cache-Control: max-age=640000", "User-Agent: My-App-Name"})
//    @GET("todos/")
//    Call<List<Todo>> getTodosByUserId(@Query("userId") int userId,
//                                      @Query("completed") boolean completed);
//
//
//    @FormUrlEncoded
//    @POST("todos/")
//    Call<Todo> addTodo(@Field("userId") int userId,
//                       @Field("title") String title,
//                       @Field("completed") boolean completed);
//
//
//
//    @GET
//    Call<List<Todo>> getTodosFromUrl(@Url String url);
}
