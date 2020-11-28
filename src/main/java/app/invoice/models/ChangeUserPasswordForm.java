package app.invoice.models;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder
@Data
public class ChangeUserPasswordForm {

    @NotNull(message = "ChangeUserPasswordForm username is blank")
    @Size(max = 50, message = "ChangeUserPasswordForm userName max size 50")
    String userName;

    @NotNull(message = "ChangeUserPasswordForm old password is blank")
    String oldPassword;

    @NotNull(message = "ChangeUserPasswordForm new password is blank")
    @Size(min = 5, message = "ChangeUserPasswordForm new password min size 5")
    String newPassword;

    @NotNull(message = "ChangeUserPasswordForm confirm password is blank")
    String confirmNewPassword;
}
