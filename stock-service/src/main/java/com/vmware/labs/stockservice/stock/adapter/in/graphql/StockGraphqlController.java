package com.vmware.labs.stockservice.stock.adapter.in.graphql;

import com.vmware.labs.stockservice.stock.application.in.RetrieveStockUseCase;
import com.vmware.labs.stockservice.stock.application.in.RetrieveStockUseCase.RetrieveStockCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
public class StockGraphqlController {

    private final RetrieveStockUseCase retrieveStockUseCase;

    @QueryMapping
    Mono<StockGraphqlView> stock( @Argument String symbol ) {

        return this.retrieveStockUseCase.execute( new RetrieveStockCommand( symbol ) )
                .map( stockCache -> new StockGraphqlView( stockCache.symbol(), stockCache.price() ) );
    }

}
