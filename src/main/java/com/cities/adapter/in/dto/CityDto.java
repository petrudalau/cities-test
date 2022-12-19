package com.cities.adapter.in.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder()
public class CityDto {
  private final String id;
  private final String name;
  private final String photo;
}
