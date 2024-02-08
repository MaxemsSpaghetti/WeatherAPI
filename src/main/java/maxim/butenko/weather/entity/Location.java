package maxim.butenko.weather.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.List;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Entity
@Table(name = "locations", schema = "weather")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    @Column(name = "name")
    private String name;

    @ManyToMany()
    @JoinColumn(name = "user_id")
    private List<User> users;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;
}
