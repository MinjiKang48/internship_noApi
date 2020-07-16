package com.o2o.action.server.app;


import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class ApiController {

    public String getGenreList(int genreId) {
        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://unogsng.p.rapidapi.com/search?genrelist=" + genreId + "&type=series&start_year=2000&subtitle=korean&audio=korean&offset=0&end_year=2020")
                    .get()
                    .addHeader("x-rapidapi-host", "unogsng.p.rapidapi.com")
                    .addHeader("x-rapidapi-key", "9e89b2aff7msh694128937ca1f7fp1ed93ajsn0832418c9fcd")
                    .build();

            Response response = client.newCall(request).execute();

            //출력
            String message = response.body().string();
            return message;
        } catch (Exception e){
            System.err.println(e.toString());
        }
        return null;
    }

    public String getCountriesList(int countryId) {
        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://unogsng.p.rapidapi.com/search?type=series&start_year=2000&subtitle=korean&countrylist=" + countryId + "&audio=korean&offset=0&end_year=2020")
                    .get()
                    .addHeader("x-rapidapi-host", "unogsng.p.rapidapi.com")
                    .addHeader("x-rapidapi-key", "9e89b2aff7msh694128937ca1f7fp1ed93ajsn0832418c9fcd")
                    .build();

            Response response = client.newCall(request).execute();

            //출력
            String message = response.body().string();
            return message;
        } catch (Exception e){
            System.err.println(e.toString());
        }
        return null;
    }

    public void getDetail_genre(int id) {
        try {
            OkHttpClient client = new OkHttpClient();

            Request request     = new Request.Builder()
                    .url("https://unogsng.p.rapidapi.com/title?netflixid=" + id)
                    .get()
                    .addHeader("x-rapidapi-host", "unogsng.p.rapidapi.com")
                    .addHeader("x-rapidapi-key", "9e89b2aff7msh694128937ca1f7fp1ed93ajsn0832418c9fcd")
                    .build();

            Response response = client.newCall(request).execute();

            //출력
            String message = response.body().string();
            System.out.println(message);
        } catch (Exception e){
            System.err.println(e.toString());
        }
    }

    public void getDetail_countries(int id){
        try {
            OkHttpClient client = new OkHttpClient();

            Request request     = new Request.Builder()
                    .url("https://unogsng.p.rapidapi.com/title?netflixid=" + id)
                    .get()
                    .addHeader("x-rapidapi-host", "unogsng.p.rapidapi.com")
                    .addHeader("x-rapidapi-key", "9e89b2aff7msh694128937ca1f7fp1ed93ajsn0832418c9fcd")
                    .build();

            Response response = client.newCall(request).execute();

            //출력
            String message = response.body().string();
            System.out.println(message);
        } catch (Exception e){
            System.err.println(e.toString());
        }
    }

}