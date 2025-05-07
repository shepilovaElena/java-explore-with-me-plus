package dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ViewStatsDto {
    @NotBlank
    String app;
    @NotBlank
    String uri;
    LocalDateTime statsFrom; // статистика с какого числа
    LocalDateTime statsTo; // по какое число
}
