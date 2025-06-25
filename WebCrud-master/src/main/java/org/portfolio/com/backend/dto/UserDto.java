package org.portfolio.com.backend.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Range;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UserDto {

    private Long id;

    @NotBlank(message = "Name may not be null")
    private String name;

    @NotNull(message = "Age may not be null")
    @Range(min=0, max=110)
    private Integer Age;
}
