package com.tracker;

public class EntregasModel {
	public Integer userID;
    public String userEmail;
    public Integer deliveryId;
    public String deliveryDesc;
    public String deliveryStatus;
    public String latitude;
    public String longitude;
    public String deliveryComment;
	
    /*@Override
	public String toString() {
		return "userID : " +userID + ", userEmail : " +userEmail + ", deliveryId : " +deliveryId + ", deliveryDesc : " +deliveryDesc + ", deliveryStatus : " +deliveryStatus + ", latitude : " +latitude + ", longitude : " +longitude + ", deliveryComment : " +deliveryComment;
	}*/
    
    @Override
	public String toString() {
		//return "userID : " +userID + ", deliveryId : " +deliveryId + ", deliveryDesc : " +deliveryDesc + ", deliveryStatus : " +deliveryStatus;
    	return "Usuario: " +userEmail + ", Pedido: " +deliveryDesc + ", Estado: " +deliveryStatus;
	}
    
    
}
