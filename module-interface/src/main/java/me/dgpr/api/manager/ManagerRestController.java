package me.dgpr.api.manager;

import me.dgpr.api.ApiResponse;
import me.dgpr.api.manager.dto.CreateManagerRequest;
import me.dgpr.domains.manager.usecase.CreateManagerUseCase;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ManagerRestController {

    private final CreateManagerUseCase createManagerUseCase;

    public ManagerRestController(final CreateManagerUseCase createManagerUseCase) {
        this.createManagerUseCase = createManagerUseCase;
    }

    @PostMapping("/v1/managers")
    public ApiResponse<Void> createManager(
            @Validated @RequestBody final CreateManagerRequest request
    ) {
        createManagerUseCase.command(request.toCommand());
        return ApiResponse.created();
    }
}
