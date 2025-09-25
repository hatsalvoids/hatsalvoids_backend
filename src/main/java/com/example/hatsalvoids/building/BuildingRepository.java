package com.example.hatsalvoids.building;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuildingRepository extends JpaRepository<Building, Long> {

    boolean existsByvWorldBuildingId(String vWorldBuildingId);


    @Query(value = "SELECT * FROM building " +
            "WHERE ST_DWithin(" +
            "building_polygon," +
            "ST_Transform(ST_SetSRID(ST_MakePoint(?2, ?1), 4326), 5186), " +
            "?3" +
            ");",nativeQuery = true)
    List<Building> findByAround(Double latitude, Double longitude, Double radius);
}
