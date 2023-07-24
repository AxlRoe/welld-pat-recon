package welld.infra;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import welld.domain.Algorithm;
import welld.domain.Point;
import welld.dto.PointDTO;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = "/")
@AllArgsConstructor
@Validated
public class Controller {

    private Algorithm algorithm;

    @PostMapping(value = "point")
    public ResponseEntity<?> insert(@Valid @RequestBody PointDTO pointDTO) {
        Point point = Point.from(pointDTO);
        if (algorithm.isPointAlreadyPresent(point)) {
            return new ResponseEntity<>("Point already inserted", HttpStatus.BAD_REQUEST);
        }
        algorithm.insert(point);
        return ResponseEntity.ok(algorithm.getSpace().stream().map(Point::to).collect(Collectors.toList()));
    }

    @GetMapping(value = "space")
    public ResponseEntity<?> space() {
        List<PointDTO> pointDTOS = algorithm.getSpace().stream().map(Point::to).collect(Collectors.toList());
        return ResponseEntity.ok(pointDTOS);
    }

    @GetMapping(value = "lines/{n}")
    public ResponseEntity<?> insert(@Valid @PathVariable @Min(2) Integer n) {

        List<List<Point>> lines = algorithm.getLines(n);
        List<List<PointDTO>> result = lines
                .stream()
                .map(line -> line.stream()
                        .map(Point::to)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @DeleteMapping(value = "space")
    public ResponseEntity<?> delete() {
        algorithm.delete();
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleException(ConstraintViolationException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
