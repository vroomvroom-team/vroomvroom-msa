package com.vroomvroom.hub.application;

import com.vroomvroom.hub.application.dto.HubRouteDetailRes;
import com.vroomvroom.hub.application.dto.OptimalRouteRes;
import com.vroomvroom.hub.domain.entity.HubRoute;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;

public class Dijkstra {

    @Getter
    @AllArgsConstructor
    static class Node {
        UUID id;
        long cost;
    }

    public static OptimalRouteRes findOptimalRoute(Map<UUID, List<HubRoute>> graph, UUID start, UUID end, String type) {
        Map<UUID, Long> distance = new HashMap<>();
        Map<UUID, UUID> prev = new HashMap<>();
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingLong(Node::getCost));
        graph.keySet().forEach(id -> distance.put(id, Long.MAX_VALUE));
        distance.put(start, 0L);
        pq.offer(new Node(start, 0));
        while (!pq.isEmpty()) {
            Node cur = pq.poll();
            if (cur.id.equals(end)) break;
            for (HubRoute route: graph.getOrDefault(cur.id, List.of())) {
                if (!route.getIsActive()) continue;
                long w = type.equalsIgnoreCase("DISTANCE") ? route.getDistance() : route.getTime();
                long newDist = cur.cost + w;
                UUID next = route.getArrivalHub().getHubId();

                if (newDist < distance.get(next)) {
                    distance.put(next, newDist);
                    prev.put(next, cur.id);
                    pq.offer(new Node(next, newDist));
                }
            }
        }
        List<UUID> path = new ArrayList<>();
        for (UUID node = end; node != null; node = prev.get(node)) path.add(node);
        Collections.reverse(path);

        List<HubRouteDetailRes> details = new ArrayList<>();
        for (int i = 0; i < path.size() - 1; i++) {
            UUID departure = path.get(i);
            UUID arrival = path.get(i + 1);
            graph.getOrDefault(departure, List.of()).stream()
                    .filter(r -> r.getArrivalHub().getHubId().equals(arrival))
                    .min(Comparator.comparingLong(r -> type.equalsIgnoreCase("DISTANCE") ? r.getDistance() : r.getTime())).ifPresent(min -> details.add(HubRouteDetailRes.from(min)));
        }
        return OptimalRouteRes.builder()
                .path(details)
                .totalCost(distance.get(end))
                .build();
    }
}
