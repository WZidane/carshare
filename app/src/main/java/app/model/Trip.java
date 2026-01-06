package app.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.sql.Time;
import java.sql.Timestamp;

public class Trip {
    private int id;
    private int userId;
    private String startTown;
    private String endTown;
    private String startAddress;
    private String endAddress;
    private LocalDateTime startDate;
    private Time startHour;
    private int nbPlaces;
    private BigDecimal price;
    private Time estimatedTime;
    private String description;
    private String vehicule;
    private int tripType;
    private Timestamp createdAt;

    private String username;

    public Trip() {}

    public Trip(int id, int userId, String startTown, String endTown,
                String startAddress, String endAddress, LocalDateTime startDate, Time startHour,
                int nbPlaces, BigDecimal price, Time estimatedTime,
                String description, String vehicule, int tripType, Timestamp createdAt) {
        this.id = id;
        this.userId = userId;
        this.startTown = startTown;
        this.endTown = endTown;
        this.startAddress = startAddress;
        this.endAddress = endAddress;
        this.startDate = startDate;
        this.startHour = startHour;
        this.nbPlaces = nbPlaces;
        this.price = price;
        this.estimatedTime = estimatedTime;
        this.description = description;
        this.vehicule = vehicule;
        this.tripType = tripType;
        this.createdAt = createdAt;
    }

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

    public String getStartTown() {
        return startTown;
    }

    public void setStartTown(String startTown) {
        this.startTown = startTown;
    }

    public String getEndTown() {
        return endTown;
    }

    public void setEndTown(String endTown) {
        this.endTown = endTown;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public String getFormattedStartDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'à' HH:mm");
        return startDate.format(formatter);
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public Time getStartHour() {
        return startHour;
    }

    public void setStartHour(Time startHour) {
        this.startHour = startHour;
    }

    public int getNbPlaces() {
        return nbPlaces;
    }

    public void setNbPlaces(int nbPlaces) {
        this.nbPlaces = nbPlaces;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Time getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(Time estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVehicule() {
        return vehicule;
    }

    public void setVehicule(String vehicule) {
        this.vehicule = vehicule;
    }

    public int getTripType() {
        return tripType;
    }

    public void setTripType(int tripType) {
        this.tripType = tripType;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}