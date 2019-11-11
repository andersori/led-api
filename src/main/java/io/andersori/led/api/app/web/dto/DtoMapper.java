package io.andersori.led.api.app.web.dto;

public interface DtoMapper<Source, Destination> {

	Destination toDto(Source entity);

	Source toEntity();

}
