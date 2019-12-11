
package com.hwh.traffic.busEntity;

import java.util.List;

public class BusDomJson {

    private int result;
    private String message;
    private Item item;
    private List<Items> items;

    public List<Items> getItems() {
        return items;
    }

    public void setItems(List<Items> items) {
        this.items = items;
    }

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

    public void setItem(Item item) {
         this.item = item;
     }
     public Item getItem() {
         return item;
     }

}