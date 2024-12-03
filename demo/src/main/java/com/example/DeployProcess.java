package com.example;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.Deployment;

import java.io.InputStream;

public class DeployProcess {

    public static void main(String[] args) {
        // Initialize the Process Engine (In-memory for development)
        ProcessEngineConfiguration processEngineConfiguration =
        ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration()
            .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE)
            .setJdbcUrl("jdbc:h2:mem:camunda;DB_CLOSE_DELAY=-1")
            .setJdbcDriver("org.h2.Driver")
            .setJdbcUsername("sa")
            .setJdbcPassword("");
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();

        RepositoryService repositoryService = processEngine.getRepositoryService();

        // Deploy the BPMN process file
        InputStream bpmnFile = DeployProcess.class.getResourceAsStream("/bpmn/appointmentProcess.bpmn");
        Deployment deployment = repositoryService.createDeployment()
                .addInputStream("appointmentProcess.bpmn", bpmnFile)
                .deploy();

        System.out.println("Deployment id: " + deployment.getId());
    }
}
