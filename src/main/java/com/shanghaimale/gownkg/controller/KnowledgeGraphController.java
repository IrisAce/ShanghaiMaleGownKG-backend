package com.shanghaimale.gownkg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * KnowledgeGraphController 类用于处理与知识图谱相关的 HTTP 请求。
 * <p>
 * 该控制器通过 Neo4j 数据库查询节点和关系，并以 JSON 格式返回结果，供前端展示知识图谱。
 */
@RestController
@RequestMapping("/api")
public class KnowledgeGraphController {

    @Autowired
    private Neo4jClient neo4jClient;

    /**
     * 获取知识图谱中的所有节点和关系。
     * <p>
     * 该方法通过两个 Cypher 查询分别获取：
     * 1. 所有节点的 ID、类别（标签）和名称。
     * 2. 所有关系的源节点 ID、目标节点 ID 和关系类型。
     * <p>
     * 如果节点的名称或类别为空，则使用默认值 "Unnamed" 或 "Unknown" 替代。
     * 如果关系类型为空，则使用默认值 "UNKNOWN" 替代。
     *
     * @return 包含节点和关系的 Map 对象，其中：
     * - "nodes" 键对应节点列表；
     * - "links" 键对应关系列表。
     */
    @GetMapping("/nodes-and-relationships")
    public Map<String, Object> getNodesAndRelationships() {
        // 查询所有节点
        List<Map<String, Object>> nodes = neo4jClient.query(
                        "MATCH (n) RETURN id(n) AS id, labels(n)[0] AS category, n.name AS name"
                ).fetch().all().stream()
                .map(record -> Map.of(
                        "id", record.get("id"),
                        "name", record.get("name") != null ? record.get("name") : "Unnamed", // 动态获取 name，如果为 null 则使用 "Unnamed"
                        "category", record.get("category") != null ? record.get("category") : "Unknown" // 动态获取 category，如果为 null 则使用 "Unknown"
                ))
                .collect(Collectors.toList());

        // 查询所有关系
        List<Map<String, Object>> links = neo4jClient.query(
                        "MATCH (a)-[r]->(b) RETURN id(a) AS source, id(b) AS target, type(r) AS name"
                ).fetch().all().stream()
                .map(record -> Map.of(
                        "source", record.get("source"),
                        "target", record.get("target"),
                        "name", record.get("name") != null ? record.get("name") : "UNKNOWN" // 动态获取关系类型，如果为 null 则使用 "UNKNOWN"
                ))
                .collect(Collectors.toList());

        // 返回包含节点和关系的结果
        return Map.of("nodes", nodes, "links", links);
    }
}
