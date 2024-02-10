package me.dgpr.api.store;

import me.dgpr.api.ApiResponse;
import me.dgpr.api.store.dto.CreateStoreRequest;
import me.dgpr.config.security.CurrentManager;
import me.dgpr.config.security.ManagerContext;
import me.dgpr.domains.store.domain.Store;
import me.dgpr.domains.store.usecase.CreateStoreUseCase;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StoreRestController {

    private final CreateStoreUseCase createStoreUseCase;

    public StoreRestController(final CreateStoreUseCase createStoreUseCase) {
        this.createStoreUseCase = createStoreUseCase;
    }

    @PostMapping("/v1/stores")
    public ApiResponse<Store> createStore(
            @CurrentManager final ManagerContext managerContext,
            @Validated @RequestBody final CreateStoreRequest request
    ) {
        Store data = createStoreUseCase.command(
                request.toCommand(managerContext.getId())
        );
        return ApiResponse.created(data);
    }
}
