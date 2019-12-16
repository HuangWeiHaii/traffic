/**
  * Copyright 2019 bejson.com 
  */
package com.hwh.traffic.busEntity;
import java.io.Serializable;
import java.util.List;

/**
 * Auto-generated: 2019-10-12 15:29:46
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Items implements Serializable {

    private String name;
    private List<Routes> routes;
    public void setName(String name) {
         this.name = name;
     }
     public String getName() {
         return name;
     }

    public void setRoutes(List<Routes> routes) {
         this.routes = routes;
     }
     public List<Routes> getRoutes() {
         return routes;
     }

}