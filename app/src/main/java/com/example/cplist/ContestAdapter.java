package com.example.cplist;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ContestAdapter extends ArrayAdapter<Contest> {


    public ContestAdapter(@NonNull Context context, ArrayList<Contest> contest) {
        super(context, 0, contest);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.contest_list_item, parent, false);
        }

        Contest currentContest = getItem(position);

        TextView eventTextView = listItemView.findViewById(R.id.event);
        eventTextView.setText(currentContest.getEvent());

        ZoneId zone = ZoneId.of("Asia/Kolkata");
        String startTimeString = currentContest.getStartTime();
        ZonedDateTime startDateTime = Instant.parse(startTimeString).atZone(zone);
        LocalTime startTime = startDateTime.toLocalTime();

        String endTimeString = currentContest.getEndTime();
        ZonedDateTime endDateTime = Instant.parse(endTimeString).atZone(zone);
        LocalTime endTime = endDateTime.toLocalTime();

        TextView startTimeTextView = listItemView.findViewById(R.id.startTime);
        LocalDate startDate = LocalDate.parse(currentContest.getStartTime(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        DateTimeFormatter startFormatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
        String startFormattedString = startDate.format(startFormatter);
        startTimeTextView.setText("Start Date: " + startFormattedString + " at " + startTime);


        TextView endTimeTextView = listItemView.findViewById(R.id.endTime);
        LocalDate endDate = LocalDate.parse(currentContest.getEndTime(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
        String endFormattedString = endDate.format(formatter);
        endTimeTextView.setText("End Date: " + endFormattedString + " at " + endTime);


        TextView durationTextView = listItemView.findViewById(R.id.duration);
        Double eventDuration = Double.parseDouble(currentContest.getDuration());
        if(eventDuration < 86400){
            eventDuration = eventDuration/3600;
            durationTextView.setText("Duration: " + String.valueOf(eventDuration) + " Hours");
        }
        else{
            eventDuration = eventDuration/86400;
            durationTextView.setText("Duration: " + String.valueOf(eventDuration.longValue()) + " Days");
        }

        if(currentContest.getIn24Hours().equals("Yes")){
            TextView in24Hours = listItemView.findViewById(R.id.in24Hours);
            CardView contestCardView = listItemView.findViewById(R.id.cardView);
            contestCardView.setCardBackgroundColor(ContextCompat.getColor(getContext(), R.color.red));
            in24Hours.setVisibility(View.VISIBLE);
        }


        return listItemView;
    }
}