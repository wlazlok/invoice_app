package app.invoice.models;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
public class ResetPasswordForm {

    @NotNull(message = "ResetPasswordForm user name is blank")
    @Size(max = 50, message = "ResetPasswordForm user name max size 50")
    String userName;

    @NotNull(message = "ResetPasswordForm email is blank")
    @Size(max = 50, message = "ResetPasswordForm email max size 50")
    String email;
}
