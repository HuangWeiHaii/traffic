
package com.hwh.traffic.busEntity;
import java.io.Serializable;
import java.util.List;


public class Items  implements Serializable {

    private String routeName;
    private List<Routes> routes;
    public void setRouteName(String routeName) {
         this.routeName = routeName;
     }
     public String getRouteName() {
         return routeName;
     }

    public void setRoutes(List<Routes> routes) {
         this.routes = routes;
     }
     public List<Routes> getRoutes() {
         return routes;
     }

}