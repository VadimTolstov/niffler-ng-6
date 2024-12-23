package guru.qa.niffler.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.data.entity.userdata.CurrencyValues;
import guru.qa.niffler.grpc.Currency;

import javax.annotation.Nonnull;

public record CurrencyJson(
    @JsonProperty("currency")
    CurrencyValues currency,
    @JsonProperty("currencyRate")
    Double currencyRate) {


  public static @Nonnull CurrencyJson fromGrpcMessage(@Nonnull Currency currencyMessage) {
    return new CurrencyJson(
        CurrencyValues.valueOf(currencyMessage.getCurrency().name()),
        currencyMessage.getCurrencyRate()
    );
  }
}
