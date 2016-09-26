package me.raptor.resellingapp.model;

/**
 * Created by Lucas on 18/09/2016.
 */
public class Client {

    private Integer clientID;
    private String name;
    private String phone;
    private String email;
    private String group;

    public Client(Integer clientID, String name, String phone, String email, String group) {
        this.clientID = clientID;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.group = group;
    }

    public Integer getClientID() {
        return clientID;
    }

    public void setClientID(Integer clientID) {
        this.clientID = clientID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Client client = (Client) o;

        return clientID.equals(client.clientID);

    }

    @Override
    public int hashCode() {
        return clientID.hashCode();
    }
}
