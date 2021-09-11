package com.bhirschberger.a20210909_bretthirschberger_nycschools.models;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;


/*
 * All of the data is being retrieved when the application loads because
 * this is faster then loading the test scores for each individual school
 * I would have also liked to of used a kotlin object
 * */
public class SchoolData {
    private static final String HIGH_SCHOOL_DIRECTORY_URL = "https://data.cityofnewyork.us/resource/s3k6-pzi2.json";
    private static final String HIGH_SCHOOL_SCORES_URL = "https://data.cityofnewyork.us/resource/f9bf-2cp4.json";

    private static SchoolData instance;

    private List<School> schools;

    public synchronized static SchoolData getInstance() {
        if (instance == null) {
            return new SchoolData();
        }
        return instance;
    }

    private SchoolData() {
        try {
            schools = new GetSchoolsTask().execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    public School getSchool(int index) {
        return schools.get(index);
    }

    public List<School> getSchoolsList() {
        return schools;
    }

    // given more time I would of liked to used the standard java.util.concurrent package
    // or if I was using kotlin i would use the concurrency library
    private class GetSchoolsTask extends AsyncTask<Void, Void, List<School>> {

        private List<School> getSchools() throws JSONException, IOException {
            // gets school data from url
            // using map insted of list so I can use the dbn as a key to add the test scores later
            Map<String, School> schoolMap = new HashMap<>();
            URL schoolUrl = new URL(HIGH_SCHOOL_DIRECTORY_URL);
            HttpURLConnection urlConnection = (HttpURLConnection) schoolUrl.openConnection();
            urlConnection.setRequestMethod("GET");
            InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder testsBuilder = new StringBuilder();

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                testsBuilder.append(line);
            }

            JSONArray result = new JSONArray(testsBuilder.toString());
            for (int i = 0; i < result.length(); i++) {
                JSONObject object = result.getJSONObject(i);
                schoolMap.put(object.getString("dbn"), new School(object.getString("dbn"),
                        object.getString("school_name"),
                        object.getString("location"),
                        object.getString("overview_paragraph"),
                        object.optString("latitude","ERROR"),
                        object.optString("longitude","ERROR"),
                        object.optString("phone_number","ERROR"),
                        object.optString("school_email","ERROR")));
            }
            addTestScores(schoolMap);
            bufferedReader.close();
            inputStream.close();

            return new ArrayList<>(schoolMap.values()); // puts map values in array list
        }


        // adds test scores from NYC Open Data API to existing schools map
        private void addTestScores(Map<String, School> schoolMap) throws JSONException, IOException {
            URL schoolUrl = new URL(HIGH_SCHOOL_SCORES_URL);
            HttpURLConnection urlConnection = (HttpURLConnection) schoolUrl.openConnection();
            urlConnection.setRequestMethod("GET");
            InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder schoolsBuilder = new StringBuilder();

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                schoolsBuilder.append(line);
            }

            JSONArray result = new JSONArray(schoolsBuilder.toString());
            for (int i = 0; i < result.length(); i++) {
                JSONObject object = result.getJSONObject(i);
                School school = schoolMap.get(object.getString("dbn"));
                if (school != null) {
                    school.setTestScores(
                            object.getString("num_of_sat_test_takers"),
                            object.getString("sat_critical_reading_avg_score"),
                            object.getString("sat_math_avg_score"),
                            object.getString("sat_writing_avg_score"));
                }
            }
            bufferedReader.close();
            inputStream.close();
        }

        @Override
        protected List<School> doInBackground(Void... urls) {
            try {
                return getSchools();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
