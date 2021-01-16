package app.invoice.models;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Getter
@Setter
public class ResetPasswordForm {

    @NotNull(message = "ResetPasswordForm user name is blank")
    @Size(max = 50, message = "ResetPasswordForm user name max size 50")
    String userName;

    @NotNull(message = "ResetPasswordForm email is blank")
    @Size(max = 50, message = "ResetPasswordForm email max size 50")
    String email;
}
