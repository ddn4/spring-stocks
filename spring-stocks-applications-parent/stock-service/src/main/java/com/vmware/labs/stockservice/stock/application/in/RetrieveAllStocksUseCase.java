package com.vmware.labs.stockservice.stock.application.in;

import com.vmware.labs.common.annotations.usecase.SelfValidating;
import com.vmware.labs.stockservice.stock.domain.StockProjection;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import reactor.core.publisher.Flux;

public interface RetrieveAllStocksUseCase {

    Flux<StockProjection> execute( RetrieveAllStocksCommand command );

    @Getter
    @EqualsAndHashCode( callSuper = false )
    final class RetrieveAllStocksCommand extends SelfValidating<RetrieveAllStocksCommand> {

        public RetrieveAllStocksCommand() {

            validateSelf();

        }

    }

}
