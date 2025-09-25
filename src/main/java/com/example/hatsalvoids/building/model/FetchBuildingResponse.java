package com.example.hatsalvoids.building.model;


import com.example.hatsalvoids.building.Building;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FetchBuildingResponse {
    private Long id;
    private String name;
    private String address;

    public static FetchBuildingResponse from(Building building) {
        return new FetchBuildingResponse(
                building.getId(),
                building.getBuildingName(),
                building.getRoadAddress()
        );
    }
}
