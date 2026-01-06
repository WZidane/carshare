package app.model;

import java.sql.Timestamp;

public class Booking {

    private int id;
    private int userId;
    private int tripId;
    private Timestamp createdAt;

    public int getId() { 
        return id; 
    }

    public void setId(int id) { 
        this.id = id; 
    }

    public int getUserId() { 
        return userId; 
    }

    public void setUserId(int userId) { 
        this.userId = userId; 
    }

    public int getTripId() { 
        return tripId; 
    }

    public void setTripId(int tripId) { 
        this.tripId = tripId; 
    }

    public Timestamp getCreatedAt() { 
        return createdAt; 
    }
    
    public void setCreatedAt(Timestamp createdAt) { 
        this.createdAt = createdAt; 
    }
}