package com.example.hatsalvoids.shade.model;


import com.example.hatsalvoids.building.model.FetchBuildingResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor(staticName = "of")
public class FetchShadeBuildingResponse {
    FetchBuildingResponse building;
    ShadeGeometryResult shadeGeometryResults;
}
