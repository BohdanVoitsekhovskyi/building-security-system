package com.building_security_system.DataBaseAccess.Entities;




import com.building_security_system.Models.Floor;
import com.building_security_system.Models.User;
import lombok.Builder;
import lombok.Getter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Builder
@Getter
@Document(collation = "facilities")
public class FacilityEntity {
    @Id
    private ObjectId id;
    private User user;
    List<Floor> floors;
}
