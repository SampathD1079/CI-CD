package com.example.devopsdashboard;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        // Explicit inputs make the local metadata contract independent of the host environment.
        "APP_NAME=DevOps Deployment Dashboard",
        "APP_VERSION=v2",
        "APP_ENVIRONMENT=local",
        "DEPLOYMENT_MESSAGE=Version 2 is running. CI/CD pipeline demonstration!",
        "COMMIT_SHA="
})
class DashboardApplicationTests {

    @Autowired
    private ConfigurableApplicationContext context;

    @Autowired
    private DeploymentMetadata metadata;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void applicationContextStartsAndRegistersController() {
        assertThat(context.isActive()).isTrue();
        assertThat(context.getBean(InfoController.class)).isNotNull();
    }

    @Test
    void bindsLocalDeploymentMetadata() {
        assertThat(metadata).isEqualTo(new DeploymentMetadata(
                "DevOps Deployment Dashboard",
                "v2",
                "local",
                "Version 2 is running. CI/CD pipeline demonstration!",
                ""
        ));
    }

    @Test
    void omitsCommitShaFromJsonWhenNotProvided() {
        var json = objectMapper.valueToTree(metadata);

        assertThat(json.has("commitSha")).isFalse();
        assertThat(json.path("version").asText()).isEqualTo("v2");
    }
}




