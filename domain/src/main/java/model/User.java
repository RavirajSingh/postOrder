package model;


import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@JsonRootName(value = "user")
@Data
@NoArgsConstructor
public class User implements Serializable {
    private Long id;
    private String name;
}

