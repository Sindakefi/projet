import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;


public class AppointmentFormHandler extends HttpServlet {

    private ProcessEngine processEngine;

    @Override
    public void init() {
        processEngine = ProcessEngines.getDefaultProcessEngine(); // Get default engine
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        RuntimeService runtimeService = processEngine.getRuntimeService();
        response.setContentType("text/plain"); // Set response type to plain text

        String executionId = request.getParameter("executionId");
        Map<String, String> formData = new HashMap<>();

        // Get form data parameters
        formData.put("patientEmail", request.getParameter("patientEmail"));
        formData.put("requestedDate", request.getParameter("requestedDate"));
        formData.put("consultationType", request.getParameter("consultationType"));


        try {
            //Set Variables - Handle potential null values
            if(formData.get("patientEmail") != null) runtimeService.setVariable(executionId, "patientEmail", formData.get("patientEmail"));
            if(formData.get("requestedDate") != null) runtimeService.setVariable(executionId, "requestedDate", formData.get("requestedDate"));
            if(formData.get("consultationType") != null) runtimeService.setVariable(executionId, "consultationType", formData.get("consultationType"));


            PrintWriter out = response.getWriter();
            out.println("Form submitted successfully!");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            PrintWriter out = response.getWriter();
            out.println("Error submitting form: " + e.getMessage());
            e.printStackTrace(); // Consider using a logger instead for production.
        }
    }
}