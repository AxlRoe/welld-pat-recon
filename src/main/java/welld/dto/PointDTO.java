package welld.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true, builderClassName = "Builder")
@Getter
@EqualsAndHashCode
public class PointDTO {
    @NotNull
    Integer x;

    @NotNull
    Integer y;
}
