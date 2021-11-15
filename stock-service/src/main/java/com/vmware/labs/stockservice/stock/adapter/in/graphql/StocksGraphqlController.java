package com.vmware.labs.stockservice.stock.adapter.in.graphql;

import com.vmware.labs.stockservice.stock.application.in.RetrieveAllStocksUseCase;
import com.vmware.labs.stockservice.stock.application.in.RetrieveAllStocksUseCase.RetrieveAllStocksCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

@Controller
@RequiredArgsConstructor
public class StocksGraphqlController {

    private final RetrieveAllStocksUseCase retrieveAllStocksUseCase;

    @QueryMapping
    Flux<StockGraphqlView> stocks() {

        return this.retrieveAllStocksUseCase.execute( new RetrieveAllStocksCommand() )
                .map( stockCache -> new StockGraphqlView( stockCache.symbol(), stockCache.price() ) );
    }

}


