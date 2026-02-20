package com.example.hatsalvoids.shade;


import com.example.hatsalvoids.global.success.PaginatedResponse;
import com.example.hatsalvoids.global.success.SuccessResponse;
import com.example.hatsalvoids.shade.model.FetchShadeBuildingResponse;
import com.example.hatsalvoids.shade.model.FetchShadeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/v1/shades")
@RequiredArgsConstructor
public class ShadeController {
    private final ShadeService shadeService;
    private final ShadeRateLimitService shadeRateLimitService;

    @CrossOrigin(origins = {"http://127.0.0.1:3001", "http://localhost:3001"})
    @GetMapping("/add-buildings")
    public ResponseEntity<SuccessResponse<PaginatedResponse<FetchShadeResponse>>> getShadesByVWorldApi(@RequestParam String latitude,
                                                                                                       @RequestParam String longitude,
                                                                                                       @RequestParam double radius,
                                                                                                       @RequestParam String time,
                                                                                                       @RequestParam String zoneId,
                                                                                                       @RequestParam(defaultValue = "0") int page,
                                                                                                       @RequestParam(defaultValue = "150") int size,
                                                                                                       HttpServletRequest request

    ) {
        shadeRateLimitService.validateShadeRequest(request);

        List<FetchShadeResponse> response = shadeService.fetchShadesByVWorldApi(latitude, longitude, radius, time, zoneId);
        PaginatedResponse<FetchShadeResponse> pageResponse =
                PaginatedResponse.of(size, page, response.size(), (response.size() + size - 1) / size, response);


        return ResponseEntity
                .status(ShadeSuccessCode.SHADE_FETCHED.getStatus())
                .body(SuccessResponse.of(ShadeSuccessCode.SHADE_FETCHED));
    }

    @CrossOrigin(origins = {"http://127.0.0.1:3001", "http://localhost:3001"})
    @GetMapping("/database")
    public ResponseEntity<SuccessResponse<PaginatedResponse<FetchShadeBuildingResponse>>> getShadesByDatabase(@RequestParam String latitude,
                                                                                                              @RequestParam String longitude,
                                                                                                              @RequestParam double radius,
                                                                                                              @RequestParam String time,
                                                                                                              @RequestParam String zoneId,
                                                                                                              @RequestParam(defaultValue = "0") int page,
                                                                                                              @RequestParam(defaultValue = "150") int size,
                                                                                                              HttpServletRequest request
    ) {
        shadeRateLimitService.validateShadeRequest(request);

        List<FetchShadeBuildingResponse> response = shadeService.fetchShadesByDatabase(latitude, longitude, radius, time, zoneId);
        PaginatedResponse<FetchShadeBuildingResponse> pageResponse =
                PaginatedResponse.of(size, page, response.size(), (response.size() + size - 1) / size, response);

        return ResponseEntity
                .status(ShadeSuccessCode.SHADE_FETCHED.getStatus())
                .body(SuccessResponse.of(ShadeSuccessCode.SHADE_FETCHED, pageResponse));
    }

    @CrossOrigin(origins = {"http://127.0.0.1:3001", "http://localhost:3001"})
    @GetMapping
    public ResponseEntity<SuccessResponse<PaginatedResponse<FetchShadeBuildingResponse>>> getShadesByDatabaseParallel(@RequestParam String latitude,
                                                                                                                      @RequestParam String longitude,
                                                                                                                      @RequestParam double radius,
                                                                                                                      @RequestParam String time,
                                                                                                                      @RequestParam String zoneId,
                                                                                                                      @RequestParam(defaultValue = "0") int page,
                                                                                                                      @RequestParam(defaultValue = "150") int size,
                                                                                                                      HttpServletRequest request
    ) {
        shadeRateLimitService.validateShadeRequest(request);

        List<FetchShadeBuildingResponse> response = shadeService.fetchShadesByDatabaseParallel(latitude, longitude, radius, time, zoneId);
        PaginatedResponse<FetchShadeBuildingResponse> pageResponse =
                PaginatedResponse.of(size, page, response.size(), (response.size() + size - 1) / size, response);

        return ResponseEntity
                .status(ShadeSuccessCode.SHADE_FETCHED.getStatus())
                .body(SuccessResponse.of(ShadeSuccessCode.SHADE_FETCHED, pageResponse));
    }
}
