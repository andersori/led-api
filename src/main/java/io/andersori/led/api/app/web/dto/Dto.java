package io.andersori.led.api.app.web.dto;

public interface Dto<DtoType, EntityType> {
		
	void toDto(EntityType entity);

	EntityType toEntity();

}
