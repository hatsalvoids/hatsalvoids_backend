package com.example.hatsalvoids.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MetricsPageController {

    @GetMapping("/metrics-view")
    public String metricsPage(Model model) {
        // 클라이언트에서 fetch로 /actuator/metrics 목록과 주요 지표를 호출하도록 함
        return "metrics-view";
    }
}
