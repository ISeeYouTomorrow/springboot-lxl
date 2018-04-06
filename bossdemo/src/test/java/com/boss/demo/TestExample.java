package com.boss.demo;

import org.junit.Test;

import java.util.HashMap;

/**
 * @author lxl lukas
 * @description
 * @create 2018/4/4
 */
public class TestExample {

    @Test
    public void jsonFormatTest(){
        String text = "{ \"a\": 1, \"b\": { \"c\": 2, \"d\": [3,4] ,\"x\":{\"y\":0,\"z\": [1,2]}} }";
        try {
            String result = Example1.jsonFormat(text);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void storeTest(){
        HashMap<String, String>[] data = new HashMap[5];
        for(int i=0;i<5;i++){
            HashMap<String, String> map = new HashMap<>();
            for(int j=0;j<2;j++) {
                map.put("key" + i+""+j,i+""+j);
            }
            data[i] = map;
        }

        String result = Example2.store(data);
        System.out.println(result);

        data = Example2.load(result);
        for(int i=0;i<data.length;i++){
            HashMap<String, String> map = data[i];
            for(String key:map.keySet()){
                System.out.print(key+" = "+map.get(key)+" ");
            }
            System.out.println();
        }
    }


    @Test
    public void graphTest() {
        Node nodeA = new Node("A",1);
        Node nodeB = new Node("B",2);
        Node nodeC = new Node("C",2);
        Node nodeD = new Node("D",3);
        Node nodeE = new Node("E",4);

//        Graph graph = new NoLoopGraph();
        Graph graph = new LoopGraph();
        graph.addNode(nodeA);
        graph.addNode(nodeB);
        graph.addNode(nodeC);
        graph.addNode(nodeD);
        graph.addNode(nodeE);

        graph.addPath(nodeA,nodeB);
        graph.addPath(nodeA,nodeC);
        graph.addPath(nodeB,nodeC);
        graph.addPath(nodeB,nodeD);
        graph.addPath(nodeC,nodeE);
        /**添加回环*/
        graph.addPath(nodeE,nodeA);

        System.out.println("最大路径权重为："+Example3.graphExample(graph,nodeA));

    }
}
