package app.invoice.models;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class ResetPasswordForm {

    @NotNull
    String userName;

    @NotNull
    String email;
}
