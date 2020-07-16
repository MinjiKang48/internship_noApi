package com.o2o.action.server.app;


import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class ApiController {

    public String getGenreList(int genreId) {
        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://unogs-unogs-v1.p.rapidapi.com/aaapi.cgi?q=get%253Anew7-!2000%2C2020-!0%2C5-!0%2C10-!" + genreId + "-!Series-!Any-!Any-!gt100-!%7Bdownloadable%7D&t=ns&cl=all&st=adv&ob=Relevance&p=1&sa=and")
                    .get()
                    .addHeader("x-rapidapi-host", "unogs-unogs-v1.p.rapidapi.com")
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

    public String getCountriesList(String countries) {
        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://unogs-unogs-v1.p.rapidapi.com/aaapi.cgi?q=get%3Aexp%3A" + countries + "&t=ns&st=adv&p=1")
                    .get()
                    .addHeader("x-rapidapi-host", "unogs-unogs-v1.p.rapidapi.com")
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

    public String getDetail(int id) {
        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://unogs-unogs-v1.p.rapidapi.com/aaapi.cgi?t=loadvideo&q=" + id)
                    .get()
                    .addHeader("x-rapidapi-host", "unogs-unogs-v1.p.rapidapi.com")
                    .addHeader("x-rapidapi-key", "9e89b2aff7msh694128937ca1f7fp1ed93ajsn0832418c9fcd")
                    .build();

            Response response = client.newCall(request).execute();

            //출력
            String message = response.body().string();
            System.out.println(message);
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        return null;
    }

}