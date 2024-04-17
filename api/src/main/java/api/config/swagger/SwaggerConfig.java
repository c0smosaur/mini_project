package api.config.swagger;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String SECURITY_SCHEME_NAME = "authorization";

    @Bean
    public ModelResolver modelResolver(ObjectMapper objectMapper) {
        return new ModelResolver(objectMapper);
    }

    @Bean
    public OpenAPI swaggerApi(){
        return new OpenAPI()
                .components(new Components()

                        .addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme()
                                .name(SECURITY_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .info(new Info()
                        .title("Mini-Project-1조")
                        .description("숙박예약 서비스 구현"))
//                .path("/api/member/logout", new Operation()
//                        .operationId("logout")
//                        .description("Log out the current user")
//                        .addTagsItem("Authentication")
//                        .addParametersItem(new Parameter()
//                                .in("header")
//                                .name("Authorization")
//                                .description("Bearer token")
//                                .required(true)
//                                .schema(new StringSchema().format("string"))
//                                .example("Bearer <token>"))
//                        .responses(new ApiResponses()
//                                .addApiResponse("200", new ApiResponse()
//                                        .description("Logout successful"))
//                                .addApiResponse("401", new ApiResponse()
//                                        .description("Unauthorized"))
//                        ))
                ;
    }

    // 헤더 파라미터 추가
    @Bean
    public OperationCustomizer globalHeader(){
        return (operation, handlerMethod) -> {
            operation.addParametersItem(new Parameter()
                    .in(ParameterIn.HEADER.toString())
                    .schema(new StringSchema().name("refreshToken"))
                    .name("RefreshToken"));

            return operation;
        };
    }

}