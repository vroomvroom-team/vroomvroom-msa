package com.vroomvroom.hub.application;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class HubConnection {
    private static final Map<String, Set<String>> connections = new HashMap<>();

    static {
        connect("경기 남부 센터", "경기 북부 센터");
        connect("경기 남부 센터", "서울특별시 센터");
        connect("경기 남부 센터", "인천광역시 센터");
        connect("경기 남부 센터", "강원특별자치도 센터");
        connect("경기 남부 센터", "경상북도 센터");
        connect("경기 남부 센터", "대전광역시 센터");
        connect("경기 남부 센터", "대구광역시 센터");

        connect("대전광역시 센터", "충청남도 센터");
        connect("대전광역시 센터", "충청북도 센터");
        connect("대전광역시 센터", "세종특별자치시 센터");
        connect("대전광역시 센터", "전북특별자치도 센터");
        connect("대전광역시 센터", "광주광역시 센터");
        connect("대전광역시 센터", "전라남도 센터");
        connect("대전광역시 센터", "경기 남부 센터");
        connect("대전광역시 센터", "대구광역시 센터");

        connect("대구광역시 센터", "경상북도 센터");
        connect("대구광역시 센터", "경상남도 센터");
        connect("대구광역시 센터", "부산광역시 센터");
        connect("대구광역시 센터", "울산광역시 센터");
        connect("대구광역시 센터", "경상북도 센터");
        connect("대구광역시 센터", "경기 남부 센터");
        connect("대구광역시 센터", "대전광역시 센터");

        connect("경상북도 센터", "경기 남부 센터");
        connect("경상북도 센터", "대구광역시 센터");
    }

    private static void connect(String hub1, String hub2) {
        connections.computeIfAbsent(hub1, a -> new HashSet<>()).add(hub2);
        connections.computeIfAbsent(hub2, a -> new HashSet<>()).add(hub1);
    }

    public boolean isConnected(String departure, String arrival) {
        return connections.get(departure) != null && connections.get(departure).contains(arrival);
    }

}