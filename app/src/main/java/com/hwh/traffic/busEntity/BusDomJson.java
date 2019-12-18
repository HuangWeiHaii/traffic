
package com.hwh.traffic.busEntity;

import java.io.Serializable;
import java.util.List;


public class BusDomJson implements Serializable {

    private int result;
    private String message;
    private int total;
    private int pageCount;
    private List<Items> items;
    public void setResult(int result) {
         this.result = result;
     }
     public int getResult() {
         return result;
     }

    public void setMessage(String message) {
         this.message = message;
     }
     public String getMessage() {
         return message;
     }

    public void setTotal(int total) {
         this.total = total;
     }
     public int getTotal() {
         return total;
     }

    public void setPageCount(int pageCount) {
         this.pageCount = pageCount;
     }
     public int getPageCount() {
         return pageCount;
     }

    public void setItems(List<Items> items) {
         this.items = items;
     }
     public List<Items> getItems() {
         return items;
     }

    @Override
    public String toString() {
        return "BusDomJson{" +
                "result=" + result +
                ", message='" + message + '\'' +
                ", total=" + total +
                ", pageCount=" + pageCount +
                ", items=" + items +
                '}';
    }
}