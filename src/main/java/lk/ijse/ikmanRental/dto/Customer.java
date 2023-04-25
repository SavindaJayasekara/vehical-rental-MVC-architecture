package lk.ijse.ikmanRental.dto;

import lombok.*;

//@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Customer {
    private String Nic;
    private String gmail;
    private String contact;
    private String name;
    private String adminNic;

    public Customer(String nic, String gmail, String contact, String name, String adminNic) {
        Nic = nic;
        this.gmail = gmail;
        this.contact = contact;
        this.name = name;
        this.adminNic = adminNic;
    }

}
