package com.boss.demo;

import java.util.*;

/**
 * @author lxl lukas
 * @description 路径规划实例
 * @create 2018/4/5
 */
public class Example3 {
    public static Integer graphExample(Graph graph,Node start){
        Integer result  = graph.findMaxTotalWeight(start);
        return result;
    }
}
/**
 * 有向图类
 */
abstract class Graph{

    /**
     * 图的所有节点
     */
    Map<String,Node> nodes = new HashMap<>();

    public Graph() {

    }

    /**
     * 添加节点到图中
     * @param node
     */
    void addNode(Node node){
        if(null == node){
            throw new IllegalArgumentException("node is null");
        }
        this.nodes.put(node.getNodeName(), node);
    }

    /**
     * <p>添加路径到图</p>
     * <strong>节点必须是已添加到图中</strong>
     * @param start
     * @param end
     */
    void addPath(Node start,Node end){
        if(null == start || null == end){
            throw new IllegalArgumentException("路径节点为null");
        }
        if(null == this.nodes.get(start.getNodeName())){
            throw new RuntimeException("路径节点不存在");
        }
        if(null == this.nodes.get(end.getNodeName())){
            throw new RuntimeException("路径终点不存在");
        }
        start.addPath(end);
    }

    /**
     * 根据输入的起始节点查询最大权重路径
     * @param startNode
     * @return
     */
   abstract Integer findMaxTotalWeight(Node startNode);

}

/**
 * 无环路有向图
 */
class NoLoopGraph extends Graph{
    /**
     * 计算指定起点到其他节点的最大权重
     * @param startNode
     * @return
     */
    @Override
    Integer findMaxTotalWeight(Node startNode) {
        if(null == startNode){
            throw new IllegalArgumentException("参数为null");
        }
        if(null == nodes.get(startNode.getNodeName())){
            throw new RuntimeException("图上不存在节点"+startNode);
        }
        Collection<Node> allNode = nodes.values();
        LinkedHashSet<String> pathRecords = new LinkedHashSet<>();
        Integer max = 0,temp;
        for (Node other : allNode) {
            if(startNode.getNodeName().equals(other.getNodeName())){
                continue;
            }
            temp = findStart2EndMaxTotalWeight(startNode,other);
            System.out.println(startNode.getNodeName()+" -> "+other.getNodeName()+"  "+temp);
            if(max <temp){
                max = temp;
            }
        }
        return max;
    }

    /**
     * 计算起点到终点的最大路径权重
     * @param start
     * @param end
     * @return
     */
    public Integer findStart2EndMaxTotalWeight(Node start,Node end){
        Set<GraphPath> nodePaths = start.getPathSet();
        if(null == nodePaths || nodePaths.size() == 0){
            return 0;
        }
        int weight ,temp ,mostWeight = 0;
        Node next;
        for(GraphPath path : nodePaths){
            temp = start.getNodeWeight();
            next = path.getEndNode();
            if (end.getNodeName().equals(next.getNodeName())){//本次递归到终止节点
                temp += next.getNodeWeight();
            }else{//继续递归计算
               weight = findStart2EndMaxTotalWeight(next, end);
               if(weight == 0){
                   continue;
               }
               temp += weight;
            }
            if(temp > mostWeight){
                mostWeight = temp;
            }
        }
        return mostWeight;
    }
}

/**
 * 有向有环图
 */
class LoopGraph extends Graph{

    @Override
    Integer findMaxTotalWeight(Node startNode) {
        if(null == startNode){
            throw new IllegalArgumentException("参数为null");
        }
        if(null == nodes.get(startNode.getNodeName())){
            throw new RuntimeException("图上不存在节点"+startNode);
        }
        Collection<Node> allNode = nodes.values();

        LinkedHashSet<String> pathList;
        Integer max = 0,temp;
        for (Node other : allNode) {
            if(startNode.getNodeName().equals(other.getNodeName())){
                continue;
            }
            pathList = new LinkedHashSet<>();
            temp = findStart2EndMaxTotalWeight(startNode,other,pathList);
            if(max <temp){
                max = temp;
            }
        }
        return max;
    }

    /**
     * 发现回环则返回
     * @param start
     * @param end
     * @param pathList
     * @return
     */
    private int findStart2EndMaxTotalWeight(Node start, Node end, LinkedHashSet<String> pathList) {
        if (start.equals(end)) {
            return 0;
        }
        Collection<GraphPath> paths = start.getPathSet();
        if (paths == null || paths.size() == 0) {
            return 0;
        }
        boolean add = pathList.add(start.getNodeName());
        if (!add) {//存在闭环，则返回
            return 0;
        }
        int weight = 0;
        LinkedHashSet<String> temp = new LinkedHashSet<>();
        temp.addAll(pathList);
        LinkedHashSet<String> temp2;
        for (GraphPath path : paths) {
            temp2 = new LinkedHashSet<>();
            temp2.addAll(temp);
            int tempWeight = start.getNodeWeight();
            Node next = path.getEndNode();
            if (!next.getNodeName().equals(end.getNodeName())) {
                int mostPath = findStart2EndMaxTotalWeight(next, end, temp2);
                if (mostPath == -1) {
                    continue;
                }
                tempWeight += mostPath;
            } else {
                tempWeight += next.getNodeWeight();
                temp2.add(end.getNodeName());
            }
            if (weight < tempWeight) {
                weight = tempWeight;
                pathList.clear();
                pathList.addAll(temp2);
            }
        }
        return weight;
    }
}


/**
 * 节点类
 */
class Node{
    /**
     * 节点名称
     */
    private String nodeName;

    /**
     * 节点权重
     */
    private Integer nodeWeight;

    /**
     * 本节点为起点的路径
     */
    private Set<GraphPath> pathSet = new HashSet<>();

    public Node() {

    }

    public Node(String nodeName, Integer nodeWeight) {
        this.nodeName = nodeName;
        this.nodeWeight = nodeWeight;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public Integer getNodeWeight() {
        return nodeWeight;
    }

    public void setNodeWeight(Integer nodeWeight) {
        this.nodeWeight = nodeWeight;
    }

    /**
     * 添加一个节点作为终点生成路径
     * @param end
     */
    public void addPath(Node end){
        GraphPath path = new GraphPath(this,end);
        pathSet.add(path);
    }

    public Set<GraphPath> getPathSet(){
        return this.pathSet;
    }

    @Override
    public String toString() {
        return "节点名称："+nodeName+"，节点权重"+nodeWeight;
    }

}

/**
 * 图路径类
 */
class GraphPath{
    /**
     * 起点
     */
    private Node startNode;

    /**
     * 终点
     */
    private Node endNode;

    public GraphPath() {
    }

    public GraphPath(Node startNode, Node endNode) {
        this.startNode = startNode;
        this.endNode = endNode;
    }

    public Node getStartNode() {
        return startNode;
    }

    public void setStartNode(Node startNode) {
        this.startNode = startNode;
    }

    public Node getEndNode() {
        return endNode;
    }

    public void setEndNode(Node endNode) {
        this.endNode = endNode;
    }

    public Integer getTotalWeight(){
        return startNode.getNodeWeight()+endNode.getNodeWeight();
    }

    @Override
    public String toString() {
        return startNode.getNodeName()+"->"+endNode.getNodeName()+" "+getTotalWeight();
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }
}

