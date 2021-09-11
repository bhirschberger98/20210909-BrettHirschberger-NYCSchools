package com.bhirschberger.a20210909_bretthirschberger_nycschools.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bhirschberger.a20210909_bretthirschberger_nycschools.ListAdapter.SchoolListAdapter;
import com.bhirschberger.a20210909_bretthirschberger_nycschools.R;
import com.bhirschberger.a20210909_bretthirschberger_nycschools.models.School;
import com.bhirschberger.a20210909_bretthirschberger_nycschools.databinding.ActivitySchoolDetailBinding;

import java.util.Objects;

public class SchoolDetailActivity extends AppCompatActivity {

    private ActivitySchoolDetailBinding binding;
    private School school;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // enables binding
        binding = ActivitySchoolDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Objects.requireNonNull(getSupportActionBar()).hide();
        Intent intent = getIntent();
        school = (School) intent.getSerializableExtra(SchoolListAdapter.SCHOOL_LIST_INDEX);

        binding.schoolNameView.setText(school.getName());
        binding.overviewText.setText(school.getOverviewParagraph());

        // checks if the scores exist
        if (scoreExits(school.getAverageMathScore())) {
            binding.mathScoreLabel.setText(getString(R.string.math_score_label, school.getAverageMathScore()));
        } else {
            binding.mathScoreLabel.setText(getString(R.string.math_score_label, getString(R.string.score_not_found_label)));
        }

        if (scoreExits(school.getAverageWritingScore())) {
            binding.writingScoreLabel.setText(getString(R.string.writing_score_label, school.getAverageWritingScore()));
        } else {
            binding.writingScoreLabel.setText(getString(R.string.writing_score_label, getString(R.string.score_not_found_label)));
        }

        if (scoreExits(school.getAverageReadingScore())) {
            binding.readingScoreLabel.setText(getString(R.string.reading_score_label, school.getAverageReadingScore()));
        } else {
            binding.readingScoreLabel.setText(getString(R.string.reading_score_label, getString(R.string.score_not_found_label)));
        }
        // removes directions button if no longitude or latitude is provided
        if (school.getLongitude().equals("ERROR") || school.getLatitude().equals("ERROR")) {
            binding.emailButton.setVisibility(View.GONE);
        }
        if (school.getPhoneNumber().equals("ERROR")) {
            binding.phoneButton.setVisibility(View.GONE);
        }
        if (school.getEmail().equals("ERROR")) {
            binding.emailButton.setVisibility(View.GONE);
        }
        binding.mapButton.setOnClickListener(this::sendToMaps);
        binding.phoneButton.setOnClickListener(this::sendToPhone);
        binding.emailButton.setOnClickListener(this::sendToEmail);
    }


    private boolean scoreExits(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
    }

    // starts map intent to get direction to the school
    public void sendToMaps(View view) {
        Log.i("School Address", school.getAddress());
        Uri uri = Uri.parse("geo:" + school.getLatitude() + "," + school.getLongitude() + "?q=" + Uri.encode(school.getName()));
        Log.i("URI", uri.toString());
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(mapIntent);
    }

    //send school phone number to phone app
    public void sendToPhone(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + school.getPhoneNumber()));
        startActivity(intent);
    }


    public void sendToEmail(View view) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, school.getEmail());
        startActivity(intent);
    }
}