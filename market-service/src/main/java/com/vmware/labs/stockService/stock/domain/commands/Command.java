package com.vmware.labs.stockService.stock.domain.commands;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type", defaultImpl = VoidCommand.class)
@JsonSubTypes({
        @JsonSubTypes.Type( name = "stock.changePrice", value = ChangePrice.class )
})
public interface Command {

}

class VoidCommand implements Command {

}
