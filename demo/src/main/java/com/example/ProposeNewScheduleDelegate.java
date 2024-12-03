package com.example;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import java.util.HashMap;
import java.util.Map;

public class ProposeNewScheduleDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        // Get variables from process
        String requestedDate = (String) execution.getVariable("requestedDate");
        String consultationType = (String) execution.getVariable("consultationType");

        // Doctor's availability schedule
        Map<String, Map<String, String>> doctorAvailability = getDoctorAvailability();

        // Propose a new schedule
        String proposedSchedule = proposeNewSchedule(requestedDate, consultationType, doctorAvailability);

        // Set the proposed schedule as a process variable
        execution.setVariable("proposedSchedule", proposedSchedule);

        // Log the result
        System.out.println("Proposed new schedule: " + proposedSchedule);
    }

    private String proposeNewSchedule(String requestedDate, String consultationType, Map<String, Map<String, String>> doctorAvailability) {
        Map<String, String> timeSlots = doctorAvailability.getOrDefault(requestedDate, new HashMap<String, String>());
        String newTime = findNextAvailableTime(requestedDate, consultationType, timeSlots);
        if (newTime == null) {
            return proposeNewDaySchedule(consultationType, doctorAvailability);
        }
        return requestedDate + " " + newTime;
    }

    private String findNextAvailableTime(String requestedDate, String consultationType, Map<String, String> timeSlots) {
        if (consultationType.equals("General Check-up")) {
            for (int hour = 10; hour <= 17; hour++) {
                String time = hour + "h";
                if (!timeSlots.containsKey(time)) {
                    return time;
                }
            }
        } else if (consultationType.equals("Neurology")) {
            if (!timeSlots.containsKey("10h")) {
                return "10h";
            }
        } else if (consultationType.equals("Cardiology")) {
            if (!timeSlots.containsKey("15h30")) {
                return "15h30";
            }
        }
        return null;
    }

    private String proposeNewDaySchedule(String consultationType, Map<String, Map<String, String>> doctorAvailability) {
        for (String date : doctorAvailability.keySet()) {
            Map<String, String> timeSlots = doctorAvailability.get(date);
            String newTime = findNextAvailableTime(date, consultationType, timeSlots);
            if (newTime != null) {
                return date + " " + newTime;
            }
        }
        return "No available schedules";
    }

    private Map<String, Map<String, String>> getDoctorAvailability() {
        // Strictly typed map initialization
        Map<String, Map<String, String>> availability = new HashMap<>();

        Map<String, String> day1 = new HashMap<>();
        day1.put("10h", "Available");
        day1.put("11h", "Unavailable");
        day1.put("14h", "Available");
        availability.put("2024-12-02", day1);

        Map<String, String> day2 = new HashMap<>();
        day2.put("10h", "Available");
        day2.put("15h30", "Available");
        availability.put("2024-12-03", day2);

        return availability; // Correctly return typed map
    }
}
