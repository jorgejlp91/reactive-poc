package com.piccolomini.reactive.config.springfox;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import springfox.documentation.builders.OperationBuilder;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ApiListingScannerPlugin;
import springfox.documentation.spi.service.contexts.DocumentationContext;
import springfox.documentation.spring.web.readers.operation.CachingOperationNameGenerator;

import java.util.*;

import static java.util.Collections.singleton;

// This is necessary because swagger does not supports functional controllers declaration
@Configuration
public class ManualApiDocumentationConfig implements ApiListingScannerPlugin {

  private final CachingOperationNameGenerator operationNames;

  @Autowired
  public ManualApiDocumentationConfig(CachingOperationNameGenerator operationNames) {
    this.operationNames = operationNames;
  }

  @Override
  public List<ApiDescription> apply(DocumentationContext context) {
    return new ArrayList<>(
        Arrays.asList(
            new ApiDescription(
                "OrderStatus",
                "/orderStatus",
                "This is a bug",
                Collections.singletonList(
                    new OperationBuilder(operationNames)
                        .authorizations(new ArrayList<>())
                        .codegenMethodNameStem("orderStatus")
                        .method(HttpMethod.GET)
                        .notes("this method obtains all order status")
                        .responseMessages(responseMessages())
                        .responseModel(new ModelRef("list"))
                        .build()),
                false)));
  }

  private Set<ResponseMessage> responseMessages() {
    return singleton(
        new ResponseMessageBuilder()
            .code(200)
            .message("Successfully getting all orders with status response")
            .responseModel(new ModelRef("collection"))
            .build());
  }

  @Override
  public boolean supports(DocumentationType delimiter) {
    return DocumentationType.SWAGGER_2.equals(delimiter);
  }
}
