package app.invoice.models;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Builder
@Data
public class ChangeUserPasswordForm {

    @NotNull(message = "change.password.username.is.null")
    String userName;

    @NotNull(message = "change.password.oldpassword.is.null")
    String oldPassword;

    @NotNull(message = "change.password.newpassword.is.null")
    String newPassword;

    @NotNull(message = "change.password.confirmpassword.is.null")
    String confirmNewPassword;
}
