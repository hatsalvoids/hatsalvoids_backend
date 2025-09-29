package com.example.hatsalvoids.building;


import com.example.hatsalvoids.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Polygon;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Building extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String buildingName;

    @Column
    private String category;

    @Column(nullable = false)
    private String roadAddress;

    @Column(columnDefinition = "geometry(Polygon, 5186)", nullable = false)
    private Polygon buildingPolygon;  // ESPG 5186

    @Column(nullable = false)
    private Double height;

    @Column(nullable = false)
    private String pnu;

    @Column(nullable = false)
    private String bdMgtSn;

    @Column(nullable = false, unique = true)
    private String vWorldBuildingId;
}
