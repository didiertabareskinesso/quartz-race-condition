package quartz;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JobMetadata {
    enum Status {
        CREATED
    }

    private UUID id;
    private Status status;
}
